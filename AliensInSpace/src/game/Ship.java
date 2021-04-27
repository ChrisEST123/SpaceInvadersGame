package game;

import utilities.SoundManager;
import utilities.Vector2D;

/**
 * This class holds all the neccessary functions for both ships. The constructor varies between having
 * a controller and not having one. It sets up the current ships position.
 */
public abstract class Ship extends GameObject{
    public static final long SHOT_DELAY=500;
    public long timeLastShot = System.currentTimeMillis();
    public Bullet bullet = null;
    public Bullet leftBullet = null;
    public Bullet rightBullet = null;
    Controller ctrl;

    public Ship(Vector2D pos, double radius, Controller cntrl) {
        super(pos, radius);
        this.ctrl = cntrl;
    }

    public Ship(Vector2D pos, double radius){
        super(pos,radius);
    }

    /**
     * This function makes the bullet with the position depending on who shot it.
     */
    protected void mkBullet(){
        bullet = new Bullet(new Vector2D(pos), this.getClass() == PlayerShip.class);
        if(this.getClass() == PlayerShip.class){
            bullet.pos.add(0, -15);
        } else {
            bullet.pos.add(0, 10);
        }
    }

    /**
     * This is for the boss ship and player ship when the player ship has destruction power up.
     * It shoots a bullet a bit from the left.
     */
    protected void mkBulletLeft(){
        if(this.getClass() == PlayerShip.class){
            double newX = pos.x - 10;
            leftBullet = new Bullet(new Vector2D(newX, pos.y), true);
            leftBullet.pos.add(0, -15);
        } else {
            double newX = pos.x - 140;
            leftBullet = new Bullet(new Vector2D(newX, pos.y), false);
            leftBullet.pos.add(0, 10);
        }
    }

    /**
     * This is for the boss ship and player ship when the player ship has destruction power up.
     * It shoots a bullet a bit from the right.
     */
    protected void mkBulletRight(){
        if(this.getClass() == PlayerShip.class){
            double newX = pos.x + 10;
            rightBullet = new Bullet(new Vector2D(newX, pos.y), true);
            rightBullet.pos.add(0, -15);
        } else {
            double newX = pos.x + 140;
            rightBullet = new Bullet(new Vector2D(newX, pos.y), false);
            rightBullet.pos.add(0, 10);
        }
    }

    /**
     * The update keeps track of if the player shoots a bullet or not. If the shooting is true then
     * update creates and updates the bullet.
     */
    @Override
    public void update() {
        Action action = new Action();
        super.update();
        if(this.getClass() == PlayerShip.class){
            action = ctrl.action();
        }
        long timeNow = System.currentTimeMillis();
        if (action.shoot && timeNow - timeLastShot > SHOT_DELAY) {
            if(this.getClass() == PlayerShip.class){
                if(((PlayerShip) this).destruction == true){
                    mkBulletLeft();
                    mkBulletRight();
                }
            }
            mkBullet();
            action.shoot = false;
            timeLastShot = timeNow;
            SoundManager.fire();
        }
    }
}
