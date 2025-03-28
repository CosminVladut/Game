package ui;

import gamestates.GameState;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.UI.Buttons.*;

public final class MenuButton
{
    private final int xPos;
    private final int yPos;
    private final int rowIndex;
    private int index;
    private final GameState state;
    private final int xOffsetCenter = B_WIDTH / 2;
    private BufferedImage[] images;
    private boolean mouseOver, mousePressed;
    private Rectangle bounds;

    public MenuButton(int xPos, int yPos, int rowIndex, GameState state)
    {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        initBounds();
        loadImages();
    }

    private void initBounds()
    {
        bounds = new Rectangle(xPos - xOffsetCenter, yPos + (B_HEIGHT / 2), B_WIDTH, B_HEIGHT);
    }

    private void loadImages()
    {
        images = new BufferedImage[4];

        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS_SPRITESHEET);
        for(int i = 0; i < images.length; ++i)
        {
            images[i] = temp.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
        }
    }

    public void draw(Graphics g)
    {
        g.drawImage(images[index], xPos - xOffsetCenter, yPos + (B_HEIGHT / 2), B_WIDTH, B_HEIGHT, null);
    }

    public void update()
    {
        index = 0;
        if(mouseOver)
        {
            index = 1;
        }
        if(mousePressed)
        {
            index = 2;
        }
    }

    public void setMouseOver(boolean value)
    {
        mouseOver = value;
    }

    public void setMousePressed(boolean value)
    {
        mousePressed = value;
    }

    public boolean isMouseOver()
    {
        return mouseOver;
    }

    public boolean isMousePressed()
    {
        return mousePressed;
    }

    public Rectangle getBounds()
    {
        return bounds;
    }

    public void applyGameState()
    {
        GameState.state = state;
    }

    public void resetBooleans()
    {
        mouseOver = false;
        mousePressed = false;
    }

    public GameState getState()
    {
        return state;
    }
}
