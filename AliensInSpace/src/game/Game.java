package game;

import utilities.JEasyFrame;
import utilities.SoundManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static game.Constants.*;

/**
 * This is the main class that keeps the game running, it first creates all the neccessary variables for
 * the game and then in the constructor sets up all the game objects, power up locations, lives, level,
 *  and also the frame of the game.
 */
public class Game {
    Keys ctrl;
    PlayerShip ship;
    Boss boss_ship;
    Collection<Object> alive;
    Collection<Ship> ships;
    Alien[] aliens = new Alien[50];
    BaseBlock[] baseBlocks = new BaseBlock[22];
    BaseBlock[] allBaseBlocks = new BaseBlock[22*4];
    boolean bossShipHere = false;
    int remainingAliens = 50;
    int score;
    int lives;
    int level;
    String message = "";
    public List<GameObject> objects;
    boolean finished;
    boolean resetting;
    long gameStartTime;
    long startTime;
    View view;
    int alienShoot = 0;
    int alienShootGap;
    Alien shooter1 = null;
    Alien shooter2 = null;
    Alien shooter3 = null;
    int messageCount = 0;
    int messageGap = 200;
    int extraLifeAlien;
    int destructionAlien;
    int precisionAlien;
    boolean extraLifeTriggered = false;
    boolean destructionTriggered = false;
    boolean precisionTriggered = false;
    boolean precision = false;
    int precisionCount = 0;
    int precisionGap = 500;


    public Game(){
        message = "Level 1";
        ctrl = new Keys();
        ship = new PlayerShip(ctrl);
        view = new View(this);
        int baseX = 80;
        int baseY = 500;
        int alienX = 75;
        int alienY = 120;
        objects = new ArrayList<GameObject>();
        ships = new ArrayList<Ship>();
        int x = 80;
        int baseBlockCount = 0;
        for(int j = 0; j <4; j++){
            for(int i = 0; i < baseBlocks.length; i++){
                if(baseY == 530 && baseX == x + 2 * 10){
                    baseX += 20;
                }

                allBaseBlocks[baseBlockCount] = new BaseBlock(baseX, baseY);
                objects.add(allBaseBlocks[baseBlockCount]);
                baseBlockCount++;
                baseX += 10;
                if(baseX == x + 6 * 10){
                    baseX = x;
                    baseY = baseY+10;
                }
            }
            baseY = 500;
            x += 140;
            baseX = x;
        }


        Random rn = new Random();
        extraLifeAlien = rn.nextInt(50) + 1;
        while (true) {
            destructionAlien = rn.nextInt(50) + 1;
            if (destructionAlien != extraLifeAlien) {
                break;
            }
        }
        while (true) {
            precisionAlien = rn.nextInt(50) + 1;
            if (precisionAlien != destructionAlien && precisionAlien != extraLifeAlien) {
                break;
            }
        }


        for(int i = 0; i < aliens.length; i++){
            if(alienY == 120){
                aliens[i] = new Alien(true, alienX, alienY, 1);
                objects.add(aliens[i]);
            } else if(alienY > 120 && alienY < 201){
                aliens[i] = new Alien(true, alienX, alienY, 2);
                objects.add(aliens[i]);
            } else {
                aliens[i] = new Alien(true, alienX, alienY, 3);
                objects.add(aliens[i]);
            }
            if(alienX == 525){
                alienX = 75;
                alienY += 40;
            } else {
                alienX += 50;
            }
        }
        objects.add(ship);
        ships.add(ship);
        score = 0;
        lives = 3;
        level = 1;
        finished = false;
        resetting = false;
        alienShootGap = 50;
        new JEasyFrame(view, "BasicGame").addKeyListener(ctrl);
    }

