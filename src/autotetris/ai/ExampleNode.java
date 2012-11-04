package autotetris.ai;

import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author rmy
 */
public class ExampleNode implements Serializable{

    // TODO Make sserialization on the database
    
    private Example example;
    private ExampleNode left;
    private ExampleNode right;
    private double probability;
    private int size;

    // initialize an example container using a real example
    public ExampleNode(Example ex, double p) {
        example = ex;
        left = null;
        right = null;
        size = 1;
        probability = p;
    }

    // initialize an example container with two smaller children containers
    public ExampleNode(ExampleNode l, ExampleNode r) {
        example = null;
        left = l;
        right = r;
        size = left.getSize() + right.getSize();
        probability = left.getProbability() + right.getProbability();
    }

    //<editor-fold defaultstate="collapsed" desc="Getter/Setter">
    /**
     * @return the example
     */
    public Example getExample() {
        return example;
    }
    
    /**
     * @param example the example to set
     */
    public void setExample(Example example) {
        this.example = example;
    }
    
    /**
     * @return the left
     */
    public ExampleNode getLeft() {
        return left;
    }
    
    /**
     * @return the right
     */
    public ExampleNode getRight() {
        return right;
    }
    
    /**
     * @return the probability
     */
    public double getProbability() {
        return probability;
    }
    
    /**
     * @param probability the probability to set
     */
    public void setProbability(double probability) {
        this.probability = probability;
    }
    
    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }
    
    /**
     * @param left the left to set
     */
    public void setLeft(ExampleNode left) {
        this.left = left;
    }
    
    /**
     * @param right the right to set
     */
    public void setRight(ExampleNode right) {
        this.right = right;
    }
    
    /**
     * @param size the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }
    //</editor-fold>
    
    // update the probablity (depth first) and sum up to the head
    public double updateProbability() {
        // if has left and right child container
        if (getLeft() != null && getRight() != null) {
            getLeft().updateProbability();
            getRight().updateProbability();
            setProbability(getLeft().getProbability() + getRight().getProbability());
        } // if has not right child
        else if (getLeft() != null) {
            getLeft().updateProbability();
            setProbability(getLeft().getProbability());
        }

        // if this is a leaf node
        return getProbability();
    }

    // update the the size (depth first)
    public int updateSize() {

        // if has left and right child container
        if (getLeft() != null && getRight() != null) {
            getLeft().updateSize();
            getRight().updateSize();
            setSize(getLeft().getSize() + getRight().getSize());
        } // if has not right child
        else if (getLeft() != null) {
            getLeft().updateSize();
            setSize(getLeft().getSize());
        }

        // if this is a leaf node
        return getSize();
    }

    // get an example container of leaf based on probability
    public ExampleNode getLeaf(double p) {

        // if having both left and right child, then split according to probability of 1
        if (getLeft() != null && getRight() != null) {
            //double leftValue = getLeft().getProbability();
            //double rightValue = getRight().getProbability();
            //double midPoint = leftValue / (leftValue + rightValue);
            double midPoint=0.5;
            
            if (p <= midPoint) {
                return getLeft().getLeaf(new Random().nextDouble());
            } else {
                return getRight().getLeaf(new Random().nextDouble());
            }
        } // if only having left child then turn it over to left child
        else if (getLeft() != null) {
            return getLeft().getLeaf(p);
        } //if this is already leaf then return this
        else {
            return this;
        }
    }

    // insert an example to the leaf, with certain probability
    public ExampleNode insertLeaf(Example ex, double p) {

        // if both chilren present, send example to child with smaller size
        if (getLeft() != null && getRight() != null) {
            if (getLeft().getSize() < getRight().getSize()) {
                return getLeft().insertLeaf(ex, p);
            } else {
                return getRight().insertLeaf(ex, p);
            }
        } // since there is no circumstance of only one child
        // check if left child is null (i.e. if this is leaf)
        // then creat a larger node contains the leaf and the new example
        else if (getLeft() == null) {
            ExampleNode newNode = new ExampleNode(ex, p);
            setRight(newNode);
            setLeft(this.clone());
            setExample(null);
            setSize(getLeft().getSize() + getRight().getSize());
            return newNode;
        }
        return null;
    }

    // clone an example containers
    @Override
    public ExampleNode clone() {
        return new ExampleNode(getExample(), getProbability());
    }
}
