package gamestates;

import main.Game;
import ui.AudioOptions;
import ui.PauseButton;
import ui.UmButton;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static main.Game.*;
import static utils.Constants.UI.PauseButtons.BUTTON_SIZE_HEIGHT;
import static utils.Constants.UI.PauseButtons.BUTTON_SIZE_WIDTH;

public final class GameOptions extends State implements StateMethods
{
    private static GameOptions instance = null;

    private BufferedImage backgroundImage, optionsBackgroundImage, controlsBackgroundImage;
    private final AudioOptions audioOptions;
    private int backgroundX, backgroundY, backgroundWidth, backgroundHeight;
    private UmButton menuButton, controlsButton, backButton;
    private boolean controls = false;

    private GameOptions(Game game)
    {
        super(game);
        loadImages();
        loadButton();
        audioOptions = game.getAudioOptions();
    }

    public static GameOptions createGameOptions(Game game)
    {
        if(instance == null)
        {
            instance = new GameOptions(game);
        }
        return instance;
    }

    private void loadImages()
    {
        backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        optionsBackgroundImage = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS);
        controlsBackgroundImage = LoadSave.GetSpriteAtlas(LoadSave.CONTROLS_BACKGROUND);
        backgroundWidth = backgroundImage.getWidth();
        backgroundHeight = backgroundImage.getHeight();
        backgroundX = GAME_WIDTH / 2 - backgroundWidth / 2;
        backgroundY = (int)(3 * SCALE);
    }

    private void loadButton()
    {
        int menuX = (int)(45 * SCALE);
        int controlsX = (int)(168 * SCALE);
        int backX = (int)(250 * SCALE);
        int bY = (int)(130 * SCALE);
        menuButton = new UmButton(menuX, bY, BUTTON_SIZE_WIDTH, BUTTON_SIZE_HEIGHT, 1);
        controlsButton = new UmButton(controlsX, bY, BUTTON_SIZE_WIDTH, BUTTON_SIZE_HEIGHT, 6);
        backButton = new UmButton(backX, bY, BUTTON_SIZE_WIDTH, BUTTON_SIZE_HEIGHT, 5);
    }

    @Override
    public void update()
    {
        if(!controls)
        {
            menuButton.update();
            backButton.update();
            audioOptions.update();
        }
        else
        {
            controlsButton.update();
        }
    }

    @Override
    public void draw(Graphics g)
    {
        g.drawImage(backgroundImage, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        if(!controls)
        {
            g.drawImage(optionsBackgroundImage, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);
            menuButton.draw(g);
            audioOptions.draw(g);
            controlsButton.draw(g);
        }
        else
        {
            g.drawImage(controlsBackgroundImage, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);
            backButton.draw(g);
        }

    }

    public void mouseDragged(MouseEvent e)
    {
        audioOptions.mouseDragged(e);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    @Override
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
                if(isIn(e, controlsButton))
                {
                    controlsButton.setMousePressed(true);
                    controls = true;
                }
                audioOptions.mousePressed(e);
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

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if(!controls)
        {
            if(isIn(e, menuButton))
            {
                if(menuButton.isMousePressed())
                {
                    GameState.state = GameState.MENU;
                }
            }
            else
            {
                audioOptions.mouseReleased(e);
            }
            menuButton.resetBooleans();
            controlsButton.resetBooleans();
        }
        else
        {
            backButton.resetBooleans();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        menuButton.setMouseOver(false);
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
                if(isIn(e, controlsButton))
                {
                    controlsButton.setMouseOver(true);
                }
                audioOptions.mouseMoved(e);
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

    @Override
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            GameState.state = GameState.MENU;
            controls = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {

    }

    private boolean isIn(MouseEvent e, PauseButton b)
    {
        return b.getBounds().contains(e.getX(), e.getY());
    }
}
