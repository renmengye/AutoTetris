/*
 * This trainer class takes in an Example and returns the trained network
 */
package autotetris.ai.neurons;

import autotetris.ai.ExampleBase;
import autotetris.ai.ExampleNode;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public Trainer(ExampleBase b, double te, int an) {

        network = new Network(2, 1, 1);
        base = b;
        network.add_hidden_layer(2);
        tar_err = te;
        avg_num = an;
        r = new Random();
    }

    public Network network() {
        return network;
    }
    
    
    @Override
    public void run(){
        double err;
        double err_avg = 0.0;
        int count;
        double rate = 1;
        LinkedList<Double> error_list = new LinkedList<Double>();
        
        //train until the error average matches the precision and over the least number of cases
        for (count = 0; err_avg > tar_err | count < avg_num; count++) {
            try {
                //get an example according to a randomized number
                ExampleNode ex = base.get(r.nextDouble());             
                
                //train the example and get the error
                err = network.train_once(ex.example()); 
                
                //reset the probability of meeting the same example, has a learning factor of 5%
                ex.set_value(ex.value() * .995 + err * 0.005);
                
                //update database probability
                base.update_value();                    //update the whole tree
                System.out.printf("count: %d\terror: %.5f\n", count, err / rate);
                
                //update the errorlist
                if (error_list.size() >= avg_num) {
                    err_avg -= Math.abs(error_list.pop()) / (double) avg_num;
                    err_avg += Math.abs(err) / (double) avg_num / rate;
                } else {
                    err_avg = err_avg * error_list.size() / (error_list.size() + 1) + Math.abs(err / rate) / (error_list.size() + 1);
                }
                
                error_list.addLast(err / rate);
            } catch (InterruptedException ex1) {
                Logger.getLogger(Trainer.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        System.out.println("Training network completed.\n");
    }
}
