package autotetris;

/**
 *
 * @author rmy
 */
public class AutoTetrisEntry {
    public static void main(String[] args) {
        //AutoTetrisFrame frame = new AutoTetrisFrame();
        //frame.setVisible(true);
        AutoTetrisCommand command=new AutoTetrisCommand();
        for(int i=0;i<100;i++){
            command.new_game();
        }
    }
}
