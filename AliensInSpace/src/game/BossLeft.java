package game;

import utilities.SoundManager;
import utilities.Vector2D;

import java.awt.*;

/**
 * This class is for the left side of the boss. For the functions you can refer to the boss class
 * as this is mostly a copy of it, just in a different location. Only difference is that everything
 * related to the sprites is not used, so draw is empty and sprite is not changed dependant on the
 * current location, as this class has no sprite to draw.
 */
public class BossLeft extends GameObject{
    int lives = 10;
    boolean moveLeft = true;
    boolean letsMove = true;
    int counter = 0;
    int counterGap = 15;

    public BossLeft(Vector2D pos, double radius) {
        super(pos, radius);
    }

    @Override
    public boolean canHit(GameObject other) {
        return other instanceof PlayerShip || other instanceof Bullet && ((Bullet) other).firedByPlayerShip;
    }

    @Override
    public void draw(Graphics2D g) {

    }

    @Override
    public void hit(){
        if(lives == 0){
            dead = true;
        } else {
            lives--;
        }
        SoundManager.play(SoundManager.bangMedium);
    }

    @Override
    public void update(){
        if(letsMove == true){
            if(moveLeft == true){
                dir = new Vector2D(-5, 0);
            } else {
                dir = new Vector2D(5, 0);
            }
            super.update();
            letsMove = false;
        }
        if(counter == counterGap){
            counter = 0;
            letsMove = true;
        }
        counter++;
    }
}
