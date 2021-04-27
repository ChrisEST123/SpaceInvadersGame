package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * This class is for the frame that shows the top scores in the end. The constructor takes in
 * the last frame, closes it, and also assigns the scores map, last players name and score.
 * In the end it launches the actionPerformed method-
 */
public class ScoreHandler{
    Map<String, Integer> scores;
    Map<String, Integer> newScores = new LinkedHashMap<>();
    String name;
    int score;
    boolean initialised = false;
   ScoreHandler(JFrame aframe, Map<String, Integer> ascores, String atextField, int ascore){
       aframe.dispatchEvent(new WindowEvent(aframe, WindowEvent.WINDOW_CLOSING));
        scores = ascores;
        name = atextField;
        score = ascore;
        actionPerformed();
    }

    /**
     * This function creates a new frame, checks if the player inserted a name and then if they did it put
     * any spacing in their name it eliminates the spaces. If there was no name then the name is Unknown.
     * After that it sets up the JPanel and its title. Furthermore, it checks if the scores map has more than
     * 1 values, and if it does then it loops through the maps to put the data into a list, sort it, and then
     * put the data into a new map, where the data is sorted. After it loops through the map to put all the
     * scores in the JPanel. In the end it writes all the scores into the score .txt file so they can be
     * retrieved afterwards.
     */
    public void actionPerformed(){
       JFrame frame = new JFrame();
        int counter = 1;
        if(name == null || name.equals("")){
            name = "Unknown";
        } else {
            name = name.replaceAll("\\s+","");
        }
        if(score == 0){
            score = 1;
        }
        scores.put(name, score);

        JPanel newPanel = new JPanel();
        newPanel.setLayout(new GridLayout(15, 1));
        JLabel title = new JLabel("Top Scores");
        title.setFont(new Font("Verdana",1,20));
        newPanel.add(title);
        JLabel scoreLine;

        ArrayList<Integer> scoreList = new ArrayList<Integer>();

        if(scores.size() > 1){
            Iterator<Map.Entry<String, Integer>> iter = scores.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Integer> pair = iter.next();
                scoreList.add(pair.getValue());
            }

            Collections.sort(scoreList);
            Collections.reverse(scoreList);

            for(Integer counting: scoreList){
                Iterator<Map.Entry<String, Integer>> iters = scores.entrySet().iterator();
                while (iters.hasNext()) {
                    Map.Entry<String, Integer> pair = iters.next();
                    if(pair.getValue().equals(counting)){
                        newScores.put(pair.getKey(), counting);
                        break;
                    }
                }
            }
            initialised = true;
        } else {
            newScores = scores;
        }

        Iterator<Map.Entry<String, Integer>> it = newScores.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> pair = it.next();
            scoreLine = new JLabel(counter + ". " + pair.getKey() + " " + pair.getValue());
            newPanel.add(scoreLine, BorderLayout.CENTER);
            counter++;
            if(counter == 11){
                break;
            }
        }

        counter = 1;
        frame.add(newPanel, BorderLayout.CENTER);
        frame.setContentPane(newPanel);

        frame.setSize(400, 400);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        File file = new File("ScoreBoard.txt");
        BufferedWriter bf = null;

        try {
            bf = new BufferedWriter(new FileWriter(file));

            for (Map.Entry<String, Integer> entry : newScores.entrySet()) {

                bf.write(counter + ". " + entry.getKey() + " "
                        + entry.getValue());

                bf.newLine();
                counter++;
                if(counter == 11){
                    break;
                }
            }

            bf.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {

            try {
                bf.close();
            }
            catch (Exception e) {

            }
        }
    }
}
