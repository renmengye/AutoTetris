/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris.ai.neurons;

import java.util.LinkedList;
import autotetris.ai.FuncHub;
import autotetris.ai.Function_a;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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

    public Network(int input_size, int output_size) {

        inlay = new LinkedList<Neuron>();
        hidlay = new LinkedList<LinkedList<Neuron>>();
        outlay = new LinkedList<Neuron>();

        this.input_size = input_size;
        this.output_size = output_size;

        r = new Random();

        //initializing input neurons
        for (int j = 0; j < input_size; j++) {
            inlay.add(new SimpleInputNeuron(j));
        }

        for (int j = 0; j < output_size; j++) {
            OutputNeuron n = new OutputNeuron(j, new Function_a() {

                public double func(double a) {
                    return FuncHub.gauss(a);
                    //return FuncHub.sigmoid(a);
                }

                public double dfunc(double a) {
                    return FuncHub.dgauss(a);
                    //return FuncHub.dsigmoid(a);
                }
            });
            n.set_rate(0.2);
            inlay.add(n);
        }

        connect(inlay, outlay);      //default with no hidden layer

    }

    private void connect(Collection<Neuron> s, Collection<Neuron> t) {

        for (Neuron n : s) {
            for (Neuron m : t) {
                n.add_target(m);
                m.add_source(n, r.nextDouble());
            }
        }
    }

    private void disconnect(Collection<Neuron> s, Collection<Neuron> t) {

        for (Neuron m : t) {
            m.reset_source();
        }

        for (Neuron n : s) {
            n.reset_target();
        }

    }

    public void add_hidden_layer(int n) {    //add n neurons to a new layer
        LinkedList<Neuron> layer = new LinkedList<Neuron>();      //initialize a new linked list
        for (int j = 0; j < n; j++) {

            //universal id as 1000, 1001, 2000....
            Perceptron p = new Perceptron(hidlay.size() * 1000 + layer.size(), new Function_a() {

                public double func(double a) {
                    return FuncHub.gauss(a);
                    //return FuncHub.sigmoid(a);
                }

                public double dfunc(double a) {
                    return FuncHub.dgauss(a);
                    //return FuncHub.dsigmoid(a);
                }
            });
            layer.add(p);
        }

        //if this is the first hidden layer
        if (hidlay.isEmpty()) {
            disconnect(inlay, outlay);
            connect(inlay, layer);
            connect(layer, outlay);
        } else {
            disconnect(hidlay.peekLast(), outlay);
            connect(hidlay.peekLast(), layer);
            connect(layer, outlay);
        }
        hidlay.add(layer);
    }

    public List<Double> test(List<Double> v) {

        //input the simple input neurons with list values
        Iterator<Double> vi = v.listIterator();
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
                n.feed.start();
            }

            //wait every neuron in the layer to finish
            try {
                for (Neuron n : layer) {
                    n.feed.join();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        //activating the output layer
        for (Neuron n : outlay) {
            n.feed.start();
        }


        //wait output layer to join back
        try {
            for (Neuron n : outlay) {
                n.feed.join();
            }
        } catch (Exception e) {
            System.out.println(e);
        }


        //store the output neurons value into a new list
        List<Double> result = new LinkedList<Double>();
        for (Neuron n : outlay) {
            result.add(n.value());
        }
        return result;

    }

    
    //train the network with one example return the error sum
    public double train_once(List<Double> v, List<Double> c) {

        test(v);

        Iterator<Double> ci = c.listIterator();

        //output layer calculate error
        for (Neuron n : outlay) {
            if (ci.hasNext()) {
                ((OutputNeuron) n).calc_error(ci.next());
            } else {
                ((OutputNeuron) n).calc_error(0.0);
            }
        }

        //output layer update weights thread start
        for (Neuron n : outlay) {
            ((OutputNeuron) n).update.start();
        }

        //wait all threads join
        for (Neuron n : outlay) {
            try {
                ((OutputNeuron) n).update.join();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        
        //hidden layer error back prop
        //iterate in descending order (near output first
        Iterator<LinkedList<Neuron>> li=hidlay.descendingIterator();
        List<Neuron> layer;
        while (li.hasNext()) {
            
            layer=li.next();
            
            //start the calc value process simultaneously throughout layer i
            for (Neuron n : layer) {
                n.back.start();
            }

            //wait every neuron in the layer to finish
            try {
                for (Neuron n : layer) {
                    n.back.join();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        
        return 0.0;

    }
}
