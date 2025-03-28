package utils;

import database.DatabaseHandler;
import entities.Enemy;
import entities.EnemyFactory;
import objects.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static main.Game.*;
import static utils.Constants.ObjectConstants.*;

public final class HelperMethods
{
    private static DatabaseHandler databaseHandler;

    private HelperMethods()
    {

    }

    public static void setDatabaseHandler(DatabaseHandler databaseHandler)
    {
        HelperMethods.databaseHandler = databaseHandler;
    }

    public static int CanSpawnEnemyOnSide(Rectangle2D.Double playerHitBox, double enemyWidth, double enemyHeight, int tilesToSpawn, int tilesInHeight, int[][] levelData)
    {
        boolean canSpawnLeft = CanMoveHere(playerHitBox.x - 3 * TILES_SIZE, (tilesToSpawn - tilesInHeight) * TILES_SIZE, enemyWidth, enemyHeight, levelData, true) &&
                IsEntityOnFloor(new Rectangle2D.Double(playerHitBox.x - 3 * TILES_SIZE, (tilesToSpawn - tilesInHeight) * TILES_SIZE, enemyWidth, enemyHeight), levelData, true);

        boolean canSpawnRight = CanMoveHere(playerHitBox.x + playerHitBox.width + 3 * TILES_SIZE, (tilesToSpawn - tilesInHeight) * TILES_SIZE, enemyWidth, enemyHeight, levelData, true) &&
                IsEntityOnFloor(new Rectangle2D.Double(playerHitBox.x + playerHitBox.width + 3 * TILES_SIZE, (tilesToSpawn - tilesInHeight) * TILES_SIZE, enemyWidth, enemyHeight), levelData, true);

        if(canSpawnLeft && canSpawnRight)
        {
            return 0;
        }
        else
        {
            if(canSpawnLeft)
            {
                return 1;
            }
            else
            {
                if(canSpawnRight)
                {
                    return 2;
                }
                else
                {
                    return 3;
                }
            }
        }
    }

