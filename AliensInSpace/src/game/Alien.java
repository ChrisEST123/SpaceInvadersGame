package game;

import utilities.SoundManager;
import utilities.Vector2D;

import java.awt.*;

/**
 * This class is for the purposes of the alien objects in the game. It first creates
 * the neccessary variables and then in the constructor takes the neccessary parameters
 * for the aliens position and direction and also the sprite version number that the
 * alien needs to use
 */
public class Alien extends GameObject{
    public static final int RADIUS = 8;
    boolean moveLeft;
    int ver;
    boolean sprites = false;
    int spriteCount = 0;
    int changeY = 0;
    boolean letsMove = false;
    int counter = 0;
    int counterGap = 75;
    Bullet bullet = null;
    boolean shoot;

    public static  final int ALIENWIDTH = 5*RADIUS;
    public static final int ALIENHEIGHT = 4*RADIUS;

    public Sprite sprite;

    public Alien(boolean move, int x, int y, int version){
        super(new Vector2D(x, y), RADIUS);
        moveLeft = move;
        ver = version;
        dir = new Vector2D(-5, changeY);

        if(ver == 1){
            Image im = Sprite.ALIEN1;
            sprite = new Sprite(im, pos, dir, ALIENWIDTH, ALIENHEIGHT);
        } else if (ver == 2){
            Image im = Sprite.ALIEN2;
            sprite = new Sprite(im, pos, dir, ALIENWIDTH, ALIENHEIGHT);
        } else {
            Image im = Sprite.ALIEN3;
            sprite = new Sprite(im, pos, dir, ALIENWIDTH, ALIENHEIGHT);
        }

        radius = sprite.getRadius();
        shoot = false;
        deathSound = SoundManager.bangSmall;
    }

    /**
     * This method is for creating the bullet from the aliens position
     */
    protected void mkBullet(){
        bullet = new Bullet(new Vector2D(pos), false);

        bullet.pos.add(0, 10);
    }

    public void shoot(){
        mkBullet();
    }

    /**
     * On each repaint or object update this method gets called. It first checks if the sprite should be moved.
     * If it does then depending on if the Y needs to be changed or not, the alien is moved to the given position:
     * left or right. This method also keeps check of the sprites, so on each next movement the sprite is changed.
     */
    public void update(){
        if(letsMove == true) {
            if (moveLeft == true) {
                if (changeY != 0) {
                    dir = new Vector2D(-5, changeY);
                } else {
                    dir = new Vector2D(-5, 0);
                }
            } else {
                if (changeY != 0) {
                    dir = new Vector2D(5, changeY);
                } else {
                    dir = new Vector2D(5, 0);
                }
            }
            super.update();

            if (spriteCount == 1) {
                if (sprites == true) {
                    SoundManager.play(SoundManager.beat1);
                    if (ver == 1) {
                        Image im = Sprite.ALIEN1;
                        sprite = new Sprite(im, pos, dir, ALIENWIDTH, ALIENHEIGHT);
                    } else if (ver == 2) {
                        Image im = Sprite.ALIEN2;
                        sprite = new Sprite(im, pos, dir, ALIENWIDTH, ALIENHEIGHT);
                    } else {
                        Image im = Sprite.ALIEN3;
                        sprite = new Sprite(im, pos, dir, ALIENWIDTH, ALIENHEIGHT);
                    }
                    sprites = false;
                } else {
                    SoundManager.play(SoundManager.beat2);
                    if (ver == 1) {
                        Image im = Sprite.ALIEN1_2;
                        sprite = new Sprite(im, pos, dir, ALIENWIDTH, ALIENHEIGHT);
                    } else if (ver == 2) {
                        Image im = Sprite.ALIEN2_2;
                        sprite = new Sprite(im, pos, dir, ALIENWIDTH, ALIENHEIGHT);
                    } else {
                        Image im = Sprite.ALIEN3_2;
                        sprite = new Sprite(im, pos, dir, ALIENWIDTH, ALIENHEIGHT);
                    }
                    sprites = true;
                }
                spriteCount = 0;
            }
            changeY = 0;
            letsMove = false;
        }
        if(counter == counterGap){
            counter = 0;
            letsMove = true;
            spriteCount++;
        }
        counter++;
    }

    /**
     * Draws the current aliens sprite
     */
    public void draw(Graphics2D g2d){
        sprite.draw(g2d);
    }

    /**
     * Overridden to check if the alien is hit by the player ship or hit by the players bullet
     */
    @Override
    public boolean canHit(GameObject other) {

        return other instanceof PlayerShip || other instanceof Bullet && ((Bullet) other).firedByPlayerShip;
    }
}
