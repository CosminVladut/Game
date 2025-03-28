package objects;

import utils.HelperMethods;

import java.awt.geom.Rectangle2D;

import static utils.Constants.ObjectConstants.BARRIER_HEIGHT;
import static utils.Constants.ObjectConstants.BARRIER_WIDTH;

public final class Barrier extends GameObject
{
    int[][] levelData = null;

    public Barrier(int x, int y, int objectType)
    {
        super(x, y, 16, BARRIER_HEIGHT, objectType, 0, 0);
    }

    public boolean willCollide(Rectangle2D.Double playerHitBox, double xSpeed)
    {
        Rectangle2D.Double tempHitBox = new Rectangle2D.Double(playerHitBox.x + xSpeed, playerHitBox.y, playerHitBox.width, playerHitBox.height);

        return hitBox.intersects(tempHitBox);
    }

    public void setLevelData(int[][] value)
    {
        levelData = value;
        hitBox.height = HelperMethods.findBarrierHeight(hitBox.x, 1, BARRIER_WIDTH, levelData);
    }

    public int[][] getLevelData()
    {
        return levelData;
    }
}
