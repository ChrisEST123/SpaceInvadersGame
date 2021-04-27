package game;

/**
 * Mostly borrowed from the Asteroids code as it has most of the functions required for this game
 */

import utilities.SoundManager;
import utilities.Vector2D;

import javax.sound.sampled.Clip;
import java.awt.*;

import static game.Constants.*;

public abstract class GameObject {
    public Vector2D pos;
    public Vector2D dir;
    public double radius;
    public boolean dead;
    public Clip deathSound = null;


    public GameObject(Vector2D pos, double radius) {
        this.pos = pos;
        this.radius = radius;
        this.dead = false;
        this.dir = new Vector2D(1,0);
    }

    public void update() {
        pos.add(dir);
        pos.wrap(FRAME_WIDTH, FRAME_HEIGHT);
    }

    public boolean overlap(GameObject other) {
        return dist(other) <= radius + other.radius;
    }

    // distance to other object in a wrapped world
    public double dist(GameObject other) {
        double dx = this.pos.x - other.pos.x;
        if (Math.abs(dx) > FRAME_WIDTH / 2)
            dx = Math.abs(dx) - FRAME_WIDTH;
        double dy = this.pos.y - other.pos.y;
        if (Math.abs(dy) > FRAME_HEIGHT / 2)
            dy = Math.abs(dy) - FRAME_HEIGHT;
        return Math.hypot(dx, dy);
    }

    public void collisionHandling(GameObject other) {
        // returns any score to be added
        if ((!this.dead || !other.dead) && this.canHit(other) && this.overlap(other)){
            this.hit();
            other.hit();
        }
    }

    public abstract boolean canHit(GameObject other);

    public abstract void draw(Graphics2D g);

    public void hit()
    { dead = true;
        if (deathSound!=null)
            SoundManager.play(deathSound);
    }
}
