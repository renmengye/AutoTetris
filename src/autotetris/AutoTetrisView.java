/*
 * Author: Mengye Ren
 * Application's main frame, control centre
 */
package autotetris;

import autotetris.ai.Player;
import autotetris.elements.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.SingleFrameApplication;

public class AutoTetrisView extends FrameView implements ATCommon, KeyListener {
    
    private TCanvas tcanvas;
    private Board board;                //store current fixed grid
    private Timer t;
    private GameMove move;              //store the keyboard action for the piece
    private Piece piece;                //store a current moving piece
    private Random random;
    private ActionListener performer;
    private int score;                  //store the score of the game
    private boolean automode;           //if it is AI's show
    private Player player;              //computer AI agent

    public AutoTetrisView(SingleFrameApplication app) {
        super(app);
        initComponents();
        JFrame mainFrame = getFrame();
        mainFrame.setTitle("Auto Tetris");
        mainFrame.setSize(TWIDTH, THEIGHT);
        mainFrame.setLocation(200, 200);
        mainFrame.setResizable(false);
        mainFrame.addKeyListener(this);
        mainFrame.setVisible(false);
        tcanvas = new TCanvas(board, piece);
        initCanvas();
        menuBar.setVisible(false);
        board = new Board();
        random = new Random();
        move = GameMove.NULL;
        score = 0;
        piece = initPiece();
        automode = true;
        player = new Player();
        if (automode) {
            player.genMoves(board, piece);
        }
        
        //Declare the methods for each timer action
        performer = new ActionListener() { 

            public void actionPerformed(ActionEvent e) {
                action();
            }
        };
        t = new Timer(50, performer);
        t.start();
    }

    //initialize canvas
    public final void initCanvas() { 
        tcanvas.setSize(TWIDTH, THEIGHT);
        tcanvas.setBackground(Color.WHITE);
        tcanvas.setStatus(GameStatus.PLAY);
        mainPanel.add(tcanvas);
    }

    //initialize piece
    public final Piece initPiece() {
        
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
        tcanvas.setBoard(board);
        tcanvas.setPiece(piece);
        tcanvas.setScore(score);
        player.genMoves(board, piece);
        t.start();
    }

    //timer action
    public void action() {
        
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
            }
            
        } 
        
        //if it is auto mode
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
    }
    

    //keyboard control
    public void keyPressed(KeyEvent e) {
        
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
                            System.out.println("AUTOMODE!!");
                            player.genMoves(board, piece);
                            break;
                    }
                    piece.move(move, board);
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
                    new_game();
                    break;
            }
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setPreferredSize(new java.awt.Dimension(202, 382));

        org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 307, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 391, Short.MAX_VALUE)
        );

        menuBar.setName("menuBar"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(autotetris.AutoTetrisApp.class).getContext().getResourceMap(AutoTetrisView.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(autotetris.AutoTetrisApp.class).getContext().getActionMap(AutoTetrisView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setComponent(mainPanel);
        setMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    // End of variables declaration//GEN-END:variables
}
