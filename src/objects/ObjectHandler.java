package objects;

import audio.AudioPlayer;
import entities.Player;
import exceptions.UnexpectedChestTypeException;
import gamestates.Playing;
import levels.Level;
import utils.CustomLogger;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static main.Game.SCALE;
import static main.Game.TILES_SIZE;
import static utils.Constants.ObjectConstants.*;
import static utils.Constants.PlayerConstants.*;
import static utils.Constants.Projectiles.*;
import static utils.HelperMethods.CanCannonSeePlayer;
import static utils.HelperMethods.IsProjectileHittingLevel;

public final class ObjectHandler
{
    private static ObjectHandler instance = null;

    private final Playing playing;
    private final ArrayList<Projectile> projectiles = new ArrayList<>();
    private BufferedImage[][] chestImages;
    private Chest chest;
    private ArrayList<Spike> spikes;
    private ArrayList<Fire> fires;
    private ArrayList<SaveSpot> saveSpots;
    private ArrayList<GameObject> decorations;
    private ArrayList<Barrier> barriers;
    private ArrayList<TouchBox> touchBoxes;
    private King king;
    private Door door;
    private int fireCooldown = 60;
    private BufferedImage spikeImage, fireBallImage, fireWaveImage, verticalFireImage, windVerticalWaveImage,
            arrowImage, interactButtonImage, bookshelf1Image, bookshelf2Image, tableImage, bag1Image, bag2Image, barrelImage,
            crate1Image, crate2Image, crate3Image, redFlagImage, weaponRack1Image, weaponRack2Image, curtainsImage, doorImage,
            barrierImage;
    private ArrayList<Cannon> cannons;
    private BufferedImage[] cannonImages, forestMagicFireImages, hellfireImages, deadlyMagicFireImages, iceBallImages,
            spearImages, iceExplosionImages, iceTornadoImages, magmaBallImages, horizontalFireImages, windProjectileImages,
            windHorizontalWaveImages, fireSpiralImages, greenRayImages, rootsImages, frozenFlameImages, iceSpiralImages,
            explosionImages, icePillarImages, frozenShockwaveImages, saveSpotImages, kingImages, candelabrumImages, candleImages,
            torchBigImages, torchSmallImages;
    private boolean talkedOnce = false;
    private int zoneToTest = 0;
    private int countdown = 120;

    private ObjectHandler(Playing playing)
    {
        this.playing = playing;
        loadImages();
    }

    public static ObjectHandler createObjectHandler(Playing playing)
    {
        if(instance == null)
        {
            instance = new ObjectHandler(playing);
        }
        return instance;
    }

    public void checkSpikesTouched(Player player)
    {
        for(Spike s : spikes)
        {
            if(s.hitBox.intersects(player.getHitBox()))
            {
                player.kill();
            }
        }
    }

    public void checkFiresTouched(Player player)
    {
        for(Fire f : fires)
        {
            if(f.hitBox.intersects(player.getHitBox()))
            {
                if(fireCooldown == 60)
                {
                    player.changeHealth(f.getObjectType() == FOREST_MAGIC_FIRE ? -5 : -10, false, false, 0);
                    playing.getGame().getAudioPlayer().playEffect(AudioPlayer.BURN);
                    --fireCooldown;
                }
                else
                {
                    fireCooldown = fireCooldown == 0 ? 60 : fireCooldown - 1;
                }
            }
        }
    }

    public void checkObjectNear(Rectangle2D.Double hitBox)
    {
        if(chest != null && chest.isActive())
        {
            if(chest.hitBox.intersects(hitBox))
            {
                chest.setShouldDrawInteractButton(chest.isInteractable());
                if(playing.getPlayer().pressedInteract() && chest.isInteractable())
                {
                    chest.setAnimation(true);
                    playing.getGame().getAudioPlayer().playEffect(AudioPlayer.CHEST_OPEN);
                    chest.setInteractable(false);
                }
            }
            else
            {
                chest.setShouldDrawInteractButton(false);
            }
        }

        if(king != null && king.isActive())
        {
            if(king.hitBox.intersects(hitBox))
            {
                if(!playing.getPlayer().isTalking() && !playing.getPlayer().isCountStarted())
                {
                    king.setShouldDrawInteractButton(true);
                    if(playing.getPlayer().pressedInteract())
                    {
                        king.setShouldDrawInteractButton(false);
                        playing.getPlayer().setTalking(true);
                        playing.setTalking(true);
                    }
                }
                if(playing.getPlayer().isCountStarted())
                {
                    playing.getPlayer().setTalking(false);
                }
            }
            else
            {
                king.setShouldDrawInteractButton(false);
            }
        }

        if(door != null && door.isActive())
        {
            if(door.hitBox.intersects(hitBox))
            {
                if(talkedOnce && (playing.getLevelManager().getLevelIndex() != playing.getLevelManager().getHowManyLevels() - 1 || playing.getEnemyHandler().areEnemiesDead()))
                {
                    door.setShouldDrawInteractButton(true);
                    if(playing.getPlayer().pressedInteract())
                    {
                        playing.setTalking(true);
                        playing.getPlayer().setTalking(true);
                        if(playing.getLevelManager().getLevelIndex() == playing.getLevelManager().getHowManyLevels() - 1)
                        {
                            playing.setFinalLevel(true);
                        }
                        else
                        {
                            playing.setLevelCompleted(true);
                        }
                    }
                }
            }
            else
            {
                door.setShouldDrawInteractButton(false);
            }
        }

        for(SaveSpot s : saveSpots)
        {
            if(s.isActive())
            {
                if(s.hitBox.intersects(hitBox))
                {
                    if(playing.getPlayer().isPraying())
                    {
                        s.setShouldDrawInteractButton(false);
                    }
                    if(!playing.getPlayer().isPraying() && (playing.getPlayer().getState() == IDLE || playing.getPlayer().getState() == RUN || playing.getPlayer().getState() == CROUCH_IDLE))
                    {
                        s.setShouldDrawInteractButton(true);
                        if(playing.getPlayer().pressedInteract())
                        {
                            playing.getGame().getAudioPlayer().playEffect(AudioPlayer.PRAY);
                            playing.getPlayer().setPraying(true);
                            playing.getPlayer().setXOfSaveSpot(s.hitBox.x);
                        }
                    }
                }
                else
                {
                    s.setShouldDrawInteractButton(false);
                }
            }
        }
    }

