package autotetris.ai;

/**
 *
 * @author rmy
 */

public class ExampleNode {

    private Example example;
    private ExampleNode left;
    private ExampleNode right;
    private double probab;
    private int size;

    //initialize an example container using a real example
    public ExampleNode(Example ex, double p) {
        example = ex;
        left = null;
        right = null;
        size = 1;
        probab = p;
    }

    //initialize an example container with two smaller children containers
    public ExampleNode(ExampleNode l, ExampleNode r) {
        example = null;
        left = l;
        right = r;
        size = left.size() + right.size();
        probab = left.value() + right.value();
    }

    //get left child
    public ExampleNode left() {
        return left;
    }

    //get right child
    public ExampleNode right() {
        return right;
    }

    //get the example
    public Example example() {
        return example;
    }

    //get the probability
    public double value() {
        return probab;
    }

    //get the size (how many real example is under the path)
    public int size() {
        return size;
    }

    //update the probablity (depth first) and sum up to the head
    public double update_value() {

        //if has left and right child container
        if (left != null && right != null) {
            left.update_value();
            right.update_value();
            probab = left.value() + right.value();
        }

        //if has not right child
        else if (left != null) {
            left.update_value();
            probab = left.value();
        }

        //if this is a leaf node
        return probab;
    }

    //manually set the probability
    public void set_value(double p) {
        probab = p;
    }

    //update the the size (depth first)
    public int update_size() {

        //if has left and right child container
        if (left != null && right != null) {
            left.update_size();
            right.update_size();
            size = left.size() + right.size();
        }

        //if has not right child
        else if (left != null) {
            left.update_size();
            size = left.size();
        }

        //if this is a leaf node
        return size;
    }

    //get an example container of leaf based on probability
    public ExampleNode get(double p) {

        //if having both left and right child, then split according to probability of 1
        if (left != null && right != null) {
            double lv = left.value();
            double rv = right.value();
            double lr = lv / rv;
            if (p <= lr) {
                return left.get(p);
            } else {
                return right.get(p);
            }
        }

        //if only having left child then turn it over to left child
        else if (left != null) {
            return left.get(p);
        }

        //if this is already leaf then return this
        else {
            return this;
        }
    }

    //returns a smaller size container, for better insertion process
    public ExampleNode min_size(ExampleNode a, ExampleNode b) {
        return a.size() <= b.size() ? a : b;
    }

    //insert an example to the leaf
    public void insert(Example ex, double p) {

        //if both chilren present, send example to child with smaller size
        if (left != null && right != null) {
            min_size(left, right).insert(ex, p);
        }

        //since there is no circumstance of only one child
        //check if left child is null (i.e. if this is leaf)
        //then creat a larger node contains the leaf and the new example
        else if (left == null) {
            ExampleNode exn = new ExampleNode(ex, p);
            right = exn;
            left = this.clone();
            this.example = null;
            this.size = left.size() + right.size();
        }
    }

    //clone an example containers
    @Override
    public ExampleNode clone() {
        return new ExampleNode(this.example, this.probab);
    }
}
