import biuoop.DrawSurface;
import biuoop.KeyboardSensor;

import java.awt.Color;

/**
 * public class Paddle that implement Sprite and Collidable interfaceses.
 */
public class Paddle implements Sprite, Collidable {
    private biuoop.KeyboardSensor keyboard;
    private Block paddle;
    private Color color;
    private int speed;
    private int widthpad;
    private static final int LOWLIMIT = 15;
    private static final int HIGHTLIMIT = 785;
    private int heightpad = 20;

    /**
     * @param sensor KeyboardSensor. Constractor of the Paddel.
     */
    public Paddle(KeyboardSensor sensor) {
        final int randColor = (int) (Math.random() * 0x1000000);
        this.keyboard = sensor;
        Point pointStartScreen = new Point(300, 570);
        this.widthpad = 200;
        this.color = new Color(randColor);
        this.paddle = new Block(new Rectangle(pointStartScreen, widthpad, heightpad), this.color);
    }

    /**
     * @param widthpad1 set Width to the paddle.
     */
    public void setWidthpad(int widthpad1) {
        this.widthpad = widthpad1;
        this.paddle = new Block(new Rectangle(new Point(300, 570), widthpad, heightpad), this.color);
    }


    /**
     * @return speed;
     */
    public int getSpeed() {
        return speed;
    }

    public void setStart() {
        this.paddle = new Block(new Rectangle(new Point(100, 570), widthpad, heightpad), this.color);
    }
    /**
     * @return width of the paddle.
     */
    public int getWidthpad() {
        return widthpad;
    }

    /**
     * @param speed1 the speed.
     */
    public void setSpeed(int speed1) {
        this.speed = speed1;
    }

    /**
     * behavior pattern of move the paddle left.
     */
    public void moveLeft(double dt) {
        double dtspeed = speed * dt;
        Rectangle recPaddle = this.paddle.getCollisionRectangle();
        /* check if the left top point is reached to the limit of the screen. if not, move each
        corners of the rectangle steps to the left.
        */
        if (recPaddle.getUpperLeft().getX() >= LOWLIMIT) {
            recPaddle.getUpperLeft().setnewPoint(recPaddle.getUpperLeft().getX() - dtspeed,
                    recPaddle.getUpperLeft().getY());
            recPaddle.getDownLeft().setnewPoint(recPaddle.getDownLeft().getX() - dtspeed,
                    recPaddle.getDownLeft().getY());
            recPaddle.getDownRight().setnewPoint(recPaddle.getDownRight().getX() - dtspeed,
                    recPaddle.getDownRight().getY());
            recPaddle.getUpperRight().setnewPoint(recPaddle.getUpperRight().getX() - dtspeed,
                    recPaddle.getUpperRight().getY());
        }
    }

    /**
     * * behavior pattern of move the paddle right.
     */
    public void moveRight(double dt) {
        Rectangle recPaddle = this.paddle.getCollisionRectangle();
        /* check if the right top point is reached to the limit of the screen. if not, move each
        corners of the rectangle steps to the right.
        */
        double dtspeed = speed * dt;
        if (recPaddle.getUpperRight().getX() <= HIGHTLIMIT) {
            recPaddle.getUpperLeft().setnewPoint(recPaddle.getUpperLeft().getX() + dtspeed,
                    recPaddle.getUpperLeft().getY());
            recPaddle.getDownLeft().setnewPoint(recPaddle.getDownLeft().getX() + dtspeed,
                    recPaddle.getDownLeft().getY());
            recPaddle.getDownRight().setnewPoint(recPaddle.getDownRight().getX() + dtspeed,
                    recPaddle.getDownRight().getY());
            recPaddle.getUpperRight().setnewPoint(recPaddle.getUpperRight().getX() + dtspeed,
                    recPaddle.getUpperRight().getY());
        }
    }

    /**
     * Check What Keyboard is press.
     */
    public void timePassed(double dt) {
        if (this.keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
            moveLeft(dt);
        }
        if (this.keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
            moveRight(dt);
        }
    }

