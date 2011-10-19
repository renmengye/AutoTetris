/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris;

import java.awt.Canvas;
import java.awt.Graphics;

/**
 *
 * @author MengYe
 */

public class TCanvas extends Canvas implements ATCommon {

    private Board board;
    private GameStatus state;

    public TCanvas(Board board) {
        this.board = board;
        this.state = GameStatus.PLAY;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setStatus(GameStatus state) {
        this.state = state;
    }

    @Override
    public void paint(Graphics g) {
        for (int i = 0; i < YNUM; i++) {
            for (int j = 0; j < XNUM; j++) {
                g.fillRect(j * (XDIM + GAP) + GAP, i * (YDIM + GAP) + GAP, XDIM, YDIM);
            }
        }

    }
}
