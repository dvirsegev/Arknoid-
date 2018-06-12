/**
 * public interface BlockCreator.
 */
public interface BlockCreator {
    /**
     * @param xpos startX of the block.
     * @param ypos startY of the block.
     * @return new Block.
     */
    Block create(int xpos, int ypos);

    int getWidth();

    int getHeight();

}
