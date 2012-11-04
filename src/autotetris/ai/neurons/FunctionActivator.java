/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris.ai.neurons;

/**
 *
 * @author renme_000
 */
public interface FunctionActivator {
    public double computeActivedValue(double a);
    public double computeActivedValueDerivative(double a);
}
