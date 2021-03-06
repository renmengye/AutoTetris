package autotetris.ai.neurons;

import autotetris.ai.Example;
import autotetris.ai.FuncHub;
import java.util.*;

/**
 *
 * @author rmy
 */
public class Network {

    private LinkedList<Neuron> inlay;
    private LinkedList<LinkedList<Neuron>> hidlay;
    private LinkedList<Neuron> outlay;
    private int input_size, output_size;
    private Random r;

    //constructor with input size and output size
    public Network(int is, int os, double lr) {

        inlay = new LinkedList<Neuron>();
        hidlay = new LinkedList<LinkedList<Neuron>>();
        outlay = new LinkedList<Neuron>();

        this.input_size = is;
        this.output_size = os;

        r = new Random();

        //initializing input neurons
        for (int j = 0; j < input_size; j++) {
            inlay.add(new SimpleInputNeuron(j));
        }

        //initializing output neurons
        for (int j = 0; j < output_size; j++) {
            OutputNeuron n = new OutputNeuron(j) {

                @Override
                public double activ_func(double a) {
                    return FuncHub.gauss(a);
                    //return FuncHub.sigmoid(a);
                }

                @Override
                public double activ_dfunc(double a) {
                    return FuncHub.dgauss(a);
                    //return FuncHub.dsigmoid(a);
                }
            };
            n.set_rate(lr);
            outlay.add(n);
        }

        //default with no hidden layer
        connect(inlay, outlay);
    }

    //connect two layers of neurons, s=source, t=target
    private void connect(Collection<Neuron> s, Collection<Neuron> t) {

        for (Neuron n : s) {
            for (Neuron m : t) {
                n.add_target(m);
                m.add_source(n, r.nextDouble());
            }
        }
    }

    //disconnect two layers of neurons, s=source, t=target
    private void disconnect(Collection<Neuron> s, Collection<Neuron> t) {

        for (Neuron m : t) {
            m.reset_source();
        }

        for (Neuron n : s) {
            n.reset_target();
        }

    }

    //add a hidden layer with n hidden nodes
    public void add_hidden_layer(int n) {    //add n neurons to a new layer

        //initialize a new linked list to store hidden neurons
        LinkedList<Neuron> layer = new LinkedList<Neuron>();

        //initialize each neuron
        for (int j = 0; j < n; j++) {

            //universal id as 1000, 1001, 2000....
            Perceptron p = new Perceptron((hidlay.size() + 1) * 1000 + layer.size()) {

                @Override
                public double activ_func(double a) {
                    return FuncHub.gauss(a);
                    //return FuncHub.sigmoid(a);
                }

                @Override
                public double activ_dfunc(double a) {
                    return FuncHub.dgauss(a);
                    //return FuncHub.dsigmoid(a);
                }
            };
            //p.set_rate(.8);
            layer.add(p);
        }

        //if this is the first hidden layer, then disconn in and out, conn hidden with in and out
        if (hidlay.isEmpty()) {
            disconnect(inlay, outlay);
            connect(inlay, layer);
            connect(layer, outlay);
        } //if not the first hidden layer, then disconn prev hidden and out, conn hidden with prev and out
        else {
            disconnect(hidlay.peekLast(), outlay);
            connect(hidlay.peekLast(), layer);
            connect(layer, outlay);
        }
        hidlay.add(layer);
    }

    public List<Double> test(Example ex) throws InterruptedException {

        //input the simple input neurons with list values
        Iterator<Double> vi = ex.input().listIterator();
        for (Neuron i : inlay) {
            if (vi.hasNext()) {
                ((SimpleInputNeuron) i).input(vi.next());
            } else {
                ((SimpleInputNeuron) i).input(0.0);
            }
        }

        //activating the hidden layers
        for (List<Neuron> layer : hidlay) {

            //start the calc value process simultaneously throughout layer i
            for (Neuron n : layer) {
                n.feed_start();
            }

            //wait every neuron in the layer to finish
            for (Neuron n : layer) {
                n.feed().join();
            }
        }

        //activating the output layer
        for (Neuron n : outlay) {
            n.feed_start();
        }


        //wait output layer to join back
        for (Neuron n : outlay) {
            n.feed().join();
            //System.out.println("o-joined");
        }


        //store the output neurons value into a new list
        List<Double> result = new LinkedList<Double>();
        for (Neuron n : outlay) {
            result.add(n.value());
        }
        return result;

    }

    //train the network with one example return the error sum
    public double train_once(Example ex) throws InterruptedException {
        
        //record the total error and return
        double esum = 0.0;
        
        //run the network in feed mode
        test(ex);
        
        
        //maybe need to fix this assumption later on, but for now it seems like all networks use double type
        Iterator<Double> ci = ex.correct().listIterator();

        //output layer calculate error
        for (Neuron n : outlay) {
            if (ci.hasNext()) {
                ((OutputNeuron) n).notify_error(ci.next());
            } else {
                ((OutputNeuron) n).notify_error(0.0);
            }
        }

        //output layer update weights thread start
        for (Neuron n : outlay) {
            
            //by the way collect the error
            esum += n.error();
            
            n.back_start();
        }

        //wait all threads join
        for (Neuron n : outlay) {
            n.back().join();
        }

        //hidden layer error back prop
        //iterate in descending order (near output first
        Iterator<LinkedList<Neuron>> li = hidlay.descendingIterator();
        List<Neuron> layer;
        while (li.hasNext()) {

            layer = li.next();

            //start the calc value process simultaneously throughout layer i
            for (Neuron n : layer) {
                n.back_start();
            }

            //wait every neuron in the layer to finish
            for (Neuron n : layer) {
                n.back().join();
            }
        }
        
        return esum;

    }
}
