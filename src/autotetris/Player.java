/*
 * This is a class that takes in board condition and piece and compute the gamemoves
 */
package autotetris;

import java.util.ArrayList;

/**
 *
 * @author MengYe
 */
public class Player implements ATCommon {

    private Board board;
    private Piece piece;
    private Enumerator enumerator; //first enumerate
    private Router router; // then check if it is possible to form a route
    //private Rater rater; //lastly give a rating for the circumstance
    //private GameMove[] moves;
    private ArrayList<GameMove> moves;
    private static int movecount;

    public Player() {
    }

    public ArrayList<GameMove> genMoves(Board board, Piece piece) {
        this.board = board;
        this.piece = piece;
        moves = null;
        int max = 0;
        Piece test = piece.clone();
        for (int h = 0; h < O_NUM[piece.getType().value()]; h++) { //first have an orientation
            System.out.printf("make turn %d\n",h);
            enumerator = new Enumerator(board, test); //initialize the enumerator for this orientation
            while (enumerator.next()) { //if it can still enumerate
                Piece candidate = enumerator.next_piece;  //set the next enumerate as candidate
                Board cboard = board.clone(); //have a new board to test the candidate
                //System.out.printf("new candidate x: %d y: %d", candidate.getX(), candidate.getY());
                router = new Router(board, piece); //initialize the router
                ArrayList<GameMove> testm = router.route(candidate, new ArrayList<GameMove>(),false);
                if (testm != null) {  //if the candidate can be routed
                    int rating = Rater.rate(cboard.bindBoard(candidate.getBoard())); //give a rating for the current candidate
                    if (rating > max) { //if greater than the current max
                        max = rating; //candidate becomes the max
                        moves=testm; //store the candidate's moves to return
                    }
                }
            }
            test.move(GameMove.CW, board); //goto the next orientation
        }
        movecount = 0;
        return moves != null ? moves : null; //need to check if this function returns to null
    }

    public GameMove getMove() {
        if (moves != null && moves.size() > movecount) {
            if ((GameMove)moves.get(movecount) == GameMove.DOWN) {
                movecount++;
                return GameMove.NULL;
            } else {
                return (GameMove)moves.get(movecount++);
            }
        } else {
            System.out.println("it's null~~~");
            return null;
        }
    }
    
    /*public GameMove[] convert_move(ArrayList<GameMove> g){
        GameMove[] x
        for(int i=0;i<g.size();i++){
            
        }
    }*/
}
