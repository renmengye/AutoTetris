/*
 * AutoTetrisApp.java
 */

package autotetris;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class AutoTetrisApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    
    
    @Override protected void startup() {
        AutoTetrisView view=new AutoTetrisView(this);
        show(view);
        view.new_game();
        //AutoTetrisCommand command=new AutoTetrisCommand();
        //for(int i=0;i<=99;i++){
            //command.new_game();
            //while(command.t.isRunning());
        //}
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of AutoTetrisApp
     */
    public static AutoTetrisApp getApplication() {
        return Application.getInstance(AutoTetrisApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(AutoTetrisApp.class, args);
    }
}
