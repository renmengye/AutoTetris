/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris;

/**
 *
 * @author rmy
 */
public class Rater implements ATCommon {

    public static final int height_coef = -50;
    public static final int holes_coef = -100;
    public static final int density_coef = 0;
    public static final int targeth_coef = -20;
    public static final int score_coef = 10000;
    private Board board;

    public int rate(Board board) {
        this.board = board;
        int pscore = board.checkFull();
        int height_score = rate_height(board);
        int holes_score = rate_holes(board);
        int targeth_score = rate_targeth(board);
        int final_rate = score_coef * pscore + height_coef * height_score + holes_coef * holes_score
                + density_coef * rate_density(board) + targeth_coef * targeth_score + 10000;
        System.out.println("new board rating::");
        board.printBoard();
        System.out.printf("pscore:%d height:%d holes:%d final:%d ", pscore, height_score, holes_score, final_rate);
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
        return height*height/10;
    }

    private int rate_holes(Board board) {
        int count = 0;
        for (int j = 2; j < YNUM; j++) {
            for (int i = 0; i < XNUM; i++) {
                if (board.getBoard()[j][i] == 0) {
                    for (int m = j - 1; m > 0; m--) {
                        if (board.getBoard()[m][i] == 1) {
                            count++;
                            if (count > 2) {
                                break;
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
        for (j = YNUM - 1; j > 0 && !board.checkEmpty(j); j--) {
            sum = board.sum_line(j);
        }
        return (int) sum * 100 / (XNUM * (YNUM - j));
    }

    private int rate_targeth(Board board) {
        int targeth = 0;
        int j = 0;
        outer:
        for (j = YNUM - 1; j > 0 && !board.checkEmpty(j); j--) {
            for (int i = 0; i < XNUM; i++) {
                if (board.getBoard()[j][i] == 0) {
                    if (board.getBoard()[j - 1][i] == 1) {
                        targeth = j;
                    }
                }
            }
        }
        if (j < YNUM * 2 / 3) {
            return j;
        } else if (j - targeth > 2) {
            return j - 2;
        } else {
            return targeth;
        }
    }
}
