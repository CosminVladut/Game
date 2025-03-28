package utils;

import exceptions.UnexpectedObjectTypeException;
import exceptions.UnexpectedPlayerActionException;
import exceptions.UnexpectedProjectyleTypeException;

import static main.Game.SCALE;

public final class Constants
{
    private Constants()
    {

    }

    public static final double GRAVITY = 0.1 * SCALE;
    public static final int ANIMATION_SPEED = 7;

    public static final class Projectiles
    {
        public static final int ICE_BALL = 1;
        public static final int FIRE_BALL = 2;
        public static final int SPEAR = 3;
        public static final int FIRE_WAVE = 4;
        public static final int ICE_EXPLOSION = 5;
        public static final int ICE_TORNADO = 6;
        public static final int MAGMA_BALL = 7;
        public static final int VERTICAL_FIRE = 8;
        public static final int HORIZONTAL_FIRE = 9;
        public static final int WIND_PROJECTILE = 10;
        public static final int WIND_VERTICAL_WAVE = 11;
        public static final int WIND_HORIZONTAL_WAVE = 12;
        public static final int FIRE_SPIRAL = 13;
        public static final int ARROW = 14;
        public static final int GREEN_RAY = 15;
        public static final int ROOTS = 16;
        public static final int FROZEN_FLAME = 17;
        public static final int ICE_SPIRAL = 18;
        public static final int ICE_PILLAR = 19;
        public static final int FROZEN_SHOCKWAVE = 20;
        public static final int EXPLOSION = 21;

        public static final int ICE_BALL_WIDTH_DEFAULT = 32;
        public static final int ICE_BALL_HEIGHT_DEFAULT = 10;
        public static final int ICE_BALL_WIDTH = (int)(ICE_BALL_WIDTH_DEFAULT * SCALE);
        public static final int ICE_BALL_HEIGHT = (int)(ICE_BALL_HEIGHT_DEFAULT * SCALE);

        public static final int FIRE_BALL_WIDTH_DEFAULT = 36;
        public static final int FIRE_BALL_HEIGHT_DEFAULT = 18;
        public static final int FIRE_BALL_WIDTH = (int)(FIRE_BALL_WIDTH_DEFAULT * SCALE);
        public static final int FIRE_BALL_HEIGHT = (int)(FIRE_BALL_HEIGHT_DEFAULT * SCALE);

        public static final int SPEAR_WIDTH_DEFAULT = 42;
        public static final int SPEAR_HEIGHT_DEFAULT = 5;
        public static final int SPEAR_WIDTH = (int)(SPEAR_WIDTH_DEFAULT * SCALE);
        public static final int SPEAR_HEIGHT = (int)(SPEAR_HEIGHT_DEFAULT * SCALE);

        public static final int FIRE_WAVE_WIDTH_DEFAULT = 24;
        public static final int FIRE_WAVE_HEIGHT_DEFAULT = 67;
        public static final int FIRE_WAVE_WIDTH = (int)(FIRE_WAVE_WIDTH_DEFAULT * SCALE);
        public static final int FIRE_WAVE_HEIGHT = (int)(FIRE_WAVE_HEIGHT_DEFAULT * SCALE);

        public static final int ICE_EXPLOSION_WIDTH_DEFAULT = 60;
        public static final int ICE_EXPLOSION_HEIGHT_DEFAULT = 60;
        public static final int ICE_EXPLOSION_WIDTH = (int)(ICE_EXPLOSION_WIDTH_DEFAULT * SCALE);
        public static final int ICE_EXPLOSION_HEIGHT = (int)(ICE_EXPLOSION_HEIGHT_DEFAULT * SCALE);

        public static final int ICE_TORNADO_WIDTH_DEFAULT = 50;
        public static final int ICE_TORNADO_HEIGHT_DEFAULT = 62;
        public static final int ICE_TORNADO_WIDTH = (int)(ICE_TORNADO_WIDTH_DEFAULT * SCALE);
        public static final int ICE_TORNADO_HEIGHT = (int)(ICE_TORNADO_HEIGHT_DEFAULT * SCALE);

        public static final int MAGMA_BALL_WIDTH_DEFAULT = 15;
        public static final int MAGMA_BALL_HEIGHT_DEFAULT = 11;
        public static final int MAGMA_BALL_WIDTH = (int)(MAGMA_BALL_WIDTH_DEFAULT * SCALE);
        public static final int MAGMA_BALL_HEIGHT = (int)(MAGMA_BALL_HEIGHT_DEFAULT * SCALE);

        public static final int VERTICAL_FIRE_WIDTH_DEFAULT = 16;
        public static final int VERTICAL_FIRE_HEIGHT_DEFAULT = 63;
        public static final int VERTICAL_FIRE_WIDTH = (int)(VERTICAL_FIRE_WIDTH_DEFAULT * SCALE);
        public static final int VERTICAL_FIRE_HEIGHT = (int)(VERTICAL_FIRE_HEIGHT_DEFAULT * SCALE);

        public static final int HORIZONTAL_FIRE_WIDTH_DEFAULT = 31;
        public static final int HORIZONTAL_FIRE_HEIGHT_DEFAULT = 21;
        public static final int HORIZONTAL_FIRE_WIDTH = (int)(HORIZONTAL_FIRE_WIDTH_DEFAULT * SCALE);
        public static final int HORIZONTAL_FIRE_HEIGHT = (int)(HORIZONTAL_FIRE_HEIGHT_DEFAULT * SCALE);

        public static final int WIND_PROJECTILE_WIDTH_DEFAULT = 32;
        public static final int WIND_PROJECTILE_HEIGHT_DEFAULT = 23;
        public static final int WIND_PROJECTILE_WIDTH = (int)(WIND_PROJECTILE_WIDTH_DEFAULT * SCALE);
        public static final int WIND_PROJECTILE_HEIGHT = (int)(WIND_PROJECTILE_HEIGHT_DEFAULT * SCALE);

        public static final int WIND_VERTICAL_WAVE_WIDTH_DEFAULT = 26;
        public static final int WIND_VERTICAL_WAVE_HEIGHT_DEFAULT = 44;
        public static final int WIND_VERTICAL_WAVE_WIDTH = (int)(WIND_VERTICAL_WAVE_WIDTH_DEFAULT * SCALE);
        public static final int WIND_VERTICAL_WAVE_HEIGHT = (int)(WIND_VERTICAL_WAVE_HEIGHT_DEFAULT * SCALE);

