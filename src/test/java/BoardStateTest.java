import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.application.domain.Coord;
import it.unibo.almarima.tablut.application.domain.Coordinates;
import it.unibo.almarima.tablut.application.domain.Move;
import it.unibo.almarima.tablut.external.Action;
import it.unibo.almarima.tablut.external.State;
import it.unibo.almarima.tablut.external.State.Pawn;
import it.unibo.almarima.tablut.external.State.Turn;
import it.unibo.almarima.tablut.external.StateTablut;



public class BoardStateTest {

    private static State s;
    private static BoardState b;
    
    @BeforeAll
    public static void init(){
        s = new StateTablut();
    }

    @BeforeEach
    public void boardInit(){
        b = new BoardState(s);
    }

    @Test
    public void creationTest() throws UnknownHostException, IOException {
        assertEquals(b.getPawnAt(Coordinates.get(4, 4)), Pawn.KING);
        assertNotEquals(b.getPawnAt(Coordinates.get(0, 0)), Pawn.KING);
    }

    @Test
    public void creationTest2(){
        assertEquals(9, b.getPlayerPieceCoordinates().size());
        assertEquals(16, b.getOpponentPieceCoordinates().size());
        assertEquals(b.getNumberPlayerPieces(BoardState.WHITE), b.getPlayerPieceCoordinates().size());
        assertEquals(b.getNumberPlayerPieces(BoardState.BLACK), b.getOpponentPieceCoordinates().size());

    }

    @Test
    public void creationTest3(){
        assertEquals(Coordinates.get(4,4), b.getKingPosition());
        assertTrue(Coordinates.isCenter(b.getKingPosition()));
        assertTrue(b.getPlayerPieceCoordinates().contains(b.getKingPosition()));
    }

    @Test
    public void cloneAndEqualsTest(){
        BoardState cl = (BoardState) b.clone();
        assertTrue(cl.equals(b));
    }

    @Test
    public void cloneAndEqualsTest2(){
        BoardState cl= (BoardState) b.clone();
        BoardState cl2 = (BoardState) cl.clone();
        assertTrue(cl2.clone().equals(b));
    }


    @Test
    public void processMoveTest() {
        Move m= new Move(Coordinates.get(4,4),Coordinates.get(4,4),b.getTurnPlayer());
        assertThrows(IllegalArgumentException.class, ()-> b.processMove(m));
    }

    @Test
    public void processMoveTest2() {
        assertEquals(BoardState.WHITE, b.getTurnPlayer());
        Move m1= new Move(Coordinates.get(4,5),Coordinates.get(5,5),b.getTurnPlayer());
        b.processMove(m1);
        assertEquals(Pawn.WHITE, b.getPawnAt(Coordinates.get(5,5)));
        assertEquals(Pawn.EMPTY, b.getPawnAt(Coordinates.get(4,5)));
        assertEquals(BoardState.BLACK, b.getTurnPlayer());
        Move m2 = new Move(Coordinates.get(4,7),Coordinates.get(3,7),b.getTurnPlayer());
        b.processMove(m2);
        assertEquals(Pawn.BLACK, b.getPawnAt(Coordinates.get(3,7)));
        assertEquals(Pawn.EMPTY, b.getPawnAt(Coordinates.get(4,7)));
        assertEquals(BoardState.WHITE, b.getTurnPlayer());
        Move m3= new Move(Coordinates.get(4,4),Coordinates.get(4,5),b.getTurnPlayer());
        b.processMove(m3);
        assertEquals(Pawn.KING, b.getPawnAt(Coordinates.get(4,5)));
        assertEquals(Pawn.THRONE, b.getPawnAt(Coordinates.get(4,4)));     
        assertEquals(Coordinates.get(4,5), b.getKingPosition());
        assertEquals(BoardState.BLACK, b.getTurnPlayer());
        Move m4= new Move(Coordinates.get(3,7),Coordinates.get(1,7),b.getTurnPlayer());
        b.processMove(m4);
        assertEquals(Pawn.BLACK, b.getPawnAt(Coordinates.get(1,7)));
        assertEquals(Pawn.EMPTY, b.getPawnAt(Coordinates.get(3,7)));     
        assertEquals(BoardState.WHITE, b.getTurnPlayer());
        Move m5= new Move(Coordinates.get(4,5),Coordinates.get(1,5),b.getTurnPlayer());
        b.processMove(m5);
        assertEquals(Pawn.KING, b.getPawnAt(Coordinates.get(1,5)));
        assertEquals(Pawn.THRONE, b.getPawnAt(Coordinates.get(4,5)));     
        assertEquals(Coordinates.get(1,5), b.getKingPosition());
        assertEquals(BoardState.BLACK, b.getTurnPlayer());
        Move m6= new Move(Coordinates.get(1,7),Coordinates.get(1,6),b.getTurnPlayer());
        b.processMove(m6);
        assertEquals(Pawn.BLACK, b.getPawnAt(Coordinates.get(1,6)));
        assertEquals(Pawn.EMPTY, b.getPawnAt(Coordinates.get(1,7)));     
        assertEquals(BoardState.WHITE, b.getTurnPlayer());
        assertNull(b.getKingPosition());
        assertEquals(BoardState.BLACK, b.getWinner());
        assertEquals(8, b.getNumberPlayerPieces(BoardState.WHITE));
        
    }

