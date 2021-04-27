package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * This class is to keep track of the player keyboard key presses. Depending on what is pressed
 * the values in Action are changed.
 */
public class Keys extends KeyAdapter implements Controller {
    Action action;
    public Keys() {
        action = new Action();
    }

    @Override
    public Action action() {
        return action;
    }

    @Override
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        switch (key){
            case KeyEvent.VK_LEFT:
                action.movement = -10;
                break;
            case KeyEvent.VK_RIGHT:
                action.movement = 10;
                break;
            case KeyEvent.VK_SPACE:
                action.shoot = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                action.movement = 0;
                break;
            case KeyEvent.VK_RIGHT:
                action.movement = 0;
                break;
            case KeyEvent.VK_SPACE:
                action.shoot = false;
                break;
        }
    }
}
