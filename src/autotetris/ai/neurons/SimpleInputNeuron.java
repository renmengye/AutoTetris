/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris.ai.neurons;

import autotetris.ai.Function_a;

/**
 *
 * @author rmy
 */
public class SimpleInputNeuron extends Neuron{
    
    protected Function_b sensor;
    
    public SimpleInputNeuron(int id, Function_b sensor){
        
        super(id,new Function_a(){
            public float func(float a){
                return a;
            }
            public float dfunc(float a){
                return 1;
            }
        }
                );
        
        this.sensor=sensor;
    }
    
    @Override
    public void calc_value(){
        value=sensor.func();
    }
}
