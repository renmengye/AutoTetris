package autotetris.ai;

import autotetris.elements.Board;
import autotetris.elements.GameHost;
import autotetris.elements.GameMove;
import autotetris.elements.Piece;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author rmy
 */
public class Player extends Thread{

    private List<GameMove> moves;           //a collection of moves
    private ListIterator<GameMove> movei;   //a pointer to current move and prepare for next
    private GameHost host;
    
    public Player(GameHost host){
        this.host=host;
    }
    public Player(){
    }

    public List<GameMove> genMoves(Board board, Piece piece) {

        //reset moves
        moves = null;

        //set max rating to negative infinity to ensure we have a max rating of the candidates
        float max = -1f / 0f;

        //a clone of test piece used to model the consequence of dropping the piece
        Piece test = piece.clone();

        //initialize the enumerator for this orientation
        Enumerator enumerator = new Enumerator(board, test);
        enumerator.enumerate();

        //if it still has next candidate, then check it out
        for (Piece candidate = enumerator.next(); candidate != null; candidate = enumerator.next()) {

            //have a new board to test the candidate
            Board cboard = board.clone();
            cboard.bindPiece(candidate);

            //initialize the router and rater
            Router router = new Router(board, piece);
            Rater rater = new Rater();

            //give a rating for the current candidate
            int rating = rater.rate(cboard, candidate);

            //if greater than the current max
            if (rating > max) {
                List<GameMove> testm = router.route(candidate, new LinkedList<GameMove>(), false);
                if (testm != null) {

                    //candidate rating becomes the max
                    max = rating;

                    //store the candidate's moves to return
                    moves = testm;
                }
            }
        }

        //redefine the list pointer to the new list
        if (moves != null) {
            movei = moves.listIterator();
        }

        return moves;
    }

    //get the next element in the moves list
    public GameMove getMove() {
        if (moves != null) {
            return movei.hasNext() ? movei.next() : null;
        } else {
            return null;
        }
    }

    public void printMove() {
        for (GameMove i : moves) {
            System.out.println(i);
        }
    }
    
    
    @Override
    public void run(){
        
    }
}