        public static final int WIND_HORIZONTAL_WAVE_WIDTH_DEFAULT = 32;
        public static final int WIND_HORIZONTAL_WAVE_HEIGHT_DEFAULT = 22;
        public static final int WIND_HORIZONTAL_WAVE_WIDTH = (int)(WIND_HORIZONTAL_WAVE_WIDTH_DEFAULT * SCALE);
        public static final int WIND_HORIZONTAL_WAVE_HEIGHT = (int)(WIND_HORIZONTAL_WAVE_HEIGHT_DEFAULT * SCALE);

        public static final int FIRE_SPIRAL_WIDTH_DEFAULT = 26;
        public static final int FIRE_SPIRAL_HEIGHT_DEFAULT = 26;
        public static final int FIRE_SPIRAL_WIDTH = (int)(FIRE_SPIRAL_WIDTH_DEFAULT * SCALE);
        public static final int FIRE_SPIRAL_HEIGHT = (int)(FIRE_SPIRAL_HEIGHT_DEFAULT * SCALE);

        public static final int ARROW_WIDTH_DEFAULT = 32;
        public static final int ARROW_HEIGHT_DEFAULT = 3;
        public static final int ARROW_WIDTH = (int)(ARROW_WIDTH_DEFAULT * SCALE);
        public static final int ARROW_HEIGHT = (int)(ARROW_HEIGHT_DEFAULT * SCALE);

        public static final int GREEN_RAY_WIDTH_DEFAULT = 256;
        public static final int GREEN_RAY_HEIGHT_DEFAULT = 15;
        public static final int GREEN_RAY_WIDTH = (int)(GREEN_RAY_WIDTH_DEFAULT * SCALE);
        public static final int GREEN_RAY_HEIGHT = (int)(GREEN_RAY_HEIGHT_DEFAULT * SCALE);

        public static final int ROOTS_WIDTH_DEFAULT = 68;
        public static final int ROOTS_HEIGHT_DEFAULT = 115;
        public static final int ROOTS_WIDTH = (int)(ROOTS_WIDTH_DEFAULT * SCALE);
        public static final int ROOTS_HEIGHT = (int)(ROOTS_HEIGHT_DEFAULT * SCALE);

        public static final int FROZEN_FLAME_WIDTH_DEFAULT = 128;
        public static final int FROZEN_FLAME_HEIGHT_DEFAULT = 26;
        public static final int FROZEN_FLAME_WIDTH = (int)(FROZEN_FLAME_WIDTH_DEFAULT * SCALE);
        public static final int FROZEN_FLAME_HEIGHT = (int)(FROZEN_FLAME_HEIGHT_DEFAULT * SCALE);

        public static final int ICE_SPIRAL_WIDTH_DEFAULT = 28;
        public static final int ICE_SPIRAL_HEIGHT_DEFAULT = 20;
        public static final int ICE_SPIRAL_WIDTH = (int)(ICE_SPIRAL_WIDTH_DEFAULT * SCALE);
        public static final int ICE_SPIRAL_HEIGHT = (int)(ICE_SPIRAL_HEIGHT_DEFAULT * SCALE);

        public static final int ICE_PILLAR_WIDTH_DEFAULT = 58;
        public static final int ICE_PILLAR_HEIGHT_DEFAULT = 75;
        public static final int ICE_PILLAR_WIDTH = (int)(ICE_PILLAR_WIDTH_DEFAULT * SCALE);
        public static final int ICE_PILLAR_HEIGHT = (int)(ICE_PILLAR_HEIGHT_DEFAULT * SCALE);

        public static final int FROZEN_SHOCKWAVE_WIDTH_DEFAULT = 27;
        public static final int FROZEN_SHOCKWAVE_HEIGHT_DEFAULT = 44;
        public static final int FROZEN_SHOCKWAVE_WIDTH = (int)(FROZEN_SHOCKWAVE_WIDTH_DEFAULT * SCALE);
        public static final int FROZEN_SHOCKWAVE_HEIGHT = (int)(FROZEN_SHOCKWAVE_HEIGHT_DEFAULT * SCALE);

        public static final int EXPLOSION_WIDTH_DEFAULT = 48;
        public static final int EXPLOSION_HEIGHT_DEFAULT = 40;
        public static final int EXPLOSION_WIDTH = (int)(EXPLOSION_WIDTH_DEFAULT * SCALE);
        public static final int EXPLOSION_HEIGHT = (int)(EXPLOSION_HEIGHT_DEFAULT * SCALE);

        public static int GetSpriteAmount(int projectile_type)
        {
            return switch(projectile_type)
            {
                case ICE_BALL -> 3;
                case FIRE_BALL, FIRE_WAVE, VERTICAL_FIRE, WIND_VERTICAL_WAVE, ARROW -> 1;
                case SPEAR, ICE_EXPLOSION, ICE_TORNADO, HORIZONTAL_FIRE, WIND_PROJECTILE, WIND_HORIZONTAL_WAVE, FIRE_SPIRAL, GREEN_RAY, ICE_SPIRAL, FROZEN_FLAME, FROZEN_SHOCKWAVE, ICE_PILLAR -> 4;
                case MAGMA_BALL -> 6;
                case ROOTS -> 12;
                case EXPLOSION -> 18;
                default ->
                {
                    CustomLogger.logException("Tip de proiectil nesteptat: " + projectile_type, new UnexpectedProjectyleTypeException());
                    System.err.println("Eroare, verifica logul");
                    yield -1;
                }
            };
        }
    }

    public static final class ObjectConstants
    {
        public static final int FOREST_CHEST = 1;
        public static final int FIRE_CHEST = 2;
        public static final int ICE_CHEST = 3;

        public static final int CHEST_WIDTH_DEFAULT = 48;
        public static final int CHEST_HEIGHT_DEFAULT = 32;
        public static final int CHEST_WIDTH = (int)(CHEST_WIDTH_DEFAULT * SCALE);
        public static final int CHEST_HEIGHT = (int)(CHEST_HEIGHT_DEFAULT * SCALE);

        public static final int SPIKE = 4;
        public static final int SPIKE_WIDTH_DEFAULT = 16;
        public static final int SPIKE_HEIGHT_DEFAULT = 16;
        public static final int SPIKE_WIDTH = (int)(SPIKE_WIDTH_DEFAULT * SCALE);
        public static final int SPIKE_HEIGHT = (int)(SPIKE_HEIGHT_DEFAULT * SCALE);

        public static final int CANNON_LEFT = 5;
        public static final int CANNON_RIGHT = 6;
        public static final int CANNON_WIDTH_DEFAULT = 32;
        public static final int CANNON_HEIGHT_DEFAULT = 16;
        public static final int CANNON_WIDTH = (int)(CANNON_WIDTH_DEFAULT * SCALE);
        public static final int CANNON_HEIGHT = (int)(CANNON_HEIGHT_DEFAULT * SCALE);

