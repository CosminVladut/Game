package entities;

import audio.AudioPlayer;
import gamestates.Playing;
import levels.Level;
import utils.Constants;
import utils.CustomLogger;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static main.Game.TILES_SIZE;
import static utils.Constants.EnemyConstants.*;
import static utils.Constants.PlayerConstants.*;

public final class EnemyHandler
{
    private static EnemyHandler instance = null;

    private final Playing playing;

    private BufferedImage[][] skeletonMacerArray, shardsoulSlayerArray, flyingDemonArray,
            huntressArray, necromancerArray, leafRangerArray, werewolfArray, iceGolemArray, iceBorneArray, snowKnightArray,
            vikingArray, fireWormArray, demonArray, fireWizardArray, minotaurArray, fireKnigtArray, finalBossArray;

    private ArrayList<SkeletonMacer> skeletonMacers = new ArrayList<>();
    private ArrayList<ShardsoulSlayer> shardsoulSlayers = new ArrayList<>();
    private ArrayList<FlyingDemon> flyingDemons = new ArrayList<>();
    private ArrayList<Huntress> huntresses = new ArrayList<>();
    private ArrayList<Necromancer> necromancers = new ArrayList<>();
    private ArrayList<LeafRanger> leafRangers = new ArrayList<>();
    private ArrayList<Demon> demons = new ArrayList<>();
    private ArrayList<FireWorm> fireWorms = new ArrayList<>();
    private ArrayList<FireWizard> fireWizards = new ArrayList<>();
    private ArrayList<Minotaur> minotaurs = new ArrayList<>();
    private ArrayList<FireKnight> fireKnights = new ArrayList<>();
    private ArrayList<IceGolem> iceGolems = new ArrayList<>();
    private ArrayList<IceBorne> iceBornes = new ArrayList<>();
    private ArrayList<SnowKnight> snowKnights = new ArrayList<>();
    private ArrayList<Werewolf> werewolves = new ArrayList<>();
    private ArrayList<Viking> vikings = new ArrayList<>();
    private ArrayList<FinalBoss> finalBosses = new ArrayList<>();

    private int zone = 0;
    private boolean enemiesAreDead = false;
    private boolean isBossDead = false;

    private EnemyHandler(Playing playing)
    {
        this.playing = playing;
        loadEnemyImages();
    }

    public static EnemyHandler createEnemyHandler(Playing playing)
    {
        if(instance == null)
        {
            instance = new EnemyHandler(playing);
        }
        return instance;
    }

    public void update(int[][] levelData, Player player)
    {
        boolean isAnyActive;
        isAnyActive = updateEnemy(skeletonMacers, levelData, player);
        isAnyActive = updateEnemy(shardsoulSlayers, levelData, player) || isAnyActive;
        isAnyActive = updateEnemy(flyingDemons, levelData, player) || isAnyActive;
        isAnyActive = updateEnemy(huntresses, levelData, player) || isAnyActive;
        isAnyActive = updateEnemy(necromancers, levelData, player) || isAnyActive;
        isAnyActive = updateEnemy(leafRangers, levelData, player) || isAnyActive;
        isAnyActive = updateEnemy(demons, levelData, player) || isAnyActive;
        isAnyActive = updateEnemy(fireWorms, levelData, player) || isAnyActive;
        isAnyActive = updateEnemy(fireWizards, levelData, player) || isAnyActive;
        isAnyActive = updateEnemy(minotaurs, levelData, player) || isAnyActive;
        isAnyActive = updateEnemy(fireKnights, levelData, player) || isAnyActive;
        isAnyActive = updateEnemy(iceGolems, levelData, player) || isAnyActive;
        isAnyActive = updateEnemy(iceBornes, levelData, player) || isAnyActive;
        isAnyActive = updateEnemy(snowKnights, levelData, player) || isAnyActive;
        isAnyActive = updateEnemy(werewolves, levelData, player) || isAnyActive;
        isAnyActive = updateEnemy(vikings, levelData, player) || isAnyActive;
        isAnyActive = updateEnemy(finalBosses, levelData, player) || isAnyActive;

        enemiesAreDead = !isAnyActive;
    }

    public void removeIfSpawnedAndDead()
    {
        removeSpecificEnemy(skeletonMacers);
        removeSpecificEnemy(shardsoulSlayers);
        removeSpecificEnemy(flyingDemons);
        removeSpecificEnemy(huntresses);
        removeSpecificEnemy(necromancers);
        removeSpecificEnemy(demons);
        removeSpecificEnemy(fireWorms);
        removeSpecificEnemy(fireWizards);
        removeSpecificEnemy(minotaurs);
        removeSpecificEnemy(iceGolems);
        removeSpecificEnemy(iceBornes);
        removeSpecificEnemy(snowKnights);
        removeSpecificEnemy(werewolves);
    }

    private void removeSpecificEnemy(ArrayList<? extends Enemy> enemies)
    {
        try
        {
            enemies.removeIf(enemy -> enemy.isSpawned && !enemy.active);
        }
        catch(NullPointerException e)
        {
            CustomLogger.logException("Nu se poate elimina deoarece filtrul este nul.", e);
        }
    }

