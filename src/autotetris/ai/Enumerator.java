package autotetris.ai;

import autotetris.ATCommon;
import autotetris.elements.Board;
import autotetris.elements.GameMove;
import autotetris.elements.Orientation;
import autotetris.elements.Piece;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author rmy
 */
public class Enumerator implements ATCommon {

    private Board board;
    private Piece piece;
    private List<Piece> candidates;
    private Iterator<Piece> iterator;

    public Enumerator(Board board, Piece piece) {
        this.board = board;
        this.piece = piece;
        this.candidates = new LinkedList<Piece>();
    }

    //enumerate every possible circustance
    public void enumerate() { 
        Piece test_piece;
        //search from bottom
        for (int j = YNUM - 1; j >= 0; j--) {

            //search from left
            for (int i = 0; i < XNUM; i++) {

                //for each different orientation
                for (int h = 0; h < O_NUM[piece.getType().value()]; h++) {

                    //generate a new test piece
                    test_piece = new Piece(piece.getType(), Orientation.get(h), i, j);

                    //if the position is not occupied and inside the range
                    if (test_piece.check_range(i, j)&&!occupy_check(test_piece,i, j)) {

                        //if the position is final
                        if (board.check_done(test_piece, GameMove.DOWN)) { 
                            candidates.add(test_piece);
                        }
                    }
                }
            }
        }
        iterator=candidates.listIterator();
    }

    //pops out the next candidate in the list
    public Piece next() { 
        if(iterator.hasNext()){
            return iterator.next();
        }else{
            return null;
        }
    }

    //check if the board position is occupied
    private boolean occupy_check(Piece piece, int x, int y) { 
        for (int k = 0; k <= 3; k++) {
            int dx = piece.contour[k][0];
            int dy = piece.contour[k][1];
            if ((y + dy >= 0 && x + dx >= 0) && (y + dy < YNUM && x + dx < XNUM)) {
                if (board.toArray()[y + dy][x + dx].value == 1) {
                    return true;
                }
            }
        }
        return false;
    }
}
