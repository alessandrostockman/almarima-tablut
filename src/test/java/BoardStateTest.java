import it.unibo.almarima.tablut.application.domain.BoardState;
import it.unibo.almarima.tablut.external.State;
import it.unibo.almarima.tablut.external.StateTablut;

public class BoardStateTest {

    public void creationTest(){
        State s = new StateTablut();
        BoardState b= new BoardState(s);

        
    }


}