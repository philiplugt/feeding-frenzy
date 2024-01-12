/**
 * Constants.java
 * 
 * @author pxv8780
 * @date   2023/03/28
 */

import java.awt.Dimension;

public final class Constants {

    // List of constants
    static final int SIZE = 15;
    static final int SPEED = 500;
    static final int FIRST_SPAWN_TIME = 500;
    static final int NEXT_SPAWN_TIME = 2000;
    static final String TITLE = "Feeding Frenzy";
    static final Dimension SCREEN = new Dimension(700, 700);
    
    // List of observer notification types
    static final String STATE_UPDATE = "state.update";
    static final String COUNT_UPDATE = "count.update";
    static final String TEXT_UPDATE = "text.update";
    static final String CREATE_UPDATE = "create.update";
    static final String MOVE_UPDATE = "move.update";
    static final String DISABLE_UPDATE = "disable.update";
    static final String RESET_UPDATE = "reset.update";
    static final String QUIT_UPDATE = "quit.update";
    
    // Empty constructor
    private Constants() { } 

}