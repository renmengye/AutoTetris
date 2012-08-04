package autotetris.ai.neurons;

/**
 *
 * @author rmy
 */

// this class is abstract because we need to implement activation function upon declaration
public abstract class OutputNeuron extends Neuron{
    
    public OutputNeuron(int id){
        super(id);
    }
    
    public void notify_error(double t){
        setError(getRate() * (t - getValue()));
    }
    
    @Override
    public void updateError(){
        
    }
    
}
