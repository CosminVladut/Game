package objects;

import static main.Game.TILES_SIZE;
import static utils.Constants.ObjectConstants.CANNON_HEIGHT_DEFAULT;
import static utils.Constants.ObjectConstants.CANNON_WIDTH_DEFAULT;

public final class Cannon extends GameObject
{
    private final int tileY;

    public Cannon(int x, int y, int objectType)
    {
        super(x, y, CANNON_WIDTH_DEFAULT, CANNON_HEIGHT_DEFAULT, objectType, 0, 0);
        tileY = y / TILES_SIZE;
    }

    public void update()
    {
        if(doAnimation)
        {
            updateAnimationTick();
        }
    }

    public int getTileY()
    {
        return tileY;
    }
}
