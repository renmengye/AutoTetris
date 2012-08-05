/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris.ai.neurons;

import autotetris.ai.Example;
import autotetris.ai.ExampleBase;
import autotetris.ai.FuncHub;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author t-mren
 */
public class CreateExampleBase {

    public static void main(String[] args) {
        ExampleBase base = new ExampleBase();

        for (int r1 = 0; r1 < 2; r1++) {
            for (int r2 = 0; r2 < 2; r2++) {
                List<Double> u = new LinkedList<Double>();
                List<Double> v = new LinkedList<Double>();
                u.add((double) r1);
                u.add((double) r2);
                v.add((double) FuncHub.xor(r1, r2));
                base.insert(new Example<Double, Double>(u, v), 1.0);
            }
        }
        
        writeBase(base);
    }

    public static void writeBase(ExampleBase base) {
        String file = "XorExampleBase.ser";
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(base);
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(boolTestSer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
