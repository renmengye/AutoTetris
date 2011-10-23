/*
 * This is a class that takes in board condition and piece and compute the gamemoves
 */
package autotetris;

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
    private GameMove[] moves;

    public Player() {
    }

    public GameMove[] genMoves(Board board, Piece piece) {

        moves = null;
        int max = 0;
        Piece test = piece.clone();
        for (int h = 0; h < O_NUM[piece.getOrient().value()]; h++) { //first have an orientation
            enumerator = new Enumerator(board, test); //initialize the enumerator for this orientation
            while (enumerator.next()) { //if it can still enumerate
                Piece candidate = enumerator.next_piece;  //set the next enumerate as candidate
                Board cboard = board.clone(); //have a new board to test the candidate
                router = new Router(board, candidate); //initialize the router
                if (router.route()) {  //if the candidate can be routed
                    int rating = Rater.rate(cboard.bindBoard(candidate.getBoard())); //give a rating for the current candidate
                    if (rating > max) { //if greater than the current max
                        max = rating; //candidate becomes the max
                        moves = router.getMoves(); //store the candidate's moves to return
                    }
                }
            }
            test.move(GameMove.CW); //goto the next orientation
        }
        return moves != null ? moves : null; //need to check if this function returns to null
    }
    
    public GameMove getMove(Piece piece){
        return GameMove.DOWN;
    }
}
