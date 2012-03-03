/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris;

/**
 *
 * @author rmy
 */
public class OutputNeuron extends Neuron{
    
    public OutputNeuron(int id){
        super(id);
    }
    
    protected float activate(){
        return 0f;
    }
    
}
