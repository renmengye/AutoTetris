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
        next_piece = new Piece(piece.getType(), piece.getOrient(), piece.range[0], piece.range[3]);
    }

    /*public void setBoard(Board board){
    this.board=board;
    }*/
    public boolean next() {
        //enumerater.next is not responsible of making rotations
        boolean found = false;
        outer:
        for (int j = next_piece.getY(); j >= piece.range[2]; j--) { //search from bottom
            for (int i = next_piece.getX(); i <= piece.range[1]; i++) { //search from left
                if (i != next_piece.getX() || i != next_piece.getY()) { //if not the previous one
                    if (!occupy_check(j, i)) { //if the position is not occupied
                        if (board.check_done(piece, GameMove.DOWN)) { //if the position is final
                            found = true; //break the loop, notify player that the next is found
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
            if (board.getBoard()[y + dy][x + dx] == 1) {
                return true;
            }
        }
        return false;
    }
}