    public boolean isAllZoneEliminated(int zoneToTest)
    {
        boolean areAnyLeftInZone;

        areAnyLeftInZone = areEnemiesInZone(skeletonMacers, zoneToTest);
        areAnyLeftInZone = areEnemiesInZone(shardsoulSlayers, zoneToTest) || areAnyLeftInZone;
        areAnyLeftInZone = areEnemiesInZone(flyingDemons, zoneToTest) || areAnyLeftInZone;
        areAnyLeftInZone = areEnemiesInZone(huntresses, zoneToTest) || areAnyLeftInZone;
        areAnyLeftInZone = areEnemiesInZone(necromancers, zoneToTest) || areAnyLeftInZone;
        areAnyLeftInZone = areEnemiesInZone(leafRangers, zoneToTest) || areAnyLeftInZone;
        areAnyLeftInZone = areEnemiesInZone(demons, zoneToTest) || areAnyLeftInZone;
        areAnyLeftInZone = areEnemiesInZone(fireWorms, zoneToTest) || areAnyLeftInZone;
        areAnyLeftInZone = areEnemiesInZone(fireWizards, zoneToTest) || areAnyLeftInZone;
        areAnyLeftInZone = areEnemiesInZone(minotaurs, zoneToTest) || areAnyLeftInZone;
        areAnyLeftInZone = areEnemiesInZone(fireKnights, zoneToTest) || areAnyLeftInZone;
        areAnyLeftInZone = areEnemiesInZone(iceGolems, zoneToTest) || areAnyLeftInZone;
        areAnyLeftInZone = areEnemiesInZone(iceBornes, zoneToTest) || areAnyLeftInZone;
        areAnyLeftInZone = areEnemiesInZone(snowKnights, zoneToTest) || areAnyLeftInZone;
        areAnyLeftInZone = areEnemiesInZone(werewolves, zoneToTest) || areAnyLeftInZone;
        areAnyLeftInZone = areEnemiesInZone(vikings, zoneToTest) || areAnyLeftInZone;
        areAnyLeftInZone = areEnemiesInZone(finalBosses, zoneToTest) || areAnyLeftInZone;

        if(!areAnyLeftInZone)
        {
            removeEnemyFromArray(skeletonMacers);
            removeEnemyFromArray(shardsoulSlayers);
            removeEnemyFromArray(flyingDemons);
            removeEnemyFromArray(huntresses);
            removeEnemyFromArray(necromancers);
            removeEnemyFromArray(leafRangers);
            removeEnemyFromArray(demons);
            removeEnemyFromArray(fireWorms);
            removeEnemyFromArray(fireWizards);
            removeEnemyFromArray(minotaurs);
            removeEnemyFromArray(fireKnights);
            removeEnemyFromArray(iceGolems);
            removeEnemyFromArray(iceBornes);
            removeEnemyFromArray(snowKnights);
            removeEnemyFromArray(werewolves);
            removeEnemyFromArray(vikings);
            removeEnemyFromArray(finalBosses);
        }

        return !areAnyLeftInZone;
    }

    private void removeEnemyFromArray(ArrayList<? extends Enemy> enemies)
    {
        try
        {
            enemies.removeIf(enemy -> enemy.zone == -1 && !enemy.active);
        }
        catch(NullPointerException e)
        {
            CustomLogger.logException("Nu se poate elimina deoarece filtrul este nul.", e);
        }
    }

    private boolean areEnemiesInZone(ArrayList<? extends Enemy> enemies, int zoneToTest)
    {
        for(Enemy e : enemies)
        {
            if(e.zone == zoneToTest)
            {
                return true;
            }
        }

        return false;
    }

    public void setZone(int value)
    {
        zone = value;
    }

    public void setZoneEnemy(double barrierX1, double barrierX2)
    {
        setZoneForEachEnemyType(skeletonMacers, barrierX1, barrierX2);
        setZoneForEachEnemyType(shardsoulSlayers, barrierX1, barrierX2);
        setZoneForEachEnemyType(flyingDemons, barrierX1, barrierX2);
        setZoneForEachEnemyType(huntresses, barrierX1, barrierX2);
        setZoneForEachEnemyType(necromancers, barrierX1, barrierX2);
        setZoneForEachEnemyType(leafRangers, barrierX1, barrierX2);
        setZoneForEachEnemyType(demons, barrierX1, barrierX2);
        setZoneForEachEnemyType(fireWorms, barrierX1, barrierX2);
        setZoneForEachEnemyType(fireWizards, barrierX1, barrierX2);
        setZoneForEachEnemyType(minotaurs, barrierX1, barrierX2);
        setZoneForEachEnemyType(fireKnights, barrierX1, barrierX2);
        setZoneForEachEnemyType(iceGolems, barrierX1, barrierX2);
        setZoneForEachEnemyType(iceBornes, barrierX1, barrierX2);
        setZoneForEachEnemyType(snowKnights, barrierX1, barrierX2);
        setZoneForEachEnemyType(werewolves, barrierX1, barrierX2);
        setZoneForEachEnemyType(vikings, barrierX1, barrierX2);
        setZoneForEachEnemyType(finalBosses, barrierX1, barrierX2);
        --zone;
    }