    @Test
    public void processMoveTest4() {
        Move m1= new Move(Coordinates.get(4,5),Coordinates.get(5,5),b.getTurnPlayer());
        b.processMove(m1);
        Move m2 = new Move(Coordinates.get(4,7),Coordinates.get(3,7),b.getTurnPlayer());
        b.processMove(m2);
        Move m3= new Move(Coordinates.get(4,4),Coordinates.get(4,5),b.getTurnPlayer());
        b.processMove(m3);
        Move m4= new Move(Coordinates.get(3,7),Coordinates.get(2,7),b.getTurnPlayer());
        b.processMove(m4);
        Move m5= new Move(Coordinates.get(4,5),Coordinates.get(1,5),b.getTurnPlayer());
        b.processMove(m5);
        Move m6= new Move(Coordinates.get(2,7),Coordinates.get(2,6),b.getTurnPlayer());
        b.processMove(m6);
        Move m7= new Move(Coordinates.get(1,5),Coordinates.get(1,8),b.getTurnPlayer());
        b.processMove(m7);
        assertEquals(Pawn.KING, b.getPawnAt(Coordinates.get(1,8)));
        assertEquals(Pawn.EMPTY, b.getPawnAt(Coordinates.get(1,5)));     
        assertEquals(BoardState.BLACK, b.getTurnPlayer());
        assertEquals(BoardState.WHITE, b.getWinner());
        assertFalse(b.getPlayerPieceCoordinates().contains(Coordinates.get(4,7)));
        assertFalse(b.getOpponentPieceCoordinates().contains(Coordinates.get(4,4)));
        assertTrue(b.getOpponentPieceCoordinates().contains(Coordinates.get(5,5)));

    }

