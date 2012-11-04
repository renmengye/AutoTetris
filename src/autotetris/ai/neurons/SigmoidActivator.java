/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris.ai.neurons;

import java.io.Serializable;

/**
 *
 * @author renme_000
 */
public class SigmoidActivator implements FunctionActivator, Serializable {

    public double computeActivedValue(double a) {
        return Math.tanh(a - 0.5);
    }

    public double computeActivedValueDerivative(double a) {
        return (1 - Math.pow(Math.tanh(a - 0.5), 2));
    }
}
