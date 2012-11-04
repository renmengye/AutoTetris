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
public class GaussianActivator implements FunctionActivator, Serializable {

    public double computeActivedValue(double a) {
        return Math.exp(-a*a);
        //return 1 / (1 + Math.exp(-a));
    }

    public double computeActivedValueDerivative(double a) {
        return Math.exp(-a * a) * (-1) * 2 * a;
        //double value = this.computeActivedValue(a);
        //return (1 + value) * (1 - value);
    }
}
