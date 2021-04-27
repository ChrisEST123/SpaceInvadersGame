package game;

import utilities.SoundManager;
import utilities.Vector2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * This class is for the blocks of the blue base. Each base consists of these base blocks and so
 * every block is checked for if it is destroyed or not. Otherwise the whole base would disappear.
 * It first takes in the position coordinated in the constructor so the class knows where the base
 * block should be placed.
 */
public class BaseBlock extends GameObject{
    public int width = 10;
    Rectangle2D block;
    int x;
    int y;

    BaseBlock(int x, int y) {
        super(new Vector2D(x, y), 5);
        this.x = x;
        this.y = y;
        block = new Rectangle(x, y, width, width);

        deathSound = SoundManager.bangMedium;
    }

    /**
     * This method checks whether any of the bullets shot hits the base.
     */
    @Override
    public boolean canHit(GameObject other) {
        return other instanceof Bullet && ((Bullet) other).firedByPlayerShip || other instanceof Bullet && !((Bullet) other).firedByPlayerShip || other instanceof Alien;
    }

    /**
     * If the block is hit then the block is dead, so no longer repainted.
     */
    @Override
    public void hit(){
        dead = true;
    }

    /**
     * Overridden so the blocks position won't update with the GameObject class
     */
    @Override
    public void update(){
    }

    /**
     * Draws the base block.
     */
    public void draw(Graphics2D g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, width, width);
    }
}