    private void setZoneForEachEnemyType(ArrayList<? extends Enemy> enemies, double barrierX1, double barrierX2)
    {
        for(Enemy e : enemies)
        {
            if(e.isActive() && e.hitBox.x > barrierX1 && e.hitBox.x < barrierX2)
            {
                e.zone = zone;
            }
        }
    }

    public boolean isBossDead()
    {
        return isBossDead;
    }

    private boolean updateEnemy(ArrayList<? extends Enemy> enemies, int[][] levelData, Player player)
    {
        boolean isAnyActive = false;
        for(Enemy e : enemies)
        {
            if(e.isActive())
            {
                e.update(levelData, player);
                isAnyActive = true;
            }
            else
            {
                if(e instanceof FinalBoss)
                {
                    isBossDead = true;
                }
            }
        }
        return isAnyActive;
    }

    public void draw(Graphics g, int xLevelOffset)
    {
        drawEnemy(g, xLevelOffset, skeletonMacers, skeletonMacerArray, Constants.EnemyConstants.SkeletonMacer.DRAWOFFSET_X, Constants.EnemyConstants.SkeletonMacer.DRAWOFFSET_Y, Constants.EnemyConstants.SkeletonMacer.WIDTH, Constants.EnemyConstants.SkeletonMacer.HEIGHT);
        drawEnemy(g, xLevelOffset, shardsoulSlayers, shardsoulSlayerArray, Constants.EnemyConstants.ShardsoulSlayer.DRAWOFFSET_X, Constants.EnemyConstants.ShardsoulSlayer.DRAWOFFSET_Y, Constants.EnemyConstants.ShardsoulSlayer.WIDTH, Constants.EnemyConstants.ShardsoulSlayer.HEIGHT);
        drawEnemy(g, xLevelOffset, flyingDemons, flyingDemonArray, Constants.EnemyConstants.FlyingDemon.DRAWOFFSET_X, Constants.EnemyConstants.FlyingDemon.DRAWOFFSET_Y, Constants.EnemyConstants.FlyingDemon.WIDTH, Constants.EnemyConstants.FlyingDemon.HEIGHT);
        drawEnemy(g, xLevelOffset, huntresses, huntressArray, Constants.EnemyConstants.Huntress.DRAWOFFSET_X, Constants.EnemyConstants.Huntress.DRAWOFFSET_Y, Constants.EnemyConstants.Huntress.WIDTH, Constants.EnemyConstants.Huntress.HEIGHT);
        drawEnemy(g, xLevelOffset, necromancers, necromancerArray, Constants.EnemyConstants.Necromancer.DRAWOFFSET_X, Constants.EnemyConstants.Necromancer.DRAWOFFSET_Y, Constants.EnemyConstants.Necromancer.WIDTH, Constants.EnemyConstants.Necromancer.HEIGHT);
        drawEnemy(g, xLevelOffset, leafRangers, leafRangerArray, Constants.EnemyConstants.LeafRanger.DRAWOFFSET_X, Constants.EnemyConstants.LeafRanger.DRAWOFFSET_Y, Constants.EnemyConstants.LeafRanger.WIDTH, Constants.EnemyConstants.LeafRanger.HEIGHT);
        drawEnemy(g, xLevelOffset, demons, demonArray, Constants.EnemyConstants.Demon.DRAWOFFSET_X, Constants.EnemyConstants.Demon.DRAWOFFSET_Y, Constants.EnemyConstants.Demon.WIDTH, Constants.EnemyConstants.Demon.HEIGHT);
        drawEnemy(g, xLevelOffset, fireWorms, fireWormArray, Constants.EnemyConstants.FireWorm.DRAWOFFSET_X, Constants.EnemyConstants.FireWorm.DRAWOFFSET_Y, Constants.EnemyConstants.FireWorm.WIDTH, Constants.EnemyConstants.FireWorm.HEIGHT);
        drawEnemy(g, xLevelOffset, fireWizards, fireWizardArray, Constants.EnemyConstants.FireWizard.DRAWOFFSET_X, Constants.EnemyConstants.FireWizard.DRAWOFFSET_Y, Constants.EnemyConstants.FireWizard.WIDTH, Constants.EnemyConstants.FireWizard.HEIGHT);
        drawEnemy(g, xLevelOffset, minotaurs, minotaurArray, Constants.EnemyConstants.Minotaur.DRAWOFFSET_X, Constants.EnemyConstants.Minotaur.DRAWOFFSET_Y, Constants.EnemyConstants.Minotaur.WIDTH, Constants.EnemyConstants.Minotaur.HEIGHT);
        drawEnemy(g, xLevelOffset, fireKnights, fireKnigtArray, Constants.EnemyConstants.FireKnight.DRAWOFFSET_X, Constants.EnemyConstants.FireKnight.DRAWOFFSET_Y, Constants.EnemyConstants.FireKnight.WIDTH, Constants.EnemyConstants.FireKnight.HEIGHT);
        drawEnemy(g, xLevelOffset, iceGolems, iceGolemArray, Constants.EnemyConstants.IceGolem.DRAWOFFSET_X, Constants.EnemyConstants.IceGolem.DRAWOFFSET_Y, Constants.EnemyConstants.IceGolem.WIDTH, Constants.EnemyConstants.IceGolem.HEIGHT);
        drawEnemy(g, xLevelOffset, iceBornes, iceBorneArray, Constants.EnemyConstants.IceBorne.DRAWOFFSET_X, Constants.EnemyConstants.IceBorne.DRAWOFFSET_Y, Constants.EnemyConstants.IceBorne.WIDTH, Constants.EnemyConstants.IceBorne.HEIGHT);
        drawEnemy(g, xLevelOffset, snowKnights, snowKnightArray, Constants.EnemyConstants.SnowKnight.DRAWOFFSET_X, Constants.EnemyConstants.SnowKnight.DRAWOFFSET_Y, Constants.EnemyConstants.SnowKnight.WIDTH, Constants.EnemyConstants.SnowKnight.HEIGHT);
        drawEnemy(g, xLevelOffset, werewolves, werewolfArray, Constants.EnemyConstants.Werewolf.DRAWOFFSET_X, Constants.EnemyConstants.Werewolf.DRAWOFFSET_Y, Constants.EnemyConstants.Werewolf.WIDTH, Constants.EnemyConstants.Werewolf.HEIGHT);
        drawEnemy(g, xLevelOffset, vikings, vikingArray, Constants.EnemyConstants.Viking.DRAWOFFSET_X, Constants.EnemyConstants.Viking.DRAWOFFSET_Y, Constants.EnemyConstants.Viking.WIDTH, Constants.EnemyConstants.Viking.HEIGHT);
        drawEnemy(g, xLevelOffset, finalBosses, finalBossArray, Constants.EnemyConstants.FinalBoss.DRAWOFFSET_X, Constants.EnemyConstants.FinalBoss.DRAWOFFSET_Y, Constants.EnemyConstants.FinalBoss.WIDTH, Constants.EnemyConstants.FinalBoss.HEIGHT);
    }

