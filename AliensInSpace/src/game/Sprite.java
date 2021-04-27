package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import utilities.ImageManager;
import utilities.Vector2D;

/**
 * This class is mostly taken from the Asteroids code from the labs as it has most of the functionality needed
 * for this game
 */
public class Sprite {
    public static Image PLAYERSHIP, PLAYERSHIPLEFT, PLAYERSHIPRIGHT, BOSS_SHIP, BOSS_SHIPLEFT, BOSS_SHIPRIGHT, BOSS_SHIPBOTH, ALIEN1, ALIEN1_2, ALIEN2,
     ALIEN2_2, ALIEN3, ALIEN3_2, BACKGROUND;
    static {
        try {
            PLAYERSHIP = ImageManager.loadImage("player_spaceship");
            PLAYERSHIPLEFT = ImageManager.loadImage("player_spaceship_left");
            PLAYERSHIPRIGHT = ImageManager.loadImage("player_spaceship_right");
            BOSS_SHIP = ImageManager.loadImage("boss_spaceship");
            BOSS_SHIPBOTH = ImageManager.loadImage("boss_spaceship_phase_2");
            ALIEN1 = ImageManager.loadImage("alien-12");
            ALIEN1_2 = ImageManager.loadImage("alien-13");
            ALIEN2 = ImageManager.loadImage("alien-08");
            ALIEN2_2 = ImageManager.loadImage("alien-09");
            ALIEN3 = ImageManager.loadImage("alien-10");
            ALIEN3_2 = ImageManager.loadImage("alien-11");
            BOSS_SHIPLEFT = ImageManager.loadImage("boss_spaceship_left_cannon_broken-06");
            BOSS_SHIPRIGHT = ImageManager.loadImage("boss_spaceship_right_cannon_broken-06-07");
            BACKGROUND = ImageManager.loadImage("space_background");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Image image;
    public Vector2D position;
    public Vector2D direction;
    public double width;
    public double height;

    public Sprite(Image image, Vector2D s, Vector2D direction, double width,
                  double height) {
        this.image = image;
        this.position = s;
        this.direction = direction;
        this.width = width;
        this.height = height;
    }

    public double getRadius() {
        return (width + height) / 4.0;
    }

    public Rectangle2D getBounds2D() {
        return new Rectangle2D.Double((position.x - width / 2), position.y - height / 2, width,
                height);
    }

    public void draw(Graphics2D g) {
        double imW = image.getWidth(null);
        double imH = image.getHeight(null);
        AffineTransform t = new AffineTransform();
        t.scale(width / imW, height / imH);
        t.translate(-imW / 2.0, -imH / 2.0);
        AffineTransform t0 = g.getTransform();
        g.translate(position.x, position.y);
        g.drawImage(image, t, null);
        g.setTransform(t0);
        g.setColor(Color.RED);
    }
}

