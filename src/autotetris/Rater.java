/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autotetris;

/**
 *
 * @author rmy
 */
public class Rater implements ATCommon{
    
    public static final int height_coef=-20;
    public static final int holes_coef=-5;
    public static final int density_coef=0;
    public static final int score_coef=1000;
    private Board board;
    
    public int rate(Board board){
        this.board=board;
        int pscore=board.checkFull();
        int height_score=rate_height(board);
        int holes_score=rate_holes(board);
        int final_rate=score_coef*pscore+height_coef*height_score+holes_coef*holes_score+density_coef*rate_density(board)+1000;
        System.out.println("new board rating::");
        board.printBoard();
        System.out.printf("pscore:%d height:%d holes:%d final:%d ",pscore,height_score,holes_score,final_rate);
        return final_rate;
    }
    
    public int rate_height(Board board){
        int height;
        for(height=0;height<YNUM;height++){
            if(!board.checkEmpty(height)){
                break;
            }
        }
        height=YNUM-height;
        return height;
    }
    
    public int rate_holes(Board board){
        int count=0;
        for(int j=2;j<YNUM;j++){
            for(int i=0;i<XNUM;i++){
                if(board.getBoard()[j][i]==0){
                    for(int m=j-1;m>0;m--){
                        if(board.getBoard()[m][i]==1){
                            count++;
                            if(count>2){
                                break;
                            }
                        }
                    }
                }
            }
        }
        return count;
    }
    
    public int rate_density(Board board){
        for(int j=0;j<YNUM;j++){
            if(board.check4(j)){
                return (int)board.density(j)*100;
            }
        }
        return 0;
    }
}
