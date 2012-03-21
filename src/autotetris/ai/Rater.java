package autotetris.ai;

import autotetris.ATCommon;
import autotetris.elements.Board;
import autotetris.elements.Piece;

/**
 *
 * @author rmy
 */
public class Rater implements ATCommon {

    
     public static int height_coef = -4; public static int holes_coef = -50;
     public static int density_coef = 0; public static int targeth_coef = 30;
     public static int score_coef = 200; public static int long_hole_coef =-25;
     
    public Board board;

    public Rater() {
    }

    public Rater(final Board board) {

        this.board = board;
    }

    public int rate(Board board, Piece piece) {
        this.board = board;
        
         int pscore = board.checkFull(); 
         
         int height_score = FuncHub.height(board); 
         
         int holes_score = FuncHub.holes(board); 
         
         int targeth_score = FuncHub.tar_height(board); 
         
         int long_hole_score = FuncHub.long_holes(board); 
         
         int final_rate = score_coef * pscore +
         height_coef * height_score + holes_coef * holes_score + targeth_coef * targeth_score + long_hole_score *
         long_hole_coef + 10000;
         
        //System.out.println("new board rating::");
        //board.printBoard(piece);
        //System.out.printf("pscore:%d height:%d holes:%d targeth:%d long:%d final:%d ", pscore, height_score, holes_score, targeth_score, long_hole_score, final_rate);
        return final_rate;
    }
}
