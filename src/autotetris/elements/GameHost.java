package autotetris.elements;

import autotetris.ATCommon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.Timer;

/**
 *
 * @author rmy
 */
public class GameHost implements ATCommon{

    private Board board;
    private Piece piece;
    private Random random;
    private int score;
    private Timer timer;

    public GameHost()  {
        board = new Board();
        random = new Random();
        
        piece = initPiece();
        
        timer = new Timer(100, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                timer_action();
            }
        });
        score = 0;
        timer.start();
    }

    public Board getBoard() {
        return board;
    }

    public Piece getPiece() {
        return piece;
    }

    public int getScore() {
        return score;
    }

    //initialize piece
    private Piece initPiece() {

        //generate random piece type
        int type = random.nextInt(7);

        //generate random orientation according to type
        int orient = random.nextInt(O_NUM[type]);

        //create a new instance of piece
        Piece new_piece = new Piece(PieceType.get(type), Orientation.get(orient));

        return new_piece;
    }

    //initiate a new game
    public void new_game() {
        random = new Random();
        score = 0;
        initPiece();
        board = new Board();
        timer.start();
    }

    private void timer_action() {
        if (board.check_done(piece, GameMove.DOWN)) {

            //the piece become history
            board.bindPiece(piece);

            //check if there is any score gained
            score += board.checkFull();

            //initialize a new piece
            piece = initPiece();
            //tcanvas.setPiece(piece);


            //if the new piece can't move down
            if (board.check_done(piece, GameMove.DOWN)) {
                //tcanvas.setStatus(GameStatus.DEAD);
                timer.stop();
                //System.out.println("piece dead");
            }
        } else {
            piece.move(GameMove.DOWN, board);
        }
    }
}
