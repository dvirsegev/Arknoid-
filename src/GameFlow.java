import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * public class GameFlow.
 */
public class GameFlow {
    private AnimationRunner animationRunner;
    private biuoop.KeyboardSensor keyboardSensor;
    private Counter score;
    private Counter lives;
    private HighScoresTable highScoresTable;
    private biuoop.DialogManager dialog;

    /**
     * @param ar       type of AnimationRunner.
     * @param ks       the keyboardSensor.
     * @param counter1 the score.
     * @param lives1   the live of the user.
     * @param table    the table
     * @param dialog   the message.
     */
    public GameFlow(AnimationRunner ar, biuoop.KeyboardSensor ks, Counter counter1, Counter lives1,
                    HighScoresTable table, biuoop.DialogManager dialog) {
        this.keyboardSensor = ks;
        this.animationRunner = ar;
        this.score = counter1;
        this.lives = lives1;
        this.highScoresTable = table;
        this.dialog = dialog;
    }


    /**
     * @param levels list of all the levels.
     */
    public void runLevels(List<LevelInformation> levels) {

        int count = 0;
        // the high score table.
        this.highScoresTable = new HighScoresTable(5);
        // create the path to the file.
        File file = new File("table.txt");
        if (!file.exists()) {
            try {
                highScoresTable.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            highScoresTable = HighScoresTable.loadFromFile(file);
        }
        for (LevelInformation levelInfo : levels) {
            GameLevel level = new GameLevel(levelInfo, this.animationRunner, this.keyboardSensor,
                    this.score, this.lives);
            // initialize the level.
            level.initialize();
            while (level.getLives() > 0 && level.getCounterBlock() > 0) {
                // if there is lives and block, play the level.
                level.playOneTurn();
                count++;
            }
            // if there is no life, check if there is new record, print the table and act the animationRunner EndGame.
            if (level.getLives() == 0) {
                if (highScoresTable.bigEnough(this.score.getValue())) {
                    String name = dialog.showQuestionDialog("Name", "What is your name?", "");
                    ScoreInfo scoreInfo = new ScoreInfo(name, this.score.getValue());
                    highScoresTable.add(scoreInfo);
                }
                // run the animation of the end game.
                animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor, "space",
                        new EndGame(level.getScore())));
                // run the animation of the table
                animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor, "space",
                        new HighScoresAnimation(this.highScoresTable)));
            } else if (count == levels.size()) {
                // run the animation of victory.
                animationRunner.run(new KeyPressStoppableAnimation(this.keyboardSensor, "space",
                        new Victory(level.getScore())));
            }
            try {
                // save the result.
                this.highScoresTable.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // restart the level.
            score.decrease(score.getValue());
            lives.decrease(lives.getValue());
            lives.increase(7);
            return;

        }
    }
}
