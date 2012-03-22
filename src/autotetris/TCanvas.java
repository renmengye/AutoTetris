package autotetris;

import autotetris.elements.Board;
import autotetris.elements.GameStatus;
import autotetris.elements.Piece;
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
    private int score;

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
    
    public GameStatus getStatus(){
        return state;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public void setScore(int score){
        this.score = score;
    }

    @Override
    public void paint(Graphics g) {
        
        //draw the frame
        g.drawRect(1, 1, TWIDTH-2, THEIGHT-2);
        
        //draw the board
        byte[][] grid = board.toArray();
        
        for (int j = 0; j < YNUM; j++) {
            for (int i = 0; i < XNUM; i++) {
                if (grid[j][i] == 1) {
                    g.fillRect(i * (XDIM + GAP) + GAP, j * (YDIM + GAP) + GAP, XDIM, YDIM);
                }
            }
        }
        
        //draw the piece
        for (int i=0;i<=3;i++){
            int x=piece.getX()+piece.getContour(i, CONTOUR_DX);
            int y=piece.getY()+piece.getContour(i,CONTOUR_DY);
            g.fillRect(x * (XDIM + GAP) + GAP, y * (YDIM + GAP) + GAP, XDIM, YDIM);
        }
        
        //draw the score
        g.drawString("SCORE: "+score, TWIDTH-80, GAP+10);
    }
}
