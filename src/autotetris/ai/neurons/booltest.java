package autotetris.ai.neurons;

import autotetris.ai.Example;
import autotetris.ai.ExampleBase;
import autotetris.ai.FuncHub;
import java.io.DataInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rmy
 */
public class booltest {

    public static void main(String[] args) throws NumberFormatException, InterruptedException {

        ExampleBase base = new ExampleBase();

        for (int r1 = 0; r1 < 2; r1++) {
            for (int r2 = 0; r2 < 2; r2++) {
                List<Double> u = new LinkedList<Double>();
                List<Double> v = new LinkedList<Double>();
                u.add((double) r1);
                u.add((double) r2);
                v.add((double) FuncHub.xor(r1, r2));
                base.insert(new Example<Double,Double>(u, v), 1.0);
            }
        }

        //initiate a trainer that trains to 0.01 precision with average of 200 cases
        Trainer train = new Trainer(base, 0.001, 200);

        //start training
        train.start();
        System.out.println("Training network started.");
        
        //wait to finish training
        train.join();
        System.out.println("Training network completed.\n");
        
        //get the trained network
        Network n = train.network();
        
        //for user to input to test the network
        DataInputStream cinput = new DataInputStream(System.in);
        
        //keeps asking user to input
        while (true) {
            
            try {
                
                //reading input
                System.out.print("Please input first one or zero: ");
                int r1 = Integer.parseInt(cinput.readLine());
                System.out.print("Please input second one or zero: ");
                int r2 = Integer.parseInt(cinput.readLine());
                
                //valiidate input
                if ((r1 == 1 || r1 == 0) && (r2 == 1 || r2 == 0)) {
                    
                    //construct example based on input
                    List<Double> u = new LinkedList<Double>();
                    u.add((double) r1);
                    u.add((double) r2);
                    List<Double> result = n.test(new Example<Double,Double>(u));
                    
                    //return result
                    System.out.printf("The neural network output: %.2f\n", result.get(0));
                    
                } 
                //invalid input
                else {
                    System.out.println("Please input one or zero!");
                }
            
            } 
            
            catch (Exception ex) {
                Logger.getLogger(booltest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
