package autotetris.ai;

import autotetris.elements.Board;
import autotetris.elements.GameHost;
import autotetris.elements.GameMove;
import autotetris.elements.Piece;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rmy
 */
public class Player extends Thread {

    private List<GameMove> moves;           //a collection of moves
    private ListIterator<GameMove> movei;   //a pointer to current move and prepare for next
    private GameHost host;
    private Board board;
    private Piece piece;
    private Piece target;
    private boolean update_piece;
    private int reaction;

    public Player(GameHost host, int reaction) {
        this.host = host;
        this.board = host.getBoard();
        this.piece = host.getPiece();
        this.reaction = reaction;
        update_piece = false;
        setPriority(MAX_PRIORITY);
        gen_target();
    }
    
    public void setReact(int reaction){
        this.reaction=reaction;
    }

    private void gen_target() {
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
            Router router = new Router(board, piece, candidate);
            Rater rater = new Rater();

            //give a rating for the current candidate
            int rating = rater.rate(cboard, candidate);

            //if greater than the current max
            if (rating > max) {
                //if(router.possible_route(candidate,false)){
                if (router.possible_route()) {
                    //store max piece
                    target = candidate;

                    //candidate rating becomes the max
                    max = rating;

                    //store the candidate's moves to return
                    //moves = testm;
                }
            }
        }
    }
    
    public void update() {
        update_piece = true;
    }

    public void end_game() {
        this.stop();
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (update_piece) {
                    piece = host.getPiece();
                    gen_target();
                    update_piece = false;
                } 
                
                //else {
                    Router router = new Router(board, piece, target);
                    piece.move(router.next(), board);
                //}
                sleep(reaction);
            } catch (InterruptedException ex) {
                Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