    public void loadEnemies(Level level)
    {
        skeletonMacers = level.getSkeletonMacers();
        loadEnemyPlaying(skeletonMacers);

        shardsoulSlayers = level.getShardSoulSlayers();
        loadEnemyPlaying(shardsoulSlayers);

        flyingDemons = level.getFlyingDemons();
        loadEnemyPlaying(flyingDemons);

        huntresses = level.getHuntresses();
        loadEnemyPlaying(huntresses);

        necromancers = level.getNecromancers();
        loadEnemyPlaying(necromancers);

        leafRangers = level.getLeafRangers();
        loadEnemyPlaying(leafRangers);

        demons = level.getDemons();
        loadEnemyPlaying(demons);

        iceGolems = level.getIceGolems();
        loadEnemyPlaying(iceGolems);

        iceBornes = level.getIceBornes();
        loadEnemyPlaying(iceBornes);

        snowKnights = level.getSnowKnights();
        loadEnemyPlaying(snowKnights);

        werewolves = level.getWerewolves();
        loadEnemyPlaying(werewolves);

        vikings = level.getVikings();
        loadEnemyPlaying(vikings);

        fireWorms = level.getFireWorms();
        loadEnemyPlaying(fireWorms);

        fireWizards = level.getFireWizards();
        loadEnemyPlaying(fireWizards);

        minotaurs = level.getMinotaurs();
        loadEnemyPlaying(minotaurs);

        fireKnights = level.getFireKnights();
        loadEnemyPlaying(fireKnights);

        finalBosses = level.getFinalBosses();
        loadEnemyPlaying(finalBosses);
    }

