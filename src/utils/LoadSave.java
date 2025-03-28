package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public final class LoadSave
{
    //Poze meniuri si jucator si bara de status
    public static final String PLAYER_SPRITESHEET = "MainCharacter/MainCharacter.png";
    public static final String STATUS_BAR = "StatusBar.png";
    public static final String LEVEL_SPRITESHEET = "TileSet.png";
    public static final String MENU_BACKGROUND = "BackgroundMenu.png";
    public static final String PAUSE_BACKGROUND = "PauseBackground.png";
    public static final String CONTROLS_BACKGROUND = "Controls.png";
    public static final String GAME_OVER = "GameOver.png";
    public static final String OPTIONS = "Options.png";
    public static final String LEVEL_COMPLETE = "LevelComplete.png";

    //Poze butoane si slider
    public static final String MENU_BUTTONS_SPRITESHEET = "Buttons.png";
    public static final String SOUND_BUTTONS = "SoundButtons.png";
    public static final String UM_LEVEL_COMPLETED_BUTTONS = "UnpauseMenuControlsLevelCompletedButtons.png";
    public static final String VOLUME_BUTTONS = "Slider.png";

    //Harta 1 si 5 fundal
    public static final String BACKGROUND_1ST_LAYER_FIRST_MAP = "1st and 5th Map/parallax/1st Layer.png";
    public static final String BACKGROUND_2ND_LAYER_FIRST_MAP = "1st and 5th Map/parallax/2nd Layer.png";

    //Harta 2 fundal
    public static final String BACKGROUND_1ST_LAYER_SECOND_MAP = "2nd Map/parallax/1st Layer.png";
    public static final String BACKGROUND_2ND_LAYER_SECOND_MAP = "2nd Map/parallax/2nd Layer.png";
    public static final String BACKGROUND_3RD_LAYER_SECOND_MAP = "2nd Map/parallax/3rd Layer.png";
    public static final String BACKGROUND_4TH_LAYER_SECOND_MAP = "2nd Map/parallax/4th Layer.png";
    public static final String BACKGROUND_IMAGE_SECOND_MAP = "2nd Map/parallax/5th Layer.png";

    //Harta 2 oponenti
    public static final String SKELETON_MACER_SPRITESHEET = "2nd Map/SkeletonMacer/SkeletonMacer.png";

    public static final String SHARDSOUL_SLAYER_SPRITESHEET = "2nd Map/ShardsoulSlayer/ShardsoulSlayer.png";

    public static final String FLYING_DEMON_SPRITESHEET = "2nd Map/FlyingDemon/FlyingDemon.png";
    public static final String FIRE_PROJECTILE_SPRITESHEET = "2nd Map/FlyingDemon/FlyingDemonProjectile.png";

    public static final String HUNTRESS_SPRITESHEET = "2nd Map/Huntress/Huntress.png";
    public static final String SPEAR_SPRITESHEET = "2nd Map/Huntress/Spear.png";

    public static final String NECROMANCER_SPRITESHEET = "2nd Map/Necromancer/Necromancer.png";
    public static final String EXPLOSION_SPRITESHEET = "2nd Map/Necromancer/Explosion.png";

    public static final String LEAF_RANGER_SPRITESHEET = "2nd Map/LeafRanger/LeafRanger.png";
    public static final String ARROW_SPRITESHEET = "2nd Map/LeafRanger/Arrow.png";
    public static final String GREEN_RAY_SPRITESHEET = "2nd Map/LeafRanger/GreenRay.png";
    public static final String ROOTS_SPRITESHEET = "2nd Map/LeafRanger/Roots.png";

    //Harta 3 fundal
    public static final String BACKGROUND_1ST_LAYER_THIRD_MAP = "3rd Map/parallax/1st Layer.png";
    public static final String BACKGROUND_2ND_LAYER_THIRD_MAP = "3rd Map/parallax/2nd Layer.png";
    public static final String BACKGROUND_3RD_LAYER_THIRD_MAP = "3rd Map/parallax/3rd Layer.png";
    public static final String BACKGROUND_4TH_LAYER_THIRD_MAP = "3rd Map/parallax/4th Layer.png";
    public static final String BACKGROUND_IMAGE_THIRD_MAP = "3rd Map/parallax/5th Layer.png";

    //Harta 3 oponenti
    public static final String DEMON_SPRITESHEET = "3rd Map/Demon/Demon.png";
    public static final String FIRE_WAVE_SPRITESHEET = "3rd Map/Demon/FireWave.png";

    public static final String FIRE_WORM_SPRITESHEET = "3rd Map/FireWorm/FireWorm.png";
    public static final String MAGMA_BALL_SPRITESHEET = "3rd Map/FireWorm/MagmaBall.png";

    public static final String FIRE_WIZARD_SPRITESHEET = "3rd Map/FireWizard/FireWizard.png";

    public static final String MINOTAUR_SPRITESHEET = "3rd Map/Minotaur/Minotaur.png";
    public static final String VERTICAL_FIRE_SPRITESHEET = "3rd Map/Minotaur/VerticalFire.png";
    public static final String HORIZONTAL_FIRE_SPRITESHEET = "3rd Map/Minotaur/HorizontalFire.png";

    public static final String FIRE_KNIGHT_SPRITESHEET = "3rd Map/FireKnight/FireKnight.png";
    public static final String FIRE_SPIRAL_SPRITESHEET = "3rd Map/FireKnight/FireSpiral.png";

    //Harta 4 fundal
    public static final String BACKGROUND_1ST_LAYER_FOURTH_MAP = "4th Map/parallax/1st Layer.png";
    public static final String BACKGROUND_2ND_LAYER_FOURTH_MAP = "4th Map/parallax/2nd Layer.png";
    public static final String BACKGROUND_3RD_LAYER_FOURTH_MAP = "4th Map/parallax/3rd Layer.png";
    public static final String BACKGROUND_4TH_LAYER_FOURTH_MAP = "4th Map/parallax/4th Layer.png";
    public static final String BACKGROUND_IMAGE_FOURTH_MAP = "4th Map/parallax/5th Layer.png";

    //Harta 5 oponenti
    public static final String ICE_GOLEM_SPRITESHEET = "4th Map/IceGolem/IceGolem.png";
    public static final String ICE_EXPLOSION_SPRITESHEET = "4th Map/IceGolem/IceExplosion.png";

    public static final String ICE_BORNE_SPRITESHEET = "4th Map/IceBorne/IceBorne.png";

    public static final String SNOW_KNIGHT_SPRITESHEET = "4th Map/SnowKnight/SnowKnight.png";
    public static final String ICE_TORNADO_SPRITESHEET = "4th Map/SnowKnight/IceTornado.png";

    public static final String WEREWOLF_SPRITESHEET = "4th Map/Werewolf/Werewolf.png";
    public static final String FROZEN_SHOCKWAVE_SPRITESHEET = "4th Map/Werewolf/FrozenShockwave.png";

    public static final String VIKING_SPRITESHEET = "4th Map/Viking/Viking.png";
    public static final String FROZEN_FLAME_SPRITESHEET = "4th Map/Viking/FrozenFlame.png";
    public static final String ICE_SPIRAL_SPRITESHEET = "4th Map/Viking/IceSpiral.png";
    public static final String ICE_PILLAR_SPRITESHEET = "4th Map/Viking/IcePillar.png";

    //Boss final
    public static final String FINAL_BOSS_SPRITESHEET = "FinalBoss/FinalBoss.png";
    public static final String WIND_PROJECTILE_SPRITESHEET = "FinalBoss/WindProjectile.png";
    public static final String WIND_VERTICAL_WAVE_SPRITESHEET = "FinalBoss/WindVerticalWave.png";
    public static final String WIND_HORIZONTAL_WAVE_SPRITESHEET = "FinalBoss/WindHorizontalWave.png";

    //Cufere, capcane, insigne,obiecte interactive si bariera
    public static final String CHESTS_SPRITESHEET = "Interactables/Chests.png";
    public static final String SPIKE_SPRITESHEET = "Traps/Spike.png";
    public static final String FOREST_MAGIC_FIRE = "Traps/ForestMagicFire.png";
    public static final String HELLFIRE = "Traps/HellFire.png";
    public static final String DEADLY_MAGIC_FIRE = "Traps/DeadlyMagicFire.png";
    public static final String CANNON_SPRITESHEET = "Traps/Cannon.png";
    public static final String ICE_PROJECTILE_SPRITESHEET = "Traps/IceProjectile.png";
    public static final String DARK_FOREST_INSIGNIA_SPRITESHEET = "Insignias/DarkForstInsignia.png";
    public static final String FIERY_ABYSS_INSIGNIA_SPRITESHEET = "Insignias/FieryAbyssInsignia.png";
    public static final String WASTELAND_OF_ABANDONED_ICE_INSIGNIA_SPRITESHEET = "Insignias/WastelandOfAbandonedIceInsignia.png";
    public static final String KING_SPRITESHEET = "Interactables/King.png";
    public static final String RED_MOON_TOWER_SPRITESHEET = "Interactables/RedMoonTower.png";
    public static final String DOOR_SPRITESHEET = "Interactables/Door.png";
    public static final String BARRIER_SPRITESHEET = "Traps/Barrier.png";

    //Tasta Interactionare
    public static final String E_SPRITESHEET = "Keys/E.png";

    //Pentru dialog
    public static final String CAN_CONTINUE_SPRITESHEET = "CanContinue.png";

    //Decoratiuni
    public static final String BOOKSHELF1_SPRITESHEET = "objects/Bookshelf1.png";
    public static final String BOOKSHELF2_SPRITESHEET = "objects/Bookshelf2.png";
    public static final String TABLE_SPRITESHEET = "objects/Table.png";
    public static final String BAG1_SPRITESHEET = "objects/Bag1.png";
    public static final String BAG2_SPRITESHEET = "objects/Bag2.png";
    public static final String BARREL_SPRITESHEET = "objects/Barrel.png";
    public static final String CRATE1_SPRITESHEET = "objects/Crate1.png";
    public static final String CRATE2_SPRITESHEET = "objects/Crate2.png";
    public static final String CRATE3_SPRITESHEET = "objects/Crate3.png";
    public static final String RED_FLAG_SPRITESHEET = "objects/RedFlag.png";
    public static final String WEAPON_RACK1_SPRITESHEET = "objects/WeaponRack1.png";
    public static final String WEAPON_RACK2_SPRITESHEET = "objects/WeaponRack2.png";
    public static final String CURTAINS_SPRITESHEET = "objects/Curtains.png";
    public static final String CANDELABRUM_SPRITESHEET = "objects/Candelabrum.png";
    public static final String CANDLE_SPRITESHEET = "objects/Candle.png";
    public static final String TORCH_BIG_SPRITESHEET = "objects/TorchBig.png";
    public static final String TORCH_SMALL_SPRITESHEET = "objects/TorchSmall.png";

    public static BufferedImage GetSpriteAtlas(String filename)
    {
        BufferedImage img = null;
        InputStream is = null;
        try
        {
            is = LoadSave.class.getClassLoader().getResourceAsStream(filename);
            img = ImageIO.read(is);
        }
        catch (IOException e)
        {
            CustomLogger.logException("Eroare la creare flux de date pentru sprite-uri.", e);
        }
        catch(IllegalArgumentException e)
        {
            CustomLogger.logException("Nu se poate citi dintr-un flux de date pentru sprite-uri inexistent.", e);
        }
        catch(SecurityException e)
        {
            CustomLogger.logException("Nu este permis din motive de securitate sa se incarce sprite-urile.", e);
        }
        catch(NullPointerException e)
        {
            CustomLogger.logException("Cale nula pentru incarcare flux de date sprite-uri.", e);
        }
        finally
        {
            try
            {
                is.close();
            }
            catch(IOException e)
            {
                CustomLogger.logException("Nu se poate inchide fluxul de date pentru sprite-uri.", e);
            }
        }
        return img;
    }

    @SuppressWarnings("DataFlowIssue")
    public static BufferedImage[] GetAllLevels( )
    {
        URL url;
        File file = null;

        try
        {
            url = LoadSave.class.getClassLoader().getResource("Levels");
            file = new File(url.toURI());
        }
        catch(URISyntaxException e)
        {
            CustomLogger.logException("Nu se poate transofrma in adresa URI.", e);
        }
        catch(NullPointerException e)
        {
            CustomLogger.logException("Nu se poate incarca url-ul dintr-un nume inexistent.", e);
        }
        catch(IllegalArgumentException e)
        {
            CustomLogger.logException("Preconditiile nu se mentin pentru a incarca nivelelor.", e);
        }
        catch(SecurityException e)
        {
            CustomLogger.logException("Nu este permis din motive de securitate sa se incarce nivelele.", e);
        }

        CustomLogger.assertWithLogging(file != null, "Fisierul de unde se incarca nivelele este nul.");
        File[] files = null;
        try
        {
             files = file.listFiles((_, name) -> name.endsWith(".png"));
        }
        catch(SecurityException e)
        {
            CustomLogger.logException("Nu este permis din motive de securitate sa se incarce directorul.", e);
        }

        CustomLogger.assertWithLogging(files != null, "Fisierul de unde se incarca nivelele este nul.");
        File[] filesSorted = new File[files.length];

        for(int i = 0; i < filesSorted.length; ++i)
        {
            for (File value : files)
            {
                if (value.getName().equals((i + 1) + ".png"))
                {
                    filesSorted[i] = value;
                }
            }
        }

        BufferedImage[] images = new BufferedImage[filesSorted.length];

        for(int i = 0; i < images.length; ++i)
        {
            try
            {
                images[i] = ImageIO.read(filesSorted[i]);
            }
            catch (IOException e)
            {
                CustomLogger.logException("Eroare la creare flux de date pentru nivele.", e);
            }
            catch(IllegalArgumentException e)
            {
                CustomLogger.logException("Nu se poate citi dintr-un flux de date pentru imagine nivele.", e);
            }
        }

        return images;
    }
}
