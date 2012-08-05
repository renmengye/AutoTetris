package autotetris.ai.neurons;

/**
 *
 * @author rmy
 */


public class InputNeuron extends Neuron{
    
    public InputNeuron(int id){
        super(id);
    }
    
    public void input(double v){
        setValue(v);
    }
}
