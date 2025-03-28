package ui;

import utils.CustomLogger;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;

import static utils.Constants.UI.PauseButtons.BUTTON_SIZE_HEIGHT_DEFAULT;
import static utils.Constants.UI.PauseButtons.BUTTON_SIZE_WIDTH_DEFAULT;

public final class UmButton extends PauseButton
{
    private BufferedImage[] images;
    private final int index;
    private boolean mouseOver, mousePressed;

    public UmButton(int x, int y, int width, int height, int index)
    {
        super(x, y, width, height);
        this.index = index;
        loadImages();
    }

    private void loadImages()
    {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.UM_LEVEL_COMPLETED_BUTTONS);
        images = new BufferedImage[7];
        for(int i = 0 ; i < images.length; ++i)
        {
            try
            {
                images[i] = temp.getSubimage(0, index * BUTTON_SIZE_HEIGHT_DEFAULT, BUTTON_SIZE_WIDTH_DEFAULT, BUTTON_SIZE_HEIGHT_DEFAULT);
            }
            catch(RasterFormatException e)
            {
                CustomLogger.logException("Nu exista partea asta a butoanelor de meniu.", e);
            }
        }
    }

    public void update()
    {

    }

    public void draw(Graphics g)
    {
        g.drawImage(images[index], x, y, width, height, null);
    }

    public void resetBooleans()
    {
        mouseOver = mousePressed = false;
    }

    public boolean isMousePressed()
    {
        return mousePressed;
    }

    public boolean isMouseOver()
    {
        return mouseOver;
    }

    public void setMouseOver(boolean value)
    {
        mouseOver = value;
    }

    public void setMousePressed(boolean value)
    {
        mousePressed = value;
    }
}
