package autotetris.elements;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author MengYe
 */
public enum GameMove {

    DROP(0), DOWN(1), NULL(2), LEFT(3), RIGHT(4), CW(5), CCW(6), UP(7);
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
    
    public static GameMove reverse(GameMove x){
        switch(x){
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            case CW:
                return CCW;
            case CCW:
                return CW;
        }
        return NULL;
    }
}
