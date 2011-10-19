/*
 * This is a class that takes in board condition and piece and compute the gamemoves
 */

package autotetris;

/**
 *
 * @author MengYe
 */
public class Player {

    private Board board;
    private Piece piece;

    public Player(){
    }

    public GameMove[] getMoves(Board board, Piece piece){
        return new GameMove[5];
    }

}
