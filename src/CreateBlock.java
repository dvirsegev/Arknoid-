import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * public class CreateBlock implements
 */
public class CreateBlock implements BlockCreator {
    // Characteristics of block.
    private int width;
    private int height;
    private String string;
    private Color color;
    private int live;
    private Color storke;
    private Image image;
    private Map<Integer, Color> colors = new HashMap<Integer, Color>();
    private Map<Integer, Image> imageMap = new HashMap<>();

    @Override
    public Block create(int xpos, int ypos) {
        Block block = new Block(new Rectangle(new Point(xpos, ypos), this.width, this.height), this.color, storke);
        block.setHit(live);
        block.setImage(image);
        block.setColors(colors);
        block.setImages(imageMap);
        return block;
    }

    /**
     * @param s set name("symbol").
     */
    public void setSymbol(String s) {
        this.string = s;
    }

    /**
     * @param integer set width.
     */
    public void setWidth(Integer integer) {
        this.width = integer;
    }

    /**
     * @param height set height.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * @param i set live.
     */
    public void setLive(int i) {
        this.live = i;
    }

    /**
     * @return the name.
     */
    public String getString() {
        return string;
    }

    /**
     * @param color1 set color.
     */
    public void setColor(Color color1) {
        this.color = color1;
    }

    /**
     * @param storke set Color Border.
     */
    public void setStorke(Color storke) {
        this.storke = storke;
    }

    /**
     * @param image setImage.
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * @return width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return height.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * @param fillNumber the life of the block.
     * @param c          the color
     */
    public void addColor(int fillNumber, Color c) {
        colors.put(fillNumber, c);
    }

    public void fillStorke(String line) {
        ColorsParser colorsParser = new ColorsParser();
        if (line.substring(0, 9).equals("color(RGB")) {
            String color = line.substring(11, 18);
            setStorke(colorsParser.colorRgb(color));
        } else if (line.substring(0, 5).equals("color")) {
            setStorke(colorsParser.colorFromString(line));
        }
    }

    /**
     * @param line the line in the text.
     */
    public void fillColorOrImage(String line) {
        ColorsParser colorsParser = new ColorsParser();
        if (line.substring(0, 9).equals("color(RGB")) {
            String color = line.substring(11, line.length() - 2);
            setColor(colorsParser.colorRgb(color));
        } else if (line.substring(0, 5).equals("color")) {
            setColor(colorsParser.colorFromString(line));
        } else if (line.substring(0, 5).equals("image")) {
            Image img;
            String nameFile = line.substring(6, line.length() - 1);
            try {
                img = ImageIO.read(new File(nameFile));
                setImage(img);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * @param fillNumber receive number of life.
     * @param line       receive line.
     */
    public void fillNColorOrImage(int fillNumber, String line) {
        ColorsParser colorsParser = new ColorsParser();
        Color c = null;
        if (line.substring(0, 9).equals("color(RGB")) {
            String color = line.substring(11, line.length() - 2);
            c = colorsParser.colorRgb(color);
        } else if (line.substring(0, 5).equals("color")) {
            c = colorsParser.colorFromString(line);
        } else if (line.substring(0, 5).equals("image")) {
            Image img;
            String nameFile = line.substring(6, line.length() - 1);
            try {
                img = ImageIO.read(new File(nameFile));
                addImage(fillNumber, img);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (c != null)
            addColor(fillNumber, c);
    }

    private void addImage(int fillNumber, Image img) {
        this.imageMap.put(fillNumber, img);
    }

    /**
     * @param map set default values. add the default values.
     */
    public void setDefault(Map<String, String> map) {
        if (map.containsKey("height")) {
            setHeight(Integer.parseInt(map.get("height")));
        }
        if (map.containsKey("width")) {
            setWidth(Integer.parseInt(map.get("width")));
        }
        if (map.containsKey("stroke")) {
            fillStorke(map.get("stroke"));
        }
        if (map.containsKey("hit_points")) {
            setLive(Integer.parseInt(map.get("hit_points")));
        }
        if (map.containsKey("color")) {
            fillColorOrImage(map.get("color"));
        }
        if (map.containsKey("color")) {
            fillColorOrImage(map.get("color"));
        }
    }
}
