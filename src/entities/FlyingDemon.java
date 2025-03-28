package entities;

import audio.AudioPlayer;
import objects.Projectile;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import static main.Game.SCALE;
import static main.Game.TILES_SIZE;
import static utils.Constants.EnemyConstants.FLYING_DEMON;
import static utils.Constants.EnemyConstants.FlyingDemon.*;
import static utils.Constants.Projectiles.*;

public final class FlyingDemon extends  Enemy
{
    private final Random random = new Random();

    private boolean death = false;

    private int attackDelayCounter;

    private int idleTimeCounter;

    public FlyingDemon(double x, double y, int tilesInHeight)
    {
        super(x, y, WIDTH, HEIGHT, tilesInHeight, FLYING_DEMON);
        state = IDLE;
        walkSpeed = 1.7 * SCALE;
        initHitBox(43, 30, 0, 0);
        initAttackBox();
        doesKnockBack = false;
    }

    private void initAttackBox()
    {
        attackBox = new Rectangle2D.Double(x, y, 0, 0);
    }

    public void update(int[][] levelData, Player player)
    {
        updateBehaviour(levelData, player);
        updateAnimationTick();
        if(state == DEATH)
        {
            if(!death)
            {
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.FLYING_DEMON_DEATH);
                death = true;
            }
        }
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
                    if (canSeePlayer(levelData, player, 12))
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
                    if (idleTimeCounter > 0 && !canSeePlayer(levelData, player, 12))
                    {
                        newState(IDLE);
                    }
                    else
                    {
                        if (canSeePlayer(levelData, player, 12))
                        {
                            ifPlayerSeen(levelData, player);
                        }
                        else
                        {
                            if(random.nextInt(2000) < 10)
                            {
                                newState(IDLE);
                                int MAX_IDLE_DURATION = 60;
                                int MIN_IDLE_DURATION = 30;
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

                case ATTACK ->
                {
                    if(animationIndex == 3 && animationTick == 0)
                    {
                        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.FLYING_DEMON_ATTACK);
                        playing.getObjectHandler().getProjectiles().add(new Projectile((int)(hitBox.x), (int)(hitBox.y + (int)(8 * SCALE)), -flipW(), false, FIRE_BALL, 2 * SCALE, FIRE_BALL_WIDTH, FIRE_BALL_HEIGHT));
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
        if (isPlayerCloseForAttack(player, 10))
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
                newState(ATTACK);
                int MAX_ATTACK_DELAY = 300;
                int MIN_ATTACK_DELAY = 240;
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
