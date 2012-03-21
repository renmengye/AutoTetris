/*
 * This is a class that takes in board condition and piece and compute the gamemoves
 */
package autotetris.ai;

import autotetris.ATCommon;
import autotetris.elements.Board;
import autotetris.elements.GameMove;
import autotetris.elements.Piece;
import java.util.ArrayList;

/**
 *
 * @author rmy
 */
public class Player implements ATCommon {

    private Board board;
    private Piece piece;
    private Enumerator enumerator; //first enumerate
    private Router router; // then check if it is possible to form a route
    public Rater rater; //lastly give a rating for the circumstance
    //private GameMove[] moves;
    private ArrayList<GameMove> moves;
    private static int movecount;

    public Player() {
        rater = new Rater();
    }

    public ArrayList<GameMove> genMoves(Board board, Piece piece) {
        
        this.board = board;
        this.piece = piece;
        moves = null;
        float max = -1000f;
        
        //a clone of test piece used to model the consequence of dropping the piece
        Piece test = piece.clone();
        
        //initialize the enumerator for this orientation
        enumerator = new Enumerator(board, test); 
        enumerator.enumerate();
        
        
        Piece candidate;
        Board maxboard = new Board();
        Piece maxpiece = piece.clone();
        for (candidate = enumerator.next(); candidate != null; candidate = enumerator.next()) { //if it still has next candidate
            Board cboard = board.clone(); //have a new board to test the candidate
            router = new Router(board, piece); //initialize the router
            cboard.bindPiece(candidate);
            int rating = rater.rate(cboard,candidate); //give a rating for the current candidate
            if (rating > max) { //if greater than the current max
                ArrayList<GameMove> testm = router.route(candidate, new ArrayList<GameMove>(), false);
                if (testm != null) {
                    maxboard=cboard.clone();
                    maxpiece=candidate.clone();
                    max = rating; //candidate becomes the max
                    moves = (ArrayList<GameMove>) testm.clone(); //store the candidate's moves to return
                }
            }
        }
        movecount = 0;
        return moves != null ? moves : null; //need to check if this function returns to null
    }

    public GameMove getMove() {
        if (moves != null && moves.size() > movecount) {
            if ((GameMove) moves.get(movecount) == GameMove.DOWN) {
                movecount++;
                return GameMove.NULL;
            } else {
                return (GameMove) moves.get(movecount++);
            }
        } else {
            //System.out.println("it's null~~~");
            return null;
        }
    }

    public void printMove(ArrayList<GameMove> x) {
        for (int i = 0; i < x.size(); i++) {
            System.out.println(x.get(i));
        }
    }
}
