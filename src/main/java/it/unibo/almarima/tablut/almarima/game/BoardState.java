package it.unibo.almarima.tablut.almarima.game;

import it.unibo.almarima.tablut.almarima.coordinates.*;
import it.unibo.almarima.tablut.almarima.coordinates.Coordinates.CoordinateDoesNotExistException;
import it.unibo.almarima.tablut.unibo.State;
import it.unibo.almarima.tablut.unibo.StateTablut;
import it.unibo.almarima.tablut.unibo.State.Pawn;
import it.unibo.almarima.tablut.unibo.State.Turn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class BoardState implements  Cloneable {

    /* Useful constants. */
    public static final int ILLEGAL = -1;
    public static final int WHITE = 1;
    public static final int BLACK = 0;
    public static final int BOARD_SIZE = 9; // 9x9 board for tablut

    static {
        Coordinates.setAllCoordinates(BOARD_SIZE);
    }

    /* These are our data storage things. */
    private Pawn[][] board;
    private HashSet<Coord> BlackCoords; 
    private HashSet<Coord> WhiteCoords;
    private Coord kingPosition;

    private Random rand = new Random(Math.round(Math.random()*2000));

    private int turnPlayer;             // 0 black, 1 white 
    private int winner = ILLEGAL;      //0 black ,1 white , -1 nobody 

    // Initial Board State creation. The genesis constructor.
    public BoardState(State st) {

        this.board= st.getBoard();
        this.turnPlayer = (st.getTurn().equalsTurn("B") ? BLACK : WHITE);
        
        // Update the lists storing the coordinates of all the pieces.
        WhiteCoords = new HashSet<>();
        BlackCoords = new HashSet<>();
        for (Coord c : Coordinates.iterCoordinates()) {
            Pawn piece = getPawnAt(c);
            if (piece.toString() == "B") {
                BlackCoords.add(c);
            } else if (piece.toString() == "W") {
                WhiteCoords.add(c);
            }else if (piece.toString()== "K"){
                this.kingPosition = c;
            }
        }
    }

    /* The below method is for the purpose of cloning. */
    private BoardState(BoardState boardState) {
        for (Coord c : Coordinates.iterCoordinates()) {
            board[c.x][c.y] = boardState.board[c.x][c.y];
        }
        WhiteCoords = new HashSet<>(boardState.WhiteCoords);
        BlackCoords = new HashSet<>(boardState.BlackCoords);
        kingPosition = boardState.kingPosition;
        turnPlayer = boardState.turnPlayer;
    }

    @Override
    public Object clone() {
        return new BoardState(this);
    }

    /**
     * Here and below are for dealing with moves, and processing captures.
     */
    public void processMove(Move m) throws IllegalArgumentException {
        if (!isLegal(m)) { // isLegal checks if the player is the correct player.
            throw new IllegalArgumentException("Invalid move for current context. " + "Move: " + m.toPrettyString());
        }

        // Process move...
        Coord oldPos = m.getFromPosition();
        Coord newPos = m.getToPosition();
        Pawn movingPiece = getPawnAt(oldPos);

        // Get memory address to the list we are working on, then update it.
        HashSet<Coord> playerCoordSet = getPlayerCoordSet();
        playerCoordSet.remove(oldPos); // We can do this remove
        playerCoordSet.add(newPos);
        if (movingPiece == Pawn.KING)
             kingPosition = newPos;

        // Now update board.
        board[oldPos.x][oldPos.y] = Pawn.EMPTY;
        board[newPos.x][newPos.y] = movingPiece;

        // Now check if a capture occurred. Only a piece next to the new position could
        // have been captured.
        List<Coord> captured = new ArrayList<>();
        for (Coord enemy : Coordinates.getNeighbors(newPos)) {
            if (isOpponentPieceAt(enemy)) {
                boolean canCapture = true;

                // If the opponent is a king, we need to check if its at the center or the
                // neighbors of center.
                // If it is, then it can only be captured on all 4 sides.
                if (getPieceAt(enemy) == Piece.KING && Coordinates.isCenterOrNeighborCenter(kingPosition)) {
                    for (Coord possibleAlly : Coordinates.getNeighbors(enemy)) {
                        if (getPieceAt(possibleAlly) != Piece.BLACK && !Coordinates.isCenter(possibleAlly)) {
                            canCapture = false;
                            break;
                        }
                    }
                } else { // Otherwise, check for the normal, sandwich-based capture rule.
                    try {
                        Coord sandwichCord = Coordinates.getSandwichCoord(newPos, enemy);
                        canCapture = canCaptureWithCoord(sandwichCord);
                    } catch (CoordinateDoesNotExistException e) {
                        canCapture = false;
                    }
                }
                if (canCapture) {
                    captured.add(enemy);
                }
            }
        }

        // Slaughter the captured enemies... like pigs. Or more like remove object
        // memory addresses... same thing.
        // Note, it is possible for multiple pieces to be captured at once, so we have a
        // list of them.
        for (Coord capturedCoord : captured) {
            if (getPawnAt(capturedCoord) == Pawn.KING) {
                kingPosition = null;
            } // the king has been captured!
            getPlayerCoordSet(getOpponent()).remove(capturedCoord);
            board[capturedCoord.x][capturedCoord.y] = Pawn.EMPTY;
        }

        // Update internal variables, winner, turn player, and turn number.
        turnPlayer = getOpponent();
        updateWinner(); // Check if anybody won and update internal variables if so.
    }

    // Determines if a player has won by updating internal variable.
    private void updateWinner() {
        // Check if the king was captured -- BLACK WIN!
        // Also checking if the WHITES even have any legal moves at all. If not, they
        // lose.
        if (kingPosition == null || !playerHasALegalMove(WHITE)) {
            winner = BLACK;
        }

        // Check if king is at corner -- SWEDES WIN!
        // Also checking if the muscovites even have any legal moves at all. If not,
        // they lose.
        else if (Coordinates.isEscape(kingPosition) || !playerHasALegalMove(BLACK)) {
            winner = WHITE;
        }

    }

    /**
     * Get all legal moves for the player. This may be expensive, so it may be more
     * desirable to select a subset of moves from specific positions.
     */
    public ArrayList<Move> getAllLegalMoves() {
        return this.getAllLegalMoves(turnPlayer);
    }

    public ArrayList<Move> getAllLegalMoves(int player) {
        ArrayList<Move> allMoves = new ArrayList<>();
        for (Coord pos : getPlayerCoordSet(player)) {
            allMoves.addAll(getLegalMovesForPosition(pos));
        }
        return allMoves;
    }

    /**
     * Check if there are any legal moves for the player.
     */
    private boolean playerHasALegalMove(int player) {

        return this.getAllLegalMoves(player).isEmpty();
    }

    /**
     * Get all legal moves for the passed position in the current board state.
     *
     * Returned moves are assumed to be moves for the player whose turn it currently
     * is.
     */
    public ArrayList<Move> getLegalMovesForPosition(Coord start) {
        ArrayList<Move> legalMoves = new ArrayList<>();

        // Check that the piece being requested actually belongs to the player.
        Pawn piece = getPawnAt(start);
        if (piece.toString() != this.fromTurnPlayerToChar()) {
            return legalMoves;
        }

        // Iterate along 4 directions.
        List<Coord> goodCoords = new ArrayList<>();
        goodCoords.addAll(getLegalCoordsInDirection(start, -1, 0)); // move in -x direction
        goodCoords.addAll(getLegalCoordsInDirection(start, 0, -1)); // move in -y direction
        goodCoords.addAll(getLegalCoordsInDirection(start, 1, 0)); // move in +x direction
        goodCoords.addAll(getLegalCoordsInDirection(start, 0, 1)); // move in +y direction

        /*
         * Add the real moves now. We do not call isLegal here; this is because we
         * efficiently enforce legality by only adding those that are legal. This makes
         * for a more efficient method so people aren't slowed down by just figuring out
         * what they can do.
         */
        for (Coord end : goodCoords) {
            legalMoves.add(new Move(start, end, this.turnPlayer));
        }
        return legalMoves;
    }

    /*TODO: ad ora questo non permette di muoversi dentro una cittadela, da modificare*/
    private List<Coord> getLegalCoordsInDirection(Coord start, int x, int y) {
        ArrayList<Coord> coords = new ArrayList<>();
        assert (!(x != 0 && y != 0));
        int startPos = (x != 0) ? start.x : start.y; // starting at x or y
        int incr = (x != 0) ? x : y; // incrementing the x or y value
        int endIdx = (incr == 1) ? BOARD_SIZE - 1 : 0; // moving in the 0 or 8 direction

        for (int i = startPos + incr; incr * i <= endIdx; i += incr) { // increasing/decreasing functionality
            // new coord is an x coord change or a y coord change
            Coord coord = (x != 0) ? Coordinates.get(i, start.y) : Coordinates.get(start.x, i);
            if (coordIsEmpty(coord) && !Coordinates.isCitadel(coord)) {
                coords.add(coord);
            } else {
                break;
            }
        }
        return coords;
    }

    // // Determines whether or not this coord is a valid coord we can sandwich with.
    // private boolean canCaptureWithCoord(Coord c) {
    //     return Coordinates.isCorner(c) || Coordinates.isCenter(c) || piecesToPlayer.get(getPieceAt(c)) == turnPlayer;
    // }

    // Returns all of the coordinates of pieces belonging to the current player.
    public HashSet<Coord> getPlayerPieceCoordinates() {
        if (turnPlayer != BLACK && turnPlayer != WHITE) {
            return null;
        }
        return new HashSet<Coord>(getPlayerCoordSet(turnPlayer)); // Copy the set so no funny business.
    }

    public HashSet<Coord> getOpponentPieceCoordinates() {
        if (turnPlayer != BLACK && turnPlayer != WHITE) {
            return null;
        }
        return new HashSet<Coord>(getPlayerCoordSet(getOpponent())); // Copy the set so no funny business.
    }

    private HashSet<Coord> getPlayerCoordSet() {
        return getPlayerCoordSet(turnPlayer);
    }

    private HashSet<Coord> getPlayerCoordSet(int player) {
        return (player == BLACK) ? BlackCoords : WhiteCoords;
    }

    public boolean isLegal(Move move) {
        // Make sure that this is the correct player.
        if (turnPlayer != move.getPlayerId() || move.getPlayerId() == ILLEGAL)
            return false;

        // Get useful things. TODO: cosa succede se la posizione non esiste??
        Coord from = move.getFromPosition();
        Coord to = move.getToPosition();
        Pawn piece = getPawnAt(from); // this will check if the position is on the board

        // Check that the piece being requested actually belongs to the player.
        if (piece.toString() != this.fromTurnPlayerToChar())
            return false;

        // Next, make sure move doesn't end on a piece.
        if (!coordIsEmpty(to))
            return false;

        // Next, make sure the move is actually a move.
        int coordDiff = from.maxDifference(to);
        if (coordDiff == 0)
            return false;

        // Now for the actual game logic. First we make sure it is moving like a rook.
        if (!(from.x == to.x || from.y == to.y))
            return false;

        // Check if i'm moving towards a citadel without coming from one 
        if (Coordinates.isCitadel(to) && !Coordinates.isCitadel(from)) {
			return false;
        }

        // Check if i'm moving toward a citadel coming from one on the opposite side of the board
        if (Coordinates.isCitadel(to) && Coordinates.isCitadel(from)) {
			if (from.x==to.x){
                if ((from.y - to.y > 5) || from.y - to.y < -5) return false;
            }
            else {
                if (from.x - to.x > 5 || from.x - to.x < -5 ) return false; 
            }
            
        }

        // Now we make sure it isn't moving through any other pieces.
        for (Coord throughCoordinate : from.getCoordsBetween(to)) {
            if (!coordIsEmpty(throughCoordinate))
                return false;
        }

        // Make sure, if its a escape tile, the king is the only one able to
        // go there.
        if (!pieceIsAllowedAt(to, piece))
            return false;

        // All of the conditions have been satisfied, we have a legal move!
        return true;
    }

    /* ----- Useful helper functions. ----- */
    public Pawn getPawnAt(Coord c) {
        return board[c.x][c.y];
    }

    public String fromTurnPlayerToChar(){
        return (this.getTurnPlayer()==BLACK) ? "B" : "W" ;
    }

    public boolean turnPlayerCanMoveFrom(Coord position) {
        return getPawnAt(position).toString() == this.fromTurnPlayerToChar();
    }

    public boolean isOpponentPieceAt(Coord position) {
        return !(coordIsEmpty(position)) && getPawnAt(position).toString() != this.fromTurnPlayerToChar();
    }

    public boolean coordIsEmpty(Coord c) {
        return getPawnAt(c) == Pawn.EMPTY;
    }

    public int getOpponent() {
        return (turnPlayer == BLACK) ? WHITE : BLACK;
    }

    public int getNumberPlayerPieces(int player) {
        return getPlayerCoordSet(player).size();
    }

    public Coord getKingPosition() {
        return kingPosition;
    }

    // If its a king, it can move anywhere. Otherwise, make sure it isn't trying to
    // move to an escape tile
    private boolean pieceIsAllowedAt(Coord pos, Pawn piece) {
        return piece ==Pawn.KING || !(Coordinates.isEscape(pos));
    }

    
    public boolean isInitialized() {
        return true;
    }

    public int getTurnPlayer() {
        return turnPlayer;
    }

    public int getWinner() {
        return winner;
    }

    public Move getRandomMove() {
        ArrayList<Move> moves = getAllLegalMoves();
        return moves.get(rand.nextInt(moves.size()));
    }

    /*** Debugging functionality is found below. ***/

    // Useful method to show the board.
    public void printBoard() {
        printSpecialBoard(new ArrayList<Coord>(), "");
    }

    // Very useful for printing any special positions (such as legal moves).
    // onto the board visualization.
    public void printSpecialBoard(List<Coord> positions, String specialChar) {
        int lastRow = 0;
        for (Coord c : Coordinates.iterCoordinates()) {
            if (c.x > lastRow) {
                lastRow = c.x;
                System.out.println();
            }
            String s =this.getPawnAt(c).toString();
            for (Coord special : positions) {
                if (special.equals(c)) {
                    s = specialChar;
                }
            }
            System.out.print(s + " ");
        }
        System.out.println();
    }

    // public static void main(String[] args) {
    //     BoardState b = new BoardState();

    //     // compute branching factors
    //     for (Integer player : Arrays.asList(MUSCOVITE, SWEDE)) {
    //         b.turnPlayer = player;
    //         int totalMoves = b.getAllLegalMoves().size();
    //         System.out.println(String.format("Player %d, %d possible moves.", player, totalMoves));
    //     }

    //     // Single coordinate observation for debugging.
    //     for (int i = 0; i < BOARD_SIZE; i++) {
    //         b.turnPlayer = MUSCOVITE;
    //         Coord start = Coordinates.get(i, 0);
    //         System.out.println("------------------\n" + start.toString());
    //         List<TablutMove> moves = b.getLegalMovesForPosition(start);
    //         List<Coord> positions = new ArrayList<>();
    //         for (TablutMove move : moves)
    //             positions.add(move.getEndPosition());
    //         b.printSpecialBoard(positions, "*");
    //     }

    //     System.out.println("------------------\nCorner check.");
    //     b.printSpecialBoard(Coordinates.getCorners(), "X");
    //     System.out.println("------------------\n\nRandom Movement check.");
    //     TablutMove move = (TablutMove) b.getRandomMove();
    //     b.printSpecialBoard(Arrays.asList(move.getStartPosition()), "S");
    //     System.out.println();
    //     b.printSpecialBoard(Arrays.asList(move.getEndPosition()), "E");
    //     b.processMove(move);
    //     System.out.println();
    //     b.printBoard();

    //     // Check capture
    //     System.out.println("------------------\n\n\nCapture check.");
    //     b = new TablutBoardState();
    //     TablutMove move1 = new TablutMove(0, 3, 2, 3, MUSCOVITE);
    //     TablutMove move2 = new TablutMove(6, 4, 6, 5, SWEDE);
    //     TablutMove move3 = new TablutMove(0, 5, 2, 5, MUSCOVITE);
    //     TablutMove move4 = new TablutMove(6, 5, 6, 6, SWEDE);
    //     TablutMove move5 = new TablutMove(1, 4, 2, 4, MUSCOVITE);
    //     List<TablutMove> moves = Arrays.asList(move1, move2, move3, move4, move5);
    //     for (TablutMove m : moves) {
    //         b.printSpecialBoard(Arrays.asList(m.getStartPosition()), "S");
    //         System.out.println();
    //         b.printSpecialBoard(Arrays.asList(m.getEndPosition()), "E");
    //         b.processMove(m);
    //         System.out.println();
    //         b.printBoard();
    //         System.out.println("MOVE COMPLETED " + m.toPrettyString() + "\n\n");
    //     }
    //     System.out.println();
    // }
}
