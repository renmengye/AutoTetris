/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris;

/**
 *
 * @author rmy
 */
public class Router {
    
    private Board board;
    private Piece piece;
    private GameMove[] moves;
    
    public Router(Board board, Piece piece){
        this.board=board;
        this.piece=piece;
    }
    
    public boolean route(){
        return true;
    }
    
    public GameMove[] getMoves(){
        return moves;
    }
}
