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

    public static void main(String[] args) throws NumberFormatException{

        final Random r = new Random();
        Trainer train = new Trainer(0.01, 200) {

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

            @Override
            public void init_base() {
                for (int r1 = 0; r1 < 2; r1++) {
                    for (int r2 = 0; r2 < 2; r2++) {
                        List<Double> u = new LinkedList<Double>();
                        List<Double> v = new LinkedList<Double>();
                        u.add((double) r1);
                        u.add((double) r2);
                        v.add((double) xor(r1, r2));
                        base.insert(new FloatExample(u, v), 1.0);
                    }
                }
            }
        };
        
        train.init_base();
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
                    System.out.printf("The neural network output: %.2f\n", result.get(0));
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
