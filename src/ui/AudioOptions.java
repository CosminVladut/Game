package ui;

import main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;

import static main.Game.SCALE;
import static utils.Constants.UI.PauseButtons.BUTTON_SIZE_HEIGHT;
import static utils.Constants.UI.PauseButtons.BUTTON_SIZE_WIDTH;
import static utils.Constants.UI.VolumeSlider.SLIDER_WIDTH;
import static utils.Constants.UI.VolumeSlider.VOLUME_HEIGHT;

public final class AudioOptions
{
    private static AudioOptions instance = null;

    private SoundButton soundButton;
    private VolumeSlider volumeSlider;
    private final Game game;

    private AudioOptions(Game game)
    {
        this.game = game;
        createButtons();
    }

    public static AudioOptions createAudioOptions(Game game)
    {
        if(instance == null)
        {
            instance = new AudioOptions(game);
        }
        return instance;
    }

    private void createButtons()
    {
        int soundX = (int)(290 * SCALE);
        int bY = (int)(130 * SCALE);
        soundButton = new SoundButton(soundX, bY, BUTTON_SIZE_WIDTH, BUTTON_SIZE_HEIGHT);
        
        int vX = (int)(140 * SCALE);
        int vY = (int)(85 * SCALE);
        volumeSlider = new VolumeSlider(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
    }

    public void update()
    {
        soundButton.update();
        volumeSlider.update();
    }

    public void draw(Graphics g)
    {
        soundButton.draw(g);
        volumeSlider.draw(g);
    }

    public void mouseDragged(MouseEvent e)
    {
        if(volumeSlider.isMousePressed())
        {
            float valueBefore = volumeSlider.getFloatValue();
            volumeSlider.changeX(e.getX());
            float valueAfter = volumeSlider.getFloatValue();
            if(valueBefore != valueAfter)
            {
                game.getAudioPlayer().setVolume(valueAfter);
            }
        }
    }

    public void mousePressed(MouseEvent e)
    {
        if (isIn(e, soundButton))
        {
            soundButton.setMousePressed(true);
        }
        else
        {
            if(isIn(e, volumeSlider))
            {
                volumeSlider.setMousePressed(true);
            }
        }
    }

    public void mouseReleased(MouseEvent e)
    {
        if (isIn(e, soundButton))
        {
            if(soundButton.isMousePressed())
            {
                soundButton.setMuted(!soundButton.isMuted());
                game.getAudioPlayer().toggleMute();
            }
        }

        soundButton.resetBooleans();
        volumeSlider.resetBooleans();
    }

    public void mouseMoved(MouseEvent e)
    {
        soundButton.setMouseOver(false);
        volumeSlider.setMouseOver(false);

        if(isIn(e, soundButton))
        {
            soundButton.setMouseOver(true);
        }
        else
        {
            if(isIn(e, volumeSlider))
            {
                volumeSlider.setMouseOver(true);
            }
        }
    }

    private boolean isIn(MouseEvent e, PauseButton b)
    {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
