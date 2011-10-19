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
    public void setBoard(byte[][] board) {
        this.board = board;
    }

    //return a matrix of grids
    public byte[][] getBoard() {
        return board;
    }

    //output the matrix in the console for debugging, obsolete
    public void printBoard() {
        for (int j = 0; j < YNUM; j++) {
            for (int i = 0; i < XNUM; i++) {
                System.out.print(board[j][i]);
            }
            System.out.println();
        }
    }

    //input another board, binding the two boards and update into the matrix
    public Board bindBoard(Board tobind) {
        Board result = new Board();
        for (int j = 0; j < YNUM; j++) {
            for (int i = 0; i < XNUM; i++) {
                result.getBoard()[j][i] = bool2byte(byte2bool(board[j][i]) || byte2bool(tobind.getBoard()[j][i]));
            }
        }
        return result;
    }

    //calculate the sum of the board, special value
    public int sum() {
        int sum = 0;
        for (int j = 0; j < YNUM; j++) {
            for (int i = 0; i < XNUM; i++) {
                sum += board[j][i];
            }
        }
        return sum;
    }

    //check if the current the piece can move or not
    public boolean check_done(Piece piece, GameMove move){
        Piece test = piece.clone();
        int mysum=sum();
        int testsum = bindBoard(test.getBoard()).sum();
        if(test.move(move)){
            return (testsum>bindBoard(test.getBoard()).sum());
        }else{
            return true;
        }
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
