package objects;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static main.Game.SCALE;
import static utils.Constants.ANIMATION_SPEED;
import static utils.Constants.ObjectConstants.*;

public class GameObject
{
    protected final GameObjectFlyweight gameObjectFlyweight;

    protected final Rectangle2D.Double hitBox;
    protected boolean doAnimation, active = true;
    protected int animationTick, animationIndex;

    public GameObject(int x, int y, int width, int height, int objectType, int xDrawOffset, int yDrawOffset)
    {
        this.gameObjectFlyweight = GameObjectFlyweightFactory.getGameObjectFlyweight(objectType, xDrawOffset, yDrawOffset);
        hitBox = new Rectangle2D.Double(x, y, (int) (width * SCALE), (int) (height * SCALE));
    }

    protected void updateAnimationTick()
    {
        ++animationTick;
        if (animationTick >= ANIMATION_SPEED)
        {
            animationTick = 0;
            ++animationIndex;
            if (animationIndex >= GetSpriteAmount(gameObjectFlyweight.objectType()))
            {
                if (gameObjectFlyweight.objectType() == FOREST_CHEST || gameObjectFlyweight.objectType() == FIRE_CHEST || gameObjectFlyweight.objectType() == ICE_CHEST)
                {
                    animationIndex = GetSpriteAmount(gameObjectFlyweight.objectType()) - 1;
                }
                else
                {
                    if(gameObjectFlyweight.objectType() == CANNON_LEFT || gameObjectFlyweight.objectType() == CANNON_RIGHT)
                    {
                        doAnimation = false;
                    }
                    animationIndex = 0;
                }
            }
        }
    }

    public void drawHitBox(Graphics g, int xLvlOffset)
    {
        g.setColor(Color.BLUE);
        g.drawRect((int) hitBox.x - xLvlOffset, (int) hitBox.y, (int) hitBox.width, (int) hitBox.height);
    }

    public void reset()
    {
        animationIndex = 0;
        animationTick = 0;
        active = true;
        if(gameObjectFlyweight.objectType() == CANNON_LEFT || gameObjectFlyweight.objectType() == CANNON_RIGHT)
        {
            doAnimation = false;
        }
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public Rectangle2D.Double getHitBox()
    {
        return hitBox;
    }

    public boolean isActive()
    {
        return active;
    }

    public int getXDrawOffset()
    {
        return gameObjectFlyweight.xDrawOffset();
    }

    public int getYDrawOffset()
    {
        return gameObjectFlyweight.yDrawOffset();
    }

    public int getAnimationIndex()
    {
        return animationIndex;
    }

    public int getObjectType()
    {
        return gameObjectFlyweight.objectType();
    }

    public void setAnimation(boolean doAnimation)
    {
        this.doAnimation = doAnimation;
    }

    public int getAnimationTick()
    {
        return animationTick;
    }
}