/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris;

/**
 *
 * @author rmy
 */
public class Enumerator implements ATCommon {

    private Board board;
    private Piece piece;
    protected Piece next_piece;

    public Enumerator(Board board, Piece piece) {
        this.board = board;
        this.piece = piece;
        next_piece = new Piece(piece.getType(), piece.getOrient(), piece.range[0]-1, piece.range[3]);
    }

    /*public void setBoard(Board board){
    this.board=board;
    }*/
    public boolean next() {
        //enumerater.next is not responsible of making rotations
        boolean found = false;
        Piece test_piece = next_piece.clone();
        outer:
        for (int j = next_piece.getY(); j >= piece.range[2]; j--) { //search from bottom
            test_piece.setXY(0, j);
            for (int i = 0; i <= piece.range[1]; i++) { //search from left
                test_piece.setXY(i, j);
                if (j != next_piece.getY()||i > next_piece.getX()) { //if not the previous one
                    //System.out.printf("checking x: %d, y: %d\n",i,j);
                    if (!occupy_check(i, j)) { //if the position is not occupied
                        //System.out.println("passing occupy test");
                        if (board.check_done(test_piece, GameMove.DOWN)) { //if the position is final
                            //System.out.println("passing done test");
                            found = true; //break the loop, notify player that the next is found
                            next_piece=test_piece;
                            break outer;
                        }
                    }
                }
            }
        }
        return found;
    }

    private boolean occupy_check(int x, int y) {
        for (int k = 0; k <= 3; k++) {
            int dx = piece.contour[k][0];
            int dy = piece.contour[k][1];
            if ((y + dy >= 0 && x + dx >= 0) && (y + dy < YNUM && x + dx < XNUM)){
                if (board.getBoard()[y + dy][x + dx] == 1) {
                    return true;
                }
            }
        }
        return false;
    }
}
