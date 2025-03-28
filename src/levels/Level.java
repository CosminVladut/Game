package levels;

import entities.Demon;
import entities.FinalBoss;
import entities.FireKnight;
import entities.FireWizard;
import entities.FireWorm;
import entities.FlyingDemon;
import entities.Huntress;
import entities.IceBorne;
import entities.IceGolem;
import entities.LeafRanger;
import entities.Minotaur;
import entities.Necromancer;
import entities.ShardsoulSlayer;
import entities.SkeletonMacer;
import entities.SnowKnight;
import entities.Viking;
import entities.Werewolf;
import objects.*;
import utils.HelperMethods;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static main.Game.TILES_IN_WIDTH;
import static main.Game.TILES_SIZE;
import static utils.Constants.EnemyConstants.*;
import static utils.HelperMethods.*;


public final class Level
{
    private final BufferedImage image;
    private int[][] levelData;
    private ArrayList<SkeletonMacer> skeletonMacers;
    private ArrayList<ShardsoulSlayer> shardsoulSlayers;
    private ArrayList<FlyingDemon> flyingDemons;
    private ArrayList<Huntress> huntresses;
    private ArrayList<Necromancer> necromancers;
    private ArrayList<LeafRanger> leafRangers;
    private ArrayList<Demon> demons;
    private ArrayList<FireWorm> fireWorms;
    private ArrayList<FireWizard> fireWizards;
    private ArrayList<Minotaur> minotaurs;
    private ArrayList<FireKnight> fireKnights;
    private ArrayList<IceGolem> iceGolems;
    private ArrayList<IceBorne> iceBornes;
    private ArrayList<SnowKnight> snowKnights;
    private ArrayList<Werewolf> werewolves;
    private ArrayList<Viking> vikings;
    private ArrayList<FinalBoss> finalBosses;
    private ArrayList<Spike> spikes;
    private ArrayList<Fire> fires;
    private ArrayList<Cannon> cannons;
    private ArrayList<SaveSpot> saveSpots;
    private ArrayList<GameObject> decorations;
    private ArrayList<Barrier> barriers;
    private ArrayList<TouchBox> touchBoxes;
    private King king;
    private Door door;
    private Chest chest;
    private int levelTilesWide;
    private int maxTilesOffset;
    private int maxLevelOffsetX;
    private Point playerSpawn;

    public Level(BufferedImage image)
    {
        this.image = image;
        createLevelData();
        createEnemies();
        createChest();
        createSpikes();
        createFires();
        createKing();
        createDoor();
        createSaveSpots();
        createCannons();
        createDecorations();
        createBarriers();
        createTouchBox();
        calculateLevelOffsets();
        calculatePlayerSpawn();
    }

    private void createCannons() { cannons = HelperMethods.GetCannons(image); }

    private void createSpikes() { spikes = HelperMethods.GetSpikes(image); }

    private void createFires() { fires = HelperMethods.GetFires(image); }

    private void createKing() { king = HelperMethods.GetKing(image); }

    private void createDoor() { door = HelperMethods.GetDoor(image); }

    private void createSaveSpots() { saveSpots = HelperMethods.GetSaveSpots(image); }

    private void createChest() { chest = HelperMethods.GetContainerSpawn(image); }

    private void createTouchBox() { touchBoxes = HelperMethods.GetTouchBoxes(image); }

    private void createDecorations() { decorations = HelperMethods.GetDecorations(image); }

    private void createBarriers() { barriers = HelperMethods.GetBarriers(image); }

    private void calculatePlayerSpawn() { playerSpawn = GetPlayerSpawn(image); }

    private void createLevelData() { levelData = GetLevelData(image); }

    private void createEnemies()
    {
        skeletonMacers = GetEnemy(image, SKELETON_MACER, SkeletonMacer.class);
        shardsoulSlayers = GetEnemy(image, SHARDSOUL_SLAYER, ShardsoulSlayer.class);
        flyingDemons = GetEnemy(image, FLYING_DEMON, FlyingDemon.class);
        huntresses = GetEnemy(image, HUNTRESS, Huntress.class);
        necromancers = GetEnemy(image, NECROMANCER, Necromancer.class);
        leafRangers = GetEnemy(image, LEAF_RANGER, LeafRanger.class);
        demons = GetEnemy(image, DEMON, Demon.class);
        fireWorms = GetEnemy(image, FIRE_WORM, FireWorm.class);
        fireWizards = GetEnemy(image, FIRE_WIZARD, FireWizard.class);
        minotaurs = GetEnemy(image, MINOTAUR, Minotaur.class);
        fireKnights = GetEnemy(image, FIRE_KNIGHT, FireKnight.class);
        iceGolems = GetEnemy(image, ICE_GOLEM, IceGolem.class);
        iceBornes = GetEnemy(image, ICE_BORNE, IceBorne.class);
        snowKnights = GetEnemy(image, SNOW_KNIGHT, SnowKnight.class);
        werewolves = GetEnemy(image, WEREWOLF, Werewolf.class);
        vikings = GetEnemy(image, VIKING, Viking.class);
        finalBosses = GetEnemy(image, FINAL_BOSS, FinalBoss.class);
    }

    private void calculateLevelOffsets()
    {
        levelTilesWide = image.getWidth();
        maxTilesOffset = levelTilesWide - TILES_IN_WIDTH;
        maxLevelOffsetX = TILES_SIZE * maxTilesOffset;
    }

    public int getSpriteIndex(int x, int y) { return levelData[y][x]; }

    public int[][] getLevelData() { return levelData; }

    public int getMaxLevelOffsetX() { return maxLevelOffsetX; }

    public ArrayList<SkeletonMacer> getSkeletonMacers() { return skeletonMacers; }

    public ArrayList<ShardsoulSlayer> getShardSoulSlayers() { return shardsoulSlayers; }

    public ArrayList<FlyingDemon> getFlyingDemons() { return flyingDemons; }

    public ArrayList<Huntress> getHuntresses() { return huntresses; }

    public ArrayList<Necromancer> getNecromancers() { return necromancers; }

    public ArrayList<LeafRanger> getLeafRangers() { return leafRangers; }

    public ArrayList<Demon> getDemons() { return demons; }

    public ArrayList<FireWorm> getFireWorms() { return fireWorms; }

    public ArrayList<FireWizard> getFireWizards() { return fireWizards; }

    public ArrayList<Minotaur> getMinotaurs() { return minotaurs; }

    public ArrayList<FireKnight> getFireKnights() { return fireKnights; }

    public ArrayList<IceGolem> getIceGolems() { return iceGolems; }

    public ArrayList<IceBorne> getIceBornes() { return iceBornes; }

    public ArrayList<SnowKnight> getSnowKnights() { return snowKnights; }

    public ArrayList<Werewolf> getWerewolves() { return werewolves; }

    public ArrayList<Viking> getVikings() { return vikings; }

    public ArrayList<FinalBoss> getFinalBosses() { return finalBosses; }

    public ArrayList<SaveSpot> getSaveSpots() { return saveSpots; }

    public King getKing() { return king; }

    public Door getDoor() { return door; }

    public Point getPlayerSpawn() { return playerSpawn; }

    public Chest getChest() { return chest; }

    public ArrayList<TouchBox> getTouchBoxes( ) { return touchBoxes; }

    public ArrayList<Spike> getSpikes() { return spikes; }

    public ArrayList<Fire> getFires() { return fires; }

    public ArrayList<Cannon> getCannons() { return cannons; }

    public ArrayList<GameObject> getDecorations(){ return decorations; }

    public ArrayList<Barrier> getBarriers(){ return barriers; }
}
