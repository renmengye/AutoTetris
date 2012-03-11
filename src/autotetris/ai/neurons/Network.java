/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris.ai.neurons;

import autotetris.ai.Example;
import java.util.LinkedList;
import autotetris.ai.FuncHub;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.omg.CORBA.TIMEOUT;

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
            OutputNeuron n = new OutputNeuron(j){

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
            Perceptron p = new Perceptron((hidlay.size()+1) * 1000 + layer.size()){

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

    public List<Double> test(Example ex) {

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
                n.feed();
                //System.out.println("p-started");
            }

            //wait every neuron in the layer to finish
            try {
                for (Neuron n : layer) {
                    n.feed.join();
                    //System.out.println("p-joined");
                }
            } catch (Exception e) {
                //System.out.println(e);
            }
        }

        //activating the output layer
        for (Neuron n : outlay) {
            //if(!n.feed.isAlive()){
            n.feed();
            //}else{
            //}
            //System.out.println("o-started");
            //n.calc_value();
        }


        //wait output layer to join back
        try {
            for (Neuron n : outlay) {
                n.feed.join();
                //System.out.println("o-joined");
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
    public double train_once(Example ex) {

        test(ex);

        Iterator<Double> ci = ex.correct().listIterator();

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
            //((OutputNeuron) n).update.start();
            n.update_weight();
        }

        //wait all threads join
        for (Neuron n : outlay) {
            try {
                ((OutputNeuron) n).update();
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
                n.back();
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
        
        double esum=0.0;
        
        for(Neuron n:outlay){
            esum+=n.error();
        }
        
        return esum;

    }
}