    public void checkTouchBoxTouched(Rectangle2D.Double hitBox)
    {
        for(TouchBox touchBox : touchBoxes)
        {
            if(touchBox.isActive())
            {
                if(touchBox.hitBox.intersects(hitBox))
                {
                    if(!touchBox.isForBarrier())
                    {
                        if(!talkedOnce)
                        {
                            playing.getPlayer().setTalking(true);
                            playing.setTalking(true);
                        }
                        if(playing.getLevelManager().getLevelIndex() != playing.getLevelManager().getHowManyLevels() - 1)
                        {
                            try
                            {
                                barriers.getFirst().setActive(true);
                            }
                            catch(NoSuchElementException e)
                            {
                                CustomLogger.logException("Index invalid pentru bariere.", e);
                            }
                        }
                    }
                    else
                    {
                        try
                        {
                            barriers.get(0).setActive(true);
                            barriers.get(1).setActive(true);
                        }
                        catch(IndexOutOfBoundsException e)
                        {
                            CustomLogger.logException("Index invalid pentru bariere.", e);
                        }
                    }
                    if(!playing.getPlayer().isTalking())
                    {
                        touchBox.setActive(false);
                    }
                }
            }
        }
    }

    public void loadObject(Level newLevel)
    {
        chest = newLevel.getChest();
        king = newLevel.getKing();
        saveSpots = newLevel.getSaveSpots();
        spikes = newLevel.getSpikes();
        fires = newLevel.getFires();
        cannons = newLevel.getCannons();
        decorations = newLevel.getDecorations();
        door = newLevel.getDoor();
        touchBoxes = newLevel.getTouchBoxes();
        barriers = newLevel.getBarriers();
        playing.getPlayer().setBarriers(barriers);
        zoneToTest = barriers.size() / 2;
        playing.getEnemyHandler().setZone(zoneToTest);
        try
        {
            for(int i = 0; i < barriers.size(); i = i + 2)
            {
                playing.getEnemyHandler().setZoneEnemy(barriers.get(i).hitBox.x + barriers.get(i).hitBox.width, barriers.get(i + 1).hitBox.x);
                barriers.get(i).setActive(false);
                if(i + 1 != barriers.size() - 1)
                {
                    barriers.get(i + 1).setActive(false);
                }
            }
        }
        catch(IndexOutOfBoundsException e)
        {
            CustomLogger.logException("Index invalid pentru bariere.", e);
        }
        projectiles.clear();
    }

