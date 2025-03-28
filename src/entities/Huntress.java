package entities;

import audio.AudioPlayer;
import objects.Projectile;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import static main.Game.SCALE;
import static main.Game.TILES_SIZE;
import static utils.Constants.EnemyConstants.HUNTRESS;
import static utils.Constants.EnemyConstants.Huntress.*;
import static utils.Constants.Projectiles.*;

public final class Huntress extends Enemy
{
    //AttackBox
    private int attackBoxOffsetX;

    private final Random random = new Random();

    private boolean death = false;

    private int attackDelayCounter;

    private int idleTimeCounter;

    private final int attackBoxOffsetY = (int)(27 * SCALE);

    public Huntress(double x, double y, int tilesInHeight)
    {
        super(x, y, WIDTH, HEIGHT, tilesInHeight, HUNTRESS);
        state = IDLE;
        walkSpeed = 1.8 * SCALE;
        initHitBox(22, 37, 0, (int)(10 * SCALE));
        initAttackBox();
        knockbackFactor = 5;
        doesKnockBack = true;
    }

    private void initAttackBox()
    {
        attackBox = new Rectangle2D.Double(x, y + attackBoxOffsetY, (int)(61 * SCALE), (int)(64 * SCALE));
        attackBoxOffsetX = (int)(30 * SCALE);
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
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.HUNTRESS_DEATH);
                death = true;
            }
        }
    }

    private void updateAttackBox()
    {
        if(flipW() == -1)
        {
            attackBox.x = hitBox.x + attackBoxOffsetX - (28 * SCALE);
        }
        else
        {
            attackBox.x = hitBox.x - attackBoxOffsetX - (10 * SCALE);
        }
        attackBox.y = hitBox.y - attackBoxOffsetY;
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
                    if (canSeePlayer(levelData, player, 10))
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
                    if (idleTimeCounter > 0 && !canSeePlayer(levelData, player, 10))
                    {
                        newState(IDLE);
                    }
                    else
                    {
                        if (canSeePlayer(levelData, player, 10))
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

                    if(state == ATTACK3 && animationIndex == 6 && animationTick == 0)
                    {
                        playing.getObjectHandler().getProjectiles().add(new Projectile((int)(hitBox.x), (int)(hitBox.y + (int)(8 * SCALE)), -flipW(), false, SPEAR, 2 * SCALE, SPEAR_WIDTH, SPEAR_HEIGHT));
                        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.HUNTRESS_ATTACK3);
                    }
                    else
                    {
                        if (animationIndex == 4 && !attackChecked)
                        {
                            if (random.nextInt(2) == 0)
                            {
                                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.HUNTRESS_ATTACK1);
                            }
                            else
                            {
                                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.HUNTRESS_ATTACK2);
                            }
                            checkEnemyHit(attackBox, player);
                        }
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
        int MAX_ATTACK_DELAY = 240;
        int MIN_ATTACK_DELAY = 120;
        if (isPlayerCloseForAttack(player, 3.8))
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
                newState(ATTACK1 + random.nextInt(2));
                attackDelayCounter = MIN_ATTACK_DELAY + random.nextInt(MAX_ATTACK_DELAY - MIN_ATTACK_DELAY + 1);
            }
        }
        else
        {
            if(isPlayerCloseForAttack(player, 8))
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
                    newState(ATTACK3);
                    attackDelayCounter = MIN_ATTACK_DELAY + random.nextInt(MAX_ATTACK_DELAY - MIN_ATTACK_DELAY + 1);
                }
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
}
