package ui;

import utils.CustomLogger;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;

import static utils.Constants.UI.PauseButtons.BUTTON_SIZE_HEIGHT_DEFAULT;
import static utils.Constants.UI.PauseButtons.BUTTON_SIZE_WIDTH_DEFAULT;

public final class SoundButton extends PauseButton
{
    private BufferedImage[] soundImages;
    private boolean mouseOver, mousePressed;
    private boolean muted;
    private int index;

    public SoundButton(int x, int y, int width, int height)
    {
        super(x, y, width, height);
        loadSoundImages();
    }

    private void loadSoundImages()
    {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SOUND_BUTTONS);
        soundImages = new BufferedImage[2];

        for(int i = 0; i < soundImages.length; ++i)
        {
            try
            {
                soundImages[i] = temp.getSubimage(i * 47, 0, BUTTON_SIZE_WIDTH_DEFAULT, BUTTON_SIZE_HEIGHT_DEFAULT);
            }
            catch(RasterFormatException e)
            {
                CustomLogger.logException("Nu exista acea parte din sprite-ul pentru butoanele de sunet.", e);
            }
        }
    }

    public void resetBooleans()
    {
        mouseOver = mousePressed = false;
    }

    public void update()
    {
        if(muted)
        {
            index = 1;
        }
        else
        {
            index = 0;
        }

        if(mousePressed)
        {
            if(index == 1)
            {
                index = 0;
            }
            else
            {
                index = 1;
            }
        }
    }

    public void draw(Graphics g)
    {
        g.drawImage(soundImages[index], x, y, width, height, null);
    }

    public boolean isMousePressed()
    {
        return mousePressed;
    }

    public boolean isMuted()
    {
        return muted;
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

    public void setMuted(boolean value)
    {
        muted = value;
    }


}
