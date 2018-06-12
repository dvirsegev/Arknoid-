
/**
 * @author dvir segev
 */

import biuoop.GUI;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Class ASS3Game.
 */

public class Ass6Game {

    /**
     * @param args the input the user gave us.
     */
    public static void main(String[] args) throws IOException {
        Ass6Game ass6Game = new Ass6Game();
        List<LevelInformation> levelInformations = new ArrayList<>();
        /*
        try {
            levelInformations = ass5Game.readLevels();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        HighScoresTable highScoresTable = new HighScoresTable(5);
        // read the file table.
        File file = new File("table.txt");
        // if exist, load it.
        if (file.exists()) {
            highScoresTable = HighScoresTable.loadFromFile(file);
        }
        biuoop.GUI gui = new GUI("Arknoid", 800, 600);
        biuoop.KeyboardSensor keyboard = gui.getKeyboardSensor();
        Counter counter = new Counter();
        Counter lives = new Counter();
        ///set live
        lives.increase(7);
        // set the dialogManger.
        biuoop.DialogManager dialog = gui.getDialogManager();
        // set the gameflow.
        GameFlow gameFlow = new GameFlow(new AnimationRunner(gui, 3), keyboard, counter,
                lives, highScoresTable, dialog);
        // set the AnimationRunner.
        AnimationRunner runner = new AnimationRunner(gui, 60);
        MenuAnimation<Task<Void>> subMenu = new MenuAnimation<Task<Void>>("Choose Level", keyboard, runner);
        ass6Game.readFile(subMenu, gameFlow);
        //subMenu.addSelection("e", "easy", new StartGame(gameFlow, levelInformations));
        //subMenu.addSelection("h", "hard", new StartGame(gameFlow, levelInformations));
        // set the menu.
        MenuAnimation<Task<Void>> menu = new MenuAnimation<>("Menu Title", keyboard, runner);
        menu.addSelection("h", "HighScores", new ShowHiScoresTask(runner,
                new HighScoresAnimation(highScoresTable), keyboard));
        menu.addSubMenu("s", "Game", subMenu);
        menu.addSelection("q", " Quit", new Quit());
        while (true) {
            // run the menu
            runner.run(menu);
            // get which task we need to do after we get the status.
            Task<Void> task = menu.getStatus();
            // run it.
            task.run();
            // restart the menu.
            menu.reset();
        }
    }

    /**
     * @return the levels from the file.
     * @throws IOException if can't read the file.
     */
    private List<LevelInformation> readLevels(String path) throws IOException {
        LevelSpecificationReader l = new LevelSpecificationReader();
        BufferedReader is = null;
        List<LevelInformation> list = null;
        try {
            is = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            list = l.fromReader(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return list;
    }

    /**
     * @param subMenu  the menu of the game.
     * @param gameFlow the run of the game.
     * @throws IOException if cant open the file.
     */
    private void readFile(MenuAnimation<Task<Void>> subMenu, GameFlow gameFlow) throws IOException {
        BufferedReader bufferedReader = null;
        try {
            // read the file
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("level_sets.txt")));
            char[] cbuf = new char[120];
            bufferedReader.read(cbuf);
            String s = new String(cbuf);
            String key = null, value = null;
            List<LevelInformation> levelInformations = null;
            // split into lines.
            String lines[] = s.split("\n");
            for (int i = 0; i < lines.length; i++) {
                if (lines[i].contains(":")) {
                    String[] keyAndValue = lines[i].split(":");
                    key = keyAndValue[0];
                    value = keyAndValue[1];
                } else {
                    if (i + 1 == lines.length) {
                        String line = lines[i].trim();
                        levelInformations = readLevels(line);
                    } else {
                        levelInformations = readLevels(lines[i]);

                    }
                    subMenu.addSelection(key, value, new StartGame(gameFlow, levelInformations));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
    }
}