/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rmy
 */
public abstract class Neuron {
    
    protected Map<Integer, Float> source;
    protected Map<Integer, Neuron> target;
    protected int id;
    
    public Neuron(int id){
        this.id=id;
        source = new HashMap<Integer, Float>();
        target = new HashMap<Integer, Neuron>();
    }
    
    public void add_target(Neuron n){
        target.put(target.size(), n);
    }
    
    public void accept_value(int id, float value){
        source.put(id, value);
    }
    
    protected abstract float activate();
    
    public void output(){
        for(Neuron n:target.values()){
            n.accept_value(id, activate());
        }
    }
    
}
