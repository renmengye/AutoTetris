/*
 * Author: Mengye Ren
 * This enumeration class specifies different orientation of a piece
 */

package autotetris;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Orientation {

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

    public static Orientation next(int num){
        if(num>=3){
            return lookup.get(0);
        }else{
            return lookup.get(num+1);
        }
    }

    public static Orientation prev(int num){
        if(num<=0){
            return lookup.get(3);
        }else{
            return lookup.get(num-1);
        }
    }
}