    private void loadImages( )
    {
        interactButtonImage = LoadSave.GetSpriteAtlas(LoadSave.E_SPRITESHEET);


        BufferedImage chestSprite = LoadSave.GetSpriteAtlas(LoadSave.CHESTS_SPRITESHEET);

        chestImages = new BufferedImage[3][10];

        for(int i = 0; i < chestImages.length; ++i)
        {
            for(int j = 0; j < chestImages[i].length; ++j)
            {
                try
                {
                    chestImages[i][j] = chestSprite.getSubimage(j * CHEST_WIDTH_DEFAULT, i * CHEST_HEIGHT_DEFAULT, CHEST_WIDTH_DEFAULT, CHEST_HEIGHT_DEFAULT);
                }
                catch(RasterFormatException e)
                {
                    CustomLogger.logException("Nu exista partea astea din sprite-ul cufarului.", e);
                }
            }
        }


        spikeImage = LoadSave.GetSpriteAtlas(LoadSave.SPIKE_SPRITESHEET);


        BufferedImage forestMagicFireImage = LoadSave.GetSpriteAtlas(LoadSave.FOREST_MAGIC_FIRE);

        forestMagicFireImages = loadSpriteSheet(forestMagicFireImage, forestMagicFireImage.getWidth() / FOREST_MAGIC_FIRE_WIDTH_DEFAULT, FOREST_MAGIC_FIRE_WIDTH_DEFAULT, FOREST_MAGIC_FIRE_HEIGHT_DEFAULT);


        BufferedImage hellfireImage = LoadSave.GetSpriteAtlas(LoadSave.HELLFIRE);

        hellfireImages = loadSpriteSheet(hellfireImage, hellfireImage.getWidth() / HELLFIRE_WIDTH_DEFAULT, HELLFIRE_WIDTH_DEFAULT, HELLFIRE_HEIGHT_DEFAULT);


        BufferedImage deadlyMagicFireImage = LoadSave.GetSpriteAtlas(LoadSave.DEADLY_MAGIC_FIRE);

        deadlyMagicFireImages = loadSpriteSheet(deadlyMagicFireImage, deadlyMagicFireImage.getWidth() / DEADLY_MAGIC_FIRE_WIDTH_DEFAULT, DEADLY_MAGIC_FIRE_WIDTH_DEFAULT, DEADLY_MAGIC_FIRE_HEIGHT_DEFAULT);


        BufferedImage kingImage = LoadSave.GetSpriteAtlas(LoadSave.KING_SPRITESHEET);

        kingImages = loadSpriteSheet(kingImage, kingImage.getWidth() / KING_WIDTH_DEFAULT, KING_WIDTH_DEFAULT, KING_HEIGHT_DEFAULT);


        BufferedImage saveSpotImage = LoadSave.GetSpriteAtlas(LoadSave.RED_MOON_TOWER_SPRITESHEET);

        saveSpotImages = loadSpriteSheet(saveSpotImage, saveSpotImage.getWidth() / SAVE_SPOT_WIDTH_DEFAULT, SAVE_SPOT_WIDTH_DEFAULT, SAVE_SPOT_HEIGHT_DEFAULT);


        BufferedImage cannonSprite = LoadSave.GetSpriteAtlas(LoadSave.CANNON_SPRITESHEET);

        cannonImages = loadSpriteSheet(cannonSprite, cannonSprite.getWidth() / CANNON_WIDTH_DEFAULT, CANNON_WIDTH_DEFAULT, CANNON_HEIGHT_DEFAULT);


        BufferedImage iceBallSprite = LoadSave.GetSpriteAtlas(LoadSave.ICE_PROJECTILE_SPRITESHEET);

        iceBallImages = loadSpriteSheet(iceBallSprite, iceBallSprite.getWidth() / ICE_BALL_WIDTH_DEFAULT, ICE_BALL_WIDTH_DEFAULT, ICE_BALL_HEIGHT_DEFAULT);


        fireBallImage = LoadSave.GetSpriteAtlas(LoadSave.FIRE_PROJECTILE_SPRITESHEET);


        BufferedImage spearSprite = LoadSave.GetSpriteAtlas(LoadSave.SPEAR_SPRITESHEET);

        spearImages = loadSpriteSheet(spearSprite, spearSprite.getWidth() / SPEAR_WIDTH_DEFAULT, SPEAR_WIDTH_DEFAULT, SPEAR_HEIGHT_DEFAULT);


        fireWaveImage = LoadSave.GetSpriteAtlas(LoadSave.FIRE_WAVE_SPRITESHEET);


        BufferedImage iceExplosionSprite = LoadSave.GetSpriteAtlas(LoadSave.ICE_EXPLOSION_SPRITESHEET);

        iceExplosionImages = loadSpriteSheet(iceExplosionSprite, iceExplosionSprite.getWidth() / ICE_EXPLOSION_WIDTH_DEFAULT, ICE_EXPLOSION_WIDTH_DEFAULT, ICE_EXPLOSION_HEIGHT_DEFAULT);


        BufferedImage iceTornadoSprite = LoadSave.GetSpriteAtlas(LoadSave.ICE_TORNADO_SPRITESHEET);

        iceTornadoImages = loadSpriteSheet(iceTornadoSprite, iceTornadoSprite.getWidth() / ICE_TORNADO_WIDTH_DEFAULT, ICE_TORNADO_WIDTH_DEFAULT, ICE_TORNADO_HEIGHT_DEFAULT);


        BufferedImage magmaBallSprite = LoadSave.GetSpriteAtlas(LoadSave.MAGMA_BALL_SPRITESHEET);

        magmaBallImages = loadSpriteSheet(magmaBallSprite, magmaBallSprite.getWidth() / MAGMA_BALL_WIDTH_DEFAULT, MAGMA_BALL_WIDTH_DEFAULT, MAGMA_BALL_HEIGHT_DEFAULT);


        verticalFireImage = LoadSave.GetSpriteAtlas(LoadSave.VERTICAL_FIRE_SPRITESHEET);


        BufferedImage horizontalFireImage = LoadSave.GetSpriteAtlas(LoadSave.HORIZONTAL_FIRE_SPRITESHEET);

        horizontalFireImages = loadSpriteSheet(horizontalFireImage, horizontalFireImage.getWidth() / HORIZONTAL_FIRE_WIDTH_DEFAULT, HORIZONTAL_FIRE_WIDTH_DEFAULT, HORIZONTAL_FIRE_HEIGHT_DEFAULT);


        windVerticalWaveImage = LoadSave.GetSpriteAtlas(LoadSave.WIND_VERTICAL_WAVE_SPRITESHEET);


        BufferedImage windProjectileImage = LoadSave.GetSpriteAtlas(LoadSave.WIND_PROJECTILE_SPRITESHEET);

        windProjectileImages = loadSpriteSheet(windProjectileImage, windProjectileImage.getWidth() / WIND_PROJECTILE_WIDTH_DEFAULT, WIND_PROJECTILE_WIDTH_DEFAULT, WIND_PROJECTILE_HEIGHT_DEFAULT);


        BufferedImage windHorizontalWaveImage = LoadSave.GetSpriteAtlas(LoadSave.WIND_HORIZONTAL_WAVE_SPRITESHEET);

        windHorizontalWaveImages = loadSpriteSheet(windHorizontalWaveImage, windHorizontalWaveImage.getWidth() / WIND_HORIZONTAL_WAVE_WIDTH_DEFAULT, WIND_HORIZONTAL_WAVE_WIDTH_DEFAULT, WIND_HORIZONTAL_WAVE_HEIGHT_DEFAULT);


        BufferedImage fireSpiralImage = LoadSave.GetSpriteAtlas(LoadSave.FIRE_SPIRAL_SPRITESHEET);

        fireSpiralImages = loadSpriteSheet(fireSpiralImage, fireSpiralImage.getWidth() / FIRE_SPIRAL_WIDTH_DEFAULT, FIRE_SPIRAL_WIDTH_DEFAULT, FIRE_SPIRAL_HEIGHT_DEFAULT);


        arrowImage = LoadSave.GetSpriteAtlas(LoadSave.ARROW_SPRITESHEET);


        BufferedImage greenRayImage = LoadSave.GetSpriteAtlas(LoadSave.GREEN_RAY_SPRITESHEET);

        greenRayImages = loadSpriteSheet(greenRayImage, greenRayImage.getWidth() / GREEN_RAY_WIDTH_DEFAULT, GREEN_RAY_WIDTH_DEFAULT, GREEN_RAY_HEIGHT_DEFAULT);


        BufferedImage rootsImage = LoadSave.GetSpriteAtlas(LoadSave.ROOTS_SPRITESHEET);

        rootsImages = loadSpriteSheet(rootsImage, rootsImage.getWidth() / ROOTS_WIDTH_DEFAULT, ROOTS_WIDTH_DEFAULT, ROOTS_HEIGHT_DEFAULT);


        BufferedImage icePillarImage = LoadSave.GetSpriteAtlas(LoadSave.ICE_PILLAR_SPRITESHEET);

        icePillarImages = loadSpriteSheet(icePillarImage, icePillarImage.getWidth() / ICE_PILLAR_WIDTH_DEFAULT, ICE_PILLAR_WIDTH_DEFAULT, ICE_PILLAR_HEIGHT_DEFAULT);


        BufferedImage frozenShockwaveImage = LoadSave.GetSpriteAtlas(LoadSave.FROZEN_SHOCKWAVE_SPRITESHEET);

        frozenShockwaveImages = loadSpriteSheet(frozenShockwaveImage, frozenShockwaveImage.getWidth() / FROZEN_SHOCKWAVE_WIDTH_DEFAULT, FROZEN_SHOCKWAVE_WIDTH_DEFAULT, FROZEN_SHOCKWAVE_HEIGHT_DEFAULT);


        BufferedImage frozenFlameImage = LoadSave.GetSpriteAtlas(LoadSave.FROZEN_FLAME_SPRITESHEET);

        frozenFlameImages = loadSpriteSheet(frozenFlameImage, frozenFlameImage.getWidth() / FROZEN_FLAME_WIDTH_DEFAULT, FROZEN_FLAME_WIDTH_DEFAULT, FROZEN_FLAME_HEIGHT_DEFAULT);


        BufferedImage iceSpiralImage = LoadSave.GetSpriteAtlas(LoadSave.ICE_SPIRAL_SPRITESHEET);

        iceSpiralImages = loadSpriteSheet(iceSpiralImage, iceSpiralImage.getWidth() / ICE_SPIRAL_WIDTH_DEFAULT, ICE_SPIRAL_WIDTH_DEFAULT, ICE_SPIRAL_HEIGHT_DEFAULT);


        BufferedImage explosionImage = LoadSave.GetSpriteAtlas(LoadSave.EXPLOSION_SPRITESHEET);

        explosionImages = loadSpriteSheet(explosionImage, explosionImage.getWidth() / EXPLOSION_WIDTH_DEFAULT, EXPLOSION_WIDTH_DEFAULT, EXPLOSION_HEIGHT_DEFAULT);


        bookshelf1Image = LoadSave.GetSpriteAtlas(LoadSave.BOOKSHELF1_SPRITESHEET);


        bookshelf2Image = LoadSave.GetSpriteAtlas(LoadSave.BOOKSHELF2_SPRITESHEET);


        tableImage = LoadSave.GetSpriteAtlas(LoadSave.TABLE_SPRITESHEET);


        bag1Image = LoadSave.GetSpriteAtlas(LoadSave.BAG1_SPRITESHEET);


        bag2Image = LoadSave.GetSpriteAtlas(LoadSave.BAG2_SPRITESHEET);


        barrelImage = LoadSave.GetSpriteAtlas(LoadSave.BARREL_SPRITESHEET);


        crate1Image = LoadSave.GetSpriteAtlas(LoadSave.CRATE1_SPRITESHEET);


        crate2Image = LoadSave.GetSpriteAtlas(LoadSave.CRATE2_SPRITESHEET);


        crate3Image = LoadSave.GetSpriteAtlas(LoadSave.CRATE3_SPRITESHEET);


        redFlagImage = LoadSave.GetSpriteAtlas(LoadSave.RED_FLAG_SPRITESHEET);


        weaponRack1Image = LoadSave.GetSpriteAtlas(LoadSave.WEAPON_RACK1_SPRITESHEET);


        weaponRack2Image = LoadSave.GetSpriteAtlas(LoadSave.WEAPON_RACK2_SPRITESHEET);


        curtainsImage = LoadSave.GetSpriteAtlas(LoadSave.CURTAINS_SPRITESHEET);


        BufferedImage candelabrumImage = LoadSave.GetSpriteAtlas(LoadSave.CANDELABRUM_SPRITESHEET);

        candelabrumImages = loadSpriteSheet(candelabrumImage, candelabrumImage.getWidth() / CANDELABRUM_WIDTH_DEFAULT, CANDELABRUM_WIDTH_DEFAULT, CANDELABRUM_HEIGHT_DEFAULT);


        BufferedImage candleImage = LoadSave.GetSpriteAtlas(LoadSave.CANDLE_SPRITESHEET);

        candleImages = loadSpriteSheet(candleImage, candleImage.getWidth() / CANDLE_WIDTH_DEFAULT, CANDLE_WIDTH_DEFAULT, CANDLE_HEIGHT_DEFAULT);


        BufferedImage torchBigImage = LoadSave.GetSpriteAtlas(LoadSave.TORCH_BIG_SPRITESHEET);

        torchBigImages = loadSpriteSheet(torchBigImage, torchBigImage.getWidth() / TORCH_BIG_WIDTH_DEFAULT, TORCH_BIG_WIDTH_DEFAULT, TORCH_BIG_HEIGHT_DEFAULT);


        BufferedImage torchSmallImage = LoadSave.GetSpriteAtlas(LoadSave.TORCH_SMALL_SPRITESHEET);

        torchSmallImages = loadSpriteSheet(torchSmallImage, torchSmallImage.getWidth() / TORCH_SMALL_WIDTH_DEFAULT, TORCH_SMALL_WIDTH_DEFAULT, TORCH_SMALL_HEIGHT_DEFAULT);


        doorImage = LoadSave.GetSpriteAtlas(LoadSave.DOOR_SPRITESHEET);


        barrierImage = LoadSave.GetSpriteAtlas(LoadSave.BARRIER_SPRITESHEET);
    }

