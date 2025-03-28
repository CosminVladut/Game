package gamestates;

import main.Game;
import ui.MenuButton;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

public final class Menu extends State implements StateMethods
{
    private static Menu instance = null;

    private final MenuButton[] buttons = new MenuButton[6];
    private BufferedImage backgroundImage;
    private int menuX, menuY, menuWidth, menuHeight;
    private boolean pressedStart = false;

    private Menu(Game game)
    {
        super(game);
        loadButtons();
        loadBackground();
    }

    public static Menu createMenu(Game game)
    {
        if(instance == null)
        {
            instance = new Menu(game);
        }
        return instance;
    }

    private void loadBackground()
    {
        backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = backgroundImage.getWidth();
        menuHeight = backgroundImage.getHeight();
        menuX = menuY = 0;
    }

    private void loadButtons()
    {
        buttons[0] = new MenuButton(GAME_WIDTH / 4, GAME_HEIGHT / 5, 0, GameState.MENU);
        buttons[1] = new MenuButton( 2  * GAME_WIDTH / 4, GAME_HEIGHT / 5, 2, GameState.OPTIONS);
        buttons[2] = new MenuButton(3 * GAME_WIDTH / 4, GAME_HEIGHT / 5, 1, GameState.QUIT);
        buttons[3] = new MenuButton(GAME_WIDTH / 4, GAME_HEIGHT / 5, 3, GameState.PLAYING);
        buttons[4] = new MenuButton( 2  * GAME_WIDTH / 4, GAME_HEIGHT / 5, 4, GameState.PLAYING);
        buttons[5] = new MenuButton(3 * GAME_WIDTH / 4, GAME_HEIGHT / 5, 5, GameState.MENU);
    }

    @Override
    public void update()
    {
        if(!pressedStart)
        {
            for(int i = 0; i < buttons.length / 2; ++i)
            {
                buttons[i].update();
            }
        }
        else
        {
            for(int i = buttons.length / 2; i < buttons.length; ++i)
            {
                buttons[i].update();
            }
        }
    }

    @Override
    public void draw(Graphics g)
    {
        g.drawImage(backgroundImage, menuX, menuY, menuWidth, menuHeight, null);
        g.setFont(new Font("Arial", Font.PLAIN, 55));
        g.setColor(new Color(136, 8, 8));
        g.drawString("TRIADA ÎNTUNECATĂ", 290, 100);
        if(!pressedStart)
        {
            for(int i = 0; i < buttons.length / 2; ++i)
            {
                buttons[i].draw(g);
            }
        }
        else
        {
            for(int i = buttons.length / 2; i < buttons.length; ++i)
            {
                buttons[i].draw(g);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if(!pressedStart)
        {
            for(int i = 0; i < buttons.length / 2; ++i)
            {
                if(isIn(e, buttons[i]))
                {
                    buttons[i].setMousePressed(true);
                }
            }
        }
        else
        {
            for(int i = buttons.length / 2; i < buttons.length; ++i)
            {
                if(isIn(e, buttons[i]))
                {
                    buttons[i].setMousePressed(true);
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if(!pressedStart)
        {
            for(int i = 0; i < buttons.length / 2; ++i)
            {
                if(isIn(e, buttons[i]))
                {
                    if(buttons[i].isMousePressed())
                    {
                        if(i == 0)
                        {
                            pressedStart = true;
                        }
                        buttons[i].applyGameState();
                        if(buttons[i].getState() == GameState.QUIT)
                        {
                            game.getAudioPlayer().cleanup();
                        }
                    }
                    if(buttons[i].getState() == GameState.PLAYING && game.getPlaying().getDatabaseHandler().getGameState().canPressContinue == 1)
                    {
                        game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex() + 1);
                    }
                    break;
                }
            }
        }
        else
        {
            for(int i = buttons.length / 2; i < buttons.length; ++i)
            {
                if(isIn(e, buttons[i]))
                {
                    if(buttons[i].isMousePressed())
                    {
                        switch(i)
                        {
                            case 3 ->
                            {
                                pressedStart = false;
                                game.getPlaying().getDatabaseHandler().updateGameState(0, 0, 1);
                                game.getPlaying().getLevelManager().resetLevelIndex();
                                game.getPlaying().loadStartLevel();
                                buttons[i].applyGameState();
                            }
                            case 4 ->
                            {
                                if(game.getPlaying().getDatabaseHandler().getGameState().canPressContinue == 1)
                                {
                                    pressedStart = false;
                                    game.getPlaying().getDatabaseHandler().updateGameState(null, 1, null);
                                    buttons[i].applyGameState();
                                }
                            }
                            case 5 -> pressedStart = false;
                        }
                        if(buttons[i].getState() == GameState.QUIT)
                        {
                            game.getAudioPlayer().cleanup();
                        }
                    }
                    if(buttons[i].getState() == GameState.PLAYING && game.getPlaying().getDatabaseHandler().getGameState().canPressContinue == 1)
                    {
                        game.getAudioPlayer().setLevelSong(game.getPlaying().getLevelManager().getLevelIndex() + 1);
                    }
                    break;
                }
            }
        }
        resetButtons();
    }

    private void resetButtons()
    {
        if(!pressedStart)
        {
            for(int i = 0; i < buttons.length / 2; ++i)
            {
                buttons[i].resetBooleans();
            }
        }
        else
        {
            for(int i = buttons.length / 2; i < buttons.length; ++i)
            {
                buttons[i].resetBooleans();
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        if(!pressedStart)
        {
            for(int i = 0; i < buttons.length / 2; ++i)
            {
                buttons[i].setMouseOver(false);
            }

            for(int i = 0; i < buttons.length / 2; ++i)
            {
                if(isIn(e, buttons[i]))
                {
                    buttons[i].setMouseOver(true);
                    break;
                }
            }
        }
        else
        {
            for(int i = buttons.length / 2; i < buttons.length; ++i)
            {
                buttons[i].setMouseOver(false);
            }
            for(int i = buttons.length / 2; i < buttons.length; ++i)
            {
                if(isIn(e, buttons[i]))
                {
                    buttons[i].setMouseOver(true);
                    break;
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e)
    {

    }

    @Override
    public void keyReleased(KeyEvent e)
    {

    }
}
