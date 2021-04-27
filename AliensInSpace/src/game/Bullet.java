package game;

import utilities.Vector2D;

import java.awt.*;

/**
 * This class is for the overall use of bullets in the game. The constructor checks if the bullet was shot by a
 *  player ship or not and then also takes in the current shooters position.
 */
public class Bullet extends GameObject{
    boolean firedByPlayerShip;
    public static final int RADIUS = 2;

    Bullet(Vector2D pos, boolean isPlayerShip){
        super(pos, RADIUS);
        firedByPlayerShip = isPlayerShip;
    }

    /**
     * The update method checks if the bullet is a player ship and then accordingly sets the bullet speed and
     * direction of travel.
     */
    public void update(){
        if(firedByPlayerShip == true){
            if(pos.y < 40){
                dead = true;
            }
            dir = new Vector2D(0, -15);
        } else {
            if(pos.y > 620){
                dead = true;
            }
            dir = new Vector2D(0, 5);
        }
        super.update();
    }

    /**
     *  Checks if the bullet is a player ship bullet or if it hits another object or bullet.
     */
    @Override
    public boolean canHit(GameObject other) {
        return firedByPlayerShip || other.getClass() == PlayerShip.class || other.getClass() == Bullet.class;
    }

    /**
     *  Draws the bullet object, colour depends on the shooter.
     */
    @Override
    public void draw(Graphics2D g) {
        if(firedByPlayerShip == true){
            g.setColor(Color.WHITE);
        } else {
            g.setColor(Color.GREEN);
        }
        g.fillOval((int) pos.x-RADIUS, (int) pos.y-RADIUS, 2*RADIUS, 2*RADIUS);
    }

    /**
     * If the bullet hits something it is considered dead.
     */
    @Override
    public void hit() {
        dead = true;
    }
}