    private BufferedImage[] loadSpriteSheet(BufferedImage image, int size, int widthDefault, int heightDefault)
    {
        BufferedImage[] images = new BufferedImage[size];

        for(int i = 0; i < images.length; ++i)
        {
            try
            {
                images[i] = image.getSubimage(i * widthDefault, 0, widthDefault, heightDefault);
            }
            catch(RasterFormatException e)
            {
                CustomLogger.logException("Nu exista partea astea din sprite-ul proiectilului.", e);
            }
        }

        return images;
    }

    public void update(int[][] levelData, Player player)
    {
        for(Barrier barrier : barriers)
        {
            if(barrier.getLevelData() == null)
            {
                barrier.setLevelData(levelData);
            }
        }

        if(chest != null && chest.isActive())
        {
            chest.update();
        }

        if(chest != null && chest.getAnimationIndex() == 7 && chest.getAnimationTick() == 0)
        {
            playing.setInsigniaGot(true);
            playing.getGame().getAudioPlayer().playEffect(AudioPlayer.INSIGNIA_GOT);
        }

        if(king != null)
        {
            updateKing();
        }

        if(door != null)
        {
            updateDoor();
        }

        updateSpikes();
        updateBarriers();
        updateDecorations();
        updateSaveSpots();
        updateFires();
        updateCannons(levelData, player);
        updateProjectile(levelData, player);
    }

    private void updateSpikes()
    {
        for(Spike s : spikes)
        {
            s.updateAnimationTick();
        }
    }

    private void updateBarriers( )
    {
        if(playing.getEnemyHandler().isAllZoneEliminated(zoneToTest) && zoneToTest > 0)
        {
            --zoneToTest;
            try
            {
                barriers.removeFirst();
                barriers.removeFirst();
            }
            catch(NoSuchElementException e)
            {
                CustomLogger.logException("Nu mai exista bariere de scos.", e);
            }
            touchBoxes.removeFirst();
        }
    }

    private void updateDoor( )
    {
        door.update();
    }

    private void updateDecorations( )
    {
        for(GameObject g : decorations)
        {
            g.updateAnimationTick();
        }
    }

