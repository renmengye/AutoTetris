/*
 * Test point for training Xor function to the neural network
 */
package autotetris.ai.neurons;

import autotetris.ai.Example;
import autotetris.ai.ExampleBase;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author rmy
 */
public class boolTestSer {

    public static final String defaultNetworkPath = "data/XorNetwork.ser";
    public static final String defaultBasePath = "data/XorExampleBase.ser";
    //public static final String defaultBasePath = "data/AndExampleBase.ser";    
    //public static final String defaultNetworkPath = "data/AndNetwork.ser";


    public static void main(String[] args) throws InterruptedException {

        Network network;
        ExampleBase base;

        try {
            network = Network.read(defaultNetworkPath);
            network.start();
        } catch (Exception ex) {
            try {
                base = ExampleBase.read(defaultBasePath);
            } catch (Exception ex2) {
                throw new IllegalArgumentException("Cannot find base file!");
            }
            // initiate a trainer that trains to 0.01 precision with average of 200 cases
            Trainer train = new Trainer(base, 0.01, 200);
            // start training
            train.start();
            System.out.println("Training network started.");

            // wait to finish training
            train.join();
            System.out.println("Training network completed.\n");

            // get the trained network
            network = train.getNetwork();

            // save the trained network
            network.save(defaultNetworkPath);
        }

        // for user to input to test the network
        DataInputStream cinput = new DataInputStream(System.in);

        // keeps asking user to input
        while (true) {

            //reading input
            int r1, r2;
            try {
                System.out.print("Please input first one or zero: ");
                r1 = Integer.parseInt(cinput.readLine());
                System.out.print("Please input second one or zero: ");
                r2 = Integer.parseInt(cinput.readLine());
            } catch (Exception ex) {
                System.out.println("Please input one or zero!");
                continue;
            }

            //validate input
            if ((r1 == 1 || r1 == 0) && (r2 == 1 || r2 == 0)) {
                List<Double> result = network.runOnce(new Example<Double, Double>((double) r1, (double) r2), false);
                //return result
                System.out.printf("The neural network output: %.2f\n", result.get(0));
            } //invalid input
            else {
                System.out.println("Please input one or zero!");
            }
        }
    }
}
