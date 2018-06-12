import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * public class EndGmae implements Animation.
 */
public class Victory implements Animation {

    private Counter score;

    /**
     * @param score the score.
     */
    public Victory(Counter score) {
        this.score = score;
    }

    /**
     * @param d doOneFrame(DrawSurface) is in charge of the logic.
     */
    public void doOneFrame(biuoop.DrawSurface d, double dt) {
        Image image = null;
        try {
            // image winning.
            image = ImageIO.read(new File("background_images/winning.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        d.drawImage(0, -100, image);
        d.setColor(Color.BLACK);
        d.drawText(100, 400, " You Winner!! Your score is your score:"
                + score.getValue(), 32);
        d.drawText(100, 500, " press space to exit program", 32);
    }

    /**
     * @return true or false.
     */
    public boolean shouldStop() {
        return false;
    }
}

