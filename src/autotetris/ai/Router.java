package autotetris.ai;

import autotetris.elements.Board;
import autotetris.elements.GameMove;
import autotetris.elements.Piece;
import java.util.List;

/**
 *
 * @author rmy
 */


//TODO finish annotating Router.java
public class Router {

    private Board board;
    private Piece target;
    //private GameMove[] moves;

    public Router(Board board, Piece target) {
        this.board = board;
        this.target = target;
    }

    
    //a recursive method to route the piece to the desired position
    public List<GameMove> route(Piece piece, List<GameMove> moves, boolean dropped) {

        
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
                            
                            //however the piece can't be move up once once more
                            if (!test.revmove(GameMove.DOWN, board)) {
                                
                                //break the loop and indicate that the drop is impossible.
                                possible_drop = false; 
                                break;
                            }
                        }
                    }
                    
                    //have a new list to store the test moves
                    List<GameMove> tmoves;
                    
                    //if the drop is possible, then add drop action
                    if (possible_drop && j > 0) { 
                        moves.add(0, GameMove.DROP);
                    }
                    
                    tmoves = route(test, moves, true); //recursion occurs here
                        
                    
                    //if got a non empty set of moves then return
                    if (tmoves != null) {
                        return tmoves;
                    }
                    
                    /*else {
                        if (!moves.isEmpty()) {
                            moves.remove(0);
                        }
                    }*/
                }
                
            }
            
            //if the object is already dropped, then search horizontally and rotationally (only one)
            else { 
                for (int i = 2; i <= 6; i++) { //search all possible moves in GameMove
                    GameMove move = GameMove.get(i);
                    Piece test = piece.clone();
                    if (moves.isEmpty()) {
                        if (!board.check_done(test, GameMove.reverse(move))) {
                            if (test.revmove(move, board)) { //if it is a possible move
                                moves.add(0, move);
                                List<GameMove> tmoves = route(test, moves, true); //recursion occurs here
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
                                List<GameMove> tmoves = route(test, moves, true); //recursion occurs here
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
        
        //if already at same vertical level but still not placed rightly, then give up
        return null;
    }

    /*public GameMove[] getMoves() {
        return moves;
    }*/
}
