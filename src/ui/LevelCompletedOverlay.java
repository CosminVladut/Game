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

public final class LevelCompletedOverlay
{
    private static LevelCompletedOverlay instance = null;

    private final Playing playing;
    private UmButton menu, next;
    private BufferedImage image;
    private int backgroundX, backgroundY, backgroundWidth, backgroundHeight;

    private LevelCompletedOverlay(Playing playing)
    {
        this.playing = playing;
        initImage();
        initButtons();
    }

    public static LevelCompletedOverlay createLevelCompletedOverlay(Playing playing)
    {
        if(instance == null)
        {
            instance = new LevelCompletedOverlay(playing);
        }
        return instance;
    }

    private void initButtons()
    {
        int menuX = (int)(120 * SCALE);
        int nextX = (int)(210 * SCALE);
        int y = (int)(110 * SCALE);
        next = new UmButton(nextX, y, BUTTON_SIZE_WIDTH, BUTTON_SIZE_HEIGHT, 4);
        menu = new UmButton(menuX, y, BUTTON_SIZE_WIDTH, BUTTON_SIZE_HEIGHT, 3);
    }

    private void initImage()
    {
        image = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_COMPLETE);
        backgroundWidth = (int)((double) image.getWidth() / 2 * SCALE);
        backgroundHeight = (int)((double) image.getHeight() / 2 * SCALE);
        backgroundX = GAME_WIDTH / 2 - backgroundWidth / 2;
        backgroundY = (int)(15 * SCALE);
    }

    public void draw(Graphics g)
    {
        g.drawImage(image, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);
        next.draw(g);
        menu.draw(g);
    }

    public void update()
    {

    }

    private boolean isIn(UmButton b, MouseEvent e)
    {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e)
    {
        next.setMouseOver(false);
        menu.setMouseOver(false);

        if(isIn(menu, e))
        {
            menu.setMouseOver(true);
        }
        else
        {
            if(isIn(next, e))
            {
                next.setMouseOver(true);
            }
        }
    }

    public void mouseReleased(MouseEvent e)
    {
        if(isIn(menu, e))
        {
            if(menu.isMousePressed())
            {
                playing.resetAll();
                playing.setGameState(GameState.MENU);
            }
        }
        else
        {
            if(isIn(next, e))
            {
                if(next.isMousePressed())
                {
                    playing.loadNextLevel();
                    playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLevelIndex() + 1);
                }
            }
        }

        menu.resetBooleans();
        next.resetBooleans();
    }

    public void mousePressed(MouseEvent e)
    {
        if(isIn(menu, e))
        {
            menu.setMousePressed(true);
        }
        else
        {
            if(isIn(next, e))
            {
                next.setMousePressed(true);
            }
        }
    }


}