    /**
     *  This function starts the game
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.gameLoop();
    }

    String name;

    /**
     * This function keeps the game going until it is finished. If the game is finished then it prints out
     * the score and game time on the terminal and sets up a new frame where it asks for the players name.
     * Before that it also checks if there were any previous scores in the scoreboard.txt. If the file does
     * not exist then it creates it instead. If the submit button is clicked then the ScoreHandler is called.
     */
    public void gameLoop() {
        gameStartTime = startTime = System.currentTimeMillis();
        while (!finished) {
            long time0 = System.currentTimeMillis();
            update();
            view.repaint();
            long timeToSleep = time0 + DELAY - System.currentTimeMillis();
            if (timeToSleep < 0)
                System.out.println("Warning: timeToSleep negative");
            else
                try {
                    Thread.sleep(timeToSleep);
                } catch (Exception e) {
                }
        }
        message = "You won! Art by Reigo Metsik";
        System.out.println("Your score was " + score);
        System.out.println("Game time " + (int) ((System.currentTimeMillis() - gameStartTime) / 1000));

        Map<String, Integer> scores = new HashMap<>();

        try {
            File f = new File("ScoreBoard.txt");
            if (f.createNewFile()) {
                System.out.println("File created: " + f.getName());
            } else {
                Scanner myReader = new Scanner(f);
                while (myReader.hasNextLine()) {
                    String data = myReader.nextLine();
                    String[] parts = data.split("\\.");
                    String better = parts[1];
                    String[] betterParts = better.split(" ");
                    String names = betterParts[1];
                    Integer score = Integer.valueOf(betterParts[2]);
                    scores.put(names, score);
                }
                myReader.close();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        JFrame frame = new JFrame();
        JPanel infoPanel = new JPanel();
        frame.setTitle("Scores");
        frame.setSize(400, 100);

        JButton submit = new JButton("Submit");
        JTextField textField = new JTextField(20);
        textField.setText("");
        JLabel title = new JLabel("Please insert your name: ");
        title.setFont(new Font("Verdana",1,10));

        infoPanel.add(title, BorderLayout.NORTH);
        infoPanel.add(textField, BorderLayout.CENTER);
        infoPanel.add(submit, BorderLayout.SOUTH);

        frame.add(infoPanel);
        frame.setContentPane(infoPanel);
        textField.setEditable(true);

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                name = textField.getText();
                ScoreHandler handler = new ScoreHandler(frame, scores, name, score);
            }
        });

