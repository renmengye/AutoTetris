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
public class Router {

    private Board board;
    private Piece target;
    private GameMove[] moves;

    public Router(Board board, Piece target) {
        this.board = board;
        this.target = target;
    }

    //another routing approach
    public ArrayList<GameMove> route(Piece piece, ArrayList<GameMove> moves, boolean dropped) {

        if (piece.getX() == target.getX() && piece.getY() == target.getY() && piece.getOrient() == target.getOrient()) { //reaches target, recursive ends
            return moves;
        }
        if (target.getY() < piece.getY()) {
            //First if we already reach the target, then return the moves directly
            //we need to first calculate the horizontal distance and the rotational distance
            int dx = target.getX() - piece.getX();
            int dr = target.getOrient().value() - piece.getOrient().value();

            //if the piece have not yet go through the reverse dropping test, then do it now
            if (!dropped) {
                for (int j = piece.getY() - target.getY() - Math.abs(dx) - Math.abs(dr); j >= 0; j--) { //repeat from the max drop to the min drop
                    Piece test = piece.clone();
                    boolean possible_drop = true;
                    for (int i = 0; i < j; i++) {
                        if (!board.check_done(test, GameMove.UP)) {
                            if (!test.revmove(GameMove.DOWN, board)) {
                                possible_drop = false; //break the loop and indicate that the drop is impossible.
                                break;
                            }
                        }
                    }
                    ArrayList<GameMove> tmoves;
                    if (possible_drop && j > 0) { //if the drop is possible
                        moves.add(0, GameMove.DROP);
                    }
                    
                        tmoves = route(test, moves, true); //recursion occurs here
                    if (tmoves != null) {
                        return tmoves;
                    } else {
                        if (!moves.isEmpty()) {
                            moves.remove(0);
                        }
                    }
                }
            } else { //if the object is already dropped, then search horizontally and rotationally (only one)
                for (int i = 2; i <= 6; i++) { //search all possible moves in GameMove
                    GameMove move = GameMove.get(i);
                    Piece test = piece.clone();
                    if (moves.isEmpty()) {
                        if (!board.check_done(test, GameMove.reverse(move))) {
                            if (test.revmove(move, board)) { //if it is a possible move
                                moves.add(0, move);
                                ArrayList<GameMove> tmoves = route(test, moves, true); //recursion occurs here
                                if (tmoves != null) {
                                    return tmoves;
                                } else {
                                    moves.remove(0);
                                }
                            }
                        }
                    } else {
                        if (!board.check_done(test, GameMove.UP) && !board.check_done(test, GameMove.reverse(move))) {
                            if (test.revmove(GameMove.DOWN, board) && test.revmove(move, board)) { //if it is a possible move
                                moves.add(0, move);
                                ArrayList<GameMove> tmoves = route(test, moves, true); //recursion occurs here
                                if (tmoves != null) {
                                    return tmoves;
                                } else {
                                    moves.remove(0);
                                }
                            }
                        }
                    }
                }
            }

        }
        return null;
    }

    public GameMove[] getMoves() {
        return moves;
    }
}
