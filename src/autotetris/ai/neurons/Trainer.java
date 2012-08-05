/*
 * This trainer class takes in an Example and returns the trained network
 */
package autotetris.ai.neurons;

import autotetris.ai.Example;
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
    private ExampleBase base;
    private double passingErrorRate;
    private double passingErrorSize;

    public Trainer(ExampleBase base, double error, int size) {
        this.network = new Network(2, 1, 1);
        this.base = base;
        this.network.addHiddenLayer(2);
        this.passingErrorRate = error;
        this.passingErrorSize = size;
    }
    
    @Override
    public void run() {
        double errorOnce;
        double errorAverage = 0.0;
        int count;
        
        LinkedList<Double> errorList = new LinkedList<Double>();

        //train until the error average matches the precision and over the least number of cases
        for (count = 0; errorAverage > getPassingErrorRate() | count < getPassingErrorSize(); count++) {
            try {
                //get an example according to a randomized number
                Example ex = getBase().getExample(new Random().nextDouble());

                //train the example and get the error
                errorOnce = getNetwork().trainOnce(ex);

                //reset the probability of meeting the same example, has a learning factor of 5%
                getBase().setExampleProbability(ex, getBase().getExampleProbability(ex) * .995 + errorOnce * 0.005);

                //update database probability
                getBase().updateProbability();                    //update the whole tree
                System.out.printf("count: %d\terror: %.5f\n", count, errorOnce/1.0);

                //update the errorlist
                if (errorList.size() >= getPassingErrorSize()) {
                    errorAverage -= Math.abs(errorList.pop()) / (double) getPassingErrorSize();
                    errorAverage += Math.abs(errorOnce) / (double) getPassingErrorSize() / 1.0;
                } else {
                    errorAverage = errorAverage * errorList.size() / (errorList.size() + 1) + Math.abs(errorOnce / 1.0) / (errorList.size() + 1);
                }

                errorList.addLast(errorOnce / 1.0);
            } catch (InterruptedException ex1) {
                Logger.getLogger(Trainer.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        System.out.println("Training network completed.\n");
    }

    /**
     * @return the network
     */
    public Network getNetwork() {
        return network;
    }

    /**
     * @param network the network to set
     */
    public void setNetwork(Network network) {
        this.network = network;
    }

    /**
     * @return the base
     */
    public ExampleBase getBase() {
        return base;
    }

    /**
     * @param base the base to set
     */
    public void setBase(ExampleBase base) {
        this.base = base;
    }

    /**
     * @return the passingErrorRate
     */
    public double getPassingErrorRate() {
        return passingErrorRate;
    }

    /**
     * @param passingErrorRate the passingErrorRate to set
     */
    public void setPassingErrorRate(double passingErrorRate) {
        this.passingErrorRate = passingErrorRate;
    }

    /**
     * @return the passingErrorSize
     */
    public double getPassingErrorSize() {
        return passingErrorSize;
    }

    /**
     * @param passingErrorSize the passingErrorSize to set
     */
    public void setPassingErrorSize(double passingErrorSize) {
        this.passingErrorSize = passingErrorSize;
    }
}