    public void addEnemy(Rectangle2D.Double playerHitBox, int tilesToSpawn, int enemyType, int direction)
    {
        switch(enemyType)
        {
            case SKELETON_MACER ->
            {
                skeletonMacers.add(EnemyFactory.createEnemy(SKELETON_MACER, direction == 1 ? (int) (playerHitBox.x - 3 * TILES_SIZE) : (int) (playerHitBox.x + playerHitBox.width + 2 * TILES_SIZE), (tilesToSpawn - 2) * TILES_SIZE, SkeletonMacer.class));
                skeletonMacers.getLast().setPlaying(playing);
                skeletonMacers.getLast().setSpawned(true);
            }
            case SHARDSOUL_SLAYER ->
            {
                shardsoulSlayers.add(EnemyFactory.createEnemy(SHARDSOUL_SLAYER, direction == 1 ? (int) (playerHitBox.x - 3 * TILES_SIZE) : (int) (playerHitBox.x + playerHitBox.width + 2 * TILES_SIZE), (tilesToSpawn - 2) * TILES_SIZE, ShardsoulSlayer.class));
                shardsoulSlayers.getLast().setPlaying(playing);
                shardsoulSlayers.getLast().setSpawned(true);
            }
            case FLYING_DEMON ->
            {
                flyingDemons.add(EnemyFactory.createEnemy(FLYING_DEMON, direction == 1 ? (int) (playerHitBox.x - 3 * TILES_SIZE) : (int) (playerHitBox.x + playerHitBox.width + 2 * TILES_SIZE), (tilesToSpawn - 3) * TILES_SIZE, FlyingDemon.class));
                flyingDemons.getLast().setPlaying(playing);
                flyingDemons.getLast().setSpawned(true);
            }
            case HUNTRESS ->
            {
                huntresses.add(EnemyFactory.createEnemy(HUNTRESS, direction == 1 ? (int) (playerHitBox.x - 3 * TILES_SIZE) : (int) (playerHitBox.x + playerHitBox.width + 2 * TILES_SIZE), (tilesToSpawn - 3) * TILES_SIZE, Huntress.class));
                huntresses.getLast().setPlaying(playing);
                huntresses.getLast().setSpawned(true);
            }
            case NECROMANCER ->
            {
                necromancers.add(EnemyFactory.createEnemy(NECROMANCER, direction == 1 ? (int) (playerHitBox.x - 3 * TILES_SIZE) : (int) (playerHitBox.x + playerHitBox.width + 2 * TILES_SIZE), (tilesToSpawn - 3) * TILES_SIZE, Necromancer.class));
                necromancers.getLast().setPlaying(playing);
                necromancers.getLast().setSpawned(true);
            }
            case DEMON ->
            {
                demons.add(EnemyFactory.createEnemy(DEMON, direction == 1 ? (int) (playerHitBox.x - 3 * TILES_SIZE) : (int) (playerHitBox.x + playerHitBox.width + 2 * TILES_SIZE), (tilesToSpawn - 5) * TILES_SIZE, Demon.class));
                demons.getLast().setPlaying(playing);
                demons.getLast().setSpawned(true);
            }
            case FIRE_WORM ->
            {
                fireWorms.add(EnemyFactory.createEnemy(FIRE_WORM, direction == 1 ? (int) (playerHitBox.x - 3 * TILES_SIZE) : (int) (playerHitBox.x + playerHitBox.width + 2 * TILES_SIZE), (tilesToSpawn - 2) * TILES_SIZE, FireWorm.class));
                fireWorms.getLast().setPlaying(playing);
                fireWorms.getLast().setSpawned(true);
            }
            case FIRE_WIZARD ->
            {
                fireWizards.add(EnemyFactory.createEnemy(FIRE_WIZARD, direction == 1 ? (int) (playerHitBox.x - 3 * TILES_SIZE) : (int) (playerHitBox.x + playerHitBox.width + 2 * TILES_SIZE), (tilesToSpawn - 3) * TILES_SIZE, FireWizard.class));
                fireWizards.getLast().setPlaying(playing);
                fireWizards.getLast().setSpawned(true);
            }
            case MINOTAUR ->
            {
                minotaurs.add(EnemyFactory.createEnemy(MINOTAUR, direction == 1 ? (int) (playerHitBox.x - 3 * TILES_SIZE) : (int) (playerHitBox.x + playerHitBox.width + 2 * TILES_SIZE), (tilesToSpawn - 3) * TILES_SIZE, Minotaur.class));
                minotaurs.getLast().setPlaying(playing);
                minotaurs.getLast().setSpawned(true);
            }
            case ICE_BORNE ->
            {
                iceBornes.add(EnemyFactory.createEnemy(ICE_BORNE, direction == 1 ? (int) (playerHitBox.x - 3 * TILES_SIZE) : (int) (playerHitBox.x + playerHitBox.width + 2 * TILES_SIZE), (tilesToSpawn - 2) * TILES_SIZE, IceBorne.class));
                iceBornes.getLast().setPlaying(playing);
                iceBornes.getLast().setSpawned(true);
            }
            case ICE_GOLEM ->
            {
                iceGolems.add(EnemyFactory.createEnemy(ICE_GOLEM, direction == 1 ? (int) (playerHitBox.x - 3 * TILES_SIZE) : (int) (playerHitBox.x + playerHitBox.width + 2 * TILES_SIZE), (tilesToSpawn - 5) * TILES_SIZE, IceGolem.class));
                iceGolems.getLast().setPlaying(playing);
                iceGolems.getLast().setSpawned(true);
            }
            case SNOW_KNIGHT ->
            {
                snowKnights.add(EnemyFactory.createEnemy(SNOW_KNIGHT, direction == 1 ? (int) (playerHitBox.x - 3 * TILES_SIZE) : (int) (playerHitBox.x + playerHitBox.width + 2 * TILES_SIZE), (tilesToSpawn - 3) * TILES_SIZE, SnowKnight.class));
                snowKnights.getLast().setPlaying(playing);
                snowKnights.getLast().setSpawned(true);
            }
            case WEREWOLF ->
            {
                werewolves.add(EnemyFactory.createEnemy(WEREWOLF, direction == 1 ? (int) (playerHitBox.x - 3 * TILES_SIZE) : (int) (playerHitBox.x + playerHitBox.width + 2 * TILES_SIZE), (tilesToSpawn - 4) * TILES_SIZE, Werewolf.class));
                werewolves.getLast().setPlaying(playing);
                werewolves.getLast().setSpawned(true);
            }
        }
    }

    private void loadEnemyPlaying(ArrayList<? extends Enemy> enemies)
    {
        for(Enemy e : enemies)
        {
            e.setPlaying(playing);
        }
    }

