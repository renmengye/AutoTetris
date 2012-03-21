/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris.elements;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rmy
 */
public class PieceMover extends Thread {

    private GameMove move;
    private Board board;
    private Piece piece;
    public boolean ready;

    public PieceMover(Board board) {
        this.board = board;
    }

    public synchronized void setMove(GameMove move) {
        this.move=move;
        ready=true;
    }

    @Override
    public void run() {
        
        //the thread will run forever unless specified
        while (true) {
            
            //if not ready to move piece then wait
            if (!ready) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PieceMover.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } 
            
            //if move piece order is here
            else {
                piece.move(move, board);
                ready=false;
            }
        }
    }
}
