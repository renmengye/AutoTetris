package autotetris.ai;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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

    //insert an example to the data base
    public void insert(Example ex, double p) {

        if (map.containsKey(ex)) {
            throw new IllegalArgumentException("Cannot insert repeated examples");
        }


        //use node method to insert
        if (head != null) {
            map.put(ex, head.insertLeaf(ex, p));
            head.updateSize();
        } //if no head then create a head
        else {
            head = new ExampleNode(ex, p);
            map.put(ex, head);
        }
        updateProbability();
    }

    //get an example based on a random probability
    public Example getExample(double probability) {
        return head != null ? head.getLeaf(probability).getExample() : null;
    }

    public double getExampleProbability(Example ex) {
        return map.get(ex).getProbability();
    }

    public void setExampleProbability(Example ex, double probability) {
        map.get(ex).setProbability(probability);
    }

    //update the probability of the entire tree
    public void updateProbability() {
        head.updateSize();
        head.updateProbability();
    }
}
