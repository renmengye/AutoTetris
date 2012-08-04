package autotetris.ai.neurons;

import autotetris.elements.Board;

/**
 *
 * @author rmy
 */


public class InputNeuron extends Neuron{
    
    public InputNeuron(int id){
        super(id);
    }
    
    public void sensor(Board board){
        
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
