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
public class FloatExample extends Example{
    
    protected List<Double> input;
    protected List<Double> correct;
    
    public FloatExample(List<Double> i, List<Double> o){
        super();
        input=i;
        correct=o;
    }
    
    public FloatExample(List<Double> i){
        super();
        input=i;
        correct=null;
    }

    @Override
    public List<Double> input() {
        return input;
    }

    @Override
    public List<Double> correct() {
        return correct;
    }

    @Override
    public String to_string() {
        return "";
    }
    
}