    @Test
    public void getAllLegalMovesTest() {
        List<Move> legal= new ArrayList<>();
        Move m1= new Move(Coordinates.get(2,4),Coordinates.get(2,3),b.getTurnPlayer());
        Move m2= new Move(Coordinates.get(2,4),Coordinates.get(2,2),b.getTurnPlayer());
        Move m3= new Move(Coordinates.get(2,4),Coordinates.get(2,1),b.getTurnPlayer());
        Move m4= new Move(Coordinates.get(2,4),Coordinates.get(2,0),b.getTurnPlayer());
        Move m5= new Move(Coordinates.get(2,4),Coordinates.get(2,5),b.getTurnPlayer());
        Move m6= new Move(Coordinates.get(2,4),Coordinates.get(2,6),b.getTurnPlayer());
        Move m7= new Move(Coordinates.get(2,4),Coordinates.get(2,7),b.getTurnPlayer());
        Move m8= new Move(Coordinates.get(2,4),Coordinates.get(2,8),b.getTurnPlayer());
        Move m10= new Move(Coordinates.get(3,4),Coordinates.get(3,3),b.getTurnPlayer());
        Move m11= new Move(Coordinates.get(3,4),Coordinates.get(3,2),b.getTurnPlayer());
        Move m12= new Move(Coordinates.get(3,4),Coordinates.get(3,1),b.getTurnPlayer());
        Move m13= new Move(Coordinates.get(3,4),Coordinates.get(3,5),b.getTurnPlayer());
        Move m14= new Move(Coordinates.get(3,4),Coordinates.get(3,6),b.getTurnPlayer());
        Move m15= new Move(Coordinates.get(3,4),Coordinates.get(3,7),b.getTurnPlayer());
        Move m16= new Move(Coordinates.get(5,4),Coordinates.get(5,3),b.getTurnPlayer());
        Move m17= new Move(Coordinates.get(5,4),Coordinates.get(5,2),b.getTurnPlayer());
        Move m18= new Move(Coordinates.get(5,4),Coordinates.get(5,1),b.getTurnPlayer());
        Move m19= new Move(Coordinates.get(5,4),Coordinates.get(5,5),b.getTurnPlayer());
        Move m20= new Move(Coordinates.get(5,4),Coordinates.get(5,6),b.getTurnPlayer());
        Move m21= new Move(Coordinates.get(5,4),Coordinates.get(5,7),b.getTurnPlayer());
        Move m22= new Move(Coordinates.get(6,4),Coordinates.get(6,3),b.getTurnPlayer());
        Move m23= new Move(Coordinates.get(6,4),Coordinates.get(6,2),b.getTurnPlayer());
        Move m24= new Move(Coordinates.get(6,4),Coordinates.get(6,1),b.getTurnPlayer());
        Move m25= new Move(Coordinates.get(6,4),Coordinates.get(6,0),b.getTurnPlayer());
        Move m26= new Move(Coordinates.get(6,4),Coordinates.get(6,5),b.getTurnPlayer());
        Move m27= new Move(Coordinates.get(6,4),Coordinates.get(6,6),b.getTurnPlayer());
        Move m28= new Move(Coordinates.get(6,4),Coordinates.get(6,7),b.getTurnPlayer());
        Move m29= new Move(Coordinates.get(6,4),Coordinates.get(6,8),b.getTurnPlayer());
        Move m30= new Move(Coordinates.get(4,2),Coordinates.get(3,2),b.getTurnPlayer());
        Move m31= new Move(Coordinates.get(4,2),Coordinates.get(2,2),b.getTurnPlayer());
        Move m32= new Move(Coordinates.get(4,2),Coordinates.get(1,2),b.getTurnPlayer());
        Move m33= new Move(Coordinates.get(4,2),Coordinates.get(0,2),b.getTurnPlayer());
        Move m34= new Move(Coordinates.get(4,2),Coordinates.get(5,2),b.getTurnPlayer());
        Move m35= new Move(Coordinates.get(4,2),Coordinates.get(6,2),b.getTurnPlayer());
        Move m36= new Move(Coordinates.get(4,2),Coordinates.get(7,2),b.getTurnPlayer());
        Move m37= new Move(Coordinates.get(4,2),Coordinates.get(8,2),b.getTurnPlayer());
        Move m38= new Move(Coordinates.get(4,3),Coordinates.get(3,3),b.getTurnPlayer());
        Move m39= new Move(Coordinates.get(4,3),Coordinates.get(2,3),b.getTurnPlayer());
        Move m40= new Move(Coordinates.get(4,3),Coordinates.get(1,3),b.getTurnPlayer());
        Move m41= new Move(Coordinates.get(4,3),Coordinates.get(5,3),b.getTurnPlayer());
        Move m42= new Move(Coordinates.get(4,3),Coordinates.get(6,3),b.getTurnPlayer());
        Move m43= new Move(Coordinates.get(4,3),Coordinates.get(7,3),b.getTurnPlayer());
        Move m44= new Move(Coordinates.get(4,5),Coordinates.get(3,5),b.getTurnPlayer());
        Move m45= new Move(Coordinates.get(4,5),Coordinates.get(2,5),b.getTurnPlayer());
        Move m46= new Move(Coordinates.get(4,5),Coordinates.get(1,5),b.getTurnPlayer());
        Move m47= new Move(Coordinates.get(4,5),Coordinates.get(5,5),b.getTurnPlayer());
        Move m48= new Move(Coordinates.get(4,5),Coordinates.get(6,5),b.getTurnPlayer());
        Move m49= new Move(Coordinates.get(4,5),Coordinates.get(7,5),b.getTurnPlayer());
        Move m50= new Move(Coordinates.get(4,6),Coordinates.get(3,6),b.getTurnPlayer());
        Move m51= new Move(Coordinates.get(4,6),Coordinates.get(2,6),b.getTurnPlayer());
        Move m52= new Move(Coordinates.get(4,6),Coordinates.get(1,6),b.getTurnPlayer());
        Move m53= new Move(Coordinates.get(4,6),Coordinates.get(0,6),b.getTurnPlayer());
        Move m54= new Move(Coordinates.get(4,6),Coordinates.get(5,6),b.getTurnPlayer());
        Move m55= new Move(Coordinates.get(4,6),Coordinates.get(6,6),b.getTurnPlayer());
        Move m56= new Move(Coordinates.get(4,6),Coordinates.get(7,6),b.getTurnPlayer());
        Move m57= new Move(Coordinates.get(4,6),Coordinates.get(8,6),b.getTurnPlayer());
        // sono 56 ho mancato un numero (9)

        legal.add(m1);
        legal.add(m2);
        legal.add(m3);
        legal.add(m4);
        legal.add(m5);
        legal.add(m6);
        legal.add(m7);
        legal.add(m8);
        legal.add(m10);
        legal.add(m11);
        legal.add(m12);
        legal.add(m13);
        legal.add(m14);
        legal.add(m15);
        legal.add(m16);
        legal.add(m17);
        legal.add(m18);
        legal.add(m19);
        legal.add(m20);
        legal.add(m21);
        legal.add(m22);
        legal.add(m23);
        legal.add(m24);
        legal.add(m25);
        legal.add(m26);
        legal.add(m27);
        legal.add(m28);
        legal.add(m29);
        legal.add(m30);
        legal.add(m31);
        legal.add(m32);
        legal.add(m33);
        legal.add(m34);
        legal.add(m35);
        legal.add(m36);
        legal.add(m37);
        legal.add(m38);
        legal.add(m39);
        legal.add(m40);
        legal.add(m41);
        legal.add(m42);
        legal.add(m43);
        legal.add(m44);
        legal.add(m45);
        legal.add(m46);
        legal.add(m47);
        legal.add(m48);
        legal.add(m49);
        legal.add(m50);
        legal.add(m51);
        legal.add(m52);
        legal.add(m53);
        legal.add(m54);
        legal.add(m55);
        legal.add(m56);
        legal.add(m57);

        List<Move> testLegal = b.getAllLegalMoves();
        testLegal = testLegal.stream().sorted((move1, move2) -> move1.toString().compareTo(move2.toString())).collect(Collectors.toList());
        legal = legal.stream().sorted((move1, move2) -> move1.toString().compareTo(move2.toString())).collect(Collectors.toList());

        assertEquals(legal, testLegal);
    }
    @Test
    public void getAllLegalMovesTest2() {
        assertEquals(56, b.getAllLegalMoves().size());
    }

