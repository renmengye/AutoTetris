/*
 * A tree structure stores Examples
 */
package autotetris.ai;

/**
 *
 * @author MengYe
 */
public class ExampleBase {

    ExampleNode head;

    public void insert(Example ex, double p) {
        if (head != null) {
            head.insert(ex, p);
            head.update_size();
        } else {
            head = new ExampleNode(ex, p);
        }
        update_value();
    }

    public ExampleNode get(double p) {
        //ExampleNode r = head.get(p);
        //return r.example();
        return head.get(p);
    }

    public void update_value() {
        head.update_size();
        head.update_value();
    }
}
