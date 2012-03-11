package autotetris.ai;

import autotetris.ATCommon;
import autotetris.ai.neurons.InputNeuron;
import autotetris.ai.neurons.OutputNeuron;
import autotetris.ai.neurons.Perceptron;
import autotetris.elements.Board;
import autotetris.elements.Piece;

/**
 *
 * @author rmy
 */
public class Rater implements ATCommon {

    /*
     * public static int height_coef = -4; public static int holes_coef = -50;
     * public static int density_coef = 0; public static int targeth_coef = 30;
     * public static int score_coef = 200; public static int long_hole_coef =
     * -25;
     */
    public Board board;

    public Rater() {
    }

    public Rater(final Board board) {

        this.board = board;

        /*//define all the neurons
        int isize = Attribute.size();
        int psize = 10;
        InputNeuron[] input = new InputNeuron[isize];
        Perceptron[] percpt = new Perceptron[psize];
        OutputNeuron out = new OutputNeuron(0,new Function_a() {
            public double func(double a) {
                return a;
            }
            public double dfunc(double a) {
                return 1;
            }
        });

        //add all the input neurons for each attribute
        for (int i = 0; i < isize; i++) {

            if (Attribute.get(i) == Attribute.SCORE) {
                input[i] = new InputNeuron(i, new Function_s() {
                    public int func(Board b) {
                        return FuncHub.score(b);
                    }
                }, board);
            } else if (Attribute.get(i) == Attribute.HEIGHT) {
                input[i] = new InputNeuron(i, new Function_s() {
                    public int func(Board b) {
                        return FuncHub.height(b);
                    }
                }, board);
            } else if (Attribute.get(i) == Attribute.HOLES) {
                input[i] = new InputNeuron(i, new Function_s() {
                    public int func(Board b) {
                        return FuncHub.holes(b);
                    }
                }, board);
            } else if (Attribute.get(i) == Attribute.TAR_HEIGHT) {
                input[i] = new InputNeuron(i, new Function_s() {
                    public int func(Board b) {
                        return FuncHub.tar_height(b);
                    }
                }, board);
            } else if (Attribute.get(i) == Attribute.LONG_HOLES) {
                input[i] = new InputNeuron(i, new Function_s() {
                    public int func(Board b) {
                        return FuncHub.long_holes(board);
                    }
                }, board);
            }
        }
        
        //add all the perceptrons
        for (int i = 0; i < psize; i++) {
            percpt[i] = new Perceptron(i, new Function_a() {
                public double func(double a) {
                    return FuncHub.tanh(a);
                }
                public double dfunc(double a) {
                    return FuncHub.dtanh(a);
                }
            });
            
            //add the perceptron to all input neurons
            for(int j=0;j<isize;j++){
                input[j].add_target(percpt[i]);
            }
            
            //add the output neuron to perceptrons
            percpt[i].add_target(out);
        }*/
    }

    public int rate(Board board, Piece piece) {
        this.board = board;
        /*
         * int pscore = board.checkFull(); int height_score =
         * rate_height(board); int holes_score = rate_holes(board); int
         * targeth_score = rate_targeth(board); int long_hole_score =
         * rate_long_hole(board); int final_rate = score_coef * pscore +
         * height_coef * height_score + holes_coef * holes_score + density_coef *
         * rate_density(board) + targeth_coef * targeth_score + long_hole_score *
         * long_hole_coef + 10000;
         */
        //System.out.println("new board rating::");
        //board.printBoard(piece);
        //System.out.printf("pscore:%d height:%d holes:%d targeth:%d long:%d final:%d ", pscore, height_score, holes_score, targeth_score, long_hole_score, final_rate);
        return 0;
    }
}
