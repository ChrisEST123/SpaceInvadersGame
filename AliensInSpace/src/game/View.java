package game;

import utilities.ImageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

import static game.Constants.*;

/**
 * This class sets up the whole painting component for the game. The constructor takes in the image sizes and
 * the current game object. It also sets up the affine transform for the images.
 */
public class View extends JComponent {
    public static final Color TEXT_BG_COLOR = Color.DARK_GRAY, TEXT_COLOR = Color.WHITE;
    private Game game;
    Image im = Sprite.BACKGROUND;
    AffineTransform bgTransf;

    public View(Game game){
        this.game = game;
        double imWidth = im.getWidth(null);
        double imHeight = im.getHeight(null);
        double stretchX = imWidth > FRAME_WIDTH ? 1 : FRAME_WIDTH / imWidth;
        double stretchY = imHeight > FRAME_HEIGHT ? 1 : FRAME_HEIGHT / imHeight;
        bgTransf = new AffineTransform();
        bgTransf.scale(stretchX, stretchY);
    }

    /**
     *  On each update it checks through the object loop on which objects need to be drawn and which not
     *  After that it always draws the upper and lower border of the game and the messages on those boxes
     *  accordingly.
     */
    public void paintComponent(Graphics graphics){
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.drawImage(im, bgTransf, null);
        synchronized (Game.class) {
            for (GameObject ob : game.objects)
                ob.draw(g2d);
        }
        g2d.setColor(TEXT_BG_COLOR);
        g2d.fillRect(0, getHeight() - SCORE_PANEL_HEIGHT, getWidth(), SCORE_PANEL_HEIGHT);
        g2d.setColor(TEXT_COLOR);
        g2d.drawRect(0, getHeight() - SCORE_PANEL_HEIGHT, getWidth(), SCORE_PANEL_HEIGHT);
        g2d.setFont(new Font("dialog", Font.BOLD, (2 * SCORE_PANEL_HEIGHT / 3)));
        g2d.drawString("Score " + Integer.toString(game.getScore()), 10, getHeight() - SCORE_PANEL_HEIGHT / 3);
        g2d.drawString("Lives " + Integer.toString(game.getLives()), 300, getHeight() - SCORE_PANEL_HEIGHT / 3);
        g2d.drawString("Level " + Integer.toString(game.getLevel()), 550, getHeight() - SCORE_PANEL_HEIGHT / 3);
        g2d.setColor(TEXT_BG_COLOR);
        g2d.fillRect(0, 0, getWidth(), POWERUP_PANEL_HEIGHT);
        g2d.setColor(TEXT_COLOR);
        g2d.drawRect(0, 0, getWidth(), POWERUP_PANEL_HEIGHT);
        g2d.setFont(new Font("dialog", Font.BOLD, (2 * POWERUP_PANEL_HEIGHT / 3)));
        g2d.drawString(game.getMessage(), getWidth() / 2 - game.getMessage().length() / 2 - 150, POWERUP_PANEL_HEIGHT / 2);
    }

    public Dimension getPreferredSize() {return Constants.FRAME_SIZE;}
}
