package autotetris.ai.neurons;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rmy
 */
public class Neuron implements Serializable, Runnable {

    // value send to next level target
    protected double value;
    // bias value add to the linear sum
    protected double bias;
    // error calced by back_prop
    protected double error;
    // learning rate
    protected double rate;
    //If the neuron is set to wait for target neurons
    protected boolean waitForTarget;
    protected boolean waitForSource;
    // manage connections to the sources
    protected final NeuronConnector sourceConnector;
    // manage connections to the targets
    protected final NeuronConnector targetConnector;
    // provide activation function
    protected FunctionActivator activator;
    // synchronization lock object
    protected final Network network;

    /**
     * Construct a neuron
     */
    public Neuron(Network network) {
        this(0.0, 1.0, new GaussianActivator(), network);
    }

    public Neuron(double bias, double rate, FunctionActivator activator, Network network) {
        this.bias = bias;
        this.rate = rate;
        this.activator = activator;
        this.network = network;
        this.sourceConnector = new NeuronConnector();
        this.targetConnector = new NeuronConnector();
    }

    /**
     * @return The stored value of the neuron
     */
    public double getValue() {
        return value;
    }

    /**
     * Add a target neuron to the neuron
     *
     * @param neuron The target neuron
     */
    public void addTarget(Neuron neuron) {
        //getTarget().add(n);
        this.targetConnector.addNeuron(neuron.sourceConnector, 1.0);
    }

    /**
     * Add a source neuron to the neuron
     *
     * @param neuron The source neuron
     * @param weight The weight of the source neuron
     */
    public void addSource(Neuron neuron, double weight) {
        //randomly put weight as 1, need to change afterwards
        //getSource().put(neuron, weight);
        this.sourceConnector.addNeuron(neuron.targetConnector, weight);
    }

    /**
     * Clear all the target neurons
     */
    public void clearTargets() {
        this.targetConnector.reset();
    }

    /**
     * Clear all the source neurons
     */
    public void clearSources() {
        this.sourceConnector.reset();
    }

    /**
     * Update the value of neuron itself after all the source values are
     * collected
     */
    protected void updateValue() {
        //multiply the non-linear activation function
        this.value =
                this.activator.computeActivedValue(
                this.sourceConnector.getNetLinearSum());
    }

    /**
     * Update the error of neuron itself after all the error values are
     * collected
     */
    protected void updateError() {
        // multiply the derivative of activation function
        this.error =
                (this.rate
                * this.activator.computeActivedValueDerivative(this.sourceConnector.getNetLinearSum())
                * this.targetConnector.getNetLinearSum());
    }

    /**
     * Update the weight of a source neuron
     */
    protected void updateWeight() throws NeuronNotConnectedException {
        this.sourceConnector.updateWeight(this.error);
    }

    public void run() {

        while (true) {

            if (this.waitForSource) {
                // Wait the sources to send value
                while (true) {

                    // Collected all the sources, can send value to targets
                    if (this.sourceConnector.isReady()) {

                        this.updateValue();

                        try {
                            this.targetConnector.sendValue(value);
                        } catch (NeuronNotConnectedException ex) {
                            Logger.getLogger(Neuron.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        this.sourceConnector.resetCount();
                        break;
                    }

                    synchronized (this.sourceConnector) {
                        try {
                            this.sourceConnector.wait();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Neuron.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }

            if (this.waitForTarget) {
                // Wait the target the send error back
                synchronized (this.targetConnector) {
                    try {
                        this.targetConnector.wait(500);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Neuron.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                // Collected all the errors, can send error to sources
                if (this.targetConnector.isReady()) {
                    this.updateError();
                    try {
                        this.updateWeight();
                        this.sourceConnector.sendValue(error);
                    } catch (NeuronNotConnectedException ex) {
                        Logger.getLogger(Neuron.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    this.targetConnector.resetCount();
                }
            }
        }
    }
}
