/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris.ai;

import java.util.List;

/**
 *
 * @author rmy
 */
public abstract class Example {
    
    
    
    public abstract List input();
    
    public abstract List correct();
    
    public abstract String to_string();
}
