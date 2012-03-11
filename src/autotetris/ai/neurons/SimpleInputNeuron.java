/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris.ai.neurons;

/**
 *
 * @author rmy
 */
public class SimpleInputNeuron extends Neuron{
    
    
    public SimpleInputNeuron(int id){
        
        super(id);
    }
    
    public void input(double v){
        value=v;
    }
}