    @Test
    public void getAllLegalMovesTest3() {
    }

    @Test
    public void getAllLegalMovesTest4() {

    }

    public void playerHasALegalMoveTest() {

    }

    public void getLegalMovesForPositionTest() {

    }

    public void getLegalCoordsInDirectionTest() {

    }

    public void canCaptureWithCoordTest() {

    }

    public void canCaptureWithCoordTest2(){

    }

    public void getPlayerPieceCoordinatesTest() {

    }

    @Test
    public void getOpponentPieceCoordinatesTest() {
        HashSet<Coord> players = b.getPlayerPieceCoordinates();
        HashSet<Coord> opponents = b.getOpponentPieceCoordinates();
        
        boolean kingFound = false;
        for (Coord c : players) {
            if (b.getPawnAt(c).equals(Pawn.KING)) {
                kingFound = true;
            } else {
                assertEquals(b.getPawnAt(c), Pawn.WHITE);
            }
        }

        assertTrue(kingFound);
        
        for (Coord c : opponents) {
            assertEquals(b.getPawnAt(c), Pawn.BLACK);
        }
    }

    public void getPlayerCoordSetTest(){

    }
    
    public void getPlayerCoordSetTest2(){

    }

    @Test
    public void isLegalTest() {

        assertFalse(b.isLegal(new Move(Coordinates.get(3, 1), Coordinates.get(4, 1), -1)));
        assertFalse(b.isLegal(new Move(Coordinates.get(4, 3), Coordinates.get(5, 3), 0)));
        assertFalse(b.isLegal(new Move(Coordinates.get(4, 4), Coordinates.get(4, 3), 1)));
        assertTrue(b.isLegal(new Move(Coordinates.get(4, 3), Coordinates.get(3, 3), 1)));
    }

