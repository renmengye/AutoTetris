/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris.ai.neurons;

import autotetris.ai.Example;
import autotetris.ai.ExampleBase;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rmy
 */
public class boolTestSer {

    public static final String networkPath = "data/XorNetwork.ser";
    public static final String defaultBasePath = "data/XorExampleBase.ser";

    public static void main(String[] args) {
        try {
            File baseFile = null;

            if (args.length == 0) {
                baseFile = new File(defaultBasePath);
            } else {
                baseFile = new File(args[0]);
            }

            Network network;
            network = tryReadNetwork();
            if (network == null) {

                ExampleBase base = tryReadExampleBase(baseFile);
                if (base == null) {
                    throw new IllegalArgumentException("Cannot find base file!");
                }

                // initiate a trainer that trains to 0.01 precision with average of 200 cases
                Trainer train = new Trainer(base, 0.001, 200);

                // start training
                train.start();
                System.out.println("Training network started.");

                // wait to finish training
                train.join();
                System.out.println("Training network completed.\n");

                // get the trained network
                network = train.network();

                // save the trained network
                trySaveNetwork(network);
            }

            // for user to input to test the network
            DataInputStream cinput = new DataInputStream(System.in);

            // keeps asking user to input
            while (true) {
                //reading input
                System.out.print("Please input first one or zero: ");
                int r1 = Integer.parseInt(cinput.readLine());
                System.out.print("Please input second one or zero: ");
                int r2 = Integer.parseInt(cinput.readLine());

                //valiidate input
                if ((r1 == 1 || r1 == 0) && (r2 == 1 || r2 == 0)) {
                    List<Double> result = network.runOnce(new Example<Double, Double>((double) r1, (double) r2));
                    //return result
                    System.out.printf("The neural network output: %.2f\n", result.get(0));
                } //invalid input
                else {
                    System.out.println("Please input one or zero!");
                }


            }
        } catch (Exception ex) {
            Logger.getLogger(booltest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Network tryReadNetwork() {

        File networkFile = new File(networkPath);
        Network network = null;

        if (networkFile.exists()) {

            FileInputStream fis = null;
            ObjectInputStream ois = null;

            try {
                fis = new FileInputStream(networkFile);
                ois = new ObjectInputStream(fis);
                try {
                    network = (Network) ois.readObject();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(boolTestSer.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(boolTestSer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return network;
    }

    public static ExampleBase tryReadExampleBase(File baseFile) {
        ExampleBase base = null;
        if (baseFile.exists()) {

            FileInputStream fis = null;
            ObjectInputStream ois = null;

            try {
                fis = new FileInputStream(baseFile);
                ois = new ObjectInputStream(fis);
                try {
                    base = (ExampleBase) ois.readObject();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(boolTestSer.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(boolTestSer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return base;
    }

    public static void trySaveNetwork(Network network) {
        String filename = networkPath;
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(filename);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(network);
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(boolTestSer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
