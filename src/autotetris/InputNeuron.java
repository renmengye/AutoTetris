/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris;

import java.util.ArrayList;

/**
 *
 * @author rmy
 */
public abstract class InputNeuron extends Neuron{
    
    public InputNeuron(int id){
        super(id);
    }
    
    protected abstract float activate();
}
