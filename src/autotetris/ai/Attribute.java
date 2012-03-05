/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package autotetris.ai;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Attribute{

    SCORE(0), HEIGHT(1), HOLES(2), TAR_HEIGHT(3), LONG_HOLES(4);
    private int num;

    Attribute(int num) {
        this.num = num;
    }

    //return the order in enumeration
    public int value() {
        return num;
    }
    
    private static final Map<Integer, Attribute> lookup = new HashMap<Integer, Attribute>();

    static {
          for(Attribute s : EnumSet.allOf(Attribute.class)){
               lookup.put(s.value(), s);
          }
     }

    //reverse lookup
    public static Attribute get(int num) {
        return lookup.get(num);
    }
    
    public static int size(){
        return values().length;
    }
}