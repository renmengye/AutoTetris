package autotetris.ai;

import autotetris.ATCommon;
import autotetris.elements.Board;
import autotetris.elements.GameMove;
import autotetris.elements.Piece;
import java.util.List;

/**
 *
 * @author rmy
 */
public class Router implements ATCommon {

    private Board board;
    private Piece end_point;
    private Piece start_point;
    //private float[][][] trend;

    //private static final float DECAY=0.8f;
    public Router(Board board, Piece start_point, Piece end_point) {
        this.board = board;
        this.start_point = start_point;
        this.end_point = end_point;
    }

    //a simplistic model of being "possible"
    public boolean possible_route() {
        Piece test = end_point.clone();

        while (start_point.getY()+2 < test.getY()) {
            if (!board.check_done(test, GameMove.UP)) {
                test.move(GameMove.UP, board);
            } else {
                //System.out.println("not possible");
                return false;
            }
        }
        return true;
    }

    //a preliminary model would simply calculate neighbouring shortest "distance"
    public GameMove next() {

        GameMove max_choice = GameMove.NULL;
        float max_score = -1 / 0f;
        float score;

        for (int i = 0; i <= 6; i++) {

            //first consider drop
            if (i == 0) {
                Piece test = start_point.clone();
                test.move(GameMove.get(i), board);
                if (test.equals(end_point)) {
                    max_score = 100;
                    max_choice = GameMove.DROP;
                }
            } //then consider other neighbouring moves
            else if (!board.check_done(start_point, GameMove.get(i))) {
                Piece test = start_point.clone();
                test.move(GameMove.get(i), board);
                score = 100 - norm(test, end_point);
                if (score > max_score) {
                    max_score = score;
                    max_choice = GameMove.get(i);
                }

            }
        }
        //System.out.println(max_choice + ": " + max_score);
        return max_choice;
    }

    private float norm(final Piece a, final Piece b) {

        int x = a.getX() - b.getX();
        int y = a.getY() - b.getY();
        int o = a.getOrient().value() - b.getOrient().value();
        return (float) Math.sqrt(10 * x * x + 0.1 * y * y + 100 * o * o);
    }
}
