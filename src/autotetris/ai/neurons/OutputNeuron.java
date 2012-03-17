package autotetris.ai.neurons;

/**
 *
 * @author rmy
 */
public class OutputNeuron extends Neuron{
    
    public OutputNeuron(int id){
        super(id);
    }
    
    public void notify_error(double t){
        error=rate*(t-value);
    }
    
    @Override
    public void calc_error(){
        
    }
    
}
