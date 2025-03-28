package entities;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static main.Game.SCALE;

public abstract class Entity
{
    protected final int tilesInHeight;
    protected double x, y;
    protected final int width, height;
    protected Rectangle2D.Double hitBox;
    protected int animationTick, animationIndex;
    protected int state;
    protected double airSpeed;
    protected boolean inAir = false;
    protected int maxHealth;
    protected int currentHealth;

    protected double walkSpeed;

    //AttackBox
    protected Rectangle2D.Double attackBox;

    public Entity(double x, double y, int width, int height, int tilesInHeight)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.tilesInHeight = tilesInHeight;
    }

    protected void initHitBox(int width, int height, int hitBoxOffsetX, int hitBoxOffsetY)
    {
        hitBox = new Rectangle2D.Double(x + hitBoxOffsetX, y + hitBoxOffsetY, (int)(width * SCALE), (int)(height * SCALE));
    }

    protected void drawHitBox(Graphics g, int xLvlOffset)
    {
        g.setColor(Color.BLUE);
        g.drawRect((int)hitBox.x - xLvlOffset,(int)hitBox.y, (int)hitBox.width, (int)hitBox.height);
    }

    protected void drawAttackBox(Graphics g, int xlevelOffset)
    {
        g.setColor(Color.RED);
        g.drawRect((int)attackBox.x - xlevelOffset, (int)attackBox.y, (int)attackBox.width, (int)attackBox.height);
    }

    protected void updateHitBox()
    {
        hitBox.x = (int)x;
        hitBox.y = (int)y;
    }

    public Rectangle2D.Double getHitBox()
    {
        return hitBox;
    }

    public int getState()
    {
        return state;
    }

    public int getAnimationIndex()
    {
        return animationIndex;
    }

    public int getTilesInHeight()
    {
        return tilesInHeight;
    }

    public int getCurrentHealth() { return currentHealth; }
}
