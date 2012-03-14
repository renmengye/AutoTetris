package autotetris.ai.neurons;

import java.util.*;
import java.util.Map.Entry;

/**
 *
 * @author rmy
 */
public abstract class Neuron {

    protected int id;                              //id number for different neurons
    protected double net;                           //net linear sum
    protected double value;                         //value send to next level
    protected double bias;                          //bias value add to the linear sum
    protected double error;                         //error calced by back_prop
    protected double rate;                          //learning rate
    protected boolean state;                           //true for forward, false for back
    protected Map<Neuron, Double> source;        //storing the source neurons and the linear weights
    protected List<Neuron> target;              //stroing the list of target neurons
    public Thread feed, back;                 //thread for forward feeding and back propagation

    public Neuron(int id) {
        this.id = id;
        bias = 0.0;                           //assume bias is 0 for this simple model
        state = true;                         //forward feed mode
        rate = 1.0;                           //assume rate is 1 for default
        source = new HashMap<Neuron, Double>();
        target = new LinkedList<Neuron>();
    }

    public double value() {
        return value;
    }

    public double error() {
        return error;
    }

    public double rate() {
        return rate;
    }

    public void set_rate(double r) {
        rate = r;
    }

    public void set_state(boolean b) {
        state = b;
    }

    public void add_target(Neuron n) {
        target.add(n);
    }

    public void reset_target() {
        target.clear();
    }

    public void add_source(Neuron n, double w) {
        source.put(n, w);                   //randomly put weight as 1, need to change afterwards
    }

    public void reset_source() {
        source.clear();
    }
    
    public Collection<Double> get_weight(){
        return source.values();
    }

    public double activ_func(double a) {
        return 0.0;
    }

    public double activ_dfunc(double a) {
        return 0.0;
    }
    
    public void feed(){
        feed = new Thread() {

            @Override
            public void run() {
                calc_value();
            }
        };
        feed.start();
    }

    public void calc_value() {
        net = 0.0;
        for (Neuron n : source.keySet()) {      //doing a linear combination sum
            net += n.value() * source.get(n);
        }
        net += bias;
        value = activ_func(net);             //multiply the non-linear activation function
    }
    
    public void back(){
        back = new Thread() {

            @Override
            public void run() {
                calc_error();
                update_weight();
            }
        };
        back.start();
    }

    public void calc_error() {
        double sum = 0;
        for (Neuron n : target) {               //get a linear sum of the error before
            sum += n.error() * n.source.get(this);
        }
        error = rate * activ_dfunc(net) * sum;       //times the derivative of activation function
    }

    public void update_weight() {

        for (Entry<Neuron, Double> e : source.entrySet()) {      //doing a linear combination sum
            double w = e.getValue();
            double y = e.getKey().value();
            e.setValue(w + error * y);

        }
    }
}
