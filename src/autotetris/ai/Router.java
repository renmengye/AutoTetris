package autotetris.ai;

import autotetris.elements.Board;
import autotetris.elements.GameMove;
import autotetris.elements.Piece;
import java.util.List;

/**
 *
 * @author rmy
 */
public class Router {

    private Board board;
    private Piece target;
    //private GameMove[] moves;

    public Router(Board board, Piece target) {
        this.board = board;
        this.target = target;
    }

    //a recursive method to route the piece to the desired position
    public List<GameMove> route_obs(Piece piece, List<GameMove> moves, boolean dropped) {


        //if the piece is already at desired position then return what we've got
        if (piece.getX() == target.getX() && piece.getY() == target.getY() && piece.getOrient() == target.getOrient()) { //reaches target, recursive ends
            return moves;
        }

        //if there is still vertical distance till the target
        if (target.getY() < piece.getY()) {

            //we need to first calculate the horizontal distance and the rotational distance
            int dx = target.getX() - piece.getX();
            int dr = target.getOrient().value() - piece.getOrient().value();

            //if the piece have not yet go through the reverse dropping test, then do it now
            if (!dropped) {
                for (int j = piece.getY() - target.getY() - Math.abs(dx) - Math.abs(dr); j > 0; j--) { //repeat from the max drop to the min drop
                    Piece test = piece.clone();
                    boolean possible_drop = true;
                    for (int i = 0; i <= j; i++) {

                        //regard dropping as moving piece up
                        //if the piece can be move up once more
                        if (!board.check_done(test, GameMove.UP)) {

                            test.move(GameMove.reverse(GameMove.DOWN), board);

                        } //otherwise break the loop and indicate that the drop is impossible.
                        else {
                            possible_drop = false;
                            break;
                        }
                    }

                    //have a new list to store the test moves
                    List<GameMove> tmoves;

                    //if the drop is possible, then add drop action
                    if (possible_drop) {
                        moves.add(0, GameMove.DROP);
                    } else {
                        test = piece;
                    }

                    //recursion occurs here
                    tmoves = route_obs(test, moves, true);


                    //if got a non empty set of moves then return
                    if (tmoves != null) {
                        return tmoves;
                    }
                }

            } //if the object is already dropped, then search horizontally and rotationally (only one)
            else {
                //search all possible moves in GameMove
                for (int i = 2; i <= 6; i++) {
                    GameMove move = GameMove.get(i);
                    Piece test = piece.clone();

                    //if the piece can move down as game host would always do, and also can do the move
                    //or if the piece is in the initial move then only check the reverse move
                    if (((!moves.isEmpty() && !board.check_done(test, GameMove.UP))
                            || (moves.isEmpty()))
                            && !board.check_done(test, GameMove.reverse(move))) {

                        //if not initial move then move reverse of down by default
                        if (!moves.isEmpty()) {
                            test.move(GameMove.UP, board);
                        }

                        //move the designated action
                        test.move(GameMove.reverse(move), board);

                        //add the action to the list
                        moves.add(0, move);

                        //recursion occurs here
                        List<GameMove> tmoves = route_obs(test, moves, true);
                        if (tmoves != null) {
                            return tmoves;
                        } else {
                            moves.remove(0);
                        }

                    }
                }
            }

        }

        //if already at same vertical level but still not placed rightly, then give up
        return null;
    }

    //a recursive method to route the piece to the desired position
    public List<GameMove> possible_route(Piece piece, List<GameMove> moves, boolean dropped) {


        //if the piece is already at desired position then return what we've got
        if (piece.getX() == target.getX() && piece.getY() == target.getY() && piece.getOrient() == target.getOrient()) { //reaches target, recursive ends
            return moves;
        }

        //if there is still vertical distance till the target
        if (target.getY() < piece.getY()) {

            //we need to first calculate the horizontal distance and the rotational distance
            int dx = target.getX() - piece.getX();
            int dr = target.getOrient().value() - piece.getOrient().value();

            //if the piece have not yet go through the reverse dropping test, then do it now
            if (!dropped) {
                for (int j = piece.getY() - target.getY() - Math.abs(dx) - Math.abs(dr); j > 0; j--) { //repeat from the max drop to the min drop
                    Piece test = piece.clone();
                    boolean possible_drop = true;
                    for (int i = 0; i <= j; i++) {

                        //regard dropping as moving piece up
                        //if the piece can be move up once more
                        if (!board.check_done(test, GameMove.UP)) {

                            test.move(GameMove.reverse(GameMove.DOWN), board);

                        } //otherwise break the loop and indicate that the drop is impossible.
                        else {
                            possible_drop = false;
                            break;
                        }
                    }

                    //have a new list to store the test moves
                    List<GameMove> tmoves;

                    //if the drop is possible, then add drop action
                    if (possible_drop) {
                        moves.add(0, GameMove.DROP);
                    } else {
                        test = piece;
                    }

                    //recursion occurs here
                    tmoves = possible_route(test, moves, true);


                    //if got a non empty set of moves then return
                    if (tmoves != null) {
                        return tmoves;
                    }
                }

            } //if the object is already dropped, then search horizontally and rotationally (only one)
            else {
                //search all possible moves in GameMove
                for (int i = 2; i <= 6; i++) {
                    GameMove move = GameMove.get(i);
                    Piece test = piece.clone();

                    //if the piece can move down as game host would always do, and also can do the move
                    //or if the piece is in the initial move then only check the reverse move
                    if (((!moves.isEmpty() && !board.check_done(test, GameMove.UP))
                            || (moves.isEmpty()))
                            && !board.check_done(test, GameMove.reverse(move))) {

                        //if not initial move then move reverse of down by default
                        if (!moves.isEmpty()) {
                            test.move(GameMove.UP, board);
                        }

                        //move the designated action
                        test.move(GameMove.reverse(move), board);

                        //add the action to the list
                        moves.add(0, move);

                        //recursion occurs here
                        List<GameMove> tmoves = possible_route(test, moves, true);
                        if (tmoves != null) {
                            return tmoves;
                        } else {
                            moves.remove(0);
                        }

                    }
                }
            }

        }

        //if already at same vertical level but still not placed rightly, then give up
        return null;
    }
}
