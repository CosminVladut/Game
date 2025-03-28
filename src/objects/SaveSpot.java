package objects;

import static main.Game.SCALE;

public final class SaveSpot extends GameObject
{
    private boolean shouldDrawInteractButton = false;

    public SaveSpot(int x, int y, int objectType)
    {
        super(x, y, (int)(33.4 * SCALE), (int)(26.67 * SCALE), objectType, 0, 0);
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
