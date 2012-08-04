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
        setValue(v);
    }

    @Override
    public double getActivatedValue(double a) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double getActivatedValueDerivative(double a) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
