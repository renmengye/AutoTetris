/*
 * Author: Mengye Ren
 * Board Class provide an instance of the grid matrix and do matrix operation
 */
package autotetris.elements;

import autotetris.ATCommon;

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
                if (board[j][i] == 1) {
                    System.out.print("◼");
                } else {
                    System.out.print("◻");
                }
            }
            System.out.println();
        }
    }

    public void printBoard(Piece piece) {
        for (int j = 0; j < YNUM; j++) {
            inner:
            for (int i = 0; i < XNUM; i++) {
                //boolean is_piece = false;
                for (int k = 0; k <= 3; k++) {
                    int x = piece.getX() + piece.getContour(k, CONTOUR_DX);
                    int y = piece.getY() + piece.getContour(k, CONTOUR_DY);
                    if (x == i && y == j) {
                        System.out.print("▩");
                        continue inner;
                    }
                }
                //if (!is_piece) {
                    if (board[j][i] == 1) {
                        System.out.print("◼");
                    } else {
                        System.out.print("◻");
                    }
                //}
            }
            System.out.println();
        }
    }

    public void bindPiece(Piece piece) {
        //System.out.println("binding piece occuring");
        for (int i = 0; i <= 3; i++) {
            int x = piece.getX() + piece.getContour(i, CONTOUR_DX);
            int y = piece.getY() + piece.getContour(i, CONTOUR_DY);
            if (piece.check_point_range(x, y)) { //check the point is in range
                board[y][x] = 1;
            }
        }
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

    public boolean check_done(Piece piece, GameMove move) {
        
        //first a range check
        Piece test_piece = piece.clone();
        if (!test_piece.move(move, this)) {
            //System.out.println("checked done because can't move");
            return true;
        }

        //second a board check
        for (int i = 0; i <= 3; i++) {
            int y = test_piece.getY() + piece.getContour(i, CONTOUR_DY);
            int x = test_piece.getX() + test_piece.getContour(i, CONTOUR_DX);
            if (board[y][x] == 1) {
                //System.out.println("checked done because occupied");
                return true;
            }
        }
        
        return false;
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

    public boolean checkEmpty(int line) {
        for (int i = 0; i < XNUM; i++) {
            if (board[line][i] == 1) {
                return false;
            }
        }
        return true;
    }

    public boolean check4(int line) {
        int count = 0;
        for (int i = 0; i < XNUM; i++) {
            if (board[line][i] == 1) {
                count++;
                if (count == 4) {
                    return true;
                }
            }
        }

        return false;
    }

    public float density(int line) {
        int count = 0;
        for (int j = YNUM - 1; j >= line; j--) {
            for (int i = 0; i < XNUM; i++) {
                count += board[j][i];
            }
        }
        return count / (float) XNUM * (YNUM - line);
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
        byte[][] x = new byte[YNUM][XNUM];
        for (int j = 0; j < YNUM; j++) {
            for (int i = 0; i < XNUM; i++) {
                x[j][i] = board[j][i];
            }
        }
        return new Board(x);
    }
}