    public static boolean CanMoveHere(double x, double y, double width, double height, int[][] levelData, boolean isForEnemy)
    {
        for(int i = 0; i <= 10; ++i)
        {
            double offsetX = x + (i * width) / 10;
            for(int j = 0; j <= 10; ++j)
            {
                double offsetY = y + (j * height) / 10;
                if(IsSolid(offsetX, offsetY, levelData, isForEnemy))
                {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean IsSolid(double x, double y, int[][] levelData, boolean isForEnemy)
    {
        int maxWidth = levelData[0].length * TILES_SIZE;
        if((x < 0 || x >= maxWidth) || (y < 0 || y >= GAME_HEIGHT))
        {
            return true;
        }
        double xIndex = x / TILES_SIZE;
        double yIndex = y / TILES_SIZE;

        return IsTileSolid((int) xIndex, (int) yIndex, levelData, isForEnemy);
    }

    public static boolean IsTileSolid(int xTile, int yTile, int[][] levelData, boolean isForEnemy)
    {
        int value = levelData[yTile][xTile];
        List<Integer> values = null;
        try
        {
             values = List.of(1, 2, 3, 4, 5, 6, 7, 10, 11, 23, 25, 26, 27, 34, 35, 38, 42, 43, 44,
                                           45, 47, 48, 49, 50, 51, 52, 55, 64, 65, 67, 68, 72, 73, 74, 75, 79, 80, 81, 82, 89, 90, 91, 92, 93, 102, 103, 104, 105, 106,
                                           107, 108, 109, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 130, 131, 132, 133, 134);
        }
        catch(NullPointerException e)
        {
            CustomLogger.logException("Exista un tile null sau nu exista niciun tile.", e);
        }

        int solidForEnemy = 133;

        if(isForEnemy)
        {
            return !values.contains(value) || value == solidForEnemy;
        }
        else
        {
            return !values.contains(value);
        }
    }

    public static double GetEntityXPosNextToWall(Rectangle2D.Double hitBox)
    {
        return hitBox.x;
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Double hitBox, int[][] levelData, boolean isForEnemy)
    {
        double startX = hitBox.x;
        double endX = hitBox.x + hitBox.width;
        double yCheck = hitBox.y + hitBox.height + 1;

        for(double x = startX; x <= endX; x += (double) TILES_SIZE / 2)
        {
            if(IsSolid(x, yCheck, levelData, isForEnemy))
            {
                return true;
            }
        }
        return false;
    }

    public static boolean CanCannonSeePlayer(int[][] levelData, Rectangle2D.Double firstHitBox, Rectangle2D.Double secondHitBox, int entityTilesInHeight, int tileY)
    {
        int firstXTile = (int) (firstHitBox.x / TILES_SIZE);
        int secondXTile = (int) (secondHitBox.x / TILES_SIZE);

        if(firstXTile > secondXTile)
        {
            return IsAllTilesClear(secondXTile, firstXTile, tileY, entityTilesInHeight, levelData, false);
        }
        else
        {
            return IsAllTilesClear(firstXTile, secondXTile, tileY, entityTilesInHeight, levelData, false);
        }
    }

    public static boolean IsAllTilesClear(int xStart, int xEnd, int y, int entityTilesInHeight, int[][] levelData, boolean isForEnemy)
    {
        for(int i = 0; i < xEnd - xStart; ++i)
        {
            for(int j = 0; j < entityTilesInHeight; ++j)
            {
                if(IsTileSolid(xStart + i, y + j, levelData, isForEnemy))
                {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean IsProjectileHittingLevel(Projectile p, int[][] levelData)
    {
        return IsSolid(p.getHitBox().x + p.getHitBox().width / 2, p.getHitBox().y + p.getHitBox().height / 2, levelData, false);
    }

    public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int entityTilesInHeight, int[][] levelData)
    {
        if(IsAllTilesClear(xStart, xEnd, y, entityTilesInHeight, levelData, true))
        {
            for(int i = 0; i < xEnd - xStart; ++i)
            {
                for(int j = 0; j < entityTilesInHeight; ++j)
                {
                    if(!IsTileSolid(xStart + i, y + j, levelData, true))
                    {
                        return false;
                    }
                }
            }
        }
        else
        {
            return false;
        }

        return true;
    }

    public static boolean IsSightClear(int[][] levelData, Rectangle2D.Double firstHitBox, Rectangle2D.Double secondHitBox)
    {
        int x0 = (int) (firstHitBox.getCenterX() / TILES_SIZE);
        int y0 = (int) (firstHitBox.getCenterY() / TILES_SIZE);
        int x1 = (int) (secondHitBox.getCenterX() / TILES_SIZE);
        int y1 = (int) (secondHitBox.getCenterY() / TILES_SIZE);

        int xStart = Math.min(x0, x1);
        int xEnd = Math.max(x0, x1);

        for (int x = xStart; x <= xEnd; ++x)
        {
            for (int y = y0; y <= y1; ++y)
            {
                if (IsTileSolid(x, y, levelData, true))
                {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean IsOkToWalkOn(Rectangle2D.Double hitBox, double xSpeed, int[][] levelData)
    {
        if(xSpeed > 0)
        {
            return !IsSolid(hitBox.x + hitBox.width + xSpeed, hitBox.y + hitBox.height, levelData, true);
        }
        return !IsSolid(hitBox.x + xSpeed, hitBox.y + hitBox.height, levelData, true);
    }

    public static int findBarrierHeight(double x, double y, double width, int[][] levelData)
    {
        int barrierHeight = 0;

        for(int i = 0; i <= 13 ; ++i)
        {
            if(IsSolid(x + width / 2, y + i * TILES_SIZE - 1, levelData, false))
            {
                barrierHeight = i * TILES_SIZE;
                break;
            }
        }

        return barrierHeight;
    }

    public static int[][] GetLevelData(BufferedImage image)
    {
        int[][] levelData = new int[image.getHeight()][image.getWidth()];
        for(int i = 0; i < image.getWidth(); ++i)
        {
            for(int j = 0; j < image.getHeight(); ++j)
            {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getRed();
                if(value >= 135)
                {
                    value = 134;
                }
                levelData[j][i] = value;
            }
        }

        return levelData;
    }

    public static <T extends Enemy> ArrayList<T> GetEnemy(BufferedImage image, int enemyType, Class<T> enemyClass)
    {
        ArrayList<T> list = new ArrayList<>();

        for(int i = 0; i < image.getWidth(); ++i)
        {
            for(int j = 0; j < image.getHeight(); ++j)
            {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getGreen();

                if(value == enemyType)
                {
                    if(i * TILES_SIZE > databaseHandler.getPlayerState().positionX)
                    {
                        T enemy = EnemyFactory.createEnemy(enemyType, i * TILES_SIZE, j * TILES_SIZE, enemyClass);
                        list.add(enemy);
                    }
                }
            }
        }

        return list;
    }

    public static Point GetPlayerSpawn(BufferedImage image)
    {
        for(int i = 0; i < image.getWidth(); ++i)
        {
            for(int j = 0; j < image.getHeight(); ++j)
            {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getGreen();
                if(value == 50)
                {
                    return new Point(i * TILES_SIZE, j * TILES_SIZE);
                }
            }
        }

        return new Point(TILES_SIZE, TILES_SIZE);
    }

    public static Chest GetContainerSpawn(BufferedImage image)
    {
        for(int i = 0; i < image.getWidth(); ++i)
        {
            for(int j = 0; j < image.getHeight(); ++j)
            {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getBlue();
                if(value == FOREST_CHEST || value == FIRE_CHEST || value == ICE_CHEST)
                {
                    return new Chest(i * TILES_SIZE, j * TILES_SIZE, value);
                }
            }
        }
        return null;
    }

    public static ArrayList<TouchBox> GetTouchBoxes(BufferedImage image)
    {
        ArrayList<TouchBox> list = new ArrayList<>();

        for(int i = 0; i < image.getWidth(); ++i)
        {
            for(int j = 0; j < image.getHeight(); ++j)
            {
                Color color = new Color(image.getRGB(i, j));
                int value1 = color.getBlue();
                int value2 = color.getGreen();
                if(value1 == TOUCHBOX && (value2 == 200 || value2 == 201))
                {
                    if(i * TILES_SIZE > databaseHandler.getPlayerState().positionX || databaseHandler.getGameState().currentLevel == 4)
                    {
                        list.add(new TouchBox(i * TILES_SIZE, j * TILES_SIZE, value1, value2));
                    }
                }
            }
        }

        return list;
    }

    public static ArrayList<Spike> GetSpikes(BufferedImage image)
    {
        ArrayList<Spike> list = new ArrayList<>();

        for(int i = 0; i < image.getWidth(); ++i)
        {
            for(int j = 0; j < image.getHeight(); ++j)
            {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getBlue();
                if(value == SPIKE || value == DEADLY_MAGIC_FIRE)
                {
                    int width = 0, height = 0;
                    switch(value)
                    {
                        case SPIKE ->
                        {
                            width = SPIKE_WIDTH;
                            height = SPIKE_HEIGHT;
                        }
                        case DEADLY_MAGIC_FIRE ->
                        {
                            width = DEADLY_MAGIC_FIRE_WIDTH;
                            height = DEADLY_MAGIC_FIRE_HEIGHT;
                        }
                    }
                    list.add(new Spike(i * TILES_SIZE, j * TILES_SIZE, width, height, value));
                }
            }
        }

        return list;
    }

    public static ArrayList<SaveSpot> GetSaveSpots(BufferedImage image)
    {
        ArrayList<SaveSpot> list = new ArrayList<>();

        for(int i = 0; i < image.getWidth(); ++i)
        {
            for(int j = 0; j < image.getHeight(); ++j)
            {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getBlue();
                if(value == SAVE_SPOT)
                {
                    list.add(new SaveSpot(i * TILES_SIZE, j * TILES_SIZE, value));
                }
            }
        }

        return list;
    }

    public static King GetKing(BufferedImage image)
    {
        King king = null;

        for(int i = 0; i < image.getWidth(); ++i)
        {
            for(int j = 0; j < image.getHeight(); ++j)
            {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getBlue();
                if(value == KING)
                {
                    king = new King(i * TILES_SIZE, j * TILES_SIZE, value);
                }
            }
        }

        return king;
    }

    public static Door GetDoor(BufferedImage image)
    {
        Door door = null;

        for(int i = 0; i < image.getWidth(); ++i)
        {
            for(int j = 0; j < image.getHeight(); ++j)
            {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getBlue();
                if(value == DOOR)
                {
                    door = new Door(i * TILES_SIZE, j * TILES_SIZE, value);
                }
            }
        }

        return door;
    }

    public static ArrayList<Fire> GetFires(BufferedImage image)
    {
        ArrayList<Fire> list = new ArrayList<>();

        for(int i = 0; i < image.getWidth(); ++i)
        {
            for(int j = 0; j < image.getHeight(); ++j)
            {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getBlue();
                if(value == FOREST_MAGIC_FIRE || value == HELLFIRE)
                {
                    int width = 0, height = 0;
                    switch(value)
                    {
                        case FOREST_MAGIC_FIRE ->
                        {
                            width = FOREST_MAGIC_FIRE_WIDTH_DEFAULT;
                            height = FOREST_MAGIC_FIRE_HEIGHT_DEFAULT;
                        }
                        case HELLFIRE ->
                        {
                            width = HELLFIRE_WIDTH_DEFAULT;
                            height = HELLFIRE_HEIGHT_DEFAULT;
                        }
                    }
                    list.add(new Fire(i * TILES_SIZE, j * TILES_SIZE, width, height, value));
                }
            }
        }

        return list;
    }

    public static ArrayList<Cannon> GetCannons(BufferedImage image)
    {
        ArrayList<Cannon> list = new ArrayList<>();

        for(int i = 0; i < image.getWidth(); ++i)
        {
            for(int j = 0; j < image.getHeight(); ++j)
            {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getBlue();
                if(value == CANNON_LEFT || value == CANNON_RIGHT)
                {
                    list.add(new Cannon(i * TILES_SIZE, j * TILES_SIZE, value));
                }
            }
        }

        return list;
    }

    public static ArrayList<GameObject> GetDecorations(BufferedImage image)
    {
        ArrayList<GameObject> list = new ArrayList<>();

        for(int i = 0; i < image.getWidth(); ++i)
        {
            for(int j = 0; j < image.getHeight(); ++j)
            {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getBlue();
                if(value >= BOOKSHELF1 && value <= TORCH_SMALL)
                {
                    int width = 0, height = 0, xDrawOffset = 0, yDrawOffset = 0;
                    switch(value)
                    {
                        case BOOKSHELF1 ->
                        {
                            width = BOOKSHELF1_WIDTH;
                            height = BOOKSHELF1_HEIGHT;
                            yDrawOffset = (int) (10 * SCALE);
                        }

                        case BOOKSHELF2 ->
                        {
                            width = BOOKSHELF2_WIDTH;
                            height = BOOKSHELF2_HEIGHT;
                            yDrawOffset = (int) (8 * SCALE);
                        }

                        case BAG1 ->
                        {
                            width = BAG1_WIDTH;
                            height = BAG1_HEIGHT;
                            yDrawOffset = (int) (6 * SCALE);
                        }

                        case BAG2 ->
                        {
                            width = BAG2_WIDTH;
                            height = BAG2_HEIGHT;
                            yDrawOffset = (int) (6 * SCALE);
                        }

                        case BARREL ->
                        {
                            width = BARREL_WIDTH;
                            height = BARREL_HEIGHT;
                            yDrawOffset = (int) (2 * SCALE);
                        }

                        case CRATE1 ->
                        {
                            width = CRATE1_WIDTH;
                            height = CRATE1_HEIGHT;
                            yDrawOffset = (int) (-1 * SCALE);
                        }

                        case CRATE2 ->
                        {
                            width = CRATE2_WIDTH;
                            height = CRATE2_HEIGHT;
                            yDrawOffset = (int) (6 * SCALE);
                        }

                        case CRATE3 ->
                        {
                            width = CRATE3_WIDTH;
                            height = CRATE3_HEIGHT;
                            yDrawOffset = (int) (6 * SCALE);
                        }

                        case RED_FLAG ->
                        {
                            width = RED_FLAG_WIDTH;
                            height = RED_FLAG_HEIGHT;
                        }

                        case TABLE ->
                        {
                            width = TABLE_WIDTH;
                            height = TABLE_HEIGHT;
                        }

                        case WEAPON_RACK1 ->
                        {
                            width = WEAPON_RACK1_WIDTH;
                            height = WEAPON_RACK1_HEIGHT;
                            yDrawOffset = (int) (16 * SCALE);
                        }

                        case WEAPON_RACK2 ->
                        {
                            width = WEAPON_RACK2_WIDTH;
                            height = WEAPON_RACK2_HEIGHT;
                            yDrawOffset = (int) (-4 * SCALE);
                        }

                        case CURTAINS ->
                        {
                            width = CURTAINS_WIDTH;
                            height = CURTAINS_HEIGHT;
                            xDrawOffset = (int) (3 * SCALE);
                        }

                        case CANDELABRUM ->
                        {
                            width = CANDELABRUM_WIDTH;
                            height = CANDELABRUM_HEIGHT;
                            yDrawOffset = (int) (-6 * SCALE);
                        }

                        case CANDLE ->
                        {
                            width = CANDLE_WIDTH;
                            height = CANDLE_HEIGHT;
                            yDrawOffset = (int) (6 * SCALE);
                        }

                        case TORCH_BIG ->
                        {
                            width = TORCH_BIG_WIDTH;
                            height = TORCH_BIG_HEIGHT;
                            xDrawOffset = (int) (3 * SCALE);
                        }

                        case TORCH_SMALL ->
                        {
                            width = TORCH_SMALL_WIDTH;
                            height = TORCH_SMALL_HEIGHT;
                            xDrawOffset = (int) (-3 * SCALE);
                        }
                    }
                    list.add(new GameObject(i * TILES_SIZE, j * TILES_SIZE, width, height, value, xDrawOffset, yDrawOffset));
                }
            }
        }

        return list;
    }

    public static ArrayList<Barrier> GetBarriers(BufferedImage image)
    {
        ArrayList<Barrier> list = new ArrayList<>();

        for(int i = 0; i < image.getWidth(); ++i)
        {
            for(int j = 0; j < image.getHeight(); ++j)
            {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getBlue();
                if(value == BARRIER)
                {
                    if(i * TILES_SIZE > databaseHandler.getPlayerState().positionX)
                    {
                        list.add(new Barrier(i * TILES_SIZE, j * TILES_SIZE, value));
                    }
                }
            }
        }

        return list;
    }
}
