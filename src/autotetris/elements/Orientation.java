/*
 * Author: Mengye Ren
 * This enumeration class specifies different orientation of a piece
 */

package autotetris.elements;

import autotetris.ATCommon;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Orientation implements ATCommon{

    CW0(0),CW90(1),CW180(2),CW270(3); //counterclockwise and the degree
    int num; //the order in the enumeration

    Orientation(int num){
        this.num=num;
    }
    
    //return the order in enumeration
    public int value() {
        return num;
    }

    private static final Map<Integer, Orientation> lookup = new HashMap<Integer, Orientation>();
    
    static {
          for(Orientation s : EnumSet.allOf(Orientation.class))
               lookup.put(s.value(), s);
     }

    //reverse lookup
    public static Orientation get(int num) {
        return lookup.get(num);
    }

    public static Orientation next(PieceType type,int num){
        if(num>=O_NUM[type.value()]-1){
            return lookup.get(0);
        }else{
            return lookup.get(num+1);
        }
    }

    public static Orientation prev(PieceType type, int num){
        if(num<=0){
            return lookup.get(O_NUM[type.value()]-1);
        }else{
            return lookup.get(num-1);
        }
    }
}
