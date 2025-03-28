package entities;

import audio.AudioPlayer;
import objects.Projectile;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import static main.Game.SCALE;
import static main.Game.TILES_SIZE;
import static utils.Constants.EnemyConstants.SNOW_KNIGHT;
import static utils.Constants.EnemyConstants.SnowKnight.*;
import static utils.Constants.Projectiles.*;

public final class SnowKnight extends Enemy
{
    //AttackBox
    private int attackBoxOffsetX;

    private final Random random = new Random();

    private boolean death = false;

    private int attackDelayCounter;

    private int idleTimeCounter;

    private int attackBoxOffsetY = 0;

    public SnowKnight(double x, double y, int tilesInHeight)
    {
        super(x, y, WIDTH, HEIGHT, tilesInHeight, SNOW_KNIGHT);
        state = IDLE;
        walkSpeed = 1.6 * SCALE;
        initHitBox(20, 47, 0, 0);
        initAttackBox();
        doesKnockBack = true;
    }

    private void initAttackBox()
    {
        attackBox = new Rectangle2D.Double(x, y + attackBoxOffsetY, (int)(53 * SCALE), (int)(47 * SCALE));
        attackBoxOffsetX = (int)(29 * SCALE);
    }

    public void update(int[][] levelData, Player player)
    {
        updateBehaviour(levelData, player);
        updateAnimationTick();
        updateAttackBox();
        if(state == DEATH)
        {
            if(!death)
            {
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.SNOW_KNIGHT_DEATH);
                death = true;
            }
        }
    }

    private void updateAttackBox()
    {
        if(flipW() == -1)
        {
            attackBox.x = hitBox.x + attackBoxOffsetX - (state == ATTACK2 ? 70 : 21) * SCALE;
        }
        else
        {
            attackBox.x = hitBox.x - attackBoxOffsetX - (10 * SCALE);
        }
        attackBox.y = hitBox.y + attackBoxOffsetY;
    }

    private void updateBehaviour(int[][] levelData, Player player)
    {
        if(firstUpdate)
        {
            firstUpdateCheck(levelData);
        }

        if(inAir)
        {
            updateInAir(levelData);
        }
        else
        {
            switch(state)
            {
                case IDLE ->
                {
                    if (canSeePlayer(levelData, player, 9))
                    {
                        ifPlayerSeen(levelData, player);
                    }
                    else
                    {
                        if (idleTimeCounter > 0)
                        {
                            --idleTimeCounter;
                        }
                        else
                        {
                            newState(WALK);
                        }
                    }
                }
                case WALK ->
                {
                    if (idleTimeCounter > 0 && !canSeePlayer(levelData, player, 9))
                    {
                        newState(IDLE);
                    }
                    else
                    {
                        if (canSeePlayer(levelData, player, 9))
                        {
                            ifPlayerSeen(levelData, player);
                        }
                        else
                        {
                            if(random.nextInt(2000) < 10)
                            {
                                newState(IDLE);
                                int MAX_IDLE_DURATION = 180;
                                int MIN_IDLE_DURATION = 90;
                                idleTimeCounter = MIN_IDLE_DURATION + random.nextInt(MAX_IDLE_DURATION - MIN_IDLE_DURATION + 1);
                            }
                            else
                            {
                                if (state != WALK)
                                {
                                    newState(WALK);
                                }
                                move(levelData);
                            }
                        }
                    }
                    tileY = (int)(hitBox.y / TILES_SIZE);
                }
                case ATTACK1, ATTACK2, ATTACK3 ->
                {
                    if(animationIndex == 0)
                    {
                        attackChecked = false;
                    }


                    if(animationIndex == 4 && !attackChecked)
                    {
                        if(state != ATTACK3)
                        {
                            if (random.nextInt(2) == 0)
                            {
                                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.SNOW_KNIGHT_ATTACK1);
                            }
                            else
                            {
                                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.SNOW_KNIGHT_ATTACK2);
                            }
                        }
                        else
                        {
                            playing.getObjectHandler().getProjectiles().add(new Projectile((int)(hitBox.x + (flipW() == -1 ? hitBox.width : (int)(-67 * SCALE))), (int)(hitBox.y + (int)(-13 * SCALE)), -flipW(), false, ICE_TORNADO, 2 * SCALE, ICE_TORNADO_WIDTH, ICE_TORNADO_HEIGHT));
                            playing.getGame().getAudioPlayer().playEffect(AudioPlayer.SNOW_KNIGHT_ATTACK3);
                        }
                        checkEnemyHit(attackBox, player);
                    }
                }
            }
        }

        if (attackDelayCounter > 0)
        {
            --attackDelayCounter;
        }
    }

    private void ifPlayerSeen(int[][] levelData, Player player)
    {
        turnTowardsPlayer(player);
        if (state == ATTACK3 ? isPlayerCloseForAttack(player, 8) : isPlayerCloseForAttack(player, 2.5))
        {
            if (attackDelayCounter > 0)
            {
                if(state != IDLE)
                {
                    newState(IDLE);
                }
            }
            else
            {
                newState(ATTACK1 + random.nextInt(3));
                switch(state)
                {
                    case ATTACK1 ->
                    {
                        attackBox.width = (int)(53 * SCALE);
                        attackBox.height = (int)(47 * SCALE);
                        attackBoxOffsetX = (int)(29 * SCALE);
                        attackBoxOffsetY = 0;
                        knockbackFactor = 3;
                    }

                    case ATTACK2 ->
                    {
                        attackBox.width = (int)(102 * SCALE);
                        attackBox.height = (int)(54 * SCALE);
                        attackBoxOffsetX = (int)(25 * SCALE);
                        attackBoxOffsetY = (int)(7 * SCALE);
                        knockbackFactor = 2;
                    }

                    case ATTACK3 ->
                    {
                        attackBox.width = (int)(53 * SCALE);
                        attackBox.height = (int)(60 * SCALE);
                        attackBoxOffsetX = (int)(34 * SCALE);
                        attackBoxOffsetY = (int)(-13 * SCALE);
                        knockbackFactor = 0;
                    }
                }
                int MIN_ATTACK_DELAY = 300;
                int MAX_ATTACK_DELAY = 420;
                attackDelayCounter = MIN_ATTACK_DELAY + random.nextInt(MAX_ATTACK_DELAY - MIN_ATTACK_DELAY + 1);
            }
        }
        else
        {
            if(state != WALK)
            {
                newState(WALK);
            }
            move(levelData);
        }
    }
}
