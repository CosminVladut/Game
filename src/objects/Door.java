package objects;

import static main.Game.SCALE;

public final class Door extends GameObject
{
    private boolean shouldDrawInteractButton = false;

    public Door(int x, int y, int objectType)
    {
        super(x, y, (int)(13.34 * SCALE), (int)(15 * SCALE), objectType, 0, (int)(11 * SCALE));
    }

    public void setShouldDrawInteractButton(boolean value)
    {
        shouldDrawInteractButton = value;
    }

    public boolean shouldDrawButton()
    {
        return shouldDrawInteractButton;
    }

    public void update()
    {
        updateAnimationTick();
    }
}