        public static final int FOREST_MAGIC_FIRE = 7;
        public static final int FOREST_MAGIC_FIRE_WIDTH_DEFAULT = 32;
        public static final int FOREST_MAGIC_FIRE_HEIGHT_DEFAULT = 32;
        public static final int FOREST_MAGIC_FIRE_WIDTH = (int)(FOREST_MAGIC_FIRE_WIDTH_DEFAULT * SCALE);
        public static final int FOREST_MAGIC_FIRE_HEIGHT = (int)(FOREST_MAGIC_FIRE_HEIGHT_DEFAULT * SCALE);

        public static final int HELLFIRE = 8;
        public static final int HELLFIRE_WIDTH_DEFAULT = 21;
        public static final int HELLFIRE_HEIGHT_DEFAULT = 32;
        public static final int HELLFIRE_WIDTH = (int)(HELLFIRE_WIDTH_DEFAULT * SCALE);
        public static final int HELLFIRE_HEIGHT = (int)(HELLFIRE_HEIGHT_DEFAULT * SCALE);

        public static final int DEADLY_MAGIC_FIRE = 9;
        public static final int DEADLY_MAGIC_FIRE_WIDTH_DEFAULT = 19;
        public static final int DEADLY_MAGIC_FIRE_HEIGHT_DEFAULT = 32;
        public static final int DEADLY_MAGIC_FIRE_WIDTH = (int)(DEADLY_MAGIC_FIRE_WIDTH_DEFAULT * SCALE);
        public static final int DEADLY_MAGIC_FIRE_HEIGHT = (int)(DEADLY_MAGIC_FIRE_HEIGHT_DEFAULT * SCALE);

        public static final int KING = 10;
        public static final int KING_WIDTH_DEFAULT = 160;
        public static final int KING_HEIGHT_DEFAULT = 113;
        public static final int KING_WIDTH = (int)(KING_WIDTH_DEFAULT * SCALE);
        public static final int KING_HEIGHT = (int)(KING_HEIGHT_DEFAULT * SCALE);

        public static final int SAVE_SPOT = 11;
        public static final int SAVE_SPOT_WIDTH_DEFAULT = 100;
        public static final int SAVE_SPOT_HEIGHT_DEFAULT = 80;
        public static final int SAVE_SPOT_WIDTH = (int)(SAVE_SPOT_WIDTH_DEFAULT * SCALE);
        public static final int SAVE_SPOT_HEIGHT = (int)(SAVE_SPOT_HEIGHT_DEFAULT * SCALE);

        public static final int KEY_WIDTH_DEFAULT = 16;
        public static final int KEY_HEIGHT_DEFAULT = 16;
        public static final int KEY_WIDTH = (int)(KEY_WIDTH_DEFAULT * SCALE);
        public static final int KEY_HEIGHT = (int)(KEY_HEIGHT_DEFAULT * SCALE);

        public static final int BOOKSHELF1 = 12;
        public static final int BOOKSHELF1_WIDTH_DEFAULT = 64;
        public static final int BOOKSHELF1_HEIGHT_DEFAULT = 90;
        public static final int BOOKSHELF1_WIDTH = (int)(BOOKSHELF1_WIDTH_DEFAULT * SCALE);
        public static final int BOOKSHELF1_HEIGHT = (int)(BOOKSHELF1_HEIGHT_DEFAULT * SCALE);

        public static final int BOOKSHELF2 = 13;
        public static final int BOOKSHELF2_WIDTH_DEFAULT = 64;
        public static final int BOOKSHELF2_HEIGHT_DEFAULT = 88;
        public static final int BOOKSHELF2_WIDTH = (int)(BOOKSHELF2_WIDTH_DEFAULT * SCALE);
        public static final int BOOKSHELF2_HEIGHT = (int)(BOOKSHELF2_HEIGHT_DEFAULT * SCALE);

        public static final int RED_FLAG = 14;
        public static final int RED_FLAG_WIDTH_DEFAULT = 47;
        public static final int RED_FLAG_HEIGHT_DEFAULT = 51;
        public static final int RED_FLAG_WIDTH = (int)(RED_FLAG_WIDTH_DEFAULT * SCALE);
        public static final int RED_FLAG_HEIGHT = (int)(RED_FLAG_HEIGHT_DEFAULT * SCALE);

        public static final int BAG1 = 15;
        public static final int BAG1_WIDTH_DEFAULT = 17;
        public static final int BAG1_HEIGHT_DEFAULT = 22;
        public static final int BAG1_WIDTH = (int)(BAG1_WIDTH_DEFAULT * SCALE);
        public static final int BAG1_HEIGHT = (int)(BAG1_HEIGHT_DEFAULT * SCALE);

        public static final int BAG2 = 16;
        public static final int BAG2_WIDTH_DEFAULT = 25;
        public static final int BAG2_HEIGHT_DEFAULT = 22;
        public static final int BAG2_WIDTH = (int)(BAG2_WIDTH_DEFAULT * SCALE);
        public static final int BAG2_HEIGHT = (int)(BAG2_HEIGHT_DEFAULT * SCALE);

        public static final int BARREL = 17;
        public static final int BARREL_WIDTH_DEFAULT = 32;
        public static final int BARREL_HEIGHT_DEFAULT = 34;
        public static final int BARREL_WIDTH = (int)(BARREL_WIDTH_DEFAULT * SCALE);
        public static final int BARREL_HEIGHT = (int)(BARREL_HEIGHT_DEFAULT * SCALE);

        public static final int CRATE1 = 18;
        public static final int CRATE1_WIDTH_DEFAULT = 35;
        public static final int CRATE1_HEIGHT_DEFAULT = 31;
        public static final int CRATE1_WIDTH = (int)(CRATE1_WIDTH_DEFAULT * SCALE);
        public static final int CRATE1_HEIGHT = (int)(CRATE1_HEIGHT_DEFAULT * SCALE);

        public static final int CRATE2 = 19;
        public static final int CRATE2_WIDTH_DEFAULT = 37;
        public static final int CRATE2_HEIGHT_DEFAULT = 54;
        public static final int CRATE2_WIDTH = (int)(CRATE2_WIDTH_DEFAULT * SCALE);
        public static final int CRATE2_HEIGHT = (int)(CRATE2_HEIGHT_DEFAULT * SCALE);

        public static final int CRATE3 = 20;
        public static final int CRATE3_WIDTH_DEFAULT = 69;
        public static final int CRATE3_HEIGHT_DEFAULT = 22;
        public static final int CRATE3_WIDTH = (int)(CRATE3_WIDTH_DEFAULT * SCALE);
        public static final int CRATE3_HEIGHT = (int)(CRATE3_HEIGHT_DEFAULT * SCALE);

