package ui;

import java.awt.*;
public class PauseButton
{
    protected int x, y, width, height;
    protected Rectangle bounds;
    protected boolean mouseOver, mousePressed;

    public PauseButton(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        createBounds();
    }

    private void createBounds()
    {
        bounds = new Rectangle(x, y, width, height);
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public Rectangle getBounds()
    {
        return bounds;
    }

    public void setX(int value)
    {
        x = value;
    }

    public void setY(int value)
    {
        y = value;
    }

    public void setWidth(int value)
    {
        width = value;
    }

    public void setHeight(int value)
    {
        height = value;
    }

    public void setBounds(Rectangle value)
    {
        bounds = value;
    }
}
