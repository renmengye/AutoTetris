package autotetris;

import autotetris.ai.Player;
import autotetris.elements.*;
import java.awt.Color;
import java.awt.event.*;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author rmy
 */
public class AutoTetrisFrame extends JFrame implements ATCommon{
    
    private JPanel mainPanel;
    private TCanvas tcanvas;
    private Timer t;
    private GameMove move;              //store the keyboard action for the piece
    private boolean automode;           //if it is AI's show
    private Player player;              //computer AI agent
    private GameHost host;

    public AutoTetrisFrame() {
        
        this.setTitle("Auto Tetris");
        this.setSize(TWIDTH+20, THEIGHT+40);
        this.setLocation(200, 200);
        this.setResizable(false);
        
        
        mainPanel = new JPanel();
        this.add(mainPanel);
        
        
        host = new GameHost();
        
        
        player = new Player(host);
        
        tcanvas = new TCanvas(host.getBoard(), host.getPiece());
        initCanvas();
        
        
        move = GameMove.NULL;
        automode = true;
        //automode = false;

        t = new Timer(20, new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                tcanvas.setBoard(host.getBoard());
                tcanvas.setPiece(host.getPiece());
                tcanvas.repaint();
            }
        });
        t.start();
        
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        
        this.addKeyListener(new KeyListener(){

            public void keyTyped(KeyEvent ke) {
            }

            public void keyPressed(KeyEvent ke) {
                keyPress(ke);
            }

            public void keyReleased(KeyEvent ke) {
            }
            
        });
        
    }

    //initialize canvas
    private void initCanvas() {
        tcanvas.setSize(TWIDTH, THEIGHT);
        tcanvas.setBackground(Color.WHITE);
        tcanvas.setStatus(GameStatus.PLAY);
        mainPanel.add(tcanvas);
    }

    
    //timer action
    /*public void action() {

        //if it is manual mode
        if (!automode) {

            // if the piece cannot move down
            if (board.check_done(piece, GameMove.DOWN)) {

                //the piece become history
                board.bindPiece(piece);

                //check if there is any score gained
                int pscore = board.checkFull();

                //if there is score then add score
                if (pscore != 0) {
                    score += pscore;
                    tcanvas.setScore(score);
                }

                //initialize a new piece
                piece = initPiece();
                tcanvas.setPiece(piece);


                //if the new piece can't move down
                if (board.check_done(piece, GameMove.DOWN)) {
                    tcanvas.setStatus(GameStatus.DEAD);
                    t.stop();
                    System.out.println("piece dead");
                }
            } else {
                piece.move(GameMove.DOWN, board);
            }
            
        } //if it is auto mode
        else {

            //get AI agent's get the next move of the piece
            GameMove tmove = player.getMove();

            //if the piece cannot move down further
            if (board.check_done(piece, GameMove.DOWN) && tmove == null) {

                //the piece become history
                board.bindPiece(piece);

                //check if there is any score gained
                int pscore = board.checkFull();

                //if there is score then add score
                if (pscore != 0) {
                    score += pscore;
                    tcanvas.setScore(score);
                }

                //initialize a new piece
                piece = initPiece();
                tcanvas.setPiece(piece);

                //if the new piece can't move down then dead
                if (board.check_done(piece, GameMove.DOWN)) {
                    tcanvas.setStatus(GameStatus.DEAD);
                    t.stop();
                    System.out.println("piece dead");
                } 
                
                //otherwise let the AI agent prepare for the new piece
                else {
                    player.genMoves(board, piece);
                }
            }

            //if the AI agent does have any moves prepared
            if (tmove != null) {
                piece.move(tmove, board);
            }

            //move down as every round
            if (!board.check_done(piece, GameMove.DOWN)) {
                piece.move(GameMove.DOWN, board);
            }
        }

        //set move to null
        move = GameMove.NULL;

        //refresh canvas
        tcanvas.repaint();
    }*/

    //keyboard control
    public void keyPress(KeyEvent e) {

        //if not automode
        if (!automode) {
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
                            host.new_game();
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
                            System.out.println("AUTOMODE!!");
                            //player.genMoves(board, piece);
                            break;
                    }
                    //piece.move(move, board);
                    break;
                }
                case DEAD: {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_ENTER:
                            //new_game();
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
                    //new_game();
                    break;
            }
        }
    }
}
