package objects;

import static main.Game.GAME_HEIGHT;
import static main.Game.SCALE;

public final class TouchBox extends GameObject
{
    private boolean forBarrier = false;

    public TouchBox(int x, int y, int objectType, int touchBoxType)
    {
        super(x, y - (touchBoxType == 200 ? (int)(-14.5 * SCALE) : 0), touchBoxType == 200 ? (int)(100 * SCALE) : 16, touchBoxType == 200 ? (int)(0.34 * SCALE) : GAME_HEIGHT, objectType, 0, 0);
        if(touchBoxType == 201)
        {
            forBarrier = true;
        }
    }

    public boolean isForBarrier()
    {
        return forBarrier;
    }
}
