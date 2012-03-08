/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris.ai.neurons;

import autotetris.ai.FuncHub;
import autotetris.ai.Function_a;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author rmy
 */
public class booltest {

    public static void main(String[] args) {
        
        int isize=2,psize=2;
        
        final Random r = new Random();
        
        SimpleInputNeuron[] i = new SimpleInputNeuron[isize];
        Perceptron[] p = new Perceptron[psize];
        OutputNeuron o = new OutputNeuron(0, new Function_a() {

            public float func(float a) {
                return FuncHub.gauss(a);
                //return FuncHub.sigmoid(a);
                //return a;
            }

            public float dfunc(float a) {
                return FuncHub.dgauss(a);
                //return FuncHub.dsigmoid(a);
                //return 1;
            }
        });

        //initializing input neurons
        for (int j = 0; j < isize; j++) {
            i[j] = new SimpleInputNeuron(j, new Function_b() {
                public int func() {
                    return r.nextInt(2);
                    //return 1;
                }
            });
        }

        //initializing perceptrons
        for (int j = 0; j < psize; j++) {
            p[j] = new Perceptron(j, new Function_a() {

                public float func(float a) {
                    //return FuncHub.sigmoid(a);
                    return FuncHub.gauss(a);
                }

                public float dfunc(float a) {
                    //return FuncHub.dsigmoid(a);
                    return FuncHub.dgauss(a);
                }
            });

            //connecting perceptrons
            for (int k = 0; k < isize; k++) {
                i[k].add_target(p[j]);
                p[j].add_source(i[k], r.nextFloat());
            }

            p[j].add_target(o);
            o.add_source(p[j], r.nextFloat());
        }
        
        LinkedList<Float> error_list=new LinkedList();
        float eavg=0f;
        int count;
        //start to train, with end condition of error average
        for(count=0;eavg>0.05f||count<100;count++) {
            
            //forward-feed
            for (int k = 0; k < isize; k++) {
                i[k].calc_value();
            }
            for (int k = 0; k < psize; k++) {
                p[k].calc_value();
            }
            o.calc_value();
            
            //error back-prop
            int correct=xor(i[0].value,i[1].value);
            
            
            o.calc_error(correct);
            
            //System.out.printf("input: %5f, %5f;\tpredict: %5f;\tcorrect: %5d\terror: %5f\n",i[0].value,i[1].value,o.value,correct,o.error*5);
            System.out.println(o.error*5);
            //update weight
            o.update_weight();
            for (int k = 0; k < psize; k++) {
                p[k].calc_error();
                p[k].update_weight();
            }
            
            if(error_list.size()>=200){
                eavg-=Math.abs(error_list.pop())/200f;
                eavg+=Math.abs(o.error)/40f;
            }else{
                eavg=eavg*error_list.size()/(error_list.size()+1)+Math.abs(o.error*5)/(error_list.size()+1);
            }
            error_list.addLast(o.error*5);
            
        }
        System.out.println(count);
    }
    
    public static int xor(float a, float b){
        if((a==0&&b!=0)||(b==0&&a!=0)){
            return 1;
        }else{
            return 0;
        }
    }
}
