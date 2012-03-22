/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris;

import autotetris.ai.Player;
import autotetris.elements.Board;
import autotetris.elements.PieceType;
import autotetris.elements.Piece;
import autotetris.elements.GameMove;
import autotetris.elements.Orientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.Timer;

/**
 *
 * @author rmy
 */
public class AutoTetrisCommand implements ATCommon {

    private Board board; //store current fixed grid
    public Timer t;
    private GameMove move; //store the keyboard action for the piece
    private Piece piece; //store a current moving piece
    private Random random;
    private ActionListener performer;
    private int score; //store the score of the game
    private boolean automode; //if it is AI's show
    private Player player; //computer AI agent
    static int height = -4;
    static int holes = -70;
    static int target = 30;
    static int scorec = 8000;
    static int lhole = -25;
    static int testcount = 0;
    static long testsum = 0;

    public AutoTetrisCommand() {
        move = GameMove.NULL;
        automode = true;
        player = new Player();
        performer = new ActionListener() { //Declare the methods for each timer action

            public void actionPerformed(ActionEvent e) {
                action();
            }
        };
        t = new Timer(0, performer);
    }

    public final Piece initPiece() { //initialize piece
        int type = random.nextInt(7); //generate random piece type
        int orient = random.nextInt(O_NUM[type]); //generate random orientation according to type
        Piece new_piece = new Piece(PieceType.get(type), Orientation.get(orient)); //create a new instance of piece
        return new_piece;
    }

    public void new_game() {

        random = new Random();

        testsum += score;

        score = 0;
        piece = initPiece();
        board = new Board();

        //player.rater=new Rater(height,holes,target,scorec,lhole);
        //player.rater=new Rater();

        //player.genMoves(board, piece);
        t.start();

        testcount++;

        if (testcount % 50 == 0) {
            System.out.printf("with lhole=%d,average score=%f\n", lhole, testsum / 50.0);
            //lhole+=10;
            testsum = 0;
        }

        if (testcount % 500 == 0) {
            System.exit(0);
        }

    }

    public void action() { //timer action
        if (!automode) {
            if (board.check_done(piece, GameMove.DOWN)) { // if the piece cannot move down
                board.bindPiece(piece); //the piece become history
                int pscore = board.checkFull(); //check if there is any score gained
                if (pscore != 0) { //if there is score
                    score += pscore; //then add score
                }
                piece = initPiece(); //initialize a new piece
                if (board.check_done(piece, GameMove.DOWN)) { //if the new piece can't move down
                    t.stop();
                    System.out.printf("piece dead. final score: %d\n", score);
                    new_game();
                }
            }
        } else {
            GameMove tmove = player.getMove();
            if (board.check_done(piece, GameMove.DOWN) && tmove == null) {
                board.bindPiece(piece); //the piece become history
                int pscore = board.checkFull(); //check if there is any score gained
                if (pscore != 0) { //if there is score
                    score += pscore; //then add score
                }
                piece = initPiece(); //initialize a new piece
                if (board.check_done(piece, GameMove.DOWN)) { //if the new piece can't move down
                    t.stop();
                    System.out.printf("piece dead. final score: %d\n", score);
                    new_game();
                } else {
                    //player.genMoves(board, piece);
                }
            }
            if (tmove != null) {
                piece.move(tmove, board);
            }
            if (!board.check_done(piece, GameMove.DOWN)) { //move down as usual
                piece.move(GameMove.DOWN, board);
            }
        }
        move = GameMove.NULL;
    }
}
