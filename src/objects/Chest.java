package objects;

import static main.Game.SCALE;

public final class Chest extends GameObject
{
    private boolean interactable = true;
    private boolean shouldDrawInteractButton = false;

    public Chest(int x, int y, int objectType)
    {
        super(x, y, (int)(16.67 * SCALE), (int)(5.34 * SCALE), objectType, (int) (-8 * SCALE), (int) (16 * SCALE));
    }

    public boolean isInteractable()
    {
        return interactable;
    }

    public void setInteractable(boolean value)
    {
        interactable = value;
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
        if(doAnimation)
        {
            updateAnimationTick();
        }
    }
}