    private void drawEnemy(Graphics g, int xLevelOffset, ArrayList<? extends Enemy> enemies, BufferedImage[][] enemyArray, int enemyDrawOffsetX, int enemyDrawOffsetY, int enemyWidth, int enemyHeight)
    {
        for(Enemy e : enemies)
        {
            if(e.isActive())
            {
                g.drawImage(enemyArray[e.getState()][e.getAnimationIndex()], (int) (e.getHitBox().x - enemyDrawOffsetX) - xLevelOffset + e.flipX(), (int) (e.getHitBox().y - enemyDrawOffsetY), enemyWidth * e.flipW(), enemyHeight, null);
//                e.drawHitBox(g, xLevelOffset);
//                e.drawAttackBox(g, xLevelOffset);
            }
        }
    }

    public void checkEnemyHit(Rectangle2D.Double attackBox)
    {
        checkEnemy(attackBox, skeletonMacers, true, true, Constants.EnemyConstants.SkeletonMacer.DEATH, Constants.EnemyConstants.SkeletonMacer.TAKE_HIT);
        checkEnemy(attackBox, shardsoulSlayers, false, true, Constants.EnemyConstants.ShardsoulSlayer.DEATH, Constants.EnemyConstants.ShardsoulSlayer.TAKE_HIT);
        checkEnemy(attackBox, flyingDemons, true, true, Constants.EnemyConstants.FlyingDemon.DEATH, Constants.EnemyConstants.FlyingDemon.TAKE_HIT);
        checkEnemy(attackBox, huntresses, true, false, Constants.EnemyConstants.Huntress.DEATH, Constants.EnemyConstants.Huntress.TAKE_HIT);
        checkEnemy(attackBox, necromancers, false, false, Constants.EnemyConstants.Necromancer.DEATH, Constants.EnemyConstants.Necromancer.TAKE_HIT);
        checkEnemy(attackBox, leafRangers, false, true, Constants.EnemyConstants.LeafRanger.DEATH, Constants.EnemyConstants.LeafRanger.TAKE_HIT);
        checkEnemy(attackBox, demons, false, true, Constants.EnemyConstants.Demon.DEATH, Constants.EnemyConstants.Demon.TAKE_HIT);
        checkEnemy(attackBox, fireWorms, false, true, Constants.EnemyConstants.FireWorm.DEATH, Constants.EnemyConstants.FireWorm.TAKE_HIT);
        checkEnemy(attackBox, fireWizards, false, true, Constants.EnemyConstants.FireWizard.DEATH, Constants.EnemyConstants.FireWizard.TAKE_HIT);
        checkEnemy(attackBox, minotaurs, true, false, Constants.EnemyConstants.Minotaur.DEATH, Constants.EnemyConstants.Minotaur.TAKE_HIT);
        checkEnemy(attackBox, fireKnights, true, false, Constants.EnemyConstants.FireKnight.DEATH, Constants.EnemyConstants.FireKnight.TAKE_HIT);
        checkEnemy(attackBox, iceGolems, false, true, Constants.EnemyConstants.IceGolem.DEATH, Constants.EnemyConstants.IceGolem.TAKE_HIT);
        checkEnemy(attackBox, iceBornes, true, false, Constants.EnemyConstants.IceBorne.DEATH, Constants.EnemyConstants.IceBorne.TAKE_HIT);
        checkEnemy(attackBox, snowKnights, false, true, Constants.EnemyConstants.SnowKnight.DEATH, Constants.EnemyConstants.SnowKnight.TAKE_HIT);
        checkEnemy(attackBox, werewolves, true, false, Constants.EnemyConstants.Werewolf.DEATH, Constants.EnemyConstants.Werewolf.TAKE_HIT);
        checkEnemy(attackBox, vikings, false, true, Constants.EnemyConstants.Viking.DEATH, Constants.EnemyConstants.Viking.TAKE_HIT);
        checkEnemy(attackBox, finalBosses, false, true, Constants.EnemyConstants.FinalBoss.DEATH, Constants.EnemyConstants.FinalBoss.TAKE_HIT);
    }

