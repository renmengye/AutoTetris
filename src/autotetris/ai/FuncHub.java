package autotetris.ai;

import autotetris.ATCommon;
import autotetris.elements.Board;

/**
 *
 * @author MengYe
 */
public class FuncHub implements ATCommon{
    
    public static int score(Board board){
        return board.checkFull();
    }
    
    public static int height(Board board) {
        int height;
        for (height = 0; height < YNUM; height++) {
            if (!board.checkEmpty(height)) {
                break;
            }
        }
        height = YNUM - height;
        return height*height;
    }

    public static int holes(Board board) {
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

    public static int density(Board board) {
        int sum = 0;
        int j = 0;
        for (j = 0; j < YNUM; j++) {
            sum = board.sum_line(j);
        }
        return 0;
    }

    public static int tar_height(Board board) {
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

    public static int long_holes(Board board) {

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
    
    
    public static float gauss(){
        return 0f;
    }
    
    public static float linear(){
        return 0f;
    }
}