    /**
     * @param d DrawSurface.
     * @apiNote draw the paddle.
     */
    public void drawOn(DrawSurface d) {
        // set Color for the edge of the rectangle.
        d.setColor(Color.BLACK);
        // draw the edge of the rectangle in black.
        d.drawRectangle((int) this.getCollisionRectangle().getUpperLeft().getX(),
                (int) this.getCollisionRectangle().getUpperLeft().getY(),
                (int) this.getCollisionRectangle().getWidth(), (int) this.getCollisionRectangle().getHeight());
        // set the Color for the paddle himself
        d.setColor(this.color);
        // fill the paddle with this color.
        d.fillRectangle((int) this.getCollisionRectangle().getUpperLeft().getX(),
                (int) this.getCollisionRectangle().getUpperLeft().getY(),
                (int) this.getCollisionRectangle().getWidth(),
                (int) this.getCollisionRectangle().getHeight());
    }

    /**
     * @return the paddle.
     */
    public Rectangle getCollisionRectangle() {
        return this.paddle.getCollisionRectangle();
    }

    /**
     * @param collisionPoint  the collision point.
     * @param currentVelocity the speed of the ball.
     * @param ball            the ball.
     * @return new velocity if there is a hit between the paddle and the ball. else return the regular veloctiy.
     */
    public Velocity hit(Ball ball, Point collisionPoint, Velocity currentVelocity) {
        // divided the width into 5 equals piceses.
        double width = this.getCollisionRectangle().getWidth() / 5;
        // height of the upper rib of the paddle.
        double heightRib = this.getCollisionRectangle().getUpperRib().end().getY();

        Line[] region = new Line[5];
        // setting the 5 equally-spaced regions.
        region[0] = new Line(this.getCollisionRectangle().getUpperLeft(),
                new Point(this.getCollisionRectangle().getUpperLeft().getX() + width, heightRib));
        region[1] = new Line(region[0].end(), new Point(region[0].end().getX() + width, heightRib));
        region[2] = new Line(region[1].end(), new Point(region[1].end().getX() + width, heightRib));
        region[3] = new Line(region[2].end(), new Point(region[2].end().getX() + width, heightRib));
        region[4] = new Line(region[3].end(), new Point(region[3].end().getX() + width, heightRib));
        // check if there is a coliision on the one of the region in this function.
        currentVelocity = checkRegionHit(region, collisionPoint, currentVelocity);
        return currentVelocity;
    }

    /**
     * @param region          5 regions on the upeerRib
     * @param collisionPoint  the collision Point
     * @param currentVelocity the speed of the ball
     * @return new speed if there is a collision, else return the regular speed. the angles is by the professor.
     */
    public Velocity checkRegionHit(Line[] region, Point collisionPoint, Velocity currentVelocity) {
        if (this.paddle.checkPoint(collisionPoint, region[0])) {
            currentVelocity = currentVelocity.fromAngleAndSpeed(240, currentVelocity.speedHit());
        } else if (this.paddle.checkPoint(collisionPoint, region[1])) {
            currentVelocity = currentVelocity.fromAngleAndSpeed(210, currentVelocity.speedHit());
        } else if (this.paddle.checkPoint(collisionPoint, region[2])) {
            currentVelocity = currentVelocity.fromAngleAndSpeed(180, currentVelocity.speedHit());
        } else if (this.paddle.checkPoint(collisionPoint, region[3])) {
            currentVelocity = currentVelocity.fromAngleAndSpeed(150, currentVelocity.speedHit());
        } else if (this.paddle.checkPoint(collisionPoint, region[4])) {
            currentVelocity = currentVelocity.fromAngleAndSpeed(130, currentVelocity.speedHit());
        } else if (this.paddle.checkPoint(collisionPoint, this.getCollisionRectangle().getRightRib())
                || (this.paddle.checkPoint(collisionPoint, this.getCollisionRectangle().getLeftRib()))) {
            currentVelocity.setDx(currentVelocity.getDx() * -1);
        }
        return currentVelocity;

    }

    /**
     * @param g GameLevel type. Add the Paddle to the GameLevel.
     */
    public void addToGame(GameLevel g) {
        g.addCollidable(this);
        g.addSprite(this);
    }
}