        frame.setVisible(true);
        frame.setResizable(false);
    }

    /**
     * This function gets called if the level is finished or the player gets hit, as indicated by the boolean
     * parameter. If the player is hit then the player loses a life and all the current objects are added back
     * to the collection. If all lives are lost or level 3 is completed then the game becomes finished. If newlevel
     * parameter is true then the next level gets called. If the current level number is 2 then all the previous
     * objects get redrawn, with the aliens being a bit closer to the player than before. If the current level number
     * is 3 then only the player ship and the boss ship are drawn. This function also puts the thread to sleep for
     * a bit as an indication that something level changing occurred.
     */
    public boolean reset(boolean newlevel) {
        objects.clear();
        ships.clear();
        if (newlevel){
            alienNum = 0;
            alienNum2 = 0;
            alienPos = 0;
            lastShooter = null;
            level++;
            remainingAliens = 50;
        }
        else{
            message = "Life lost!";
            lives--;
            score -= 500;
        }
        if (lives == 0) {
            message = "Game Over!";
            return false;
        }

        if(level == 4){
            message = "You Won!";
            return false;
        }

        if(level == 3){
            message = "Boss approaches!";
        }

        int alienX = 75;
        int alienY = 200;

        if(newlevel){
            if(level == 3){
                bossShipHere = true;
                shooter1 = null;
            } else {
                extraLifeTriggered = false;
                destructionTriggered = false;
                precisionTriggered = false;
                Random rn = new Random();
                extraLifeAlien = rn.nextInt(50) + 1;
                while (true) {
                    destructionAlien = rn.nextInt(50) + 1;
                    if (destructionAlien != extraLifeAlien) {
                        break;
                    }
                }
                while (true) {
                    precisionAlien = rn.nextInt(50) + 1;
                    if (precisionAlien != destructionAlien && precisionAlien != extraLifeAlien) {
                        break;
                    }
                }
                for (int i = 0; i < aliens.length; i++) {
                    if(alienY == 200){
                        aliens[i] = new Alien(true, alienX, alienY, 1);
                        objects.add(aliens[i]);
                    } else if(alienY > 200 && alienY < 281){
                        aliens[i] = new Alien(true, alienX, alienY, 2);
                        objects.add(aliens[i]);
                    } else {
                        aliens[i] = new Alien(true, alienX, alienY, 3);
                        objects.add(aliens[i]);
                    }
                    if(alienX == 525){
                        alienX = 75;
                        alienY += 40;
                    } else {
                        alienX += 50;
                    }
                }
                alienShootGap = 50;
            }
            shooter1 = null;
        } else {
            for (int i = 0; i < aliens.length; i++) {
                if(!aliens[i].dead){
                    objects.add(aliens[i]);
                }
            }
        }
        ship = null;
        int baseX = 80;
        int baseY = 500;
        int x = 80;
        int baseBlockCount = 0;
        if(bossShipHere == false) {
            for (int j = 0; j < 4; j++) {
                if (newlevel) {
                    if (level == 2) {
                        for (int i = 0; i < baseBlocks.length; i++) {
                            if (baseY == 530 && baseX == x + 2 * 10) {
                                baseX += 20;
                            }
                            allBaseBlocks[baseBlockCount] = new BaseBlock(baseX, baseY);
                            objects.add(allBaseBlocks[baseBlockCount]);
                            baseBlockCount++;
                            baseX += 10;
                            if (baseX == x + 6 * 10) {
                                baseX = x;
                                baseY = baseY + 10;
                            }
                        }
                        baseY = 500;
                        x += 140;
                        baseX = x;
                    } else {
                        break;
                    }
                } else if (!newlevel) {
                    for (int i = 0; i < allBaseBlocks.length; i++) {
                        if (!allBaseBlocks[i].dead) {
                            if (baseY == 530 && baseX == x + 2 * 10) {
                                baseX += 20;
                            }
                            objects.add(allBaseBlocks[i]);
                            baseX += 10;
                            if (baseX == x + 6 * 10) {
                                baseX = x;
                                baseY = baseY + 10;
                            }
                        } else {
                            if (baseY == 530 && baseX == x + 2 * 10) {
                                baseX += 20;
                            }
                            baseX += 10;
                            if (baseX == x + 6 * 10) {
                                baseX = x;
                                baseY = baseY + 10;
                            }
                        }
                    }
                    baseY = 500;
                    x += 140;
                    baseX = x;
                }
            }
        }
        if(bossShipHere){
            if(newlevel){
                boss_ship = new Boss();
                objects.add(boss_ship);
                objects.add(boss_ship.leftBoss);
                objects.add(boss_ship.rightBoss);
                ships.add(boss_ship);
            } else {
                objects.add(boss_ship);
                if(!boss_ship.leftBoss.dead){
                    objects.add(boss_ship.leftBoss);
                }
                if(!boss_ship.rightBoss.dead){
                    objects.add(boss_ship.rightBoss);
                }
                ships.add(boss_ship);
            }
        }
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }
        startTime = System.currentTimeMillis();
        ship = new PlayerShip(ctrl);
        objects.add(ship);
        ships.add(ship);
        ship.destruction = false;
        precision = false;
        alienShoot = 0;
        return true;
    }

    /**
     * These getter methods are for the View class so it gets the neccessary data.
     */
    public int getScore(){
        return  score;
    }

    public int getLives(){
        return lives;
    }

    public int getLevel(){
        return level;
    }

    public String  getMessage(){
        return message;
    }

    Alien lastShooter;
    double alienPos = 0;
    int alienNum = 0;
    int alienNum2 = 0;

    /**
     * The update method is the main component of the game running.
     */
    public void update() {
        boolean mustUpdate = false;
        /**
         * Checks for the precision power up existence.
         */
        if(precision == true){
            if(precisionCount < precisionGap){
                precisionCount++;
            } else {
                precision = false;
            }
        }

        /**
         * Checks what message to show the player
         */
        if(!message.equals("")){
            if(ship.destruction == true){
                message = "Destruction Power-Up Active!";
                messageCount = messageGap;
            }else if(messageCount == messageGap){
                message = "Level " + level;
                messageCount = 0;
            } else {
                messageCount++;
            }
        }
        if (bossShipHere == false) {

            boolean changeDirLeft = false;
            boolean changeDirRight = false;
            int shoot1 = -1;
            int shoot2 = -1;
            int shoot3 = -1;
            /**
             * If it is time for the alien to shoot then this section sets up the required number of shooters.
             * The number of shooters depends on the amount of aliens remaining.
             */
            if (alienShoot == alienShootGap) {
                boolean done1 = false;
                boolean done2 = false;
                boolean done3 = false;
                Random rn = new Random();
                while (done1 == false) {
                    shoot1 = rn.nextInt(50) + 1;
                    for (int i = 0; i < aliens.length; i++) {
                        if (shoot1 == i) {
                            if (!aliens[i].dead) {
                                done1 = true;
                                shooter1 = aliens[i];
                                alienNum2 = i;
                                if(lastShooter == null){
                                    lastShooter = shooter1;
                                    alienPos = aliens[i].pos.x;
                                    alienNum = i;
                                }
                                break;
                            } else {
                                shoot1++;
                            }
                        }
                    }
                }
                if (remainingAliens > 5) {
                    while (done2 == false) {
                        shoot2 = rn.nextInt(50) + 1;
                        if (shoot2 != shoot1) {
                            for (int i = 0; i < aliens.length; i++) {
                                if (shoot2 == i) {
                                    if (!aliens[i].dead) {
                                        done2 = true;
                                        shooter2 = aliens[i];
                                        break;
                                    } else {
                                        shoot2++;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    shooter2 = null;
                }
                if (remainingAliens > 20) {
                    while (done3 == false) {
                        shoot3 = rn.nextInt(50) + 1;
                        if (shoot3 != shoot2 && shoot3 != shoot1) {
                            for (int i = 0; i < aliens.length; i++) {
                                if (shoot3 == i) {
                                    if (!aliens[i].dead) {
                                        done3 = true;
                                        shooter3 = aliens[i];
                                        break;
                                    } else {
                                        shoot3++;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    shooter3 = null;
                }
            }

            /**
             * This is a way to make sure that the aliens keep moving as otherwise ate points they would freeze.
             * It is also an attempt to keep at least 1 alien always shooting but does not work entirely.
             */
            if(alienShoot == alienShootGap) {
                if (alienPos != shooter1.pos.x) {
                    if (lastShooter.pos.x == aliens[alienNum].pos.x) {
                        mustUpdate = true;
                        lastShooter = shooter1;
                        alienNum = alienNum2;
                    } else {
                        mustUpdate = false;
                        alienNum = alienNum2;
                        lastShooter = shooter1;
                    }
                }
                if(shooter1 == null){
                    shooter1 = lastShooter;
                }
            }

            /**
             * This loop is for updating everything related to the alien moevement and their speed.
             * If the aliens reach an edge then puts them a row lower and changes their movement
             * direction.
             */
            for(int i = 0; i < aliens.length; i++){
                if(!aliens[i].dead){
                    if(mustUpdate == true){
                        aliens[i].letsMove = true;
                    }
                    if(aliens[i].pos.y > PLAYERSHIPY){
                        lives = 0;
                        reset(false);
                    }
                    if(remainingAliens < 25){
                        aliens[i].counterGap = 50;
                        alienShootGap = 40;
                    } else if(remainingAliens < 17){
                        aliens[i].counterGap = 25;
                        alienShootGap = 30;
                    } else if(remainingAliens < 10){
                        aliens[i].counterGap = 15;
                        alienShootGap = 25;
                    } else if(remainingAliens < 5){
                        alienShootGap = 15;
                        aliens[i].counterGap = 7;
                    } else if(remainingAliens < 2){
                        alienShootGap = 10;
                        aliens[i].counterGap = 1;
                    }
                    if(i == 0 || i == 10 || i == 20 || i == 30 || i == 40){
                        if(aliens[i].pos.x < aliens[i].ALIENWIDTH){
                            changeDirRight = true;
                            break;
                        } else if( aliens[i].pos.x + aliens[i].ALIENWIDTH + 20 > FRAME_WIDTH){
                            changeDirLeft = true;
                            break;
                        }
                    } else if(i == 9 || i == 19 || i == 29 || i == 39 || i == 49){
                        if( aliens[i].pos.x + aliens[i].ALIENWIDTH > FRAME_WIDTH){
                            changeDirLeft = true;
                            break;
                        } else if(aliens[i].pos.x - 20 < aliens[i].ALIENWIDTH){
                            changeDirRight = true;
                            break;
                        }
                    } else {
                        if(aliens[i].pos.x - 20 < aliens[i].ALIENWIDTH){
                            changeDirRight = true;
                            break;
                        } else if( aliens[i].pos.x + aliens[i].ALIENWIDTH + 10 > FRAME_WIDTH){
                            changeDirLeft = true;
                            break;
                        }
                    }
                    /**
                     * This else checks if a power up is triggered on dead aliens.
                     */
                } else {
                    if(extraLifeTriggered == false){
                        if(i == extraLifeAlien){
                            message = "Extra life acquired!";
                            lives++;
                            extraLifeTriggered = true;
                        }
                    }
                    if(destructionTriggered == false){
                        if(i == destructionAlien){
                            message = "Destruction Power-Up Active!";
                            destructionTriggered = true;
                            ship.destruction = true;
                        }
                    }
                    if(precisionTriggered == false){
                        if(i == precisionAlien){
                            message = "Precision Power-Up Active!";
                            precisionTriggered = true;
                            precision = true;
                        }
                    }
                }
            }
            /**
             * This loop sets the movement if required. Also if aliens were frozen then forces
             * them to move.
             */
            for(int i = 0; i < aliens.length; i++){
                if(changeDirLeft == true){
                    aliens[i].moveLeft = true;
                    aliens[i].changeY = 20;
                } else if(changeDirRight == true){
                    aliens[i].moveLeft = false;
                    aliens[i].changeY = 20;
                }
                if(mustUpdate == true){
                    aliens[i].counter = aliens[i].counterGap;
                }
            }
            /**
             * This else keeps track of the boss movement. If it reaches either edge then change direction.
             */
        }else {
            if(boss_ship.pos.x < 150){
                boss_ship.changeDirection();
                if(boss_ship.moveLeft == true){
                    boss_ship.changeDirection();
                }
            } else if(boss_ship.pos.x+ 210 > 580){
                boss_ship.changeDirection();
                if(boss_ship.moveLeft == false){
                    boss_ship.changeDirection();
                }
            }
        }

        /**
         * Checks all objects to see if any collided. Afterwards puts all the alive objects into a new
         * ArrayList. If the ship is hit then sets up the reset function, if another object is hit then
         * calls the updateScore function.
         */
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                objects.get(i).collisionHandling(objects.get(j));
            }
        }
        finished = true;
        List<GameObject> alive = new ArrayList<>();
        for (GameObject o : objects) {
            if (!o.dead) {
                o.update();
                alive.add(o);
                if (o == ship) finished = false;
            }
            else if (o== ship){
                resetting = true;
                break;
            }
            else updateScore(o);
        }
        /**
         * Checks if the ships have shot a bullet or not, if they have then adds to the alive arraylist.
         */
        if(bossShipHere == false){
            if (ship.bullet != null) {
                if(ship.destruction == true){
                    alive.add(ship.rightBullet);
                    alive.add(ship.leftBullet);
                }
                alive.add(ship.bullet);
                ship.bullet = null;
            }
        } else {
            for (Ship shipy:ships)
                if (shipy.bullet != null) {
                    if(shipy.getClass() == boss_ship.getClass()){
                        if(boss_ship.rightBoss.dead == false){
                            alive.add(boss_ship.rightBullet);
                        }
                        if(boss_ship.leftBoss.dead == false){
                            alive.add(boss_ship.leftBullet);
                        }
                    }
                    alive.add(shipy.bullet);
                    shipy.bullet = null;
                }
        }
        /**
         * If an alien shoots then the bullet is added to alive objects.
         */
        if(bossShipHere == false){
            if(alienShoot == alienShootGap){
                if(shooter1!=null){
                    SoundManager.play(SoundManager.fire2);
                    shooter1.shoot();
                    alive.add(shooter1.bullet);
                    shooter1.bullet = null;
                    if(shooter2!=null){
                        shooter2.shoot();
                        alive.add(shooter2.bullet);
                        shooter2.bullet = null;
                        if(shooter3!=null){
                            shooter3.shoot();
                            alive.add(shooter3.bullet);
                            shooter3.bullet = null;
                        }
                    }
                }
                alienShoot = 0;
            }
            alienShoot++;
        }

        /**
         * Checks all the different conditions for finishing the game and depending on that
         * either calls reset or just redraws with all the alive objects.
         */
        synchronized (Game.class) {
            if(bossShipHere == false){
                if (remainingAliens==0 || (remainingAliens == 1 && !aliens[0].dead)){
                    score += 1000;
                    message = "Level " + level + " finished!";
                    reset(true);
                }
                else if (resetting) {
                    finished = !reset(false);
                    resetting = false;
                }
                else {
                    objects.clear();
                    for (GameObject o : alive) objects.add(o);

                }
            } else {
                if (boss_ship.dead == true){
                    score += 3000;
                    message = "Level " + level + " finished!";
                    reset(true);
                }
                else if (resetting) {
                    finished = !reset(false);
                    resetting = false;
                }
                else {
                    objects.clear();
                    for (GameObject o : alive) objects.add(o);

                }
            }
        }
    }

    /**
     * Checks the object in the parameter and depending on that adds the given score to the player score.
     */
    public void updateScore(GameObject o) {
        if (o.getClass() == Alien.class) {
            if(((Alien) o).ver == 1){
                if(precision == true){
                    score += 200;
                } else {
                    score += 100;
                }
            } else if( ((Alien) o).ver == 2){
                if(precision == true){
                    score += 120;
                } else {
                    score += 60;
                }
            } else {
                if(precision == true){
                    score+= 60;
                } else {
                    score += 30;
                }
            }
            remainingAliens -= 1;
        }
        if(o.getClass() == BossRight.class){
            score+=500;
        }
        if(o.getClass() == BossLeft.class){
            score+=500;
        }
        if(o.getClass() == Boss.class){
            score += 1000;
        }
    }
}
