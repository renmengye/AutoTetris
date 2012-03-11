/*
 * This trainer class takes in an Example and returns the trained network
 */
package autotetris.ai.neurons;

import autotetris.ai.Example;
import java.util.LinkedList;

/**
 *
 * @author rmy
 */
public class Trainer extends Thread{
    
    private Network network;
    private double tar_err;
    private double avg_num;
    
    public Trainer(double te, int an){
        network=new Network(2,1);
        tar_err=te;
        avg_num=an;
    }
    
    public Network network(){
        return network;
    }
    
    public Example ex_gen(){
        return null;
    }

    @Override
    public void run() {
        double err=0.0;
        double err_avg=0.0;
        int count=0;
        double rate=1;
        LinkedList<Double> error_list=new LinkedList<Double>();
        for(count=0;err_avg>tar_err|count<avg_num;count++) {
            err=network.train_once(ex_gen());
            System.out.printf("count: %d, err: %5f, err_avg:%5f\n",count,err/rate,err_avg);
            if(error_list.size()>=avg_num){
                err_avg-=Math.abs(error_list.pop())/(double)avg_num;
                err_avg+=Math.abs(err)/(double)avg_num/rate;
            }else{
                err_avg=err_avg*error_list.size()/(error_list.size()+1)+Math.abs(err/rate)/(error_list.size()+1);
            }
            error_list.addLast(err/rate);
        }
    }
}