    private void updateFires( )
    {
        for(Fire f : fires)
        {
            f.updateAnimationTick();
        }
    }

    private void updateKing( )
    {
        king.update();
    }

    private void updateSaveSpots( )
    {
        for(SaveSpot s : saveSpots)
        {
            s.update();
        }
    }

    private void updateProjectile(int[][] levelData, Player player)
    {
        for(Iterator<Projectile> iterator = projectiles.iterator(); iterator.hasNext();)
        {
            Projectile p = null;

            try
            {
                p = iterator.next();
            }
            catch(NoSuchElementException e)
            {
                CustomLogger.logException("Proiectilul nu exista.", e);
            }

            p.update();

            if(p.getHitBox().intersects(player.getHitBox()) && !player.isDodging() ||
                    ((p.getProjectileType() == ICE_EXPLOSION || p.getProjectileType() == GREEN_RAY || p.getProjectileType() == ICE_PILLAR || p.getProjectileType() == FROZEN_SHOCKWAVE) && p.getAnimationIndex() == 3) ||
                    (p.getProjectileType() == ROOTS && p.getAnimationIndex() == 11) ||
                    (p.getProjectileType() == EXPLOSION && p.getAnimationIndex() == 17))
            {

                int healthValue = switch(p.getProjectileType())
                {
                    case FIRE_BALL, ICE_TORNADO, WIND_HORIZONTAL_WAVE, FIRE_SPIRAL, GREEN_RAY, ICE_BALL, ICE_PILLAR ->
                            -30;
                    case SPEAR, WIND_PROJECTILE, ROOTS, FROZEN_SHOCKWAVE, ICE_SPIRAL -> -25;
                    case FIRE_WAVE, VERTICAL_FIRE, HORIZONTAL_FIRE, WIND_VERTICAL_WAVE, FROZEN_FLAME -> -20;
                    case ICE_EXPLOSION, ARROW, MAGMA_BALL -> -15;
                    case EXPLOSION -> -10;
                    default -> 0;
                };

                player.changeHealth((p.getProjectileType() == ICE_EXPLOSION || p.getProjectileType() == GREEN_RAY ||
                                            p.getProjectileType() == ROOTS || p.getProjectileType() == ICE_PILLAR ||
                                            p.getProjectileType() == FROZEN_SHOCKWAVE || p.getProjectileType() == FROZEN_FLAME ||
                                            p.getProjectileType() == EXPLOSION)
                                            && !p.getHitBox().intersects(player.getHitBox()) ? 0 :
                                    ((p.getProjectileType() == ROOTS && (p.getAnimationIndex() < 3 || p.getAnimationIndex() > 5) ||
                                            (p.getProjectileType() == GREEN_RAY && p.getAnimationIndex() != 0) ||
                                            (p.getProjectileType() == EXPLOSION && (p.getAnimationIndex() < 8 || p.getAnimationIndex() > 10)) ||
                                            (p.getProjectileType() == FROZEN_FLAME && p.getAnimationIndex() != 0)) ? 0 : healthValue),
                                    p.getHitBox().x > player.getHitBox().x, true, 3);

                if((p.getProjectileType() == GREEN_RAY || p.getProjectileType() == ROOTS || p.getProjectileType() == FROZEN_FLAME || p.getProjectileType() == EXPLOSION) && p.getHitBox().intersects(player.getHitBox()))
                {
                    if(((p.getProjectileType() == GREEN_RAY || p.getProjectileType() == FROZEN_FLAME) && p.getAnimationIndex() == 3) ||
                            (p.getProjectileType() == ROOTS && p.getAnimationIndex() == 11) ||
                            (p.getProjectileType() == EXPLOSION && p.getAnimationIndex() == 17))
                    {
                        try
                        {
                            iterator.remove();
                        }
                        catch(UnsupportedOperationException ex)
                        {
                            CustomLogger.logException("Operatie nesuportata de sirul de proiectile.", ex);
                        }
                        catch(IllegalStateException ex)
                        {
                            CustomLogger.logException("Nu a fost mers prin sirul de proiectile exclusiv doar odata.", ex);
                        }
                    }
                }
                else
                {
                    try
                    {
                        iterator.remove();
                    }
                    catch(UnsupportedOperationException ex)
                    {
                        CustomLogger.logException("Operatie nesuportata de sirul de proiectile.", ex);
                    }
                    catch(IllegalStateException ex)
                    {
                        CustomLogger.logException("Nu a fost mers prin sirul de proiectile exclusiv doar odata.", ex);
                    }
                }
            }
            else
            {
                if(IsProjectileHittingLevel(p, levelData) && p.getProjectileType() != GREEN_RAY && p.getProjectileType() != ROOTS && p.getProjectileType() != EXPLOSION)
                {
                    try
                    {
                        iterator.remove();
                    }
                    catch(UnsupportedOperationException ex)
                    {
                        CustomLogger.logException("Operatie nesuportata de sirul de proiectile.", ex);
                    }
                    catch(IllegalStateException ex)
                    {
                        CustomLogger.logException("Nu a fost mers prin sirul de proiectile exclusiv doar odata.", ex);
                    }
                }
            }
        }
    }

    private boolean isPlayerInRange(Cannon cannon, Player player)
    {
        double absoluteValue = Math.abs(player.getHitBox().x - cannon.hitBox.x);
        return absoluteValue <= TILES_SIZE * 23;
    }

    private boolean isPlayerInFrontOfCannon(Cannon cannon, Player player)
    {
        if(cannon.getObjectType() == CANNON_LEFT)
        {
            return cannon.hitBox.x > player.getHitBox().x;
        }
        else
        {
            return cannon.hitBox.x < player.getHitBox().x;
        }
    }

    private void updateCannons(int[][] levelData, Player player)
    {
        for(Cannon c : cannons)
        {
            if(!c.doAnimation)
            {
                if(c.getTileY() == player.getTileY() + player.getTilesInHeight() - 1)
                {
                    if(isPlayerInRange(c, player))
                    {
                        if(isPlayerInFrontOfCannon(c, player))
                        {
                            if(CanCannonSeePlayer(levelData, player.getHitBox(), c.hitBox, 1, c.getTileY()))
                            {
                                --countdown;
                                if(countdown == 0)
                                {
                                    c.setAnimation(true);
                                    countdown = 120;
                                }
                            }
                        }
                    }
                }
            }
            c.update();
            if(c.getAnimationIndex() == 2 && c.getAnimationTick() == 0)
            {
                shootCannon(c);
            }
        }
    }

