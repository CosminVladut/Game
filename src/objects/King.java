package objects;

import static main.Game.SCALE;

public final class King extends GameObject
{
    private boolean shouldDrawInteractButton = false;

    public King(int x, int y, int objectType)
    {
        super(x, y, (int)(16.67 * SCALE), (int)(3.67 * SCALE), objectType, (int) (30 * SCALE), (int) (74 * SCALE));
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
