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

    public ArrayList<GameMove> route_old(Piece piece, Piece target, ArrayList<GameMove> moves) {
        System.out.printf("x:%d y:%d\n", piece.getX(), piece.getY());
        for (int i = 0; i < moves.size(); i++) {
            System.out.printf("%s x:%d y:%d\n", moves.get(i), piece.getX(), piece.getY());
        }
        System.out.println();
        if (piece.getX() == target.getX() && piece.getY() == target.getY() && piece.getOrient() == target.getOrient()) {
            return moves;
        } else {
            GameMove move;
            for (int i = 1; i <= 5; i++) {
                move = GameMove.get(i);
                if (move == GameMove.RIGHT) {
                    if (target.getX() >= piece.getX()) {
                        continue;
                    }
                } else if (move == GameMove.LEFT) {
                    if (target.getX() <= piece.getX()) {
                        continue;
                    }
                } else if (move == GameMove.CW || move == GameMove.CCW) {
                    if (target.getOrient() == piece.getOrient()) {
                        continue;
                    }
                }
                Piece test = piece.clone();
                if (test.revmove(move, board)) {
                    if (move != GameMove.DOWN && !test.revmove(GameMove.DOWN, board)) {
                        continue;
                    } else {
                        moves.add(move);
                        ArrayList<GameMove> tmoves = route_old(test, target, moves); //recursion
                        if (tmoves != null) {
                            return tmoves;
                        } else {
                            moves.remove(moves.size() - 1);
                        }
                    }
                }
            }
        }
        return null;
    }

    //another routing approach
    public ArrayList<GameMove> route(Piece piece, ArrayList<GameMove> moves, boolean dropped) {
        //System.out.printf("tx:%d ty:%d to:%d x:%d y:%d o:%d\n", target.getX(), target.getY(), target.getOrient().value(), piece.getX(), piece.getY(), piece.getOrient().value());
        if (piece.getX() == target.getX() && piece.getY() == target.getY() && piece.getOrient() == target.getOrient()) {
            //System.out.println("RETURNED!!");
            return moves;
        }
        if (target.getY() < piece.getY()) {
            //First if we already reach the target, then return the moves directly
            //we need to first calculate the horizontal distance and the rotational distance
            int dx = target.getX() - piece.getX();
            int dr = target.getOrient().value() - piece.getOrient().value();

            //if the piece have not yet go through the reverse dropping test, then do it now
            if (!dropped) {
                for (int j = piece.getY() - target.getY() - Math.abs(dx) - Math.abs(dr) - 1; j >= 0; j--) { //repeat from the max drop to the min drop
                    Piece test = piece.clone();
                    boolean possible_drop = true;
                    for (int i = 0; i <= j; i++) {
                        if (!test.revmove(GameMove.DOWN, board)) {
                            possible_drop = false; //break the loop and indicate that the drop is impossible.
                            break;
                        }
                    }
                    if (possible_drop) { //if the drop is possible
                        moves.add(0, GameMove.DROP);
                        ArrayList<GameMove> tmoves = route(test, moves, true); //recursion occurs here
                        if (tmoves != null) {
                            return tmoves;
                        } else {
                            moves.remove(0);
                        }
                    }
                }
            } else { //if the object is already dropped, then search horizontally and rotationally (only one)
                for (int i = 2; i <= 6; i++) { //search all possible moves in GameMove
                    GameMove move = GameMove.get(i);
                    if (move == GameMove.LEFT && dx < 0) { //get rid of redundant moves (might have better method than this)
                        continue;
                    } else if (move == GameMove.RIGHT && dx > 0) {
                        continue;
                    } else if (dr==0&&(move == GameMove.CW||move == GameMove.CW)){
                        continue;
                    } else { // now start search
                        //System.out.println(move);
                        Piece test = piece.clone();
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
        return null;
    }

    public GameMove[] getMoves() {
        return moves;
    }
}
