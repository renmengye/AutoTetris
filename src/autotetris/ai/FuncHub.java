package autotetris.ai;

import autotetris.ATCommon;
import autotetris.elements.Board;

/**
 *
 * @author MengYe
 */
public class FuncHub implements ATCommon{

    public static int xor(double a, double b) {
        if ((a == 0 && b != 0) || (b == 0 && a != 0)) {
            return 1;
        } else {
            return 0;
        }
    }

    public static int and(double a, double b) {
        if (a != 0 && b != 0) {
            return 1;
        } else {
            return 0;
        }
    }
    
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
        int count = 0;
        outer:
        for (int j = 2; j < YNUM; j++) {
            for (int i = 0; i < XNUM; i++) {
                if (board.toArray()[j][i].value == 0) {
                    for (int m = j - 1; m > 0; m--) {
                        if (board.toArray()[m][i].value == 1) {
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
        for (int j = 0; j < YNUM; j++) {
            sum = board.sum_line(j);
        }
        return 0;
    }

    public static int tar_height(Board board) {
        int targeth = YNUM-1;
        for (int j = YNUM - 1; j > 0 && !board.checkEmpty(j); j--) {
            for (int i = 0; i < XNUM; i++) {
                if (board.toArray()[j][i].value == 0) {
                    if (board.toArray()[j - 1][i].value == 1) {
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
                    if (board.toArray()[j][i].value == 1) {
                        counti = 0;
                    } else {
                        if (i > 0) {
                            if (i < XNUM - 1) {
                                if (board.toArray()[j][i + 1].value == 1 && board.toArray()[j][i - 1].value == 1) {
                                    counti++;
                                }
                            } else {
                                if (board.toArray()[j][i - 1].value == 1) {
                                    counti++;
                                }
                            }
                        } else {
                            if (board.toArray()[j][i + 1].value == 1) {
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
    
    
    public static double tanh(double a){
        return Math.tanh(a);
    }
    
    public static double dtanh(double a){
        return (1-Math.pow(Math.tanh(a),2));
    }
    
    public static double gauss(double a){
        return Math.exp(-a*a);
    }
    
    public static double dgauss(double a){
        return Math.exp(-a*a)*(-1)*2*a;
    }
    
    public static double sigmoid(double a){
        return 1.0/(1+Math.exp(-a));
    }
    
    public static double dsigmoid(double a){
        double b=Math.exp(-a);
        return b*Math.pow(1.0/(1+b),2);
    }
}
