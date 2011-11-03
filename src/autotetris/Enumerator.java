/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris;

import java.util.ArrayList;

/**
 *
 * @author rmy
 */
public class Enumerator implements ATCommon {

    private Board board;
    private Piece piece;
    protected ArrayList<Piece> candidates;

    public Enumerator(Board board, Piece piece) {
        this.board = board;
        this.piece = piece;
        this.candidates = new ArrayList<Piece>();
    }

    public void enumerate() { //enumerate every circustance
        Piece test_piece;
        for (int j = YNUM - 1; j >= 0; j--) { //search from bottom
            for (int i = 0; i < XNUM; i++) {
                for (int h = 0; h < O_NUM[piece.getType().value()]; h++) {
                    test_piece = new Piece(piece.getType(), Orientation.get(h), i, j); //generate a new test piece
                    if (test_piece.check_range(i, j)&&!occupy_check(test_piece,i, j)) { //if the position is not occupied and inside the range
                        if (board.check_done(test_piece, GameMove.DOWN)) { //if the position is final
                            candidates.add(test_piece);
                        }
                    }
                }
            }
        }
    }

    public Piece next() { //pops out the next candidate in the list
        if (candidates.size() > 0) {
            Piece next = (Piece) candidates.get(0);
            candidates.remove(0);
            return next;
        } else {
            return null;
        }
    }

    private boolean occupy_check(Piece piece, int x, int y) { //check if the board position is occupied
        for (int k = 0; k <= 3; k++) {
            int dx = piece.contour[k][0];
            int dy = piece.contour[k][1];
            if ((y + dy >= 0 && x + dx >= 0) && (y + dy < YNUM && x + dx < XNUM)) {
                if (board.getBoard()[y + dy][x + dx] == 1) {
                    return true;
                }
            }
        }
        return false;
    }
}
