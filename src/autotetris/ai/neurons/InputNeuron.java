package autotetris.ai.neurons;

/**
 *
 * @author rmy
 */
public class InputNeuron extends Neuron {

    public InputNeuron(Network network) {
        super(network);
    }

    public InputNeuron(double bias, double rate, FunctionActivator activator, Network network) {
        super(bias, rate, activator, network);
    }

    public void input(double v) throws NeuronNotConnectedException {
        this.value = v;
        this.targetConnector.sendValue(this.value);

        synchronized (this.network) {
            this.network.notifyAll();
        }
    }
}
