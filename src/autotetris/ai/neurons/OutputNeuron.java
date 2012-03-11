package autotetris.ai.neurons;

/**
 *
 * @author rmy
 */
public class OutputNeuron extends Neuron{
    
    public Thread update; 
    
    public OutputNeuron(int id){
        super(id);
    }
    
    public void calc_error(double t){
        error=rate*(t-value);
    }
    
    public void update(){
        update=new Thread(){
            @Override
            public void run(){
                update_weight();
            }
        };
    }
    
}
