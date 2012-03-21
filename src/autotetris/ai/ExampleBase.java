package autotetris.ai;

/**
 *
 * @author MengYe
 */

public class ExampleBase {

    ExampleNode head;

    //insert an example to the data base
    public void insert(Example ex, double p) {

        //use node method to insert
        if (head != null) {
            head.insert(ex, p);
            head.update_size();
        }
        //if no head then create a head
        else {
            head = new ExampleNode(ex, p);
        }
        update_value();
    }

    //get an example based on a random probability
    public ExampleNode get(double p) {
        return head!=null?head.get(p):null;
    }

    //update the probability of the entire tree
    public void update_value() {
        head.update_size();
        head.update_value();
    }
}
