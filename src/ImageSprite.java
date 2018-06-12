import java.awt.Image;

import biuoop.DrawSurface;

/**
 * class ImageSprite implements Sprite.
 */
public class ImageSprite implements Sprite {

    private Image image;

    /**
     * @param image put image.
     */
    public ImageSprite(Image image) {
        this.image = image;
    }

    @Override
    public void drawOn(DrawSurface d) {
        d.drawImage(0, 0, image); // draw the image at location 10, 20.
    }

    @Override
    public void timePassed(double dt) {
    }

}
