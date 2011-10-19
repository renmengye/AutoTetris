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

    LEFT(0), RIGHT(1), DOWN(2), CW(3), CCW(4), DROP(5);
    int num;

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
