/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris;

/**
 *
 * @author rmy
 */
public class Rater {
    public static int rate(Board board){
        return board.checkFull()+10;
    }
    
    
}
