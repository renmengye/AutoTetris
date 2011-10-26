/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author MengYe
 */
public enum GameMove {

    DROP(0), DOWN(1), LEFT(2), RIGHT(3), CW(4), CCW(5), NULL(6),UP(7);
    int num;
    //int dxy[];

    GameMove(int num) {
        this.num = num;
    }

    public int value() {
        return num;
    }
    private static final Map<Integer, GameMove> lookup = new HashMap<Integer, GameMove>();

    static {
        for (GameMove s : EnumSet.allOf(GameMove.class)) {
            lookup.put(s.value(), s);
        }
    }

    public static GameMove get(int num) {
        return lookup.get(num);
    }
}
