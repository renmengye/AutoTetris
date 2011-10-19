/*
 * Author: Mengye Ren
 * Application's main frame, control centre
 */
package autotetris;

import java.awt.Color;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.Timer;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class AutoTetrisView extends FrameView implements ATCommon, KeyListener {

    public AutoTetrisView(SingleFrameApplication app) {
        super(app);
        initComponents();
        ResourceMap resourceMap = getResourceMap();
        JFrame mainFrame = getFrame();
        mainFrame.setTitle("Auto Tetris");
        mainFrame.setSize(TWIDTH, THEIGHT);
        mainFrame.setLocation(200, 200);
        mainFrame.setResizable(false);
        mainFrame.addKeyListener(this);
        menuBar.setVisible(false);
        board = new Board();
        random = new Random();
        move = GameMove.NULL;
        score = 0;
        initPiece();
        tcanvas = new TCanvas(board, piece);
        new_round = true;
        initCanvas();
        performer = new ActionListener() { //Declare the methods for each timer action

            public void actionPerformed(ActionEvent e) {
                if (board.check_done(piece, GameMove.DOWN)) { //if can't move, then goes to new round
                    if (!new_round) { //if it is not a new round, then generate new piece
                        new_round = true;
                        board.setBoard(board.bindBoard(piece.getBoard()).getBoard());
                        int pscore = board.checkFull();
                        if (pscore != 0) {
                            score += pscore;
                            tcanvas.setScore(score);
                        }
                        initPiece();
                        tcanvas.setPiece(piece);
                    } else { //if it is new round, then dead
                        tcanvas.setStatus(GameStatus.DEAD);
                        t.stop();
                        System.out.println("piece dead");
                    }
                } else { //if we can still move the piece, then move
                    //if can still move down, move down
                    piece.move(GameMove.DOWN);
                    new_round = false;
                }
                move = GameMove.NULL;
                tcanvas.repaint();
            }
        };
        t = new Timer(500, performer);
        t.start();
    }

    public void keyPressed(KeyEvent e) {
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
                move = GameMove.DROP;
                break;
            case KeyEvent.VK_SPACE:
                if (t.isRunning()) {
                    t.stop();
                } else {
                    t.restart();
                }
                break;
        }
        if (!board.check_done(piece, move)) {
            if (move != GameMove.DROP) {
                piece.move(move);
                new_round = false;
            } else {
                while (!board.check_done(piece, GameMove.DOWN)) { //if drop, moving down until done
                    piece.move(GameMove.DOWN);
                    new_round = false;
                }
            }
        }
        move = GameMove.NULL;
        tcanvas.repaint();
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = AutoTetrisApp.getApplication().getMainFrame();
            aboutBox = new AutoTetrisAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        //AutoTetrisApp.getApplication().show(aboutBox);
    }

    public void initCanvas() {
        tcanvas.setSize(TWIDTH, THEIGHT);
        tcanvas.setBackground(Color.WHITE);
        mainPanel.add(tcanvas);
    }

    public void initPiece() {
        int type = random.nextInt(7); //generate random piece type
        int orient = random.nextInt(O_NUM[type]); //generate random orientation according to type
        piece = new Piece(PieceType.get(type), Orientation.get(orient)); //create a new instance of piece
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
    private JDialog aboutBox;
    private TCanvas tcanvas;
    private Board board; //store current fixed grid
    private Timer t;
    private GameMove move; //store the keyboard action for the piece
    private Piece piece; //store a current moving piece
    private boolean new_round; //store whether initiate a new piece
    private Random random;
    private ActionListener performer;
    private int score;
}
