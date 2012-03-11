/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris.ai.neurons;

import autotetris.ai.Example;
import autotetris.ai.FloatExample;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rmy
 */
public class booltest {

    public static void main(String[] args) {

        /*int isize=2,psize=100;
        
        final Random r = new Random();
        
        SimpleInputNeuron[] i = new SimpleInputNeuron[isize];
        Perceptron[] p = new Perceptron[psize];
        OutputNeuron o = new OutputNeuron(0) {
        
        @Override
        public double activ_func(double a) {
        return FuncHub.gauss(a);
        }
        
        @Override
        public double activ_dfunc(double a) {
        return FuncHub.dgauss(a);
        }
        };
        
        //initializing input neurons
        for (int j = 0; j < isize; j++) {
        i[j] = new SimpleInputNeuron(j);
        }
        
        //initializing perceptrons
        for (int j = 0; j < psize; j++) {
        p[j] = new Perceptron(j){
        @Override
        public double activ_func(double a) {
        return FuncHub.gauss(a);
        }
        @Override
        public double activ_dfunc(double a) {
        return FuncHub.dgauss(a);
        }
        };
        
        //connecting perceptrons
        for (int k = 0; k < isize; k++) {
        i[k].add_target(p[j]);
        p[j].add_source(i[k], r.nextFloat());
        }
        
        p[j].add_target(o);
        o.add_source(p[j], r.nextFloat());
        }
        
        LinkedList<Double> error_list=new LinkedList();
        double eavg=0f;
        int count;
        int max_list=50;
        double rate=.2f;
        //start to train, with end condition of error average
        for(count=0;eavg>0.01f||count<max_list;count++) {
        
        //forward-feed
        for (int k = 0; k < isize; k++) {
        i[k].input(r.nextInt(2));
        }
        for (int k = 0; k < psize; k++) {
        p[k].calc_value();
        }
        o.calc_value();
        
        //error back-prop
        int correct=xor(i[0].value,i[1].value);
        o.calc_error(correct);
        
        System.out.printf("input: %5f, %5f;\tpredict: %5f;\tcorrect: %5d\terror: %5f\n",i[0].value,i[1].value,o.value,correct,o.error/rate);
        
        //update weight
        o.update_weight();
        for (int k = 0; k < psize; k++) {
        p[k].calc_error();
        p[k].update_weight();
        }
        
        if(error_list.size()>=max_list){
        eavg-=Math.abs(error_list.pop())/(double)max_list;
        eavg+=Math.abs(o.error)/(double)max_list/rate;
        }else{
        eavg=eavg*error_list.size()/(error_list.size()+1)+Math.abs(o.error/rate)/(error_list.size()+1);
        }
        error_list.addLast(o.error/rate);
        
        }
        System.out.println(count);
         */

        final Random r = new Random();
        Trainer train = new Trainer(0.001, 200) {

            @Override
            public Example ex_gen() {
                List<Double> u = new LinkedList<Double>();
                List<Double> v = new LinkedList<Double>();
                int r1 = r.nextInt(2);
                int r2 = r.nextInt(2);
                u.add((double) r1);
                u.add((double) r2);
                v.add((double) xor(r1, r2));
                return new FloatExample(u, v);
            }
        };
        train.start();
        System.out.println("Training network started.");
        try {
            train.join();
            System.out.println("Training network completed.\n");
        } catch (InterruptedException ex) {
            Logger.getLogger(booltest.class.getName()).log(Level.SEVERE, null, ex);
        }
        Network n = train.network();
        DataInputStream cinput = new DataInputStream(System.in);
        while (true) {
            try {
                System.out.print("Please input first one or zero: ");
                int r1 = Integer.parseInt(cinput.readLine());
                System.out.print("Please input second one or zero: ");
                int r2 = Integer.parseInt(cinput.readLine());
                if ((r1 == 1 || r1 == 0) && (r2 == 1 || r2 == 0)) {
                    List<Double> u = new LinkedList<Double>();
                    u.add((double) r1);
                    u.add((double) r2);
                    List<Double> result = n.test(new FloatExample(u));
                    System.out.printf("The neural network output: %.2f\n",result.get(0));
                } else {
                    System.out.println("Please input one or zero!");
                }
            } catch (IOException ex) {
                Logger.getLogger(booltest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static int xor(double a, double b) {
        if ((a == 0 && b != 0) || (b == 0 && a != 0)) {
            return 1;
        } else {
            return 0;
        }
    }

    public static int and(double a, double b) {
        if (a != 0 && b != 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
