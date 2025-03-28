package levels;

import audio.AudioPlayer;
import gamestates.GameState;
import main.Game;
import utils.CustomLogger;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Random;

import static main.Game.TILES_IN_HEIGHT;
import static main.Game.TILES_SIZE;

public final class LevelHandler
{
    private static LevelHandler instance = null;

    private final Game game;
    private final ArrayList<Level> levels;
    private final int timeToSpawn = 900;
    private final Random random = new Random();
    private BufferedImage[] levelSprite;
    private int levelIndex;
    private int counterUntilSpawn = 0;

    private LevelHandler(Game game, int levelIndex)
    {
        this.game = game;
        this.levelIndex = levelIndex;
        importOutsideSprites();
        levels = new ArrayList<>();
        buildAllLevels();
    }

    public static LevelHandler createLevelHandler(Game game, int levelIndex)
    {
        if(instance == null)
        {
            instance = new LevelHandler(game, levelIndex);
        }
        return instance;
    }

    public void loadNextLevel( )
    {
        ++levelIndex;
        game.getPlaying().getDatabaseHandler().updateGameState(levelIndex, null, null);
        if(levelIndex >= levels.size())
        {
            levelIndex = 0;
            game.getPlaying().getDatabaseHandler().updateGameState(levelIndex, 0, 0);
            game.getPlaying().setGameState(GameState.MENU);
        }

        Level newLevel = null;
        try
        {
            newLevel = levels.get(levelIndex);
        }
        catch(IndexOutOfBoundsException e)
        {
            CustomLogger.logException("Nu s-a putut accesa nivelul " + levelIndex + ".", e);
        }


        CustomLogger.assertWithLogging(newLevel != null, "Nu se poate pune un nivel nul.");

        game.getPlaying().getEnemyHandler().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLevelData(newLevel.getLevelData());
        game.getPlaying().setMaxLevelOffsetX(newLevel.getMaxLevelOffsetX());
        game.getPlaying().getPlayer().setSpawn(newLevel.getPlayerSpawn());
        game.getPlaying().getObjectHandler().loadObject(newLevel);
        game.getPlaying().getObjectHandler().setTalkedOnce(false);
    }

    private void buildAllLevels( )
    {
        BufferedImage[] allLevels = LoadSave.GetAllLevels();

        for(BufferedImage image : allLevels)
        {
            levels.add(new Level(image));
        }
    }

    public void importOutsideSprites( )
    {
        BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_SPRITESHEET);
        levelSprite = new BufferedImage[134];
        for(int i = 0; i < 2; ++i)
        {
            for(int j = 0; j < 67; ++j)
            {
                int index = i * 67 + j;
                try
                {
                    levelSprite[index] = image.getSubimage(j * 16, i * 16, 16, 16);
                }
                catch(RasterFormatException e)
                {
                    CustomLogger.logException("Partea asta nu exista in poza nivelului.", e);
                }
            }
        }
    }

    public void draw(Graphics g, int levelOffset)
    {
        for(int i = 0; i < levels.get(levelIndex).getLevelData()[0].length; ++i)
        {
            for(int j = 0; j < TILES_IN_HEIGHT; ++j)
            {
                int index = 0;

                try
                {
                    index = levels.get(levelIndex).getSpriteIndex(i, j);
                }
                catch(IndexOutOfBoundsException e)
                {
                    CustomLogger.logException("Nu s-a putut accesa nivelul " + levelIndex + ".", e);
                }

                if(index != 0)
                {
                    g.drawImage(levelSprite[index - 1], i * TILES_SIZE - levelOffset, j * TILES_SIZE, TILES_SIZE, TILES_SIZE, null);
                }
            }
        }
    }

    public void resetLevelIndex()
    {
        levelIndex = 0;
    }

    public void update( )
    {
        try
        {
            if(levelIndex == levels.size() - 1 && !game.getPlaying().getObjectHandler().getTouchBoxes().getFirst().isActive())
            {
                ++counterUntilSpawn;
                if(counterUntilSpawn == timeToSpawn && !game.getPlaying().getEnemyHandler().isBossDead())
                {
                    int enemy = random.nextInt(15) + 1;
                    enemy = enemy == 6 ? random.nextInt(5) + 1 : (enemy == 11 ? random.nextInt(5, 10) + 1 : enemy);
                    int howManyTilesFromTheMiddle = random.nextInt(12) + 1;
                    Rectangle2D.Double hitBoxForSpawn = new Rectangle2D.Double((25 - howManyTilesFromTheMiddle) * TILES_SIZE, 0, (25 - howManyTilesFromTheMiddle) * 2, 14 * TILES_SIZE);
                    game.getPlaying().getEnemyHandler().addEnemy(hitBoxForSpawn, 10, enemy, hitBoxForSpawn.x < 25 * TILES_SIZE ? 1 : 2);
                    game.getAudioPlayer().playEffect(AudioPlayer.NECROMANCER_ATTACK2);
                    counterUntilSpawn = 0;
                }
                game.getPlaying().getEnemyHandler().removeIfSpawnedAndDead();
            }
        }
        catch(NoSuchElementException e)
        {
            CustomLogger.logException("Nu exista touchbox-ul.", e);
        }
    }

    public void resetCounter( )
    {
        counterUntilSpawn = 0;
    }

    public Level getCurrentLevel( )
    {
        try
        {
            return levels.get(levelIndex);
        }
        catch(IndexOutOfBoundsException e)
        {
            CustomLogger.logException("Nu s-a putut accesa nivelul " + levelIndex + ".", e);
        }
        return null;
    }

    public int getLevelIndex( )
    {
        return levelIndex;
    }

    public int getHowManyLevels()
    {
        return levels.size();
    }
}
