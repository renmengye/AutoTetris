package autotetris.ai;

import autotetris.ai.neurons.boolTestSer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MengYe
 */
public class ExampleBase implements Serializable {

    private ExampleNode head;
    private Map<Example, ExampleNode> map;

    public ExampleBase() {
        head = null;
        map = new HashMap<Example, ExampleNode>();
    }

    //<editor-fold defaultstate="collapsed" desc="Getter/Setter">
    /**
     * @return the head
     */
    public ExampleNode getHead() {
        return head;
    }

    /**
     * @param head the head to set
     */
    private void setHead(ExampleNode head) {
        this.head = head;
    }

    /**
     * @return the map
     */
    private Map<Example, ExampleNode> getMap() {
        return map;
    }

    /**
     * @param map the map to set
     */
    private void setMap(Map<Example, ExampleNode> map) {
        this.map = map;
    }

    //get an example based on a random probability
    public Example getExample(double probability) {
        return getHead() != null ? getHead().getLeaf(probability).getExample() : null;
    }

    public double getExampleProbability(Example ex) {
        return getMap().get(ex).getProbability();
    }

    public void setExampleProbability(Example ex, double probability) {
        getMap().get(ex).setProbability(probability);
    }
    //</editor-fold>

    //insert an example to the data base
    public void insert(Example ex, double p) {

        if (getMap().containsKey(ex)) {
            throw new IllegalArgumentException("Cannot insert repeated examples");
        }


        //use node method to insert
        if (getHead() != null) {
            getMap().put(ex, getHead().insertLeaf(ex, p));
            getHead().updateSize();
        } //if no head then create a head
        else {
            setHead(new ExampleNode(ex, p));
            getMap().put(ex, getHead());
        }
        updateProbability();
    }

    //update the probability of the entire tree
    public void updateProbability() {
        getHead().updateSize();
        getHead().updateProbability();
    }

    public static ExampleBase read(String path) throws FileNotFoundException, IOException, ClassNotFoundException {
        ExampleBase base = null;
        File baseFile = new File(path);
        if (baseFile.exists()) {
            FileInputStream fis;
            ObjectInputStream ois;
            fis = new FileInputStream(baseFile);
            ois = new ObjectInputStream(fis);
            base = (ExampleBase) ois.readObject();
        } else {
            throw new FileNotFoundException();
        }
        return base;
    }

    public void save(String path) {
        FileOutputStream fos;
        ObjectOutputStream oos;

        try {
            fos = new FileOutputStream(path);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(boolTestSer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
