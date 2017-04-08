/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris.ai.neurons;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

/**
 *
 * @author t-mren
 */
public class NeuronConnector implements Serializable {

    private class ConnectedState implements Serializable {

        private boolean receivedValue;
        private double weight;
        private double value;

        public ConnectedState(double weight) {
            this.weight = weight;
        }
    }
    private HashMap<NeuronConnector, ConnectedState> map;
    private int receivedCount;
    private boolean ready;

    public NeuronConnector() {
        this.map = new HashMap<NeuronConnector, ConnectedState>();
        this.receivedCount = 0;
    }

    public synchronized void addNeuron(NeuronConnector neuron, double weight) {
        this.map.put(neuron, new ConnectedState(weight));
    }

    public synchronized boolean isReady() {
        return this.ready;
    }

    public synchronized void resetCount() {
        this.receivedCount = 0;
        for (ConnectedState state : this.map.values()) {
            state.receivedValue = false;
        }
        this.ready = false;
    }

    public synchronized void reset() {
        this.map.clear();
        this.receivedCount = 0;
    }

    public synchronized void receiveValue(NeuronConnector sender, double value) throws NeuronNotConnectedException {
        ConnectedState state = this.map.get(sender);

        if (state == null) {
            throw new NeuronNotConnectedException();
        }

        if (state.receivedValue == false) {
            state.value = value;
            state.receivedValue = true;
            this.receivedCount++;
        }

        this.ready = this.receivedCount == this.map.size() && this.receivedCount > 0;

        if (this.ready) {
            this.notifyAll();
        }
    }

    public synchronized double getNetLinearSum() {
        double sum = 0.0;
        for (ConnectedState state : map.values()) {
            sum += state.value * state.weight;
        }
        return sum;
    }

    // update as a souce connector
    public synchronized void updateWeight(double error) throws NeuronNotConnectedException {
        for (Entry<NeuronConnector, ConnectedState> entry : this.map.entrySet()) {
            ConnectedState state = entry.getValue();
            state.weight = state.weight + error * state.value;
            NeuronConnector connector = entry.getKey();

            if (connector != this) {
                connector.updateWeight(this, state.weight);
            }
        }
    }

    // update as a target connector
    private synchronized void updateWeight(NeuronConnector connector, double weight) throws NeuronNotConnectedException {
        ConnectedState state = this.map.get(connector);
        if (state == null) {
            throw new NeuronNotConnectedException();
        }
        state.weight = weight;
    }

    public synchronized void sendValue(double value) throws NeuronNotConnectedException {
        for (NeuronConnector receiver : map.keySet()) {
            receiver.receiveValue(this, value);
        }
    }

    public synchronized List<Double> getValueList() {
        LinkedList<Double> result = new LinkedList<Double>();
        for (ConnectedState state : map.values()) {
            result.add(state.value);
        }
        return result;
    }
}