        public static final int TABLE = 21;
        public static final int TABLE_WIDTH_DEFAULT = 60;
        public static final int TABLE_HEIGHT_DEFAULT = 32;
        public static final int TABLE_WIDTH = (int)(TABLE_WIDTH_DEFAULT * SCALE);
        public static final int TABLE_HEIGHT = (int)(TABLE_HEIGHT_DEFAULT * SCALE);

        public static final int WEAPON_RACK1 = 22;
        public static final int WEAPON_RACK1_WIDTH_DEFAULT = 30;
        public static final int WEAPON_RACK1_HEIGHT_DEFAULT = 48;
        public static final int WEAPON_RACK1_WIDTH = (int)(WEAPON_RACK1_WIDTH_DEFAULT * SCALE);
        public static final int WEAPON_RACK1_HEIGHT = (int)(WEAPON_RACK1_HEIGHT_DEFAULT * SCALE);

        public static final int WEAPON_RACK2 = 23;
        public static final int WEAPON_RACK2_WIDTH_DEFAULT = 26;
        public static final int WEAPON_RACK2_HEIGHT_DEFAULT = 28;
        public static final int WEAPON_RACK2_WIDTH = (int)(WEAPON_RACK2_WIDTH_DEFAULT * SCALE);
        public static final int WEAPON_RACK2_HEIGHT = (int)(WEAPON_RACK2_HEIGHT_DEFAULT * SCALE);

        public static final int CURTAINS = 24;
        public static final int CURTAINS_WIDTH_DEFAULT = 23;
        public static final int CURTAINS_HEIGHT_DEFAULT = 27;
        public static final int CURTAINS_WIDTH = (int)(CURTAINS_WIDTH_DEFAULT * SCALE);
        public static final int CURTAINS_HEIGHT = (int)(CURTAINS_HEIGHT_DEFAULT * SCALE);

        public static final int CANDELABRUM = 25;
        public static final int CANDELABRUM_WIDTH_DEFAULT = 16;
        public static final int CANDELABRUM_HEIGHT_DEFAULT = 74;
        public static final int CANDELABRUM_WIDTH = (int)(CANDELABRUM_WIDTH_DEFAULT * SCALE);
        public static final int CANDELABRUM_HEIGHT = (int)(CANDELABRUM_HEIGHT_DEFAULT * SCALE);

        public static final int CANDLE = 26;
        public static final int CANDLE_WIDTH_DEFAULT = 6;
        public static final int CANDLE_HEIGHT_DEFAULT = 22;
        public static final int CANDLE_WIDTH = (int)(CANDLE_WIDTH_DEFAULT * SCALE);
        public static final int CANDLE_HEIGHT = (int)(CANDLE_HEIGHT_DEFAULT * SCALE);

        public static final int TORCH_BIG = 27;
        public static final int TORCH_BIG_WIDTH_DEFAULT = 41;
        public static final int TORCH_BIG_HEIGHT_DEFAULT = 57;
        public static final int TORCH_BIG_WIDTH = (int)(TORCH_BIG_WIDTH_DEFAULT * SCALE);
        public static final int TORCH_BIG_HEIGHT = (int)(TORCH_BIG_HEIGHT_DEFAULT * SCALE);

        public static final int TORCH_SMALL = 28;
        public static final int TORCH_SMALL_WIDTH_DEFAULT = 25;
        public static final int TORCH_SMALL_HEIGHT_DEFAULT = 41;
        public static final int TORCH_SMALL_WIDTH = (int)(TORCH_SMALL_WIDTH_DEFAULT * SCALE);
        public static final int TORCH_SMALL_HEIGHT = (int)(TORCH_SMALL_HEIGHT_DEFAULT * SCALE);

        public static final int DOOR = 29;
        public static final int DOOR_WIDTH_DEFAULT = 41;
        public static final int DOOR_HEIGHT_DEFAULT = 59;
        public static final int DOOR_WIDTH = (int)(DOOR_WIDTH_DEFAULT * SCALE);
        public static final int DOOR_HEIGHT = (int)(DOOR_HEIGHT_DEFAULT * SCALE);

        public static final int TOUCHBOX = 30;

        public static final int BARRIER = 31;
        public static final int BARRIER_WIDTH_DEFAULT = 16;
        public static final int BARRIER_HEIGHT_DEFAULT = 256;
        public static final int BARRIER_WIDTH = (int)(BARRIER_WIDTH_DEFAULT * SCALE);
        public static final int BARRIER_HEIGHT = (int)(BARRIER_HEIGHT_DEFAULT * SCALE);

        public static int GetSpriteAmount(int object_type)
        {
            return switch(object_type)
            {
                case SAVE_SPOT -> 11;
                case KING -> 8;
                case FOREST_CHEST, FIRE_CHEST, ICE_CHEST -> 10;
                case SPIKE, BOOKSHELF1, BOOKSHELF2, TABLE, BAG1, BAG2, BARREL, CRATE1, CRATE2, CRATE3, RED_FLAG,
                     WEAPON_RACK1, WEAPON_RACK2, CURTAINS, DOOR, TOUCHBOX, BARRIER -> 1;
                case CANNON_LEFT, CANNON_RIGHT -> 5;
                case FOREST_MAGIC_FIRE, HELLFIRE -> 4;
                case DEADLY_MAGIC_FIRE -> 3;
                case CANDELABRUM, CANDLE, TORCH_BIG, TORCH_SMALL -> 6;
                default ->
                {
                    CustomLogger.logException("Tip de obiect neasteptat: " + object_type, new UnexpectedObjectTypeException());
                    yield -1;
                }
            };
        }
    }

    public static final class UI
    {
        public static final class Buttons
        {
            public static final int B_WIDTH_DEFAULT = 68;
            public static final int B_HEIGHT_DEFAULT = 68;
            public static final int B_WIDTH = (int)(B_WIDTH_DEFAULT * SCALE);
            public static final int B_HEIGHT = (int)(B_HEIGHT_DEFAULT * SCALE);
        }

        public static final class PauseButtons
        {
            public static final int BUTTON_SIZE_WIDTH_DEFAULT = 47;
            public static final int BUTTON_SIZE_HEIGHT_DEFAULT = 44;
            public static final int BUTTON_SIZE_WIDTH = (int)(BUTTON_SIZE_WIDTH_DEFAULT * SCALE);
            public static final int BUTTON_SIZE_HEIGHT = (int)(BUTTON_SIZE_HEIGHT_DEFAULT * SCALE);
        }