    private void shootCannon(Cannon c)
    {
        int direction = 1;
        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.BURN);
        if(c.getObjectType() == CANNON_LEFT)
        {
            direction = -1;
        }
        projectiles.add(new Projectile((int) (c.hitBox.x), (int) (c.hitBox.y + (int)(2 * SCALE)), direction, false, ICE_BALL, 2 * SCALE, ICE_BALL_WIDTH, ICE_BALL_HEIGHT));
    }

    public void draw(Graphics g, int xLevelOffset) throws UnexpectedChestTypeException
    {
        drawBarriers(g, xLevelOffset);
//        drawTouchBoxes(g, xLevelOffset);
        drawDecorations(g, xLevelOffset);
        drawTraps(g, xLevelOffset);
        drawInteractables(g, xLevelOffset);
        drawProjectiles(g, xLevelOffset);
        drawCannons(g, xLevelOffset);
    }

    private void drawBarriers(Graphics g, int xLevelOffset)
    {
        for(Barrier barrier : barriers)
        {
            if(barrier.isActive())
            {
                g.drawImage(barrierImage, (int) (barrier.hitBox.x - xLevelOffset - barrier.getXDrawOffset()), (int) (barrier.hitBox.y - barrier.getYDrawOffset()), BARRIER_WIDTH, (int) (barrier.hitBox.height), null);
            }
        }
    }

    private void drawTouchBoxes(Graphics g, int xLevelOffset)
    {
        for(TouchBox touchBox : touchBoxes)
        {
            if(touchBox.isActive())
            {
                touchBox.drawHitBox(g, xLevelOffset);
            }
        }
    }

    private void drawDecorations(Graphics g, int xLevelOffset)
    {
        for(GameObject gO : decorations)
        {
            switch(gO.getObjectType())
            {
                case RED_FLAG ->
                        drawSimpleDecorations(g, xLevelOffset, gO, redFlagImage, RED_FLAG_WIDTH, RED_FLAG_HEIGHT);

                case BOOKSHELF1 ->
                        drawSimpleDecorations(g, xLevelOffset, gO, bookshelf1Image, BOOKSHELF1_WIDTH, BOOKSHELF1_HEIGHT);

                case BOOKSHELF2 ->
                        drawSimpleDecorations(g, xLevelOffset, gO, bookshelf2Image, BOOKSHELF2_WIDTH, BOOKSHELF2_HEIGHT);

                case TABLE -> drawSimpleDecorations(g, xLevelOffset, gO, tableImage, TABLE_WIDTH, TABLE_HEIGHT);

                case BAG1 -> drawSimpleDecorations(g, xLevelOffset, gO, bag1Image, BAG1_WIDTH, BAG1_HEIGHT);

                case BAG2 -> drawSimpleDecorations(g, xLevelOffset, gO, bag2Image, BAG2_WIDTH, BAG2_HEIGHT);

                case BARREL -> drawSimpleDecorations(g, xLevelOffset, gO, barrelImage, BARREL_WIDTH, BARREL_HEIGHT);

                case CRATE1 -> drawSimpleDecorations(g, xLevelOffset, gO, crate1Image, CRATE1_WIDTH, CRATE1_HEIGHT);

                case CRATE2 -> drawSimpleDecorations(g, xLevelOffset, gO, crate2Image, CRATE2_WIDTH, CRATE2_HEIGHT);

                case CRATE3 -> drawSimpleDecorations(g, xLevelOffset, gO, crate3Image, CRATE3_WIDTH, CRATE3_HEIGHT);

                case WEAPON_RACK1 ->
                        drawSimpleDecorations(g, xLevelOffset, gO, weaponRack1Image, WEAPON_RACK1_WIDTH, WEAPON_RACK1_HEIGHT);

                case WEAPON_RACK2 ->
                        drawSimpleDecorations(g, xLevelOffset, gO, weaponRack2Image, WEAPON_RACK2_WIDTH, WEAPON_RACK2_HEIGHT);

                case CURTAINS ->
                        drawSimpleDecorations(g, xLevelOffset, gO, curtainsImage, CURTAINS_WIDTH, CURTAINS_HEIGHT);

                case CANDELABRUM ->
                        drawAnimatedDecorations(g, xLevelOffset, gO, candelabrumImages, CANDELABRUM_WIDTH, CANDELABRUM_HEIGHT);

                case CANDLE -> drawAnimatedDecorations(g, xLevelOffset, gO, candleImages, CANDLE_WIDTH, CANDLE_HEIGHT);

                case TORCH_BIG ->
                        drawAnimatedDecorations(g, xLevelOffset, gO, torchBigImages, TORCH_BIG_WIDTH, TORCH_BIG_HEIGHT);

                case TORCH_SMALL ->
                        drawAnimatedDecorations(g, xLevelOffset, gO, torchSmallImages, TORCH_SMALL_WIDTH, TORCH_SMALL_HEIGHT);
            }
        }
    }

    private void drawSimpleDecorations(Graphics g, int xLevelOffset, GameObject gO, BufferedImage image, int width, int height)
    {
        g.drawImage(image, (int) (gO.hitBox.x - gO.getXDrawOffset() - xLevelOffset), (int) (gO.hitBox.y - gO.getYDrawOffset()), width, height, null);
    }

    private void drawAnimatedDecorations(Graphics g, int xLevelOffset, GameObject gO, BufferedImage[] images, int width, int height)
    {
        g.drawImage(images[gO.getAnimationIndex()], (int) (gO.hitBox.x - gO.getXDrawOffset() - xLevelOffset), (int) (gO.hitBox.y - gO.getYDrawOffset()), width, height, null);
    }

    private void drawProjectiles(Graphics g, int xLevelOffset)
    {
        for(Projectile p : projectiles)
        {
            int drawDirection = -p.getDirection();
            switch(p.getProjectileType())
            {
                case ICE_BALL ->
                        drawAnimatedProjectiles(g, xLevelOffset, p, drawDirection, ICE_BALL_WIDTH, ICE_BALL_HEIGHT, iceBallImages);

                case FIRE_BALL ->
                        drawSimpleProjectile(g, xLevelOffset, p, drawDirection, FIRE_BALL_WIDTH, FIRE_BALL_HEIGHT, fireBallImage);

                case SPEAR ->
                        drawAnimatedProjectiles(g, xLevelOffset, p, drawDirection, SPEAR_WIDTH, SPEAR_HEIGHT, spearImages);

                case FIRE_WAVE ->
                        drawSimpleProjectile(g, xLevelOffset, p, drawDirection, FIRE_WAVE_WIDTH, FIRE_WAVE_HEIGHT, fireWaveImage);

                case ICE_EXPLOSION ->
                        drawAnimatedProjectiles(g, xLevelOffset, p, drawDirection, ICE_EXPLOSION_WIDTH, ICE_EXPLOSION_HEIGHT, iceExplosionImages);

                case ICE_TORNADO ->
                        drawAnimatedProjectiles(g, xLevelOffset, p, drawDirection, ICE_TORNADO_WIDTH, ICE_TORNADO_HEIGHT, iceTornadoImages);

                case MAGMA_BALL ->
                        drawAnimatedProjectiles(g, xLevelOffset, p, drawDirection, MAGMA_BALL_WIDTH, MAGMA_BALL_HEIGHT, magmaBallImages);

                case VERTICAL_FIRE ->
                        drawSimpleProjectile(g, xLevelOffset, p, drawDirection, VERTICAL_FIRE_WIDTH, VERTICAL_FIRE_HEIGHT, verticalFireImage);

                case HORIZONTAL_FIRE ->
                        drawAnimatedProjectiles(g, xLevelOffset, p, drawDirection, HORIZONTAL_FIRE_WIDTH, HORIZONTAL_FIRE_HEIGHT, horizontalFireImages);

                case WIND_VERTICAL_WAVE ->
                        drawSimpleProjectile(g, xLevelOffset, p, drawDirection, WIND_VERTICAL_WAVE_WIDTH, WIND_VERTICAL_WAVE_HEIGHT, windVerticalWaveImage);

                case WIND_HORIZONTAL_WAVE ->
                        drawAnimatedProjectiles(g, xLevelOffset, p, drawDirection, WIND_HORIZONTAL_WAVE_WIDTH, WIND_HORIZONTAL_WAVE_HEIGHT, windHorizontalWaveImages);

                case WIND_PROJECTILE ->
                        drawAnimatedProjectiles(g, xLevelOffset, p, drawDirection, WIND_PROJECTILE_WIDTH, WIND_PROJECTILE_HEIGHT, windProjectileImages);

                case FIRE_SPIRAL ->
                        drawAnimatedProjectiles(g, xLevelOffset, p, drawDirection, FIRE_SPIRAL_WIDTH, FIRE_SPIRAL_HEIGHT, fireSpiralImages);

                case ARROW ->
                        drawSimpleProjectile(g, xLevelOffset, p, drawDirection, ARROW_WIDTH, ARROW_HEIGHT, arrowImage);

                case GREEN_RAY ->
                        drawAnimatedProjectiles(g, xLevelOffset, p, drawDirection, GREEN_RAY_WIDTH, GREEN_RAY_HEIGHT, greenRayImages);

                case ROOTS ->
                        drawAnimatedProjectiles(g, xLevelOffset, p, drawDirection, ROOTS_WIDTH, ROOTS_HEIGHT, rootsImages);

                case EXPLOSION ->
                        drawAnimatedProjectiles(g, xLevelOffset, p, drawDirection, EXPLOSION_WIDTH, EXPLOSION_HEIGHT, explosionImages);

                case ICE_PILLAR ->
                        drawAnimatedProjectiles(g, xLevelOffset, p, drawDirection, ICE_PILLAR_WIDTH, ICE_PILLAR_HEIGHT, icePillarImages);

                case ICE_SPIRAL ->
                        drawAnimatedProjectiles(g, xLevelOffset, p, drawDirection, ICE_SPIRAL_WIDTH, ICE_SPIRAL_HEIGHT, iceSpiralImages);

                case FROZEN_FLAME ->
                        drawAnimatedProjectiles(g, xLevelOffset, p, drawDirection, FROZEN_FLAME_WIDTH, FROZEN_FLAME_HEIGHT, frozenFlameImages);

                case FROZEN_SHOCKWAVE ->
                        drawAnimatedProjectiles(g, xLevelOffset, p, drawDirection, FROZEN_SHOCKWAVE_WIDTH, FROZEN_SHOCKWAVE_HEIGHT, frozenShockwaveImages);
            }
        }
    }

    private void drawSimpleProjectile(Graphics g, int xLevelOffset, Projectile p, int drawDirection, int projectileWidth, int projectileHeight, BufferedImage projectileImage)
    {
        int positionAdjust = drawDirection == 1 ? 0 : projectileWidth;
        g.drawImage(projectileImage, (int) (p.getHitBox().x - xLevelOffset + positionAdjust), (int) (p.getHitBox().y), projectileWidth * drawDirection, projectileHeight, null);
//        g.setColor(Color.RED);
//        g.drawRect((int)p.getHitBox().x - xLevelOffset, (int)p.getHitBox().y, projectileWidth, projectileHeight);
    }

    private void drawAnimatedProjectiles(Graphics g, int xLevelOffset, Projectile p, int drawDirection, int projectileWidth, int projectileHeight, BufferedImage[] projectileImages)
    {
        int positionAdjust = drawDirection == 1 ? 0 : projectileWidth;
        g.drawImage(projectileImages[p.getAnimationIndex()], (int) (p.getHitBox().x - xLevelOffset + positionAdjust), (int) (p.getHitBox().y), projectileWidth * drawDirection, projectileHeight, null);
//        g.setColor(Color.RED);
//        g.drawRect((int)p.getHitBox().x - xLevelOffset, (int)p.getHitBox().y, projectileWidth, projectileHeight);
    }

    private void drawCannons(Graphics g, int xLevelOffset)
    {
        for(Cannon c : cannons)
        {
            int x = (int) (c.hitBox.x - xLevelOffset);
            int width = CANNON_WIDTH;

            if(c.getObjectType() == CANNON_RIGHT)
            {
                x += width;
                width *= -1;
            }
            g.drawImage(cannonImages[c.getAnimationIndex()], x, (int) (c.hitBox.y), width, CANNON_HEIGHT, null);
//            c.drawHitBox(g, xLevelOffset);
        }
    }

    private void drawTraps(Graphics g, int xLevelOffset)
    {
        for(Spike s : spikes)
        {
            g.drawImage(s.getObjectType() == SPIKE ? spikeImage : deadlyMagicFireImages[s.getAnimationIndex()], (int) (s.hitBox.x - xLevelOffset), (int) (s.hitBox.y - s.getYDrawOffset()), s.getObjectType() == SPIKE ? SPIKE_WIDTH : DEADLY_MAGIC_FIRE_WIDTH, s.getObjectType() == SPIKE ? SPIKE_HEIGHT : DEADLY_MAGIC_FIRE_HEIGHT, null);
//            s.drawHitBox(g, xLevelOffset);
        }

        for(Fire f : fires)
        {
            g.drawImage(f.getObjectType() == FOREST_MAGIC_FIRE ? forestMagicFireImages[f.getAnimationIndex()] : hellfireImages[f.getAnimationIndex()], (int) (f.hitBox.x - xLevelOffset), (int) (f.hitBox.y - f.getYDrawOffset()), f.getObjectType() == FOREST_MAGIC_FIRE ? FOREST_MAGIC_FIRE_WIDTH : HELLFIRE_WIDTH, f.getObjectType() == FOREST_MAGIC_FIRE ? FOREST_MAGIC_FIRE_HEIGHT : HELLFIRE_HEIGHT, null);
//            f.drawHitBox(g, xLevelOffset);
        }
    }

    private void drawInteractables(Graphics g, int xLevelOffset) throws UnexpectedChestTypeException
    {
        if(king != null)
        {
            g.drawImage(kingImages[king.getAnimationIndex()], (int) (king.hitBox.x - king.getXDrawOffset() - xLevelOffset), (int) (king.hitBox.y - king.getYDrawOffset()), KING_WIDTH, KING_HEIGHT, null);
            if(king.shouldDrawButton())
            {
                g.drawImage(interactButtonImage, (int) (king.hitBox.x + king.hitBox.width - (double) interactButtonImage.getWidth() / 1.5 * SCALE - xLevelOffset), (int) (king.hitBox.y - king.hitBox.y / 4), KEY_WIDTH, KEY_HEIGHT, null);
            }
//            king.drawHitBox(g, xLevelOffset);
        }

        if(chest != null)
        {
            int ChestType =
                    switch(chest.getObjectType())
                    {
                        case FOREST_CHEST -> FOREST_CHEST;
                        case FIRE_CHEST -> FIRE_CHEST;
                        case ICE_CHEST -> ICE_CHEST;
                        default ->
                        {
                            CustomLogger.logException("Tip de cufar neasteptat: " + chest.getObjectType(), new UnexpectedChestTypeException());
                            yield 0;
                        }
                    } - 1;

            g.drawImage(chestImages[ChestType][chest.getAnimationIndex()], (int) (chest.hitBox.x - chest.getXDrawOffset() - xLevelOffset), (int) (chest.hitBox.y - chest.getYDrawOffset()), CHEST_WIDTH, CHEST_HEIGHT, null);
            if(chest.shouldDrawButton())
            {
                g.drawImage(interactButtonImage, (int) (chest.hitBox.x + chest.hitBox.width / 2 - (double) interactButtonImage.getWidth() / 2 * SCALE - xLevelOffset), (int) (chest.hitBox.y - chest.hitBox.y / 5), KEY_WIDTH, KEY_HEIGHT, null);
            }
//            chest.drawHitBox(g, xLevelOffset);
        }

        if(door != null)
        {
            g.drawImage(doorImage, (int) (door.hitBox.x - door.getXDrawOffset() - xLevelOffset), (int) (door.hitBox.y - door.getYDrawOffset()), DOOR_WIDTH, DOOR_HEIGHT, null);
            if(door.shouldDrawButton())
            {
                g.drawImage(interactButtonImage, (int) (door.hitBox.x + door.hitBox.width / 2 - (double) interactButtonImage.getWidth() / 2 * SCALE - xLevelOffset), (int) (door.hitBox.y - door.hitBox.y / 3.7), KEY_WIDTH, KEY_HEIGHT, null);
            }
//            door.drawHitBox(g, xLevelOffset);
        }

        for(SaveSpot s : saveSpots)
        {
            g.drawImage(saveSpotImages[s.getAnimationIndex()], (int) (s.hitBox.x - s.getXDrawOffset() - xLevelOffset), (int) (s.hitBox.y - s.getYDrawOffset()), SAVE_SPOT_WIDTH, SAVE_SPOT_HEIGHT, null);
            if(s.shouldDrawButton())
            {
                g.drawImage(interactButtonImage, (int) (s.hitBox.x + s.hitBox.width / 2 - (double) interactButtonImage.getWidth() / 2 * SCALE - xLevelOffset), (int) (s.hitBox.y - s.hitBox.y / 5), KEY_WIDTH, KEY_HEIGHT, null);
            }
//            s.drawHitBox(g, xLevelOffset);
        }
    }

    public void setTalkedOnce(boolean value)
    {
        talkedOnce = value;
    }

    public ArrayList<Projectile> getProjectiles( )
    {
        return projectiles;
    }

    public ArrayList<TouchBox> getTouchBoxes()
    {
        return touchBoxes;
    }

    public boolean intersetsTouchBox(Rectangle2D.Double enemyHitBox)
    {
        if(touchBoxes.isEmpty())
        {
            return false;
        }

        return touchBoxes.getLast().isActive() && touchBoxes.getLast().hitBox.intersects(enemyHitBox);
    }

    public void resetAllObjects( )
    {
        if(chest != null)
        {
            chest.reset();
            for(Cannon c : cannons)
            {
                c.reset();
            }
        }

        for(TouchBox touchBox : touchBoxes)
        {
            touchBox.setActive(true);
        }

        zoneToTest = barriers.size() / 2;
        playing.getEnemyHandler().setZone(zoneToTest);
        playing.getPlayer().setBarriers(barriers);
        try
        {
            for(int i = 0; i < barriers.size(); i = i + 2)
            {
                playing.getEnemyHandler().setZoneEnemy(barriers.get(i).hitBox.x + barriers.get(i).hitBox.width, barriers.get(i + 1).hitBox.x);
                barriers.get(i).setActive(false);
                if(i + 1 != barriers.size() - 1)
                {
                    barriers.get(i + 1).setActive(false);
                }
            }
        }
        catch(IllegalArgumentException e)
        {
            CustomLogger.logException("Index invalid pentru bariere.", e);
        }

        projectiles.clear();
    }
}
