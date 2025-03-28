package gamestates;

import database.DatabaseHandler;
import entities.EnemyHandler;
import entities.Player;
import levels.Level;
import levels.LevelHandler;
import main.Game;
import objects.ObjectHandler;
import ui.*;
import utils.CustomLogger;
import utils.HelperMethods;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static database.DatabaseHandler.createDatabaseHandler;
import static entities.EnemyHandler.createEnemyHandler;
import static entities.Player.createPlayer;
import static levels.LevelHandler.createLevelHandler;
import static main.Game.*;
import static objects.ObjectHandler.createObjectHandler;
import static ui.GameOverOverlay.createGameOverOverlay;
import static ui.InsigniaOverlay.createInsigniaOverlay;
import static ui.LevelCompletedOverlay.createLevelCompletedOverlay;
import static ui.PauseOverlay.createPauseOverlay;
import static ui.TalkingOverlay.createTalkingOverlay;
import static utils.Constants.PlayerConstants.*;

public final class Playing extends State implements StateMethods
{
    private static Playing instance = null;

    private Player player;
    private LevelHandler levelHandler;
    private EnemyHandler enemyHandler;
    private ObjectHandler objectHandler;
    private PauseOverlay pauseOverlay;
    private TalkingOverlay talkingOverlay;
    private InsigniaOverlay insigniaOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private DatabaseHandler databaseHandler;

    private int xLevelOffset;
    private final int leftBorder = (int) (0.49 * GAME_WIDTH);
    private final int rightBorder = (int) (0.51 * GAME_WIDTH);
    private int maxLevelOffsetX;

    private BufferedImage BackgroundImage, Layer4, Layer3, Layer2, Layer1;

    private boolean paused;
    private boolean gameOver;
    private boolean levelCompleted;
    private boolean playerDying;
    private boolean insigniaGot;
    private boolean talking;

    private Playing(Game game)
    {
        super(game);
        initClasses();
        calculateLevelOffset();
        loadStartLevel();
    }

    public static Playing createPlaying(Game game)
    {
        if(instance == null)
        {
            instance = new Playing(game);
        }
        return instance;
    }

    private void loadBackground(int currentLevelIndex)
    {
        switch(currentLevelIndex)
        {
            case 0, 4 ->
            {
                BackgroundImage = Layer4 = Layer3 = null;
                Layer2 = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_2ND_LAYER_FIRST_MAP);
                Layer1 = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_1ST_LAYER_FIRST_MAP);
            }

