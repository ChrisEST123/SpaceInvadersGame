package game;

import utilities.SoundManager;
import utilities.Vector2D;

import java.awt.*;

import static game.Constants.FRAME_HEIGHT;
import static game.Constants.FRAME_WIDTH;

/**
 * This class is for the main component of the boss. It sets up the overall width and height of the boss and also
 * handles the overall sprite of the boss. The constructor initialises the boss with a position, sets up the sprite,
 * and also sets up the left and right boss components accordingly. The reasoning behind having separate components
 * of the boss is that in this way the boss fight can be made up into stages, so the middle part only takes damage when
 * the left and right sides are destroyed.
 */
public class Boss extends Ship{
    public static final int BOSS_WIDTH = 320;
    public static final int BOSS_HEIGHT = 160;

    public static final int MID_WIDTH = 100;

    BossLeft leftBoss;
    BossRight rightBoss;
    boolean stage2 = false;
    boolean moveLeft = true;
    boolean letsMove = true;
    int counter = 0;
    int counterGap = 10;
    int shoot = 0;
    int shooterGap1 = 25;
    int shooterGap2 = 15;
    int lives = 25;
    Sprite sprite;

    public Boss() {
        super(new Vector2D(270, 120), MID_WIDTH/2);

        Image im = Sprite.BOSS_SHIP;
        sprite = new Sprite(im, new Vector2D(pos.x - 110, pos.y), dir, BOSS_WIDTH, BOSS_HEIGHT);

        leftBoss = new BossLeft(new Vector2D(pos.x - 110, pos.y), sprite.getRadius()/3);
        rightBoss = new BossRight(new Vector2D(pos.x + 110, pos.y), sprite.getRadius()/3);

        deathSound = SoundManager.bangLarge;
    }

    /**
     * Checks if the boss is hit by the player ship bullets
     */
    @Override
    public boolean canHit(GameObject other) {
        return other instanceof PlayerShip || other instanceof Bullet && ((Bullet) other).firedByPlayerShip;
    }

    /**
     * If the boss is hit then remove 1 health point, if lives are 0 then the boss is dead
     */
    @Override
    public void hit(){
        if(leftBoss.dead && rightBoss.dead){
            if(lives == 0){
                dead = true;
            } else {
                lives--;
                SoundManager.play(SoundManager.bangMedium);
            }
        }
    }

    /**
     * This method updates the Boss on every repaint and call to update. It first checks if the boss can move,
     * if it can then it checks whether or not it should move left or not. Afterwards it then checks if the boss
     * is in its last stage. If not then it checks if all the sides of the boss are alive, then accordingly sets
     * up the correct sprite and the correct shooting locations. If they are both dead then the bos goes into
     * second stage and starts shooting faster with the proper sprite.
     */
    @Override
    public void update(){
        if(letsMove == true){
            if(moveLeft){
                dir = new Vector2D(-5, 0);
            } else {
                dir = new Vector2D(5, 0);
            }
            letsMove = false;
            super.update();
        }
        if(counter == counterGap){
            counter = 0;
            letsMove = true;
        }
        counter++;

        if(stage2 == false){
            if(leftBoss.dead || rightBoss.dead){
                if(leftBoss.dead && rightBoss.dead){
                    stage2 = true;
                    shoot = 0;
                    counter = 0;
                } else if(leftBoss.dead){
                    Image im = Sprite.BOSS_SHIPLEFT;
                    sprite = new Sprite(im, pos, dir, BOSS_WIDTH, BOSS_HEIGHT);
                    if(shoot == shooterGap1){
                        SoundManager.play(SoundManager.fire2);
                        mkBullet();
                        mkBulletRight();
                        shoot = 0;
                    }
                } else {
                    Image im = Sprite.BOSS_SHIPRIGHT;
                    sprite = new Sprite(im, pos, dir, BOSS_WIDTH, BOSS_HEIGHT);
                    if(shoot == shooterGap1){
                        SoundManager.play(SoundManager.fire2);
                        mkBullet();
                        mkBulletLeft();
                        shoot = 0;
                    }
                }
            } else {
                Image im = Sprite.BOSS_SHIP;
                sprite = new Sprite(im, pos, dir, BOSS_WIDTH, BOSS_HEIGHT);
                if(shoot == shooterGap1){
                    SoundManager.play(SoundManager.fire2);
                    mkBullet();
                    mkBulletLeft();
                    mkBulletRight();
                    shoot = 0;
                }
            }
        } else {
            Image im = Sprite.BOSS_SHIPBOTH;
            sprite = new Sprite(im, pos, dir, BOSS_WIDTH, BOSS_HEIGHT);
            counterGap = 10;

            if(shoot == shooterGap2){
                SoundManager.play(SoundManager.fire2);
                mkBullet();
                shoot = 0;
            }
        }
        shoot++;
    }

    /**
     * Draws the current sprite.
     */
    @Override
    public void draw(Graphics2D g) {
        sprite.draw(g);
    }

    /**
     * This is for changing the boss direction from the main update method in Game
     */
    public void changeDirection(){
        if(moveLeft == true){
            moveLeft = false;
            rightBoss.moveLeft = false;
            leftBoss.moveLeft = false;
        } else {
            moveLeft = true;
            rightBoss.moveLeft = true;
            leftBoss.moveLeft = true;
        }
    }
}
