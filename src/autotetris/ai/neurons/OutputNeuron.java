package autotetris.ai.neurons;

/**
 *
 * @author rmy
 */
// this class is abstract because we need to implement activation function upon declaration
public class OutputNeuron extends Neuron {

    public OutputNeuron(Network network) {
        super(network);
    }

    public OutputNeuron(double bias, double rate, FunctionActivator activator, Network network) {
        super(bias, rate, activator, network);
    }

    public void setError(double t) throws NeuronNotConnectedException {
        this.error = 
                this.rate 
                * (t - getValue()) 
                * this.activator.computeActivedValueDerivative(this.sourceConnector.getNetLinearSum());
        this.updateWeight();
        this.sourceConnector.sendValue(this.error);

        synchronized (this.network) {
            this.network.notifyAll();
        }
    }
}
