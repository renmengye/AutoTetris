/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris;

/**
 *
 * @author 
 * rmy
 */
public class Rater implements ATCommon {

    public static int height_coef = -4;
    public static int holes_coef = -50;
    public static int density_coef = 0;
    public static int targeth_coef = 30;
    public static int score_coef = 200;
    public static int long_hole_coef = -25;
    private Board board;

    public Rater() {
    }
    
    public Rater(int height,int holes,int target,int score,int lhole){
        height_coef=height;
        holes_coef=holes;
        targeth_coef=target;
        score_coef=score;
        long_hole_coef=lhole;
    }

    public int rate(Board board, Piece piece) {
        this.board = board;
        int pscore = board.checkFull();
        int height_score = rate_height(board);
        int holes_score = rate_holes(board);
        int targeth_score = rate_targeth(board);
        int long_hole_score = rate_long_hole(board);
        int final_rate = score_coef * pscore + height_coef * height_score + holes_coef * holes_score
                + density_coef * rate_density(board) + targeth_coef * targeth_score + long_hole_score * long_hole_coef + 10000;
        //System.out.println("new board rating::");
        //board.printBoard(piece);
        //System.out.printf("pscore:%d height:%d holes:%d targeth:%d long:%d final:%d ", pscore, height_score, holes_score, targeth_score, long_hole_score, final_rate);
        return final_rate;
    }

    private int rate_height(Board board) {
        int height;
        for (height = 0; height < YNUM; height++) {
            if (!board.checkEmpty(height)) {
                break;
            }
        }
        height = YNUM - height;
        return height*height;
    }

    private int rate_holes(Board board) {
        int count = 0, count2 = 0;
        outer:
        for (int j = 2; j < YNUM; j++) {
            for (int i = 0; i < XNUM; i++) {
                if (board.getBoard()[j][i] == 0) {
                    for (int m = j - 1; m > 0; m--) {
                        if (board.getBoard()[m][i] == 1) {
                            count++;
                            if (j - m > 2) {
                                count--;
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    private int rate_density(Board board) {
        int sum = 0;
        int j = 0;
        for (j = 0; j < YNUM; j++) {
            sum = board.sum_line(j);
        }
        return 0;
    }

    private int rate_targeth(Board board) {
        int targeth = YNUM-1;
        int j=0;
        outer:
        for (j = YNUM - 1; j > 0 && !board.checkEmpty(j); j--) {
            for (int i = 0; i < XNUM; i++) {
                if (board.getBoard()[j][i] == 0) {
                    if (board.getBoard()[j - 1][i] == 1) {
                        targeth = j - 1;
                    }
                }
            }
        }
        return targeth;
    }

    private int rate_long_hole(Board board) {

        int result = 0;
        for (int i = 0; i < XNUM; i++) {
            int counti = 0;
            int j;
            for (j = YNUM - 1; j > 0; j--) {
                if (!board.checkEmpty(j)) {
                    if (board.getBoard()[j][i] == 1) {
                        counti = 0;
                    } else {
                        if (i > 0) {
                            if (i < XNUM - 1) {
                                if (board.getBoard()[j][i + 1] == 1 && board.getBoard()[j][i - 1] == 1) {
                                    counti++;
                                }
                            } else {
                                if (board.getBoard()[j][i - 1] == 1) {
                                    counti++;
                                }
                            }
                        } else {
                            if (board.getBoard()[j][i + 1] == 1) {
                                counti++;
                            }
                        }
                    }
                } else {
                    break;
                }
            }
            if (j < YNUM / 2 && counti > 4) {
                result += counti - 4;
            } else if (j >= YNUM / 2 && counti > 2) {
                result += counti - 2;
            }
        }
        return result;
    }
}
