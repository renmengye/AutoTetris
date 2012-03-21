/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris;

import autotetris.ai.Player;
import autotetris.elements.Board;
import autotetris.elements.GameStatus;
import autotetris.elements.PieceType;
import autotetris.elements.Piece;
import autotetris.elements.GameMove;
import autotetris.elements.Orientation;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import javax.swing.Timer;
import javax.swing.JApplet;

/**
 *
 * @author rmy
 */
public class AutoTetrisApplet extends JApplet implements ATCommon, KeyListener {

    /**
     * Initialization method that will be called after the applet is loaded
     * into the browser.
     */
    private TCanvas tcanvas;
    private Board board; //store current fixed grid
    private Timer t;
    private GameMove move; //store the keyboard action for the piece
    private Piece piece; //store a current moving piece
    private Random random;
    private ActionListener performer;
    private int score;
    private boolean automode; //if it is AI's show
    private Player player; //computer AI agent

    @Override
    public void init() {
        board = new Board();
        random = new Random();
        move = GameMove.NULL;
        score = 0;
        piece = initPiece();
        tcanvas = new TCanvas(board, piece);
        automode = true;
        player = new Player();
        if (automode) {
            player.genMoves(board, piece);
        }
        initCanvas();
        performer = new ActionListener() { //Declare the methods for each timer action

            public void actionPerformed(ActionEvent e) {
                action();
            }
        };
        t = new Timer(50, performer);
        t.start();
        setFocusable(true);
        requestFocus();
        addKeyListener(this);
    }

    public final Piece initPiece() {
        int type = random.nextInt(7); //generate random piece type
        int orient = random.nextInt(O_NUM[type]); //generate random orientation according to type
        Piece new_piece = new Piece(PieceType.get(type), Orientation.get(orient)); //create a new instance of piece
        return new_piece;
    }

    public final void initCanvas() {
        tcanvas.setSize(TWIDTH, THEIGHT);
        tcanvas.setBackground(Color.WHITE);
        tcanvas.setStatus(GameStatus.PLAY);
        this.add(tcanvas);
    }

    public void new_game() {
        random = new Random();
        score = 0;
        initPiece();
        board = new Board();
        player.genMoves(board, piece);
        tcanvas.setBoard(board);
        tcanvas.setPiece(piece);
        tcanvas.setScore(score);
        t.start();
    }
    
    public void action() { //timer action
        if (!automode) {
            if (board.check_done(piece, GameMove.DOWN)) { // if the piece cannot move down
                board.bindPiece(piece); //the piece become history
                int pscore = board.checkFull(); //check if there is any score gained
                if (pscore != 0) { //if there is score
                    score += pscore; //then add score
                    tcanvas.setScore(score);
                }
                piece = initPiece(); //initialize a new piece
                tcanvas.setPiece(piece);
                if (board.check_done(piece, GameMove.DOWN)) { //if the new piece can't move down
                    tcanvas.setStatus(GameStatus.DEAD);
                    t.stop();
                    System.out.println("piece dead");
                }
            }
        } else {
            GameMove tmove = player.getMove();
            if (board.check_done(piece, GameMove.DOWN) && tmove == null) {
                board.bindPiece(piece); //the piece become history
                int pscore = board.checkFull(); //check if there is any score gained
                if (pscore != 0) { //if there is score
                    score += pscore; //then add score
                    tcanvas.setScore(score);
                }
                piece = initPiece(); //initialize a new piece
                tcanvas.setPiece(piece);
                if (board.check_done(piece, GameMove.DOWN)) { //if the new piece can't move down
                    tcanvas.setStatus(GameStatus.DEAD);
                    t.stop();
                    System.out.println("piece dead");
                } else {
                    player.genMoves(board, piece);
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
        tcanvas.repaint();
    }

    public void keyPressed(KeyEvent e) {
        if (!automode) { //if not automode
            switch (tcanvas.getStatus()) {
                case PLAY: {
                    move = GameMove.NULL;
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT:
                            move = GameMove.LEFT;
                            break;
                        case KeyEvent.VK_RIGHT:
                            move = GameMove.RIGHT;
                            break;
                        case KeyEvent.VK_DOWN:
                            move = GameMove.DROP;
                            break;
                        case KeyEvent.VK_SHIFT:
                            move = GameMove.CW;
                            break;
                        case KeyEvent.VK_UP:
                            move = GameMove.CW;
                            break;
                        case KeyEvent.VK_ENTER:
                            new_game();
                            break;
                        case KeyEvent.VK_SPACE:
                            if (t.isRunning()) {
                                t.stop();
                            } else {
                                t.restart();
                            }
                            break;
                        case KeyEvent.VK_9:
                            automode = !automode;
                            System.out.println("AUTOMODE!!1");
                            player.genMoves(board, piece);
                            break;
                    }
                    //if (!board.check_done(piece, move)) {
                    piece.move(move, board);
                    //}
                    break;
                }
                case DEAD: {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_ENTER:
                            new_game();
                            break;
                    }
                }
            }
            move = GameMove.NULL;
            tcanvas.repaint();
        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_9:
                    automode = !automode;
                    break;
                case KeyEvent.VK_ENTER:
                    random = new Random();
                    new_game();
                    break;
            }
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}
