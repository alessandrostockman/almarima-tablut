package it.unibo.almarima.tablut.application.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import it.unibo.almarima.tablut.application.domain.Coordinates.CoordinateDoesNotExistException;
import it.unibo.almarima.tablut.external.State;
import it.unibo.almarima.tablut.external.State.Pawn;

public class BoardState implements  Cloneable {

    /* Useful constants. */
    public static final int ILLEGAL = -1;
    public static final int WHITE = 1;
    public static final int BLACK = 0;
    public static final int BOARD_SIZE = 9;           // 9x9 board for tablut

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
    private int winner = ILLEGAL;       //0 black ,1 white , -1 nobody 

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
            } else if (piece.toString() == "W" || piece.toString()== "K") {
                WhiteCoords.add(c);
                if (piece.toString()== "K"){
                    this.kingPosition = c;
                }
            }
        }
    }

    /* The below method is for the purpose of cloning. */
    private BoardState(BoardState boardState) {
        this.board = new Pawn[BOARD_SIZE][BOARD_SIZE];
        for (Coord c : Coordinates.iterCoordinates()) {
            this.board[c.x][c.y] = boardState.board[c.x][c.y];
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

        // Now update board.
        board[oldPos.x][oldPos.y] = Pawn.EMPTY;
        board[newPos.x][newPos.y] = movingPiece;

        //TODO: controllare che questo è giusto 
        //if i'm moving the king update king position and if it's the first time i move it (it is in the center) put the throne there 
        if (movingPiece == Pawn.KING)
             kingPosition = newPos;
             if (Coordinates.isCenter(oldPos)) board[oldPos.x][oldPos.y] = Pawn.THRONE;
        
        // Now check if a capture occurred. Only a piece next to the new position could
        // have been captured.
        List<Coord> captured = new ArrayList<>();
        for (Coord enemy : Coordinates.getNeighbors(newPos)) {
            if (isOpponentPieceAt(enemy)) {
                boolean canCapture = true;

                // If the opponent is a king, we need to check if its at the center or the
                // neighbors of center.
                // If it is, then it can only be captured on all 4 sides.
                if (getPawnAt(enemy) == Pawn.KING && Coordinates.isCenterOrNeighborCenter(kingPosition)) {
                    for (Coord possibleAlly : Coordinates.getNeighbors(enemy)) {
                        if (getPawnAt(possibleAlly) != Pawn.BLACK && !Coordinates.isCenter(possibleAlly)) {
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
        // Check if the king was captured -- BLACK WINS
        // Also checking if the WHITES even have any legal moves at all. If not, they
        // lose.
        if (kingPosition == null || !playerHasALegalMove(WHITE)) {
            winner = BLACK;
        }

        // Check if king is at corner -- WHITE WINS
        // Also checking if the BLACKS even have any legal moves at all. If not,
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
     * Check if there are any legal move for the player.
     */
    private boolean playerHasALegalMove(int player) {
        for (Coord c : getPlayerCoordSet(player)) {
            for (Coord neighbor : Coordinates.getNeighbors(c)) {
                if (coordIsEmpty(neighbor) && citadelRules(c, neighbor)) {
                    return true;       
                }
            }
        }
        return false;
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
            if (piece.equalsPawn("K") && this.getTurnPlayer()!=WHITE)     //the king belongs to the whites 
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
         * for a more efficient method so player aren't slowed down by just figuring out
         * what they can do.
         */
        for (Coord end : goodCoords) {
            legalMoves.add(new Move(start, end, this.turnPlayer));
        }
        return legalMoves;
    }

    private List<Coord> getLegalCoordsInDirection(Coord start, int x, int y) {
        ArrayList<Coord> coords = new ArrayList<>();
        assert (!(x != 0 && y != 0));
        int startPos = (x != 0) ? start.x : start.y; // starting at x or y
        int incr = (x != 0) ? x : y; // incrementing the x or y value
        int endIdx = (incr == 1) ? BOARD_SIZE - 1 : 0; // moving in the 0 or 8 direction

        for (int i = startPos + incr; incr * i <= endIdx; i += incr) { // increasing/decreasing functionality
            // new coord is an x coord change or a y coord change
            Coord coord = (x != 0) ? Coordinates.get(i, start.y) : Coordinates.get(start.x, i);
            if (coordIsEmpty(coord) && this.citadelRules(start, coord)) {
                coords.add(coord);
            } else {
                break; 
            }
        }
        return coords;
    }
    

    //return the number of pieces of turnPlayer that are endangered aka pieces that can be eaten in the current situation
    public int numEndangeredPieces(int turnPlayer){
        int num=0;
        HashSet<Coord> playerCoord = this.getPlayerCoordSet(turnPlayer);
        for (Coord c : playerCoord){
            List<Coord> neigh = Coordinates.getNeighbors(c);
            if (getPawnAt(c)!= Pawn.KING){
                for (Coord n: neigh){
                    try {
                        Coord s= Coordinates.getSandwichCoord(n,c);
                        if (getPawnAt(c)==Pawn.WHITE){
                            if (canCaptureWithCoord(s, turnPlayer==BLACK ? WHITE : BLACK) && getPawnAt(n)==Pawn.EMPTY) {
                                num+=1;
                                break;
                            }
                        }
                        else {
                            if (canCaptureWithCoord(s,turnPlayer==BLACK ? WHITE : BLACK) && getPawnAt(n)==Pawn.EMPTY && !Coordinates.isCitadel(n)) {
                            num+=1;
                            break;
                            }
                        }
                    }catch(Exception e) {}
                }
            }
        }
        return num;
    }

    // Determines whether or not this coord is a valid coord we can sandwich with.
    private boolean canCaptureWithCoord(Coord c) {
        return this.canCaptureWithCoord(c,this.getTurnPlayer());
    }

    private boolean canCaptureWithCoord(Coord c, int turn){
        return Coordinates.isCenter(c) || Coordinates.isCitadel(c) || getPawnAt(c).toString() == this.fromTurnPlayerToChar(turn);
    }

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
        if (piece.toString() != this.fromTurnPlayerToChar()){
            if(piece.equalsPawn("K") && this.getTurnPlayer()!=WHITE)   //the king belongs to the whites 
                return false;
        }

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

        // Check if trying to go to citadel without coming from one 
        if (!this.citadelRules(from, to)){
            return false;
        }

        // Now we make sure it isn't moving through any other pieces.
        for (Coord throughCoordinate : from.getCoordsBetween(to)) {
            if (!coordIsEmpty(throughCoordinate) && this.citadelRules(from, throughCoordinate))
                return false;
        }

        // All of the conditions have been satisfied, we have a legal move!
        return true;
    }

    /* ----- Useful helper functions. ----- */
    public Pawn getPawnAt(Coord c) {
        return board[c.x][c.y];
    }

    public String fromTurnPlayerToChar(){
        return fromTurnPlayerToChar(this.getTurnPlayer()) ;
    }

    public String fromTurnPlayerToChar(int turnPlayer){
        return (turnPlayer==BLACK) ? "B" : "W" ;
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


    //check if a piece can move to a citadel or not 
    //returns TRUE when you can do the move , FALSE otherwise
    //TODO: i Bianchi non possono mai andare in una cittadella
    public boolean citadelRules(Coord from , Coord to){
        
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
        return true;
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

    /*Method that controls if two boards are equal*/
    public boolean equals(BoardState b){
        for(Coord c: Coordinates.iterCoordinates()){
            if(this.getPawnAt(c) != b.getPawnAt(c)){
                return false;
            }
        }
        return true;
    }

}
