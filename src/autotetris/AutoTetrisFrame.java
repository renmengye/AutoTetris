package autotetris;

import autotetris.ai.Player;
import autotetris.elements.*;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author rmy
 */
public class AutoTetrisFrame extends JFrame implements ATCommon {

    private JPanel mainPanel;
    private TCanvas tcanvas;
    private Timer t;
    private GameMove move;              //store the keyboard action for the piece
    private boolean automode;           //if it is AI's show
    private Player player;              //computer AI agent
    private GameHost host;
    private static int player_react = 0;
    private static int host_react = 200;

    public AutoTetrisFrame() {

        this.setTitle("Auto Tetris");
        this.setSize(TWIDTH + 20, THEIGHT + 40);
        this.setLocation(200, 200);
        this.setResizable(false);

        mainPanel = new JPanel();
        this.add(mainPanel);


        /*host = new GameHost(host_react);


        player = new Player(host, player_react);

        host.setPlayer(player);*/
        new_game();

        tcanvas = new TCanvas(host.getBoard(), host.getPiece());
        mainPanel.add(tcanvas);

        move = GameMove.NULL;
        automode = true;
        //automode = false;

        t = new Timer(5, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                tcanvas.setBoard(host.getBoard());
                tcanvas.setPiece(host.getPiece());
                tcanvas.setScore(host.getScore());
                tcanvas.repaint();
            }
        });
        t.start();
        //host.start();

        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });


        this.addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent ke) {
            }

            public void keyPressed(KeyEvent ke) {
                keyPress(ke);
            }

            public void keyReleased(KeyEvent ke) {
            }
        });

    }

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
                            break;
                    }
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
                    new_game();
                    break;
            }
        }
    }

    private void new_game() {
        host = new GameHost(host_react);
        player = new Player(host, player_react);
        host.setPlayer(player);
        host.start();
    }
}
