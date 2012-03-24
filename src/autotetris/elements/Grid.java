package autotetris.elements;

import java.awt.Color;

/**
 *
 * @author renmengy
 */
public class Grid {

    public byte value;
    public Color color;

    public Grid(byte value, Color color){
        this.value=value;
        this.color=color;
    }

    @Override
    public Grid clone(){
        return new Grid(value,color);
    }

}
