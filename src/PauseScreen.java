import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * public class PauseScreen that implements Animation.
 */
public class PauseScreen implements Animation {

    /**
     * @param d doOneFrame(DrawSurface) is in charge of the logic.
     * @param dt the frame.
     */
    public void doOneFrame(biuoop.DrawSurface d, double dt) {
        Image image = null;
        try {
            // image stop.
            image = ImageIO.read(new File("background_images/stop.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        d.drawImage(0, -100, image);
        d.setColor(Color.RED);
        d.drawText(150, (int) (d.getHeight() / 9), "paused -- press space to continue", 32);
    }

    /**
     * @return true or false.
     */
    public boolean shouldStop() {
        return false;
    }
}