        public static final class VolumeSlider
        {
            public static final int VOLUME_DEFAULT_WIDTH = 23;
            public static final int VOLUME_DEFAULT_HEIGHT = 32;
            public static final int SLIDER_DEFAULT_WIDTH = 104;
            public static final int VOLUME_WIDTH = (int)(VOLUME_DEFAULT_WIDTH * SCALE);
            public static final int VOLUME_HEIGHT = (int)(VOLUME_DEFAULT_HEIGHT * SCALE);
            public static final int SLIDER_WIDTH = (int)(SLIDER_DEFAULT_WIDTH * SCALE);
        }
    }

    public static final class Directions
    {
        public static final int LEFT = 0;
        public static final int RIGHT = 1;
    }

    public static final class PlayerConstants
    {
        public static final int DROP_ATTACK = 0;
        public static final int ATTACKS1 = 1;
        public static final int ATTACKS2 = 2;
        public static final int ATTACKS3 = 3;
        public static final int ATTACKS4 = 4;
        public static final int CROUCH_ATTACKS1 = 5;
        public static final int CROUCH_ATTACKS2 = 6;
        public static final int CROUCH_IDLE = 7;
        public static final int DEATH = 8;
        public static final int HEAL = 9;
        public static final int TAKE_HIT = 10;
        public static final int IDLE = 11;
        public static final int JUMP = 12;
        public static final int FALL = 13;
        public static final int PRAY = 14;
        public static final int ROLL = 15;
        public static final int RUN = 16;

        public static int GetSpriteAmount(int player_action)
        {
            return switch (player_action)
            {
                case DROP_ATTACK -> 7;
                case ATTACKS1 -> 5;
                case ATTACKS4 -> 6;
                case CROUCH_IDLE, IDLE, RUN -> 8;
                case ATTACKS2, DEATH, ATTACKS3, CROUCH_ATTACKS2, ROLL, CROUCH_ATTACKS1, HEAL -> 4;
                case TAKE_HIT -> 3;
                case PRAY -> 12;
                case JUMP -> 2;
                case FALL -> 1;
                default ->
                {
                    CustomLogger.logException("Actiune a jucatorului neasteptata: " + player_action, new UnexpectedPlayerActionException());
                    yield -1;
                }
            };
        }
    }

    public static final class EnemyConstants
    {
        //1st Map
        public static final int SKELETON_MACER = 1;
        public static final int SHARDSOUL_SLAYER = 2;
        public static final int FLYING_DEMON = 3;
        public static final int HUNTRESS = 4;
        public static final int NECROMANCER = 5;
        public static final int LEAF_RANGER = 6;

        //2nd Map
        public static final int DEMON = 7;
        public static final int FIRE_WORM = 8;
        public static final int FIRE_WIZARD = 9;
        public static final int MINOTAUR = 10;
        public static final int FIRE_KNIGHT = 11;

        //3rd Map
        public static final int ICE_GOLEM = 12;
        public static final int ICE_BORNE = 13;
        public static final int SNOW_KNIGHT = 14;
        public static final int WEREWOLF = 15;
        public static final int VIKING = 16;

        // Final Boss
        public static final int FINAL_BOSS = 17;

        //1st Map
        public static final class SkeletonMacer
        {
            public static final int ATTACK1 = 0;
            public static final int ATTACK2 = 1;
            public static final int DEATH = 2;
            public static final int WALK = 3;
            public static final int IDLE = 4;
            public static final int TAKE_HIT = 5;

            public static final int WIDTH_DEFAULT = 64;
            public static final int HEIGHT_DEFAULT = 64;

            public static final int WIDTH = (int) (WIDTH_DEFAULT * SCALE);
            public static final int HEIGHT = (int) (HEIGHT_DEFAULT * SCALE);

            public static final int DRAWOFFSET_X = (int) (24 * SCALE);
            public static final int DRAWOFFSET_Y = (int) (16 * SCALE);
        }

        public static final class ShardsoulSlayer
        {
            public static final int IDLE = 0;
            public static final int WALK = 1;
            public static final int ATTACK = 2;
            public static final int TAKE_HIT = 3;
            public static final int DEATH = 4;

            public static final int WIDTH_DEFAULT = 64;
            public static final int HEIGHT_DEFAULT = 64;

            public static final int WIDTH = (int) (WIDTH_DEFAULT * SCALE);
            public static final int HEIGHT = (int) (HEIGHT_DEFAULT * SCALE);

            public static final int DRAWOFFSET_X = (int) (20 * SCALE);
            public static final int DRAWOFFSET_Y = (int) (32 * SCALE);
        }

        public static final class FlyingDemon
        {
            public static final int ATTACK = 0;
            public static final int DEATH = 1;
            public static final int WALK = 2;
            public static final int TAKE_HIT = 3;
            public static final int IDLE = 4;

            public static final int WIDTH_DEFAULT = 79;
            public static final int HEIGHT_DEFAULT = 69;

            public static final int WIDTH = (int) (WIDTH_DEFAULT * SCALE);
            public static final int HEIGHT = (int) (HEIGHT_DEFAULT * SCALE);

            public static final int DRAWOFFSET_X = (int) (18 * SCALE);
            public static final int DRAWOFFSET_Y = (int) (25 * SCALE);
        }

        public static final class Huntress
        {
            public static final int ATTACK1 = 0;
            public static final int ATTACK2 = 1;
            public static final int ATTACK3 = 2;
            public static final int DEATH = 3;
            public static final int IDLE = 4;
            public static final int WALK = 5;
            public static final int TAKE_HIT = 6;

            public static final int WIDTH_DEFAULT = 150;
            public static final int HEIGHT_DEFAULT = 150;

            public static final int WIDTH = (int) (WIDTH_DEFAULT * SCALE);
            public static final int HEIGHT = (int) (HEIGHT_DEFAULT * SCALE);

            public static final int DRAWOFFSET_X = (int) (65 * SCALE);
            public static final int DRAWOFFSET_Y = (int) (60 * SCALE);
        }

        public static final class Necromancer
        {
            public static final int WALK = 0;
            public static final int ATTACK1 = 1;
            public static final int ATTACK2 = 2;
            public static final int TAKE_HIT = 3;
            public static final int DEATH = 4;

            public static final int WIDTH_DEFAULT = 160;
            public static final int HEIGHT_DEFAULT = 128;

            public static final int WIDTH = (int) (WIDTH_DEFAULT * SCALE);
            public static final int HEIGHT = (int) (HEIGHT_DEFAULT * SCALE);

            public static final int DRAWOFFSET_X = (int) (71 * SCALE);
            public static final int DRAWOFFSET_Y = (int) (65 * SCALE);
        }

