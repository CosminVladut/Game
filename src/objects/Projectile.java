package objects;

import java.awt.geom.Rectangle2D;

import static utils.Constants.ANIMATION_SPEED;
import static utils.Constants.Projectiles.GetSpriteAmount;

public final class Projectile
{
    private final ProjectileFlyweight projectileFlyweight;

    private final Rectangle2D.Double hitBox;
    private final int direction;
    private int animationTick, animationIndex;
    private final boolean vertical;

    public Projectile(int x, int y, int direction, boolean vertical, int projectileType, double speed, int width, int height)
    {
        this.direction = direction;
        this.vertical = vertical;
        this.projectileFlyweight = FlyweightProjectileFactory.getProjectileFlyweight(projectileType, speed, width, height);
        hitBox = new Rectangle2D.Double(x, y, width, height);
    }

    public void updateAnimationTick()
    {
        ++animationTick;
        if (animationTick >= ANIMATION_SPEED)
        {
            animationTick = 0;
            ++animationIndex;
            if (animationIndex >= GetSpriteAmount(projectileFlyweight.projectileType()))
            {
                animationIndex = 0;
            }
        }
    }

    public void updatePos()
    {
        if(!vertical)
        {
            hitBox.x += direction * projectileFlyweight.speed();
        }
        else
        {
            hitBox.y += direction * projectileFlyweight.speed();
        }
    }

    public Rectangle2D.Double getHitBox()
    {
        return hitBox;
    }

    public int getAnimationIndex()
    {
        return animationIndex;
    }

    public int getDirection()
    {
        return direction;
    }

    public int getProjectileType() { return projectileFlyweight.projectileType(); }

    public void update()
    {
        updatePos();
        updateAnimationTick();
    }
}
