package game;

import utilities.SoundManager;
import utilities.Vector2D;

import java.awt.*;

import static game.Constants.*;

/**
 * This class is for the player ship. It sets up the required variables and then the constructor takes in a controller
 * object and creates a new ship object with it. It also sets up the direction, starting sprite and death sound.
 */
public class PlayerShip extends Ship{
    public static final int RADIUS = 8;

    public static  final int SHIPWIDTH = 5*RADIUS;
    public static final int SHIPHEIGHT = 4*RADIUS;
    public boolean destruction = false;
    int destructionCount = 0;
    int destructionGap = 500;

    public Sprite sprite;

    private Controller ctrl;

    public PlayerShip(Controller ctrl){
        super(new Vector2D(FRAME_WIDTH / 2, PLAYERSHIPY), RADIUS, ctrl);
        this.ctrl = ctrl;
        dir = new Vector2D(0, -1);

        Image im = Sprite.PLAYERSHIP;
        sprite = new Sprite(im, pos, dir, SHIPWIDTH, SHIPHEIGHT);
        deathSound = SoundManager.bangMedium;
        radius = sprite.getRadius();
    }

    /**
     * The update function sets the direction depending on the action value from the current key press.
     * This method also sets the correct sprite depending on how the ship is moving. Afterwards it checks
     * if the player ship wants to go out of bounds of the frame, if it does then it puts the ship back
     * into the frame. It also keeps the track of the destruction power up being active.
     */
    public void update() {
        Action action = ctrl.action();
        dir = new Vector2D(action.movement, 0);
        super.update();
        if(action.movement > 0){
            Image im = Sprite.PLAYERSHIPRIGHT;
            sprite = new Sprite(im, pos, dir, SHIPWIDTH, SHIPHEIGHT);
        } else if(action.movement < 0) {
            Image im = Sprite.PLAYERSHIPLEFT;
            sprite = new Sprite(im, pos, dir, SHIPWIDTH, SHIPHEIGHT);
        } else {
            Image im = Sprite.PLAYERSHIP;
            sprite = new Sprite(im, pos, dir, SHIPWIDTH, SHIPHEIGHT);
        }
        if(pos.x < SHIPWIDTH){
            pos.x = SHIPWIDTH;
        } else if(pos.x + SHIPWIDTH > FRAME_WIDTH){
            pos.x = FRAME_WIDTH - SHIPWIDTH;
        }
        if(destruction == true){
            if(destructionCount == destructionGap){
                destruction = false;
            } else {
                destructionCount++;
            }
        }
    }

    /**
     * This makes sure that the ship can only be hit by aliens and their bullets.
     */
    @Override
    public boolean canHit(GameObject other) {
        return other instanceof Alien || other instanceof Bullet && !((Bullet) other).firedByPlayerShip;
    }

    /**
     * Draws the ship sprite.
     */
    public void draw(Graphics2D g) {
        sprite.draw(g);
    }

    /**
     * If the ship is shot it is dead
     */
    @Override
    public void hit() {
        this.dead = true;
    }
}
