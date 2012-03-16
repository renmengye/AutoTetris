/*
 * This trainer class takes in an Example and returns the trained network
 */
package autotetris.ai.neurons;

import autotetris.ai.Example;
import autotetris.ai.ExampleBase;
import autotetris.ai.ExampleNode;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author rmy
 */
public class Trainer extends Thread {

    private Network network;
    protected ExampleBase base;
    private double tar_err;
    private double avg_num;
    private Random r;

    public Trainer(double te, int an) {

        network = new Network(2, 1, 1);
        base = new ExampleBase();
        network.add_hidden_layer(2);
        tar_err = te;
        avg_num = an;
        r=new Random();
    }

    public Network network() {
        return network;
    }

    public Example ex_gen() {
        return null;
    }

    public void init_base() {
    }

    @Override
    public void run() {
        double err;
        double err_avg = 0.0;
        int count;
        double rate = .2;
        LinkedList<Double> error_list = new LinkedList<Double>();
        for (count = 0; err_avg > tar_err | count < avg_num; count++) {
            double p=r.nextDouble();                //randomized number
            ExampleNode ex=base.get(p);             //get an example according to the number
            err = network.train_once(ex.example()); //train the example and get the error
            ex.set_value(ex.value()*.95+err*0.05);  //reset the probability of meeting the same example, has a learning factor of 5%
            base.update_value();                    //update the whole tree
            System.out.printf("count: %d\terror: %.5f\n", count, err / rate);
            if (error_list.size() >= avg_num) {
                err_avg -= Math.abs(error_list.pop()) / (double) avg_num;
                err_avg += Math.abs(err) / (double) avg_num / rate;
            } else {
                err_avg = err_avg * error_list.size() / (error_list.size() + 1) + Math.abs(err / rate) / (error_list.size() + 1);
            }
            error_list.addLast(err / rate);
        }
    }
}
