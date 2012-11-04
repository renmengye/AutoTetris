/**
 * Trainer takes in an Example database and returns the trained network
 */
package autotetris.ai.neurons;

import autotetris.ai.Example;
import autotetris.ai.ExampleBase;
import java.util.LinkedList;
import java.util.List;
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
        this.network = new Network(2, 1, .5);
        this.base = base;
        this.network.addHiddenLayer(3);
        this.network.addHiddenLayer(10);
        this.network.addHiddenLayer(3);
        this.passingErrorRate = error;
        this.passingErrorSize = size;
    }

    @Override
    public void run() {
        double errorOnce;
        double errorAverage = 0.0;
        int count;

        this.network.start();

        LinkedList<Double> errorList = new LinkedList<Double>();

        //train until the error average matches the precision and over the least number of cases
        for (count = 0; errorAverage > this.passingErrorRate | count < this.passingErrorSize; count++) {
            try {
                //get an example according to a randomized number
                Example<Double, Double> ex = this.base.getExample(new Random().nextDouble());

                network.setTraining(true);
                List<Double> result = this.network.runOnce(ex);
                //train the example and get the error
                errorOnce = ex.getExpectedValues().get(0) - result.get(0);

                //reset the probability of meeting the same example, has a learning factor of 5%
                this.base.setExampleProbability(ex, this.base.getExampleProbability(ex) * .995 + errorOnce * 0.005);

                //update database probability
                this.base.updateProbability();                    //update the whole tree
                System.out.printf("count: %d\tinput:%.1f %.1f\toutput:%.5f \terror: %.5f\n", count, ex.getInputValues().get(0), ex.getInputValues().get(1), result.get(0), errorOnce / 1.0);

                //update the errorlist
                if (errorList.size() >= this.passingErrorSize) {
                    errorAverage -= Math.abs(errorList.pop()) / (double) this.passingErrorSize;
                    errorAverage += Math.abs(errorOnce) / (double) this.passingErrorSize / 1.0;
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
}
