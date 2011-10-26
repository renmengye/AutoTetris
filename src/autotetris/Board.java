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

    public int sum_line(int line) {
        int r = 0;
        for (int i = 0; i < XNUM; i++) {
            r += board[line][i];
        }
        return r;
    }

    //check if the current the piece can move or not
    public boolean check_done_old(Piece piece, GameMove move) {
        Piece test = piece.clone();
        int mysum = sum(); //the board sum
        int piecesum = test.getBoard().sum(); //the piece sum
        if (test.move(move,this)) { //if move is not out of bound
            return (bindBoard(test.getBoard()).sum() < mysum + piecesum); //if sum equals after bind then not done, otherwise done
        } else {
            return true; //if out of bound then done
        }
    }
    
    //check if the current the piece can move or not
    public boolean check_done(Piece piece, GameMove move) {
        Piece test = piece.clone();
        boolean mytest=false;
        if (test.move(move,this)) { //if move is not out of bound
            for(int j=0;j<YNUM;j++){
                for(int i=0;i<XNUM;i++){
                    if(test.getBoard().getBoard()[j][i]+board[j][i]==2){
                        mytest=true;
                    }
                }
            }
            if(mytest){
                return true;
            }else{
                return check_done_old(piece, move);
            }
        } else {
            return true; //if out of bound then done
        }
    }

    public int checkFull() {
        int fullcount = 0;
        for (int j = 0; j < YNUM; j++) {
            if (sum_line(j) == XNUM) {
                fullcount++;
                for (int k = j; k > 0; k--) {
                    for (int m = 0; m < XNUM; m++) {
                        board[k][m] = board[k - 1][m];
                    }
                }
                for (int k = 0; k < XNUM; k++) {
                    board[0][k] = 0;
                }
            }

        }
        return SCORE[fullcount];
    }

    
    //convert from byte to boolean, tool
    public static boolean byte2bool(byte x) {
        return x == 1;
    }

    //conver from boolean to byte, tool
    public static byte bool2byte(boolean x) {
        return x ? (byte) 1 : (byte) 0;
    }

    @Override
    public Board clone() {
        return new Board(board);
    }
}
