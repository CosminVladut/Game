package main;

import audio.AudioPlayer;
import gamestates.GameOptions;
import gamestates.GameState;
import gamestates.Menu;
import gamestates.Playing;
import ui.AudioOptions;
import utils.CustomLogger;

import java.awt.*;
import java.awt.event.WindowEvent;

import static audio.AudioPlayer.createAudioPlayer;
import static gamestates.GameOptions.createGameOptions;
import static gamestates.Menu.createMenu;
import static gamestates.Playing.createPlaying;
import static ui.AudioOptions.createAudioOptions;

public final class Game implements Runnable
{
    private final GameWindow gameWindow;
    private final GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 60;
    private final int UPS_SET = 60;

    private Playing playing;
    private Menu menu;
    private AudioOptions audioOptions;
    private GameOptions gameOptions;
    private AudioPlayer audioPlayer;

    public final static int TILE_DEFAULT_SIZE = 16;
    public final static double SCALE = 3;
    public final static int TILES_IN_WIDTH = 24;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int)(TILE_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    public Game()
    {
        initClasses();
        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        CustomLogger.setGameWindow(gameWindow);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();
        startGameLoop();
    }

    private void initClasses()
    {
        audioOptions = createAudioOptions(this);
        menu = createMenu(this);
        playing = createPlaying(this);
        gameOptions = createGameOptions(this);
        audioPlayer = createAudioPlayer();
    }

    private void startGameLoop()
    {
        gameThread = new Thread(this);
        try
        {
            gameThread.start();
        }
        catch(IllegalThreadStateException e)
        {
            CustomLogger.logException("Thread-ul jocului a fost pornit deja.", e);
        }
    }

    public void update()
    {
        switch(GameState.state)
        {
            case MENU ->
            {
                menu.update();
                audioPlayer.update();
            }
            case PLAYING ->
            {
                playing.update();
                audioPlayer.update();
            }
            case OPTIONS ->
            {
                gameOptions.update();
                audioPlayer.update();
            }
            case QUIT -> gameWindow.dispatchEvent(new WindowEvent(gameWindow, WindowEvent.WINDOW_CLOSING));
        }
    }

    public void render(Graphics g)
    {
        switch(GameState.state)
        {
            case MENU -> menu.draw(g);
            case OPTIONS -> gameOptions.draw(g);
            case PLAYING -> playing.draw(g);
        }
    }

    public void windowFocusLost()
    {
        if(GameState.state == GameState.PLAYING)
        {
            playing.getPlayer().resetBooleans();
        }
    }

    public Menu getMenu()
    {
        return menu;
    }

    public Playing getPlaying()
    {
        return playing;
    }

    public GameOptions getGameOptions()
    {
        return gameOptions;
    }

    public AudioOptions getAudioOptions()
    {
        return audioOptions;
    }

    public AudioPlayer getAudioPlayer()
    {
        return audioPlayer;
    }

    @Override
    public void run()
    {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true)
        {

            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1)
            {

                update();
                updates++;
                deltaU--;

            }

            if (deltaF >= 1)
            {

                gamePanel.repaint();
                frames++;
                deltaF--;

            }

            if (System.currentTimeMillis() - lastCheck >= 1000)
            {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }
}
