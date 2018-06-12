import biuoop.DrawSurface;

import java.awt.Color;
import java.util.List;

/**
 * Class ASS3Game.
 */
public class GameLevel implements Animation {
    private AnimationRunner runner;
    private boolean running;
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private biuoop.GUI gui;
    private Paddle paddle;
    private Counter counterBlocks;
    // new counter for how much balls there is
    private Counter counterBalls = new Counter();
    private Counter score;
    private Counter lives;
    private biuoop.KeyboardSensor keyboard;
    private int maxWidth;
    private int maxHeight;

    private static final int RAND_COLOR = (int) (Math.random() * 0x1000000);
    private LevelInformation levelInformation;

    /**
     * initialize the game.
     *
     * @param levelInformation the level.
     * @param animationRunner  type of AnimationRunner.
     * @param keyboard         the keyboardSensor.
     * @param score1           the score of the games.
     * @param lives1           the lives of the user.
     */
    public GameLevel(LevelInformation levelInformation, AnimationRunner animationRunner,
                     biuoop.KeyboardSensor keyboard, Counter score1, Counter lives1) {
        // new SpriteCollection.
        this.sprites = new SpriteCollection();
        // the size and the name of the screen.
        this.gui = animationRunner.getGui();
        // new environment collection.
        this.environment = new GameEnvironment();
        // new counter of remove block
        this.counterBlocks = new Counter();
        // new counter for scoring.
        this.score = score1;
        // new counter for lives.
        this.lives = lives1;
        // new runner for moveOneStep.
        this.runner = new AnimationRunner(gui, 60);
        // getKeyboardSenor.
        this.keyboard = keyboard;
        this.levelInformation = levelInformation;
        this.maxWidth = 800;
        this.maxHeight = 600;
    }

    /**
     * @param c type of Collidable.
     * @apiNote adding the c to the list of Collidables.
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }

    /**
     * @param s object type of sprite.
     * @apiNote adding the s to the list of sprites.
     */
    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
    }

    /**
     * @param c remove from collidable list.
     */
    public void removeCollidable(Collidable c) {
        this.environment.removeCollidable(c);

    }

    /**
     * @param s remove from sprite list.
     */
    public void removeSprite(Sprite s) {
        this.sprites.removeSprite(s);
    }


    /**
     * intialize the game: put the balls and the blocks to the game.
     */
    public void initialize() {
        this.sprites.addSprite(levelInformation.getBackground());
        // adding ballRemover for removing balls.
        BallRemover ballRemover = new BallRemover(this, counterBalls);
        // setting the frame of the game.
        Rectangle info = new Rectangle(new Point(0, 0), maxWidth, 20);
        Rectangle left = new Rectangle(new Point(0, 30), 10, maxHeight);
        Rectangle up = new Rectangle(new Point(0, 21), maxWidth, 20);
        Rectangle down = new Rectangle(new Point(0, maxHeight - 10), maxWidth, 10);
        Rectangle right = new Rectangle(new Point(maxWidth - 10, 30), 10, maxHeight);
        Block information = new Block(info, Color.BLACK);
        information.addToGame(this);
        // adding  randmoize color for the frame.
        Block leftEdge = new Block(left, new Color(RAND_COLOR));
        leftEdge.addToGame(this);
        Block upEdge = new Block(up, new Color(RAND_COLOR));
        upEdge.addToGame(this);
        Block downEdge = new Block(down, new Color(RAND_COLOR));
        downEdge.addToGame(this);
        // add the down block to be "death region". adding it listener and actiing according to ballRemover.
        downEdge.addHitListener(ballRemover);
        //lives.increase(7);
        LivesIndicator livesIndicator = new LivesIndicator(information, lives);
        addSprite(livesIndicator);
        Block rightEdge = new Block(right, new Color(RAND_COLOR));
        rightEdge.addToGame(this);
        // Create scoreIndicator for the score and add it to the spriteCo   llection.
        ScoreIndicator scoreIndicator = new ScoreIndicator(information, score, levelInformation);
        // add it to the sprite Collection.
        this.addSprite(scoreIndicator);
        // call function addBlocks.
        List<Block> list = this.levelInformation.blocks();
        BlockRemover blockRemover = new BlockRemover(this, counterBlocks);
        // create trackingListener method, for the scoing each time we hit a block.
        ScoreTrackingListener trackingListener = new ScoreTrackingListener(score);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).addToGame(this);
            list.get(i).addHitListener(blockRemover);
            list.get(i).addHitListener(trackingListener);
            counterBlocks.increase(1);
        }
    }

    /**
     * @return this running.
     */
    public boolean shouldStop() {
        return !this.running;
    }

    /**
     * adding the balls and the paddle to the game.
     */
    public void createBallsOnTopOfPaddle() {
        this.paddle = new Paddle(gui.getKeyboardSensor());
        this.paddle.setWidthpad(this.levelInformation.paddleWidth());
        int index = (int) this.paddle.getCollisionRectangle().getUpperLeft().getX();
        if (paddle.getCollisionRectangle().getUpperLeft().getX() + this.paddle.getWidthpad() > 800) {
            this.paddle.setStart();
        }
        for (int i = 0; i < this.levelInformation.numberOfBalls(); i++) {
            if (this.levelInformation.numberOfBalls() == 1) {
                Ball ball = new Ball(index + this.paddle.getWidthpad() / 2 ,
                        560, 6, Color.WHITE, this.environment);
                ball.setVelocity(levelInformation.initialBallVelocities().get(i));
                ball.addToGame(this);
                counterBalls.increase(1);
            } else {
                Ball ball = new Ball(index + 50 * i, 560, 6, Color.WHITE, this.environment);
                ball.setVelocity(levelInformation.initialBallVelocities().get(i));
                ball.addToGame(this);
                counterBalls.increase(1);
            }

        }
        // adding the paddle to the game.
        this.paddle.setSpeed(this.levelInformation.paddleSpeed());
        this.paddle.addToGame(this);
    }

    /**
     * @param d doOneFrame(DrawSurface) is in charge of the logic.
     * @param dt the frame.
     */
    public void doOneFrame(DrawSurface d, double dt) {

        // drawAllOn the sprites.
        this.sprites.drawAllOn(d);
        this.sprites.notifyAllTimePassed(dt);
        // if the user pressed p,  acting PauseScreen.
        if (this.keyboard.isPressed("p")) {
            this.runner.run(new KeyPressStoppableAnimation(keyboard, "space", new PauseScreen()));
        }
        if (this.counterBlocks.getValue() == 0) {
            score.increase(100);
            this.removeSprite(paddle);
            this.removeCollidable(paddle);
            running = false;
        }
        if (this.counterBalls.getValue() == 0) {
            this.lives.decrease(1);
            this.removeSprite(paddle);
            this.removeCollidable(paddle);
            running = false;
        }
    }

    /**
     * Run the game -start the animation loop, drawing the blocks and moving the balls.
     */
    public void playOneTurn() {
        this.createBallsOnTopOfPaddle();
        this.runner.run(new CountdownAnimation(2, 3, sprites));
        this.running = true;
        this.runner.run(this);
    }

    /**
     * @return the lives in the game.
     */
    public int getLives() {
        return this.lives.getValue();
    }

    /**
     * @return how much blocks there is.
     */
    public int getCounterBlock() {
        return this.counterBlocks.getValue();
    }

    /**
     * @return the score.
     */
    public Counter getScore() {
        return this.score;
    }
}