        public static final class LeafRanger
        {
            public static final int ATTACK1 = 0;
            public static final int ATTACK2 = 1;
            public static final int ATTACK3 = 2;
            public static final int ATTACK4 = 3;
            public static final int BLOCK = 4;
            public static final int IDLE = 5;
            public static final int DEATH = 6;
            public static final int TAKE_HIT = 7;
            public static final int WALK = 8;

            public static final int WIDTH_DEFAULT = 288;
            public static final int HEIGHT_DEFAULT = 130;

            public static final int WIDTH = (int) (WIDTH_DEFAULT * SCALE);
            public static final int HEIGHT = (int) (HEIGHT_DEFAULT * SCALE);

            public static final int DRAWOFFSET_X = (int) (133 * SCALE);
            public static final int DRAWOFFSET_Y = (int) (80 * SCALE);
        }

        //2nd Map
        public static final class Demon
        {
            public static final int ATTACK = 0;
            public static final int DEATH = 1;
            public static final int IDLE = 2;
            public static final int TAKE_HIT = 3;
            public static final int WALK = 4;

            public static final int WIDTH_DEFAULT = 288;
            public static final int HEIGHT_DEFAULT = 162;

            public static final int WIDTH = (int) (WIDTH_DEFAULT * SCALE);
            public static final int HEIGHT = (int) (HEIGHT_DEFAULT * SCALE);

            public static final int DRAWOFFSET_X = (int) (120 * SCALE);
            public static final int DRAWOFFSET_Y = (int) (80 * SCALE);
        }

        public static final class FireWorm
        {
            public static final int ATTACK = 0;
            public static final int DEATH = 1;
            public static final int TAKE_HIT = 2;
            public static final int WALK = 3;
            public static final int IDLE = 4;

            public static final int WIDTH_DEFAULT = 90;
            public static final int HEIGHT_DEFAULT = 90;

            public static final int WIDTH = (int) (WIDTH_DEFAULT * SCALE);
            public static final int HEIGHT = (int) (HEIGHT_DEFAULT * SCALE);

            public static final int DRAWOFFSET_X = (int) (25 * SCALE);
            public static final int DRAWOFFSET_Y = (int) (25 * SCALE);
        }

        public static final class FireWizard
        {
            public static final int ATTACK = 0;
            public static final int DEATH = 1;
            public static final int IDLE = 2;
            public static final int WALK = 3;
            public static final int TAKE_HIT = 4;

            public static final int WIDTH_DEFAULT = 150;
            public static final int HEIGHT_DEFAULT = 152;

            public static final int WIDTH = (int) (WIDTH_DEFAULT * SCALE);
            public static final int HEIGHT = (int) (HEIGHT_DEFAULT * SCALE);

            public static final int DRAWOFFSET_X = (int) (65 * SCALE);
            public static final int DRAWOFFSET_Y = (int) (55 * SCALE);
        }

        public static final class Minotaur
        {
            public static final int ATTACK1 = 0;
            public static final int ATTACK2 = 1;
            public static final int BLOCK = 2;
            public static final int DEATH = 3;
            public static final int TAKE_HIT = 4;
            public static final int IDLE = 5;
            public static final int WALK = 6;

            public static final int WIDTH_DEFAULT = 96;
            public static final int HEIGHT_DEFAULT = 98;

            public static final int WIDTH = (int) (WIDTH_DEFAULT * SCALE);
            public static final int HEIGHT = (int) (HEIGHT_DEFAULT * SCALE);

            public static final int DRAWOFFSET_X = (int) (32 * SCALE);
            public static final int DRAWOFFSET_Y = (int) (17 * SCALE);
        }

        public static final class FireKnight
        {
            public static final int ATTACK1 = 0;
            public static final int ATTACK2 = 1;
            public static final int ATTACK3 = 2;
            public static final int ATTACK4 = 3;
            public static final int BLOCK = 4;
            public static final int ROLL = 5;
            public static final int IDLE = 6;
            public static final int WALK = 7;
            public static final int DEATH = 8;
            public static final int TAKE_HIT = 9;

            public static final int WIDTH_DEFAULT = 288;
            public static final int HEIGHT_DEFAULT = 130;

            public static final int WIDTH = (int) (WIDTH_DEFAULT * SCALE);
            public static final int HEIGHT = (int) (HEIGHT_DEFAULT * SCALE);

            public static final int DRAWOFFSET_X = (int) (128 * SCALE);
            public static final int DRAWOFFSET_Y = (int) (80 * SCALE);
        }

        //3rd Map
        public static final class IceGolem
        {
            public static final int ATTACK = 0;
            public static final int DEATH = 1;
            public static final int IDLE = 2;
            public static final int TAKE_HIT = 3;
            public static final int WALK = 4;

            public static final int WIDTH_DEFAULT = 192;
            public static final int HEIGHT_DEFAULT = 130;

            public static final int WIDTH = (int) (WIDTH_DEFAULT * SCALE);
            public static final int HEIGHT = (int) (HEIGHT_DEFAULT * SCALE) + 70;

            public static final int DRAWOFFSET_X = (int) (70 * SCALE);
            public static final int DRAWOFFSET_Y = (int) (52 * SCALE);
        }

        public static final class IceBorne
        {
            public static final int ATTACK = 0;
            public static final int DEATH = 1;
            public static final int TAKE_HIT = 2;
            public static final int IDLE = 3;
            public static final int WALK = 4;

            public static final int WIDTH_DEFAULT = 80;
            public static final int HEIGHT_DEFAULT = 82;

            public static final int WIDTH = (int) (WIDTH_DEFAULT * SCALE * 1.2);
            public static final int HEIGHT = (int) (HEIGHT_DEFAULT * SCALE * 1.2);

            public static final int DRAWOFFSET_X = (int) (30 * SCALE);
            public static final int DRAWOFFSET_Y = (int) (44 * SCALE);
        }

        public static final class SnowKnight
        {
            public static final int ATTACK1 = 0;
            public static final int ATTACK2 = 1;
            public static final int ATTACK3 = 2;
            public static final int DEATH = 3;
            public static final int IDLE = 4;
            public static final int WALK = 5;
            public static final int TAKE_HIT = 6;

            public static final int WIDTH_DEFAULT = 162;
            public static final int HEIGHT_DEFAULT = 164;

            public static final int WIDTH = (int) (WIDTH_DEFAULT * SCALE);
            public static final int HEIGHT = (int) (HEIGHT_DEFAULT * SCALE);

            public static final int DRAWOFFSET_X = (int) (70 * SCALE);
            public static final int DRAWOFFSET_Y = (int) (54 * SCALE);
        }

