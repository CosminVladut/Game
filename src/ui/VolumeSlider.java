package ui;

import utils.CustomLogger;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;

import static utils.Constants.UI.VolumeSlider.*;

public final class VolumeSlider extends PauseButton
{
    private BufferedImage button, slider;
    private int buttonX;
    private final int minimumX, maximumX;
    private float floatValue = 0f;

    public VolumeSlider(int x, int y, int width, int height)
    {
        super(x + width / 2, y, VOLUME_WIDTH, height);
        bounds.x  -= VOLUME_WIDTH / 2;
        minimumX = x + VOLUME_WIDTH / 2;
        maximumX = x + width - VOLUME_WIDTH / 2;
        buttonX = x + width / 2;
        this.x = x;
        this.width = width;
        loadImages();
    }

    private void loadImages()
    {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.VOLUME_BUTTONS);
        try
        {
            button = temp.getSubimage(0, 0, VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
            slider = temp.getSubimage(button.getWidth() + 1, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
        }
        catch(RasterFormatException e)
        {
            CustomLogger.logException("Nu exista aceasta parte in slider-ul de volum.", e);
        }
    }

    public void update()
    {

    }

    public void draw(Graphics g)
    {
        g.drawImage(slider, x, y, SLIDER_WIDTH, height, null);
        g.drawImage(button, buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height, null);
    }

    public void changeX(int x)
    {
        if(x < minimumX)
        {
            buttonX = minimumX;
        }
        else
        {
            buttonX = Math.min(x, maximumX);
        }

        updateFloatValue();
        bounds.x = buttonX;
    }

    private void updateFloatValue()
    {
        float range = maximumX - minimumX;
        float value = buttonX - minimumX;
        floatValue = value / range;
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

    public float getFloatValue()
    {
        return floatValue;
    }
}
