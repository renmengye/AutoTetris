package autotetris.ai.neurons;

import autotetris.ai.Example;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rmy
 */
public class Network implements Serializable {

    private LinkedList<Neuron> inputLayer;
    private LinkedList<LinkedList<Neuron>> hiddenLayers;
    private LinkedList<Neuron> outputLayer;
    private Neuron errorNeuron;
    private Neuron resultNeuron;
    private double learningRate;

    /**
     * Construct a network
     *
     * @param inputSize Input layer size
     * @param outputSize Output layer size
     * @param learningRate Learning rate
     */
    public Network(int inputSize, int outputSize, double learningRate) {

        this.inputLayer = new LinkedList<Neuron>();
        this.hiddenLayers = new LinkedList<LinkedList<Neuron>>();
        this.outputLayer = new LinkedList<Neuron>();
        this.resultNeuron = new Neuron(this);
        this.errorNeuron = new Neuron(this);
        this.learningRate = learningRate;

        //initializing input neurons
        for (int j = 0; j < inputSize; j++) {
            InputNeuron neuron = new InputNeuron(0.0, learningRate, new GaussianActivator(), this);
            this.connectNeurons(this.errorNeuron, neuron, new Random().nextDouble() * 0.5 + 0.5);
            this.inputLayer.add(neuron);
        }

        //initializing output neurons
        for (int j = 0; j < outputSize; j++) {
            OutputNeuron neuron = new OutputNeuron(0.0, learningRate, new GaussianActivator(), this);
            this.connectNeurons(neuron, this.resultNeuron, new Random().nextDouble() * 0.5 + 0.5);
            this.outputLayer.add(neuron);
        }

        //default with no hidden layer
        connectLayers(this.inputLayer, this.outputLayer);
    }

    /**
     * Connect two layers of neurons together, default weight 1.0
     *
     * @param sourceLayer The source layer
     * @param targetLayer The target layer
     */
    private void connectLayers(Collection<Neuron> sourceLayer, Collection<Neuron> targetLayer) {

        for (Neuron sourceNeuron : sourceLayer) {
            for (Neuron targetNeuron : targetLayer) {
                this.connectNeurons(sourceNeuron, targetNeuron, new Random().nextDouble() * 0.5 + 0.5);
            }
        }
    }

    /**
     * Connect two neurons together
     *
     * @param sourceNeuron The source neuron
     * @param targetNeuron The target neuron
     * @param sourceWeight The weight of the source neuron for the target neuron
     */
    private void connectNeurons(Neuron sourceNeuron, Neuron targetNeuron, double sourceWeight) {
        sourceNeuron.addTarget(targetNeuron);
        targetNeuron.addSource(sourceNeuron, sourceWeight);
    }

    /**
     * Disconnect two layers of neurons
     *
     * @param sourceLayer
     * @param targetLayer
     */
    private void disconnectLayers(Collection<Neuron> sourceLayer, Collection<Neuron> targetLayer) {

        for (Neuron neuron : targetLayer) {
            neuron.clearSources();
        }

        for (Neuron neuron : sourceLayer) {
            neuron.clearTargets();
        }

    }

    /**
     * Add one hidden layer to the network
     *
     * @param n The size of the hidden layer
     */
    public void addHiddenLayer(int n) {    //add n neurons to a new layer

        //initialize a new linked list to store hidden neurons
        LinkedList<Neuron> layer = new LinkedList<Neuron>();

        //initialize each neuron
        for (int j = 0; j < n; j++) {
            Neuron perceptron = new Neuron(0.0, this.learningRate, new SigmoidActivator(), this);
            layer.add(perceptron);
        }

        //if this is the first hidden layer, then disconn in and out, conn hidden with in and out
        if (this.hiddenLayers.isEmpty()) {
            disconnectLayers(this.inputLayer, this.outputLayer);
            connectLayers(this.inputLayer, layer);
            connectLayers(layer, this.outputLayer);
        } //if not the first hidden layer, then disconn prev hidden and out, conn hidden with prev and out
        else {
            disconnectLayers(this.hiddenLayers.peekLast(), this.outputLayer);
            connectLayers(this.hiddenLayers.peekLast(), layer);
            connectLayers(layer, this.outputLayer);
        }
        this.hiddenLayers.add(layer);
    }

    public void start() {
        for (Neuron neuron : this.inputLayer) {
            new Thread(neuron).start();
        }
        for (List<Neuron> hiddenLayer : this.hiddenLayers) {
            for (Neuron neuron : hiddenLayer) {
                new Thread(neuron).start();
            }
        }
        for (Neuron neuron : this.outputLayer) {
            new Thread(neuron).start();
        }
    }

    /**
     * Run the network once and return the network result
     *
     * @param example The train example to be send to the input layer
     * @param train Whether this example will be used to train the network
     * @return The result of the output layer
     * @throws InterruptedException
     */
    public List<Double> runOnce(Example example, boolean train) throws InterruptedException {
        // Input the input neurons with list values
        Iterator<Double> inputIterator = example.getInputValues().listIterator();
        for (Neuron i : this.inputLayer) {
            try {
                if (inputIterator.hasNext()) {
                    ((InputNeuron) i).input(inputIterator.next());
                } else {
                    ((InputNeuron) i).input(0.0);
                }
            } catch (Exception ex) {
                Logger.getLogger(boolTestSer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        synchronized (this) {
            while (true) {
                if (this.resultNeuron.sourceConnector.isReady()) {
                    this.resultNeuron.sourceConnector.resetCount();
                    // If want to train the network then force the output neuron to send the errors
                    if (train) {
                        Iterator<Double> resultIterator = example.getExpectedValues().listIterator();
                        for (Neuron neuron : this.outputLayer) {
                            try {
                                ((OutputNeuron) neuron).setError(resultIterator.next());
                            } catch (NeuronNotConnectedException ex) {
                                Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } else {
                        return this.resultNeuron.sourceConnector.getValueList();
                    }
                }

                if (this.errorNeuron.targetConnector.isReady()) {
                    this.errorNeuron.targetConnector.resetCount();
                    return this.resultNeuron.sourceConnector.getValueList();
                }

                this.wait();
            }
        }
    }

    public static Network read(String path) throws FileNotFoundException, IOException, ClassNotFoundException {
        File networkFile = new File(path);
        Network network = null;

        if (networkFile.exists()) {

            FileInputStream fis;
            ObjectInputStream ois;

            fis = new FileInputStream(networkFile);
            ois = new ObjectInputStream(fis);
            network = (Network) ois.readObject();
        } else {
            throw new FileNotFoundException();
        }
        return network;
    }

    public void save(String path) {
        FileOutputStream fos;
        ObjectOutputStream oos;
        try {
            fos = new FileOutputStream(path);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(boolTestSer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
