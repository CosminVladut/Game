package ui;

import gamestates.GameState;
import gamestates.Playing;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static main.Game.GAME_WIDTH;
import static main.Game.SCALE;
import static utils.Constants.UI.PauseButtons.BUTTON_SIZE_HEIGHT;
import static utils.Constants.UI.PauseButtons.BUTTON_SIZE_WIDTH;

public final class PauseOverlay
{
    private static PauseOverlay instance = null;

    private final Playing playing;
    private BufferedImage backgroundImage, controlsBackgroundImage;
    private int backgroundX, backgroundY, backgroundWidth, backgroundHeight;
    private UmButton menuButton, unpauseButton, controlsButton, backButton;
    private final AudioOptions audioOptions;
    private boolean controls = false;

    private PauseOverlay(Playing playing)
    {
        this.playing = playing;
        loadBackground();
        audioOptions = playing.getGame().getAudioOptions();
        createButtons();
    }

    public static PauseOverlay createPauseOverlay(Playing playing)
    {
        if(instance == null)
        {
            instance = new PauseOverlay(playing);
        }
        return instance;
    }

    private void createButtons()
    {
        int menuX = (int)(45 * SCALE);
        int unpauseX = (int)(123 * SCALE);
        int controlsX = (int)(212 * SCALE);
        int backX = (int)(250 * SCALE);
        int bY = (int)(130 * SCALE);
        menuButton = new UmButton(menuX, bY, BUTTON_SIZE_WIDTH, BUTTON_SIZE_HEIGHT, 1);
        unpauseButton = new UmButton(unpauseX, bY,  BUTTON_SIZE_WIDTH, BUTTON_SIZE_HEIGHT, 0);
        controlsButton = new UmButton(controlsX, bY,  BUTTON_SIZE_WIDTH, BUTTON_SIZE_HEIGHT, 6);
        backButton = new UmButton(backX, bY,  BUTTON_SIZE_WIDTH, BUTTON_SIZE_HEIGHT, 5);
    }

    private void loadBackground()
    {
        backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        controlsBackgroundImage = LoadSave.GetSpriteAtlas(LoadSave.CONTROLS_BACKGROUND);
        backgroundWidth = backgroundImage.getWidth() * 2;
        backgroundHeight = (int)(backgroundImage.getHeight() * 1.5);
        backgroundX = GAME_WIDTH / 2 - backgroundWidth / 2;
        backgroundY = (int)(3 * SCALE);
    }

    public void update()
    {
        if(!controls)
        {
            audioOptions.update();
            menuButton.update();
            unpauseButton.update();
            controlsButton.update();
        }
        else
        {
            backButton.update();
        }
    }

    public void draw(Graphics g)
    {
        if(!controls)
        {
            g.drawImage(backgroundImage, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);
            audioOptions.draw(g);
            menuButton.draw(g);
            unpauseButton.draw(g);
            controlsButton.draw(g);
        }
        else
        {
            g.drawImage(controlsBackgroundImage, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);
            backButton.draw(g);
        }
    }

    public void setControls(boolean value)
    {
        controls = value;
    }

    public void mouseDragged(MouseEvent e)
    {
        audioOptions.mouseDragged(e);
    }

    public void mousePressed(MouseEvent e)
    {
        if(!controls)
        {
            if(isIn(e, menuButton))
            {
                menuButton.setMousePressed(true);
            }
            else
            {
                if(isIn(e, unpauseButton))
                {
                    unpauseButton.setMousePressed(true);
                }
                else
                {
                    if(isIn(e, controlsButton))
                    {
                        controlsButton.setMousePressed(true);
                        controls = true;
                    }
                    else
                    {
                        audioOptions.mousePressed(e);
                    }
                }
            }
        }
        else
        {
            if(isIn(e, backButton))
            {
                backButton.setMousePressed(true);
                controls = false;
            }
        }
    }

    public void mouseReleased(MouseEvent e)
    {
        if(!controls)
        {
            if(isIn(e, menuButton))
            {
                if(menuButton.isMousePressed())
                {
                    playing.resetAll();
                    playing.setGameState(GameState.MENU);
                    playing.unpauseGame();
                }
            }
            else
            {
                if(isIn(e, unpauseButton))
                {
                    if(unpauseButton.isMousePressed())
                    {
                        playing.unpauseGame();
                    }
                }
                else
                {
                    audioOptions.mouseReleased(e);
                }
            }
            menuButton.resetBooleans();
            unpauseButton.resetBooleans();
            controlsButton.resetBooleans();
        }
        else
        {
            backButton.resetBooleans();
        }
    }

    public void mouseMoved(MouseEvent e)
    {
        menuButton.setMouseOver(false);
        unpauseButton.setMouseOver(false);
        controlsButton.setMouseOver(false);
        backButton.setMouseOver(false);

        if(!controls)
        {
            if(isIn(e, menuButton))
            {
                menuButton.setMouseOver(true);
            }
            else
            {
                if(isIn(e, unpauseButton))
                {
                    unpauseButton.setMouseOver(true);
                }
                else
                {
                    if(isIn(e, controlsButton))
                    {
                        controlsButton.setMouseOver(true);
                    }
                    audioOptions.mouseMoved(e);
                }
            }
        }
        else
        {
            if(isIn(e, backButton))
            {
                backButton.setMouseOver(true);
            }
        }
    }

    private boolean isIn(MouseEvent e, PauseButton b)
    {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
