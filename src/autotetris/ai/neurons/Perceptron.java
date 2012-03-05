/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rmy
 */
public abstract class Perceptron extends Neuron{
    
    
    public Perceptron(int id){
        super(id);
    }
    
    public void accept_value(int input_id, float input_value){
        source.put(input_id, input_value);
    }
    
    protected float activate(){
        return 0f;
    }
    
}
