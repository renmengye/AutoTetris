/*
 * Author: Mengye Ren
 * Board Class provide an instance of the grid matrix and do matrix operation
 */
package autotetris;

public class Board implements ATCommon {

    private byte[][] board = new byte[YNUM][XNUM]; //store the matrix of grids

    //initialize a blank board
    public Board() {
    }

    //initialize an existing board
    public Board(byte[][] board) {
        this.board = board;
    }

    //manually set the board
    protected void setBoard(byte[][] board) {
        this.board = board;
    }

    //return a matrix of grids
    protected byte[][] getBoard() {
        return board;
    }

    //output the matrix in the console for debugging, obsolete
    protected void printBoard() {
        for (int j = 0; j <= YNUM; j++) {
            for (int i = 0; i <= XNUM; i++) {
                System.out.print(board[j][i]);
            }
            System.out.println();
        }
    }

    //input another board, binding the two boards and update into the matrix
    protected void bindBoard(Board tobind) {
        for (int j = 0; j <= YNUM; j++) {
            for (int i = 0; i <= XNUM; i++) {
                board[j][i] = bool2byte(byte2bool(board[j][i]) || byte2bool(tobind.getBoard()[j][i]));
            }
        }
    }

    //calculate the sum of the board, special value
    protected int sum() {
        int sum = 0;
        for (int j = 0; j <= YNUM; j++) {
            for (int i = 0; i <= XNUM; i++) {
                sum += board[j][i];
            }
        }
        return sum;
    }

    //check if the current the piece can move or not
    protected boolean check_done(Piece piece){
        return true;
    }

    //convert from byte to boolean, tool
    public static boolean byte2bool(byte x) {
        return x == 1;
    }

    //conver from boolean to byte, tool
    public static byte bool2byte(boolean x) {
        return x ? (byte) 1 : (byte) 0;
    }
}