    public void getPawnAtTest(){

    }

    public void pieceBelongsToTest(){

    }

    public void isPawnKingAndTurnWhiteTest(){

    }

    public void fromTurnPlayerToCharTest(){

    }

    public void fromTurnPlayerToCharTest2(){
        
    }

    public void turnPlayerCanMoveFromTest(){

    }

    @Test
    public void IsOpponentPieceAtTest() {
        
        assertTrue(b.isOpponentPieceAt(Coordinates.get(4, 0)));
        assertFalse(b.isOpponentPieceAt(Coordinates.get(4, 4)));
        assertFalse(b.isOpponentPieceAt(Coordinates.get(3, 4)));
        assertFalse(b.isOpponentPieceAt(Coordinates.get(0, 0)));

        b.processMove(new Move(Coordinates.get(4, 3), Coordinates.get(3, 3), 1));
        assertFalse(b.isOpponentPieceAt(Coordinates.get(4, 0)));
        assertTrue(b.isOpponentPieceAt(Coordinates.get(4, 4)));
        assertFalse(b.isOpponentPieceAt(Coordinates.get(3, 4)));
        assertFalse(b.isOpponentPieceAt(Coordinates.get(0, 0)));
        
    }

    public void coordIsEmptyTest(){

    }


    @Test
    public void getOpponent() {

        int firstPlayer = b.getTurnPlayer();
        assertNotEquals(b.getOpponent(), b.getTurnPlayer());

        b.processMove(new Move(Coordinates.get(4, 3), Coordinates.get(3, 3), 1));
        assertEquals(b.getOpponent(), firstPlayer);
    }

    public void getNumberPlayerPiecesTest() {

        BoardState b = new BoardState(new StateTablut());
        assertEquals(b.getPawnAt(b.getKingPosition()), Pawn.KING);
    }

