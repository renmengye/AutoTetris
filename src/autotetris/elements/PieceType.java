/*
 * Author: Mengye Ren
 * This enumeration class specifies different shape of a piece
 */
package autotetris.elements;

import autotetris.ATCommon;
import java.awt.Color;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum PieceType implements ATCommon {

    O(0), I(1), S(2), Z(3), J(4), L(5), T(6);
    private int num;
    private Color color;

    PieceType(int num) {
        this.num = num;
        this.color=new Color(PIECE_COLOR[num][0],PIECE_COLOR[num][1],PIECE_COLOR[num][2]);
    }

    //return the order in enumeration
    public int value() {
        return num;
    }

    //return the color of type
    public Color color(){
        return color;
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