        public static final class Werewolf
        {
            public static final int ATTACK1 = 0;
            public static final int ATTACK2 = 1;
            public static final int ATTACK3 = 2;
            public static final int ATTACK4 = 3;
            public static final int IDLE = 4;
            public static final int JUMP = 5;
            public static final int DEATH = 6;
            public static final int RUN = 7;
            public static final int TAKE_HIT = 8;
            public static final int WALK = 9;

            public static final int WIDTH_DEFAULT = 128;
            public static final int HEIGHT_DEFAULT = 130;

            public static final int WIDTH = (int) ((WIDTH_DEFAULT * SCALE) / 1.5);
            public static final int HEIGHT = (int) ((HEIGHT_DEFAULT * SCALE) / 1.5);

            public static final int DRAWOFFSET_X = (int) (20 * SCALE);
            public static final int DRAWOFFSET_Y = (int) (33 * SCALE);
        }

        public static final class Viking
        {
            public static final int ATTACK1 = 0;
            public static final int ATTACK2 = 1;
            public static final int ATTACK3 = 2;
            public static final int ATTACK4 = 3;
            public static final int BLOCK = 4;
            public static final int DEATH = 5;
            public static final int IDLE = 6;
            public static final int WALK = 7;
            public static final int TAKE_HIT = 8;

            public static final int WIDTH_DEFAULT = 115;
            public static final int HEIGHT_DEFAULT = 86;

            public static final int WIDTH = (int) (WIDTH_DEFAULT * SCALE);
            public static final int HEIGHT = (int) (HEIGHT_DEFAULT * SCALE);

            public static final int DRAWOFFSET_X = (int) (42 * SCALE);
            public static final int DRAWOFFSET_Y = (int) (25 * SCALE);
        }

        // Final Boss
        public static final class FinalBoss
        {
            public static final int ATTACK1 = 0;
            public static final int ATTACK2 = 1;
            public static final int ATTACK3 = 2;
            public static final int DEATH = 3;
            public static final int IDLE = 4;
            public static final int WALK = 5;
            public static final int TAKE_HIT = 6;

            public static final int WIDTH_DEFAULT = 160;
            public static final int HEIGHT_DEFAULT = 113;

            public static final int WIDTH = (int) (WIDTH_DEFAULT * SCALE);
            public static final int HEIGHT = (int) (HEIGHT_DEFAULT * SCALE);

            public static final int DRAWOFFSET_X = (int) (70 * SCALE);
            public static final int DRAWOFFSET_Y = (int) (57 * SCALE);
        }

        public static int GetSpriteAmountEnemy(int enemy_type, int enemy_state)
        {
            switch(enemy_type)
            {
                case SKELETON_MACER ->
                {
                    return switch(enemy_state)
                    {
                        case SkeletonMacer.DEATH -> 13;
                        case SkeletonMacer.WALK -> 12;
                        case SkeletonMacer.ATTACK1 -> 9;
                        case SkeletonMacer.ATTACK2 -> 8;
                        case SkeletonMacer.IDLE -> 4;
                        case SkeletonMacer.TAKE_HIT -> 3;
                        default -> 1;
                    };
                }

                case SHARDSOUL_SLAYER ->
                {
                    return switch(enemy_state)
                    {
                        case ShardsoulSlayer.DEATH, ShardsoulSlayer.ATTACK -> 6;
                        case ShardsoulSlayer.IDLE, ShardsoulSlayer.WALK -> 8;
                        case ShardsoulSlayer.TAKE_HIT -> 4;
                        default -> 1;
                    };
                }

                case FLYING_DEMON ->
                {
                    return switch(enemy_state)
                    {
                        case FlyingDemon.ATTACK -> 8;
                        case FlyingDemon.DEATH -> 6;
                        case FlyingDemon.IDLE, FlyingDemon.WALK, FlyingDemon.TAKE_HIT -> 4;
                        default -> 1;
                    };
                }

                case HUNTRESS ->
                {
                    return switch(enemy_state)
                    {
                        case Huntress.DEATH, Huntress.WALK, Huntress.IDLE -> 8;
                        case Huntress.ATTACK1, Huntress.ATTACK2 -> 6;
                        case Huntress.ATTACK3 -> 7;
                        case Huntress.TAKE_HIT -> 3;
                        default -> 1;
                    };
                }

                case NECROMANCER ->
                {
                    return switch(enemy_state)
                    {
                        case Necromancer.WALK -> 8;
                        case Necromancer.ATTACK1 -> 13;
                        case Necromancer.ATTACK2 -> 17;
                        case Necromancer.TAKE_HIT -> 5;
                        case Necromancer.DEATH -> 9;
                        default -> 1;
                    };
                }

                case LEAF_RANGER ->
                {
                    return switch(enemy_state)
                    {
                        case LeafRanger.ATTACK1, LeafRanger.WALK -> 10;
                        case LeafRanger.ATTACK2, LeafRanger.IDLE -> 12;
                        case LeafRanger.ATTACK3 -> 15;
                        case LeafRanger.ATTACK4 -> 17;
                        case LeafRanger.BLOCK, LeafRanger.DEATH -> 19;
                        case LeafRanger.TAKE_HIT -> 6;
                        default -> 1;
                    };
                }

                case DEMON ->
                {
                    return switch(enemy_state)
                    {
                        case Demon.ATTACK -> 15;
                        case Demon.DEATH -> 22;
                        case Demon.IDLE -> 6;
                        case Demon.TAKE_HIT -> 5;
                        case Demon.WALK -> 12;
                        default -> 1;
                    };
                }

                case FIRE_WORM ->
                {
                    return switch(enemy_state)
                    {
                        case FireWorm.ATTACK -> 16;
                        case FireWorm.DEATH -> 8;
                        case FireWorm.IDLE, FireWorm.WALK -> 9;
                        case FireWorm.TAKE_HIT -> 3;
                        default -> 1;
                    };
                }

                case FIRE_WIZARD ->
                {
                    return switch(enemy_state)
                    {
                        case FireWizard.ATTACK -> 11;
                        case FireWizard.DEATH -> 5;
                        case FireWizard.IDLE, FireWizard.WALK -> 8;
                        case FireWizard.TAKE_HIT -> 4;
                        default -> 1;
                    };
                }

                case MINOTAUR ->
                {
                    return switch(enemy_state)
                    {
                        case Minotaur.ATTACK1 -> 12;
                        case Minotaur.ATTACK2 -> 9;
                        case Minotaur.BLOCK, Minotaur.IDLE -> 5;
                        case Minotaur.DEATH -> 6;
                        case Minotaur.WALK -> 8;
                        case Minotaur.TAKE_HIT -> 3;
                        default -> 1;
                    };
                }

                case FIRE_KNIGHT ->
                {
                    return switch(enemy_state)
                    {
                        case FireKnight.ATTACK1 -> 11;
                        case FireKnight.ATTACK2 -> 12;
                        case FireKnight.ATTACK3 -> 9;
                        case FireKnight.ATTACK4 -> 18;
                        case FireKnight.BLOCK -> 10;
                        case FireKnight.ROLL, FireKnight.WALK, FireKnight.IDLE -> 8;
                        case FireKnight.DEATH -> 13;
                        case FireKnight.TAKE_HIT -> 6;
                        default -> 1;
                    };
                }

                case ICE_GOLEM ->
                {
                    return switch(enemy_state)
                    {
                        case IceGolem.ATTACK -> 14;
                        case IceGolem.DEATH -> 16;
                        case IceGolem.IDLE -> 6;
                        case IceGolem.TAKE_HIT -> 7;
                        case IceGolem.WALK -> 10;
                        default -> 1;
                    };
                }

                case ICE_BORNE ->
                {
                    return switch(enemy_state)
                    {
                        case IceBorne.ATTACK -> 12;
                        case IceBorne.DEATH -> 23;
                        case IceBorne.IDLE -> 9;
                        case IceBorne.TAKE_HIT -> 5;
                        case IceBorne.WALK -> 7;
                        default -> 1;
                    };
                }

                case SNOW_KNIGHT ->
                {
                    return switch(enemy_state)
                    {
                        case SnowKnight.ATTACK1, SnowKnight.ATTACK2, SnowKnight.ATTACK3, SnowKnight.DEATH -> 7;
                        case SnowKnight.WALK -> 8;
                        case SnowKnight.IDLE -> 10;
                        case SnowKnight.TAKE_HIT -> 3;
                        default -> 1;
                    };
                }

                case WEREWOLF ->
                {
                    return switch(enemy_state)
                    {
                        case Werewolf.ATTACK1 -> 6;
                        case Werewolf.ATTACK2 -> 4;
                        case Werewolf.ATTACK3 -> 5;
                        case Werewolf.ATTACK4 -> 7;
                        case Werewolf.IDLE -> 8;
                        case Werewolf.JUMP, Werewolf.WALK -> 11;
                        case Werewolf.DEATH, Werewolf.TAKE_HIT -> 2;
                        case Werewolf.RUN -> 9;
                        default -> 1;
                    };
                }

                case VIKING ->
                {
                    return switch(enemy_state)
                    {
                        case Viking.ATTACK1, Viking.ATTACK2, Viking.ATTACK3 -> 6;
                        case Viking.BLOCK, Viking.IDLE, Viking.WALK -> 8;
                        case Viking.DEATH, Viking.ATTACK4 -> 12;
                        case Viking.TAKE_HIT -> 4;
                        default -> 1;
                    };
                }

                case FINAL_BOSS ->
                {
                    return switch(enemy_state)
                    {
                        case FinalBoss.TAKE_HIT -> 4;
                        case FinalBoss.DEATH, FinalBoss.ATTACK1, FinalBoss.ATTACK2, FinalBoss.ATTACK3 -> 6;
                        case FinalBoss.WALK,FinalBoss.IDLE -> 8;
                        default -> 1;
                    };
                }
            }

            return 0;
        }

