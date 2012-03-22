package autotetris.elements;

/**
 *
 * @author rmy
 */
public class PieceMover extends Thread {

    private GameMove move;
    private Board board;
    private Piece piece;

    public PieceMover(Piece piece, Board board, GameMove move) {

        this.piece = piece;
        this.board = board;
        this.move = move;

    }

    @Override
    public void run() {
        if (board.check_done(piece, move)) {
            piece.move(move, board);
        }
    }
}
