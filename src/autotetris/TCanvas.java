package autotetris;

import autotetris.elements.Board;
import autotetris.elements.GameStatus;
import autotetris.elements.Grid;
import autotetris.elements.Piece;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

/**
 *
 * @author MengYe
 */
public class TCanvas extends Canvas implements ATCommon {

    private Board board;
    private GameStatus state;
    private Piece piece;
    private int score;

    public TCanvas(Board board, Piece piece) {
        this.board = board;
        this.state = GameStatus.PLAY;
        this.piece = piece;


        setSize(TWIDTH, THEIGHT);
        setBackground(Color.WHITE);

    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setStatus(GameStatus state) {
        this.state = state;
    }

    public GameStatus getStatus() {
        return state;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public void paint(Graphics g) {

        //draw the frame
        g.drawRect(1, 1, TWIDTH - 2, THEIGHT - 2);

        //draw the board
        Grid[][] grid = board.toArray();

        for (int j = 0; j < YNUM; j++) {
            for (int i = 0; i < XNUM; i++) {
                if (grid[j][i].value == 1) {
                    g.setColor(grid[j][i].color);
                    g.fillRect(i * (XDIM + GAP) + GAP, j * (YDIM + GAP) + GAP, XDIM, YDIM);
                    g.setColor(Color.black);
                    g.drawRect(i * (XDIM + GAP) + GAP, j * (YDIM + GAP) + GAP, XDIM, YDIM);
                }
            }
        }

        //draw the piece
        for (int i = 0; i <= 3; i++) {
            int x = piece.getX() + piece.getContour(i, CONTOUR_DX);
            int y = piece.getY() + piece.getContour(i, CONTOUR_DY);

            g.setColor(piece.getType().color());
            g.fillRect(x * (XDIM + GAP) + GAP, y * (YDIM + GAP) + GAP, XDIM, YDIM);
            g.setColor(Color.black);
            g.drawRect(x * (XDIM + GAP) + GAP, y * (YDIM + GAP) + GAP, XDIM, YDIM);
        }

        g.setColor(Color.black);
        //draw the score
        g.drawString("SCORE: " + score, TWIDTH - 80, GAP + 10);

        //g.drawImage(offscreen, 0, 0, this);
    }

    //double buffer
    @Override
    public void update(Graphics g) {
        createBufferStrategy(2);
        BufferStrategy bf = this.getBufferStrategy();
        g = bf.getDrawGraphics();
        paint(g);
        g.dispose();
        bf.show();
        Toolkit.getDefaultToolkit().sync();
    }
}