            case 1 ->
            {
                BackgroundImage = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_IMAGE_SECOND_MAP);
                Layer4 = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_4TH_LAYER_SECOND_MAP);
                Layer3 = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_3RD_LAYER_SECOND_MAP);
                Layer2 = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_2ND_LAYER_SECOND_MAP);
                Layer1 = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_1ST_LAYER_SECOND_MAP);
            }

            case 2 ->
            {
                BackgroundImage = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_IMAGE_THIRD_MAP);
                Layer4 = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_4TH_LAYER_THIRD_MAP);
                Layer3 = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_3RD_LAYER_THIRD_MAP);
                Layer2 = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_2ND_LAYER_THIRD_MAP);
                Layer1 = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_1ST_LAYER_THIRD_MAP);
            }

            case 3 ->
            {
                BackgroundImage = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_IMAGE_FOURTH_MAP);
                Layer4 = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_4TH_LAYER_FOURTH_MAP);
                Layer3 = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_3RD_LAYER_FOURTH_MAP);
                Layer2 = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_2ND_LAYER_FOURTH_MAP);
                Layer1 = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_1ST_LAYER_FOURTH_MAP);
            }
        }
    }

    public void loadNextLevel()
    {
        resetAll();
        levelHandler.loadNextLevel();
        loadBackground(levelHandler.getLevelIndex());
    }

    public void loadStartLevel()
    {
        Level newLevel = null;
        try
        {
            newLevel = levelHandler.getCurrentLevel();
        }
        catch(IndexOutOfBoundsException e)
        {
            CustomLogger.logException("Nu s-a putut accesa nivelul 0.", e);
        }


        CustomLogger.assertWithLogging(newLevel != null, "Nu se poate pune un nivel nul.");

        enemyHandler.loadEnemies(newLevel);
        player.loadLevelData(newLevel.getLevelData());
        setMaxLevelOffsetX(newLevel.getMaxLevelOffsetX());
        objectHandler.loadObject(newLevel);
        objectHandler.setTalkedOnce(false);
        loadBackground(levelHandler.getLevelIndex());
        if(databaseHandler.getGameState().isContinueGame == 0)
        {
            player.setSpawn(levelHandler.getCurrentLevel().getPlayerSpawn());
        }
    }

    private void calculateLevelOffset()
    {
        maxLevelOffsetX = levelHandler.getCurrentLevel().getMaxLevelOffsetX();
    }

    private void initClasses()
    {
        databaseHandler = createDatabaseHandler();
        int levelIndex = 0;
        if(databaseHandler.getGameState().isContinueGame == 1)
        {
            levelIndex = databaseHandler.getGameState().currentLevel;
        }
        HelperMethods.setDatabaseHandler(databaseHandler);
        levelHandler = createLevelHandler(game, levelIndex);
        enemyHandler = createEnemyHandler(this);
        objectHandler = createObjectHandler(this);

        double playerX, playerY;
        if(databaseHandler.getGameState().isContinueGame == 0)
        {
            playerX = levelHandler.getCurrentLevel().getPlayerSpawn().x + 23 * SCALE;
            playerY = levelHandler.getCurrentLevel().getPlayerSpawn().y;
        }
        else
        {
            playerX = databaseHandler.getPlayerState().positionX;
            playerY = databaseHandler.getPlayerState().positionY;
        }

        player = createPlayer(playerX, playerY, (int)(128 * SCALE), (int)(66 * SCALE), this);
        player.loadLevelData(levelHandler.getCurrentLevel().getLevelData());
        pauseOverlay = createPauseOverlay(this);
        talkingOverlay = createTalkingOverlay(this);
        insigniaOverlay = createInsigniaOverlay(this);
        gameOverOverlay = createGameOverOverlay(this);
        levelCompletedOverlay = createLevelCompletedOverlay(this);
    }

    public Player getPlayer()
    {
        return player;
    }

    public DatabaseHandler getDatabaseHandler()
    {
        return databaseHandler;
    }

    public void unpauseGame()
    {
        paused = false;
    }

    public void windowFocusLost()
    {
        player.resetBooleans();
    }

    @Override
    public void update()
    {
        if(insigniaGot)
        {
            insigniaOverlay.update();
        }
        else
        {
            if(paused)
            {
                pauseOverlay.update();
            }
            else
            {
                if(levelCompleted)
                {
                    levelCompletedOverlay.update();
                }
                else
                {
                    if(gameOver)
                    {
                        gameOverOverlay.update();
                    }
                    else
                    {
                        if(playerDying)
                        {
                            player.update();
                        }
                        else
                        {
                            if(talking)
                            {
                                talkingOverlay.update();
                            }
                            levelHandler.update();
                            player.update();
                            enemyHandler.update(levelHandler.getCurrentLevel().getLevelData(), player);
                            objectHandler.update(levelHandler.getCurrentLevel().getLevelData(), player);
                            checkCloseToBorder();
                        }
                    }
                }
            }
        }

    }

    private void checkCloseToBorder()
    {
        int playerX = (int)player.getHitBox().x;
        int diff = playerX - xLevelOffset;

        if(diff > rightBorder)
        {
            xLevelOffset += diff - rightBorder;
        }
        else
        {
            if(diff < leftBorder)
            {
                xLevelOffset += diff - leftBorder;
            }
        }

        if(xLevelOffset > maxLevelOffsetX)
        {
            xLevelOffset = maxLevelOffsetX;
        }
        else
        {
            if(xLevelOffset < 0)
            {
                xLevelOffset = 0;
            }
        }
    }

    public EnemyHandler getEnemyHandler()
    {
        return enemyHandler;
    }

    public ObjectHandler getObjectHandler()
    {
        return objectHandler;
    }

    public void setMaxLevelOffsetX(int levelOffset)
    {
        maxLevelOffsetX = levelOffset;
    }

    public void setLevelCompleted(boolean isLevelCompleted)
    {
        levelCompleted = isLevelCompleted;
        if(levelCompleted)
        {
            game.getAudioPlayer().levelCompleted();
        }
    }

    public void setPlayerDying(boolean playerDying)
    {
        this.playerDying =  playerDying;
    }

    public void setInsigniaGot(boolean gotInsignia)
    {
        insigniaGot = gotInsignia;
        paused = false;
    }

    public void setTalking(boolean isTalking)
    {
        talking = isTalking;
    }

    public void setFinalLevel(boolean value)
    {
        talkingOverlay.setFinal(value);
    }

    @Override
    public void draw(Graphics g)
    {
        g.drawImage(BackgroundImage, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);

        drawLayers(g);

        levelHandler.draw(g, xLevelOffset);
        objectHandler.draw(g, xLevelOffset);
        enemyHandler.draw(g, xLevelOffset);
        player.render(g, xLevelOffset);

        if(talking)
        {
            talkingOverlay.draw(g);
        }

        if(insigniaGot)
        {
            insigniaOverlay.draw(g);
        }
        else
        {
            if(paused)
            {
                g.setColor(new Color(136, 8, 8, 200));
                g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
                pauseOverlay.draw(g);
            }
            else
            {
                if(gameOver)
                {
                    gameOverOverlay.draw(g);
                }
                else
                {
                    if(levelCompleted)
                    {
                        levelCompletedOverlay.draw(g);
                    }
                }
            }
        }
    }

    private void drawLayers(Graphics g)
    {
        if(BackgroundImage != null)
        {
            for(int i = 0; i < 12; ++i)
            {
                g.drawImage(Layer4, i * GAME_WIDTH - (int) (xLevelOffset * 0.2), 0, GAME_WIDTH, GAME_HEIGHT, null);
                g.drawImage(Layer3, i * GAME_WIDTH - (int) (xLevelOffset * 0.4), 0, GAME_WIDTH, GAME_HEIGHT, null);
                g.drawImage(Layer2, i * GAME_WIDTH - (int) (xLevelOffset * 0.6), 0, GAME_WIDTH, GAME_HEIGHT, null);
                g.drawImage(Layer1, i * GAME_WIDTH - (int) (xLevelOffset * 0.9), 0, GAME_WIDTH, GAME_HEIGHT, null);
            }
        }
        else
        {
            for(int i = 0; i < 3; ++i)
            {
                g.drawImage(Layer2, i * GAME_WIDTH - (int) (xLevelOffset * 0.08), 0, GAME_WIDTH, GAME_HEIGHT, null);
                g.drawImage(Layer1, i * GAME_WIDTH - (int) (xLevelOffset * 0.4), 0, GAME_WIDTH, GAME_HEIGHT, null);
            }
        }
    }

    public void resetAll()
    {
        gameOver = false;
        paused = false;
        levelCompleted = false;
        playerDying = false;
        insigniaGot = false;
        talking = false;
        talkingOverlay.resetCurrentIndexInDialogue();
        player.resetAll();
        enemyHandler.resetAllEnemies();
        objectHandler.resetAllObjects();
        levelHandler.resetCounter();
    }

    public void setGameOver(boolean gameOver)
    {
        this.gameOver = gameOver;
    }

    public void checkEnemyHit(Rectangle2D.Double attackBox)
    {
        enemyHandler.checkEnemyHit(attackBox);
    }

    public void checkObjectNear(Rectangle2D.Double hitBox)
    {
        objectHandler.checkObjectNear(hitBox);
    }

    public void checkSpikesTouched(Player player)
    {
        objectHandler.checkSpikesTouched(player);
    }

    public void checkFiresTouched(Player player)
    {
        objectHandler.checkFiresTouched(player);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if(!gameOver)
        {
            if(paused)
            {
                pauseOverlay.mousePressed(e);
            }
            else
            {
                if(levelCompleted)
                {
                    levelCompletedOverlay.mousePressed(e);
                }
            }
        }
        else
        {
            gameOverOverlay.mousePressed(e);
        }
    }

    public void mouseDragged(MouseEvent e)
    {
        if(paused)
        {
            pauseOverlay.mouseDragged(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if(!gameOver)
        {
            if(paused)
            {
                pauseOverlay.mouseReleased(e);
            }
            else
            {
                if(levelCompleted)
                {
                    levelCompletedOverlay.mouseReleased(e);
                }
            }
        }
        else
        {
            gameOverOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        if(!gameOver)
        {
            if(paused)
            {
                pauseOverlay.mouseMoved(e);
            }
            else
            {
                if(levelCompleted)
                {
                    levelCompletedOverlay.mouseMoved(e);
                }
            }
        }
        else
        {
            gameOverOverlay.mouseMoved(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        if(gameOver)
        {
            gameOverOverlay.keyPressed(e);
        }
        else
        {
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_E ->
                {
                    insigniaOverlay.setContinue(true);
                    talkingOverlay.setContinue(true);
                    player.setInteracting(true);
                }
                case KeyEvent.VK_A -> player.setLeft(true);
                case KeyEvent.VK_D -> player.setRight(true);
                case KeyEvent.VK_S -> player.setCrouching(true);
                case KeyEvent.VK_W -> player.setUp(true);
                case KeyEvent.VK_R -> player.setHeal(true);
                case KeyEvent.VK_F -> player.setJump(true);
                case KeyEvent.VK_J ->
                {
                    if (!(player.getState() == DROP_ATTACK || player.getState() == CROUCH_ATTACKS1 || player.getState() == CROUCH_ATTACKS2 || player.getState() == ATTACKS1 || player.getState() == ATTACKS2 || player.getState() == ATTACKS3 || player.getState() == ATTACKS4) && player.getEnergy() >= 25 && !player.isDodging() && !player.isHealing() && !player.isPraying())
                    {
                        player.changeEnergy(-25);
                        player.setAttack(true);
                        player.incrementAttackNumber();
                    }
                }
                case KeyEvent.VK_SPACE -> player.dodge();
                case KeyEvent.VK_ESCAPE ->
                {
                    paused = !paused;
                    pauseOverlay.setControls(false);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if(!gameOver)
        {
            switch (e.getKeyCode())
            {
                case KeyEvent.VK_E -> player.setInteracting(false);
                case KeyEvent.VK_A -> player.setLeft(false);
                case KeyEvent.VK_D -> player.setRight(false);
                case KeyEvent.VK_W -> player.setUp(false);
                case KeyEvent.VK_F -> player.setJump(false);
                case KeyEvent.VK_S -> player.setDown(false);
            }
        }
    }

    public LevelHandler getLevelManager()
    {
        return levelHandler;
    }
}
