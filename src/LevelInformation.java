import java.util.List;

/**
 * public interface LevelInformation.
 */
public interface LevelInformation {
    /**
     * @return the number of the balls.
     */
    int numberOfBalls();

    /**
     * @return the list that have  initial velocity of each ball
     */
    List<Velocity> initialBallVelocities();

    /**
     * @return paddleSpeed.
     */
    int paddleSpeed();

    /**
     * @return paddleWidth
     */
    int paddleWidth();

    /**
     * @return the level name will be displayed at the top of the screen.
     */
    String levelName();

    /**
     * @return a sprite with the background of the level
     */
    Sprite getBackground();
    // The Blocks that make up this level, each block contains
    // its size, color and location.

    void setBackground(Sprite sprite);
    /**
     * @return The Blocks that make up this level, each block contains its size, color and location.
     */
    List<Block> blocks();

}