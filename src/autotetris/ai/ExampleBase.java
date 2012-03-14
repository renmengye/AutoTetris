/*
 * A tree structure stores Examples
 */
package autotetris.ai;

/**
 *
 * @author MengYe
 */
class ExampleNode {

    private Example example;
    private ExampleNode left;
    private ExampleNode right;
    private double probab;
    private int size;

    public ExampleNode(Example ex, double p) {
        left = null;
        right = null;
        size = 0;
        probab = p;
    }

    public ExampleNode(ExampleNode l, ExampleNode r) {
        example = null;
        left = l;
        right = r;
        size = left.size()+right.size();
        probab = left.value() + right.value();
    }

    public ExampleNode left() {
        return left;
    }

    public ExampleNode right() {
        return right;
    }

    public Example example() {
        return example;
    }

    public double value() {
        return probab;
    }
    
    public int size(){
        return size;
    }

    public double update_value() {
        if(left!=null&&right!=null){
            left.update_value();
            right.update_value();
            probab=left.value()+right.value();
        }else if(left!=null){
            left.update_value();
            probab=left.value();
        }
        return probab;
    }

    /*public void set_value(double p) {
        probab = p;
    }*/
    
    public int update_size(){
        if(left!=null&&right!=null){
            left.update_size();
            right.update_size();
            size=left.size()+right.size();
        }else if(left!=null){
            left.update_size();
            size=left.size();
        }else{
            size=0;
        }
        return size;
    }

    public ExampleNode get(double p) {
        if (left != null && right != null) {
            double lv = left.value();
            double rv = right.value();
            double lr = lv / rv;
            if (p <= lr) {
                return left.get(p);
            } else {
                return right.get(p);
            }
        } else if (left != null) {
            return left.get(p);
        } else {
            return this;
        }
    }
    
    public ExampleNode min_size(ExampleNode a, ExampleNode b){
        return a.size()<=b.size()?a:b;
    }

    public void insert(Example ex,double p) {
        if(left.size()==0){
            ExampleNode exn=new ExampleNode(left,new ExampleNode(ex,p));
            left=exn;
            left.size=2;
        }else if(right.size()==0){
            ExampleNode exn=new ExampleNode(right,new ExampleNode(ex,p));
            right=exn;
            right.size=2;
        }else{
            min_size(left,right).insert(ex, p);
        }
    }
}

public class ExampleBase {

    ExampleNode head;

    public void insert(Example ex,double p) {
        head.insert(ex,p);
        head.update_size();
    }

    public Example get(double p) {
        ExampleNode r = head.get(p);
        return r.example();
    }
}
