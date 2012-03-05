/*
 * Author: Mengye Ren
 * This enumeration class specifies different shape of a piece
 */
package autotetris;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum PieceType {

    O(0), I(1), S(2), Z(3), J(4), L(5), T(6);
    private int num;

    PieceType(int num) {
        this.num = num;
    }

    //return the order in enumeration
    public int value() {
        return num;
    }
    
    private static final Map<Integer, PieceType> lookup = new HashMap<Integer, PieceType>();

    static {
          for(PieceType s : EnumSet.allOf(PieceType.class))
               lookup.put(s.value(), s);
     }

    //reverse lookup
    public static PieceType get(int num) {
        return lookup.get(num);
    }
}
