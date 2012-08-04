package autotetris.ai.neurons;

import java.util.*;
import java.util.Map.Entry;

/**
 *
 * @author rmy
 */
public abstract class Neuron {

    // id number for different neurons
    private int id; 
    
    // net linear sum from the source
    private double netLinearSum;
    
    // value send to next level target
    private double value;
    
    // bias value add to the linear sum
    private double bias;
    
    // error calced by back_prop
    private double error;
    
    // learning rate
    private double rate;
    
    // storing the source neurons and the linear weights
    private Map<Neuron, Double> source;
    
    // stroing the list of target neurons
    private List<Neuron> target;
    
    // Forward feed thread
    private Thread feed;
    
    // Back propagate thread
    private Thread back;

    // Contruct a neuron with an id
    public Neuron(int id) {
        this.id = id;
        bias = 0.0;                           //assume bias is 0 for this simple model
        rate = 1.0;                           //assume rate is 1 for default
        source = new HashMap<Neuron, Double>();
        target = new LinkedList<Neuron>();
    }

    //add a target neuron to this neuron
    public void addTarget(Neuron n) {
        getTarget().add(n);
    }

    // add a source neuron to this neuron
    public void addSource(Neuron n, double w) {
        getSource().put(n, w);                   //randomly put weight as 1, need to change afterwards
    }
    
    // returns the collection of the weights for the source neurons
    public Collection<Double> getWeights(){
        return getSource().values();
    }

    // pass the net sum to activation function, needs to be overrided
    public abstract double getActivatedValue(double a);

    // pass the net sum to the derivative of activation function
    public abstract double getActivatedValueDerivative(double a);
    
    public void startFeed(){
        setFeed(new Thread() {

             @Override
             public void run() {
                 updateValue();
             }
         });
        getFeed().start();
    }

    public void startBack(){
        setBack(new Thread() {

             @Override
             public void run() {
                 updateError();
                 updateWeight();
             }
         });
        getBack().start();
    }
    
    public void updateValue() {
        netLinearSum = 0.0;
        for (Neuron n : getSource().keySet()) {      //doing a linear combination sum
            netLinearSum += n.getValue() * getSource().get(n);
        }
        netLinearSum += getBias();
        setValue(getActivatedValue(getNetLinearSum()));             //multiply the non-linear activation function
    }
    

    public void updateError() {
        double sum = 0;
        for (Neuron n : getTarget()) {               //get a linear sum of the error before
            sum += n.getError() * n.getSource().get(this);
        }
        setError(getRate() * getActivatedValueDerivative(getNetLinearSum()) * sum);       //times the derivative of activation function
    }

    public void updateWeight() {

        for (Entry<Neuron, Double> e : getSource().entrySet()) {      //doing a linear combination sum
            double w = e.getValue();
            double y = e.getKey().getValue();
            e.setValue(w + getError() * y);
        }
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the netLinearSum
     */
    public double getNetLinearSum() {
        return netLinearSum;
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * @return the bias
     */
    public double getBias() {
        return bias;
    }

    /**
     * @return the error
     */
    public double getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(double error) {
        this.error = error;
    }

    /**
     * @return the rate
     */
    public double getRate() {
        return rate;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(double rate) {
        this.rate = rate;
    }

    /**
     * @return the source
     */
    public Map<Neuron, Double> getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(Map<Neuron, Double> source) {
        this.source = source;
    }

    /**
     * @return the target
     */
    public List<Neuron> getTarget() {
        return target;
    }

    /**
     * @param target the target to set
     */
    public void setTarget(List<Neuron> target) {
        this.target = target;
    }

    /**
     * @return the feed
     */
    public Thread getFeed() {
        return feed;
    }

    /**
     * @param feed the feed to set
     */
    public void setFeed(Thread feed) {
        this.feed = feed;
    }

    /**
     * @return the back
     */
    public Thread getBack() {
        return back;
    }

    /**
     * @param back the back to set
     */
    public void setBack(Thread back) {
        this.back = back;
    }

    /**
     * @param value the value to set
     */
    protected void setValue(double value) {
        this.value = value;
    }
}
