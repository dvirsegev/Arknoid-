
/**
 * class BlockRemover that implement HitListener interface.
 */
public class BlockRemover implements HitListener {
    // members.
    private GameLevel gameLevel;
    private Counter remainingBlocks;

    /**
     * Constractor.
     *
     * @param gameLevel     the GameLevel.
     * @param removedBlocks Coutner type.
     */
    public BlockRemover(GameLevel gameLevel, Counter removedBlocks) {
        this.gameLevel = gameLevel;
        this.remainingBlocks = removedBlocks;
    }

    /**
     * @param beingHit the block that being hit
     * @param hitter   the ball the hit the block.
     */
    public void hitEvent(Block beingHit, Ball hitter) {

        // if the life of the block iz zero, remove it.
        if (beingHit.getHitPoints() == 0) {
            // remove listener.
            beingHit.removeHitListener(this);
            // remove from the gameLevel
            beingHit.removeFromGame(gameLevel);
            // remove 1 block of how much remainingblocks there is.
            remainingBlocks.decrease(1);
        }
    }
}