    private void checkEnemy(Rectangle2D.Double attackBox, ArrayList<? extends Enemy> enemies, boolean flick2, boolean flick3, int deathState, int takeHitState)
    {
        for(Enemy e : enemies)
        {
            if(e.isActive() && e.getCurrentHealth() > 0)
            {
                if(attackBox.intersects(e.getHitBox()))
                {
                    if((e instanceof LeafRanger) && e.getState() == Constants.EnemyConstants.LeafRanger.BLOCK)
                    {
                        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.LEAF_RANGER_BLOCK);
                    }
                    else
                    {
                        if((e instanceof Minotaur) && e.getState() == Constants.EnemyConstants.Minotaur.BLOCK)
                        {
                            playing.getGame().getAudioPlayer().playEffect(AudioPlayer.MINOTAUR_BLOCK);
                        }
                        else
                        {
                            if((e instanceof FireKnight) && (e.getState() == Constants.EnemyConstants.FireKnight.BLOCK || e.getState() == Constants.EnemyConstants.FireKnight.ROLL))
                            {
                                if(e.getState() == Constants.EnemyConstants.FireKnight.BLOCK)
                                {
                                    playing.getGame().getAudioPlayer().playEffect(AudioPlayer.FIRE_KNIGHT_BLOCK);
                                }
                            }
                            else
                            {
                                if((e instanceof Viking && (e.getState() == Constants.EnemyConstants.Viking.BLOCK)))
                                {
                                    playing.getGame().getAudioPlayer().playEffect(AudioPlayer.VIKING_BLOCK);
                                }
                                else
                                {
                                    switch(playing.getPlayer().getState())
                                    {
                                        case ATTACKS1, ATTACKS2, CROUCH_ATTACKS1, CROUCH_ATTACKS2 ->
                                        {
                                            playing.getGame().getAudioPlayer().playHitSound();
                                            e.hurt(1000, false, deathState, takeHitState);
                                        }

                                        case ATTACKS3, DROP_ATTACK ->
                                        {
                                            playing.getGame().getAudioPlayer().playHitSound();
                                            e.hurt(20, flick2, deathState, takeHitState);
                                        }

                                        case ATTACKS4 ->
                                        {
                                            playing.getGame().getAudioPlayer().playEffect(AudioPlayer.HARD_HIT);
                                            e.hurt(25, flick3, deathState, takeHitState);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void resetAllEnemies( )
    {
        resetEnemy(skeletonMacers, Constants.EnemyConstants.SkeletonMacer.IDLE);
        resetEnemy(shardsoulSlayers, Constants.EnemyConstants.ShardsoulSlayer.IDLE);
        resetEnemy(flyingDemons, Constants.EnemyConstants.FlyingDemon.IDLE);
        resetEnemy(huntresses, Constants.EnemyConstants.Huntress.IDLE);
        resetEnemy(necromancers, Constants.EnemyConstants.Necromancer.WALK);
        resetEnemy(leafRangers, Constants.EnemyConstants.LeafRanger.IDLE);
        resetEnemy(demons, Constants.EnemyConstants.Demon.IDLE);
        resetEnemy(fireWorms, Constants.EnemyConstants.FireWorm.IDLE);
        resetEnemy(fireWizards, Constants.EnemyConstants.FireWizard.IDLE);
        resetEnemy(minotaurs, Constants.EnemyConstants.Minotaur.IDLE);
        resetEnemy(fireKnights, Constants.EnemyConstants.FireKnight.IDLE);
        resetEnemy(iceGolems, Constants.EnemyConstants.IceGolem.IDLE);
        resetEnemy(iceBornes, Constants.EnemyConstants.IceBorne.IDLE);
        resetEnemy(snowKnights, Constants.EnemyConstants.SnowKnight.IDLE);
        resetEnemy(werewolves, Constants.EnemyConstants.Werewolf.IDLE);
        resetEnemy(vikings, Constants.EnemyConstants.Viking.IDLE);
        resetEnemy(finalBosses, Constants.EnemyConstants.FinalBoss.IDLE);
    }

    private void resetEnemy(ArrayList<? extends Enemy> enemies, int idleState)
    {
        Iterator<? extends Enemy> iterator = enemies.iterator();
        while(iterator.hasNext())
        {
            Enemy e = null;
            try
            {
                e = iterator.next();
            }
            catch(NoSuchElementException ex)
            {
                CustomLogger.logException("Nu mai exista oponenti.", ex);
            }

            CustomLogger.assertWithLogging(e != null, "Oponentul nu exista");

            if(!e.isSpawned)
            {
                e.resetEnemy(idleState);
            }
            else
            {
                try
                {
                    iterator.remove();
                }
                catch(UnsupportedOperationException ex)
                {
                    CustomLogger.logException("Operatie nesuportata de sirul de oponenti.", ex);
                }
                catch(IllegalStateException ex)
                {
                    CustomLogger.logException("Nu a fost mers prin sirul de oponenti exclusiv doar odata.", ex);
                }
            }
        }
    }

    public boolean areEnemiesDead()
    {
        return enemiesAreDead;
    }

    private void loadEnemyImages( )
    {
        skeletonMacerArray = new BufferedImage[6][13];
        loadBufferArray(skeletonMacerArray, LoadSave.SKELETON_MACER_SPRITESHEET, Constants.EnemyConstants.SkeletonMacer.WIDTH_DEFAULT, Constants.EnemyConstants.SkeletonMacer.HEIGHT_DEFAULT);

        shardsoulSlayerArray = new BufferedImage[5][8];
        loadBufferArray(shardsoulSlayerArray, LoadSave.SHARDSOUL_SLAYER_SPRITESHEET, Constants.EnemyConstants.ShardsoulSlayer.WIDTH_DEFAULT, Constants.EnemyConstants.ShardsoulSlayer.HEIGHT_DEFAULT);

        flyingDemonArray = new BufferedImage[5][8];
        loadBufferArray(flyingDemonArray, LoadSave.FLYING_DEMON_SPRITESHEET, Constants.EnemyConstants.FlyingDemon.WIDTH_DEFAULT, Constants.EnemyConstants.FlyingDemon.HEIGHT_DEFAULT);

        huntressArray = new BufferedImage[7][8];
        loadBufferArray(huntressArray, LoadSave.HUNTRESS_SPRITESHEET, Constants.EnemyConstants.Huntress.WIDTH_DEFAULT, Constants.EnemyConstants.Huntress.HEIGHT_DEFAULT);

        necromancerArray = new BufferedImage[5][17];
        loadBufferArray(necromancerArray, LoadSave.NECROMANCER_SPRITESHEET, Constants.EnemyConstants.Necromancer.WIDTH_DEFAULT, Constants.EnemyConstants.Necromancer.HEIGHT_DEFAULT);

        leafRangerArray = new BufferedImage[9][19];
        loadBufferArray(leafRangerArray, LoadSave.LEAF_RANGER_SPRITESHEET, Constants.EnemyConstants.LeafRanger.WIDTH_DEFAULT, Constants.EnemyConstants.LeafRanger.HEIGHT_DEFAULT);

        demonArray = new BufferedImage[5][22];
        loadBufferArray(demonArray, LoadSave.DEMON_SPRITESHEET, Constants.EnemyConstants.Demon.WIDTH_DEFAULT, Constants.EnemyConstants.Demon.HEIGHT_DEFAULT);

        iceGolemArray = new BufferedImage[5][16];
        loadBufferArray(iceGolemArray, LoadSave.ICE_GOLEM_SPRITESHEET, Constants.EnemyConstants.IceGolem.WIDTH_DEFAULT, Constants.EnemyConstants.IceGolem.HEIGHT_DEFAULT);

        iceBorneArray = new BufferedImage[5][23];
        loadBufferArray(iceBorneArray, LoadSave.ICE_BORNE_SPRITESHEET, Constants.EnemyConstants.IceBorne.WIDTH_DEFAULT, Constants.EnemyConstants.IceBorne.HEIGHT_DEFAULT);

        snowKnightArray = new BufferedImage[7][10];
        loadBufferArray(snowKnightArray, LoadSave.SNOW_KNIGHT_SPRITESHEET, Constants.EnemyConstants.SnowKnight.WIDTH_DEFAULT, Constants.EnemyConstants.SnowKnight.HEIGHT_DEFAULT);

        werewolfArray = new BufferedImage[10][11];
        loadBufferArray(werewolfArray, LoadSave.WEREWOLF_SPRITESHEET, Constants.EnemyConstants.Werewolf.WIDTH_DEFAULT, Constants.EnemyConstants.Werewolf.HEIGHT_DEFAULT);

        vikingArray = new BufferedImage[9][12];
        loadBufferArray(vikingArray, LoadSave.VIKING_SPRITESHEET, Constants.EnemyConstants.Viking.WIDTH_DEFAULT, Constants.EnemyConstants.Viking.HEIGHT_DEFAULT);

        fireWormArray = new BufferedImage[5][16];
        loadBufferArray(fireWormArray, LoadSave.FIRE_WORM_SPRITESHEET, Constants.EnemyConstants.FireWorm.WIDTH_DEFAULT, Constants.EnemyConstants.FireWorm.HEIGHT_DEFAULT);

        fireWizardArray = new BufferedImage[5][11];
        loadBufferArray(fireWizardArray, LoadSave.FIRE_WIZARD_SPRITESHEET, Constants.EnemyConstants.FireWizard.WIDTH_DEFAULT, Constants.EnemyConstants.FireWizard.HEIGHT_DEFAULT);

        minotaurArray = new BufferedImage[7][12];
        loadBufferArray(minotaurArray, LoadSave.MINOTAUR_SPRITESHEET, Constants.EnemyConstants.Minotaur.WIDTH_DEFAULT, Constants.EnemyConstants.Minotaur.HEIGHT_DEFAULT);

        fireKnigtArray = new BufferedImage[10][18];
        loadBufferArray(fireKnigtArray, LoadSave.FIRE_KNIGHT_SPRITESHEET, Constants.EnemyConstants.FireKnight.WIDTH_DEFAULT, Constants.EnemyConstants.FireKnight.HEIGHT_DEFAULT);

        finalBossArray = new BufferedImage[7][8];
        loadBufferArray(finalBossArray, LoadSave.FINAL_BOSS_SPRITESHEET, Constants.EnemyConstants.FinalBoss.WIDTH_DEFAULT, Constants.EnemyConstants.FinalBoss.HEIGHT_DEFAULT);
    }

    private void loadBufferArray(BufferedImage[][] enemyArray, String enemySpriteSheet, int enemyWidth, int enemyHeight)
    {
        BufferedImage temp = LoadSave.GetSpriteAtlas(enemySpriteSheet);

        for(int i = 0; i < enemyArray.length; ++i)
        {
            for(int j = 0; j < enemyArray[i].length; ++j)
            {
                try
                {
                    enemyArray[i][j] = temp.getSubimage(j * enemyWidth, i * enemyHeight, enemyWidth, enemyHeight);
                }
                catch(RasterFormatException e)
                {
                    CustomLogger.logException("Nu exista zona asta in incarcarea oponentului", e);
                }
            }
        }
    }
}
