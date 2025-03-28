package ui;

import gamestates.GameState;
import gamestates.Playing;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static main.Game.*;
import static utils.Constants.UI.PauseButtons.BUTTON_SIZE_HEIGHT;
import static utils.Constants.UI.PauseButtons.BUTTON_SIZE_WIDTH;

public final class GameOverOverlay
{
    private static GameOverOverlay instance = null;

    private final Playing playing;
    private BufferedImage image;
    private int imageX, imageY, imageWidth, imageHeight;
    private UmButton menu, play;

    private GameOverOverlay(Playing playing)
    {
        this.playing = playing;
        createImage();
        createButtons();
    }

    public static GameOverOverlay createGameOverOverlay(Playing playing)
    {
        if(instance == null)
        {
            instance = new GameOverOverlay(playing);
        }
        return instance;
    }

    private void createButtons()
    {
        int menuX = (int)(110 * SCALE);
        int playX = (int)(220 * SCALE);
        int y = (int)(110 * SCALE);
        play = new UmButton(playX, y, BUTTON_SIZE_WIDTH, BUTTON_SIZE_HEIGHT, 2);
        menu = new UmButton(menuX, y, BUTTON_SIZE_WIDTH, BUTTON_SIZE_HEIGHT, 3);
    }

    private void createImage()
    {
        image = LoadSave.GetSpriteAtlas(LoadSave.GAME_OVER);
        imageWidth = (int)(image.getWidth() * SCALE / 1.5);
        imageHeight = (int)(image.getHeight() * SCALE / 1.5);
        imageX = GAME_WIDTH / 2 - imageWidth / 2;
        imageY = (int)(5 * SCALE);
    }

    public void draw(Graphics g)
    {
        g.setColor(new Color(0,0,0, 200));
        g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

        g.drawImage(image, imageX, imageY, imageWidth, imageHeight, null);
        menu.draw(g);
        play.draw(g);

        //        g.setColor(Color.white);
//        g.drawString("Game Over", GAME_WIDTH / 2, 150);
//        g.drawString("Press esc to enter Main Menu!", GAME_WIDTH / 2, 300);
    }

    public void update()
    {

    }

    public void keyPressed(KeyEvent e)
    {

    }

    private boolean isIn(UmButton b, MouseEvent e)
    {
        return b.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseMoved(MouseEvent e)
    {
        play.setMouseOver(false);
        menu.setMouseOver(false);

        if(isIn(menu, e))
        {
            menu.setMouseOver(true);
        }
        else
        {
            if(isIn(play, e))
            {
                play.setMouseOver(true);
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
            if(isIn(play, e))
            {
                if(play.isMousePressed())
                {
                    playing.resetAll();
                    playing.getGame().getAudioPlayer().setLevelSong(playing.getLevelManager().getLevelIndex() + 1);
                }
            }
        }

        menu.resetBooleans();
        play.resetBooleans();
    }

    public void mousePressed(MouseEvent e)
    {
        if(isIn(menu, e))
        {
            menu.setMousePressed(true);
        }
        else
        {
            if(isIn(play, e))
            {
                play.setMousePressed(true);
            }
        }
    }

}