        public static int GetMaxHealth(int enemy_type)
        {
            return switch(enemy_type)
            {
                case SKELETON_MACER -> 60;
                case SHARDSOUL_SLAYER, HUNTRESS, ICE_BORNE -> 100;
                case NECROMANCER ->  80;
                case WEREWOLF -> 150;
                case VIKING -> 300;
                case FLYING_DEMON -> 70;
                case LEAF_RANGER -> 200;
                case DEMON -> 130;
                case FIRE_WORM -> 90;
                case FIRE_WIZARD -> 140;
                case MINOTAUR -> 110;
                case FIRE_KNIGHT, ICE_GOLEM -> 250;
                case SNOW_KNIGHT -> 270;
                case FINAL_BOSS -> 400;
                default -> 1;
            };
        }

        public static int GetEnemyDamage(int enemy_type, int attack)
        {
            switch(enemy_type)
            {
                case SKELETON_MACER ->
                {
                    return switch(attack)
                    {
                        case SkeletonMacer.ATTACK1 -> 10;
                        case SkeletonMacer.ATTACK2 -> 12;
                        default -> 0;
                    };
                }

                case SHARDSOUL_SLAYER -> { return 15; }

                case HUNTRESS ->
                {
                    return switch(attack)
                    {
                        case Huntress.ATTACK1 -> 15;
                        case Huntress.ATTACK2 -> 20;
                        default -> 0;
                    };
                }

                case LEAF_RANGER, DEMON -> { return 25; }

                case FIRE_WIZARD -> { return 7; }

                case MINOTAUR ->
                {
                    return switch(attack)
                    {
                        case Minotaur.ATTACK1 -> 25;
                        case Minotaur.ATTACK2 -> 30;
                        default -> 0;
                    };
                }

                case FIRE_KNIGHT ->
                {
                    return switch(attack)
                    {
                        case FireKnight.ATTACK1 -> 25;
                        case FireKnight.ATTACK2 -> 15;
                        case FireKnight.ATTACK3 -> 30;
                        case FireKnight.ATTACK4 -> 40;
                        default -> 0;
                    };
                }

                case ICE_GOLEM -> { return 35; }

                case ICE_BORNE -> 
                { 
                    return switch(attack)
                    {
                        case IceBorne.ATTACK -> 50;
                        case IceBorne.DEATH -> 25;
                        default -> 0;
                    };
                }

                case SNOW_KNIGHT ->
                {
                    return switch(attack)
                    {
                        case SnowKnight.ATTACK1, SnowKnight.ATTACK2 -> 30;
                        case SnowKnight.ATTACK3 -> 40;
                        default -> 0;
                    };
                }

                case WEREWOLF ->
                {
                    return switch(attack)
                    {
                        case Werewolf.ATTACK1, Werewolf.ATTACK2 -> 25;
                        case Werewolf.ATTACK3 -> 30;
                        case Werewolf.ATTACK4 -> 35;
                        default -> 0;
                    };
                }

                case VIKING ->
                {
                    return switch(attack)
                    {
                        case Viking.ATTACK1, Viking.ATTACK2 -> 35;
                        case Viking.ATTACK3 -> 40;
                        case Viking.ATTACK4 -> 50;
                        default -> 0;
                    };
                }

                case FINAL_BOSS ->
                {
                    return switch(attack)
                    {
                        case FinalBoss.ATTACK1, FinalBoss.ATTACK2 -> 20;
                        case FinalBoss.ATTACK3 -> 30;
                        default -> 0;
                    };
                }

                default -> { return 0; }
            }
        }
    }
}