    @Test
    public void getKingPositionTest() {
        BoardState b = new BoardState(new StateTablut());
        assertEquals(b.getPawnAt(b.getKingPosition()), Pawn.KING);

        //Move the king to add a little spice
        b.processMove(new Move(Coordinates.get(4, 3), Coordinates.get(5, 3), 1));
        b.processMove(new Move(Coordinates.get(3, 0), Coordinates.get(2, 0), 0));
        b.processMove(new Move(Coordinates.get(4, 4), Coordinates.get(4, 3), 1));
        b.processMove(new Move(Coordinates.get(2, 0), Coordinates.get(1, 0), 0));
                
        assertEquals(b.getPawnAt(b.getKingPosition()), Pawn.KING);

        b.processMove(new Move(Coordinates.get(4, 3), Coordinates.get(1, 3), 1));
        b.processMove(new Move(Coordinates.get(1, 0), Coordinates.get(2, 0), 0));

        assertEquals(b.getPawnAt(b.getKingPosition()), Pawn.KING);

        b.processMove(new Move(Coordinates.get(1, 3), Coordinates.get(1, 0), 1));
        assertEquals(b.getPawnAt(b.getKingPosition()), Pawn.KING);

    }

    @Test
    public void citadelRulesTest() {
        BoardState b = new BoardState(new StateTablut());
        BoardState b2 = new BoardState(new StateTablut());
        BoardState b3 = new BoardState(new StateTablut());
        BoardState b4 = new BoardState(new StateTablut());
        BoardState b5 = new BoardState(new StateTablut());

        /**
         * Testing whether non black can move from into citadels
         */
        assertTrue(b.citadelRules(Coordinates.get(4, 5), Coordinates.get(5, 5)));
        b.processMove(new Move(Coordinates.get(4, 5), Coordinates.get(5, 5), 1));
       
        assertTrue(b.citadelRules(Coordinates.get(4, 5), Coordinates.get(5, 5)));
        b.processMove(new Move(Coordinates.get(4, 1), Coordinates.get(3, 1), 0));
        
        assertFalse(b.citadelRules(Coordinates.get(4, 3), Coordinates.get(4, 1)));
        assertThrows(IllegalArgumentException.class, () -> {
            b.processMove(new Move(Coordinates.get(4, 3), Coordinates.get(4, 1), 1));
        });


        /**
         * Testing whether black can move outside citadels
         */
        b2.processMove(new Move(Coordinates.get(4, 3), Coordinates.get(3, 3), 1));
        assertTrue(b2.citadelRules(Coordinates.get(4, 1), Coordinates.get(3, 1)));

        /**
         * Testing whether black can move back into citadels
         */
        b3.processMove(new Move(Coordinates.get(4, 3), Coordinates.get(3, 3), 1));
        b3.processMove(new Move(Coordinates.get(3, 0), Coordinates.get(2, 0), 0));
        b3.processMove(new Move(Coordinates.get(3, 3), Coordinates.get(4, 3), 1));
        assertFalse(b3.citadelRules(Coordinates.get(2, 0), Coordinates.get(3, 0)));

        /**
         * Testing whether black can move inside its own citadels
         */
        b4.processMove(new Move(Coordinates.get(4, 3), Coordinates.get(3, 3), 1));
        b4.processMove(new Move(Coordinates.get(3, 0), Coordinates.get(2, 0), 0));
        b4.processMove(new Move(Coordinates.get(3, 3), Coordinates.get(4, 3), 1));
        assertTrue(b4.citadelRules(Coordinates.get(4, 0), Coordinates.get(3, 0)));

        /**
         * Testing whether black can move to other citadels
         */
        b5.processMove(new Move(Coordinates.get(3, 4), Coordinates.get(3, 3), 1));
        b5.processMove(new Move(Coordinates.get(0, 3), Coordinates.get(1, 3), 0));
        b5.processMove(new Move(Coordinates.get(3, 3), Coordinates.get(2, 3), 1));
        b5.processMove(new Move(Coordinates.get(3, 8), Coordinates.get(3, 7), 0));
        b5.processMove(new Move(Coordinates.get(2, 3), Coordinates.get(1, 3), 1));
        assertFalse(b5.citadelRules(Coordinates.get(3, 0), Coordinates.get(3, 8)));
    }

    @Test
    public void getRandomMoveTest() {
        BoardState bs = new BoardState(new StateTablut());
        assertTrue(bs.getAllLegalMoves().stream().anyMatch(i -> bs.getRandomMove().equals(i)));
    }




    
}
