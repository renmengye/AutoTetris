package autotetris.ai;

import java.util.List;

/**
 *
 * @author rmy
 */
public class Example<TI,TO> {
    
    protected List<TI> input;
    protected List<TO> correct;
    
    public Example(List<TI> i, List<TO> o){
        input=i;
        correct=o;
    }
    
    public Example(List<TI> i){
        input=i;
        correct=null;
    }
    
    public List input(){
        return input;
    }
    
    public List correct(){
        return correct;
    }
    
    public String to_string(){
        return null;
    }
}
