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
    private Piece piece;

    public TCanvas(Board board, Piece piece) {
        this.board = board;
        this.state = GameStatus.PLAY;
        this.piece = piece;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setStatus(GameStatus state) {
        this.state = state;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    @Override
    public void paint(Graphics g) {
        //System.out.println("hey");
        //board.printBoard();
        byte[][] grid = board.getBoard();
        byte[][] pgrid = piece.getBoard().getBoard();
        for (int j = 0; j < YNUM; j++) {
            for (int i = 0; i < XNUM; i++) {
                if (grid[j][i] == 1 || pgrid[j][i] == 1) {
                    g.fillRect(i * (XDIM + GAP) + GAP, j * (YDIM + GAP) + GAP, XDIM, YDIM);
                }
            }
        }

    }
}
