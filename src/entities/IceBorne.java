package entities;

import audio.AudioPlayer;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import static main.Game.SCALE;
import static main.Game.TILES_SIZE;
import static utils.Constants.EnemyConstants.ICE_BORNE;
import static utils.Constants.EnemyConstants.IceBorne.*;

public final class IceBorne extends  Enemy
{
    //AttackBox
    private int attackBoxOffsetX;

    private final Random random = new Random();

    private boolean death = false;

    private int attackDelayCounter;

    private int idleTimeCounter;

    private final int attackBoxOffsetY = (int)(-23 * SCALE);

    public IceBorne(double x, double y, int tilesInHeight)
    {
        super(x, y, WIDTH, HEIGHT, tilesInHeight, ICE_BORNE);
        state = IDLE;
        walkSpeed = 1.8 * SCALE;
        initHitBox(34, 31, 0, 0);
        initAttackBox();
        knockbackFactor = 4;
        doesKnockBack = true;
    }

    private void initAttackBox()
    {
        attackBox = new Rectangle2D.Double(x, y + attackBoxOffsetY, (int)(50 * SCALE), (int)(55 * SCALE));
        attackBoxOffsetX = (int)(22 * SCALE);
    }

    public void update(int[][] levelData, Player player)
    {
        updateBehaviour(levelData, player);
        updateAnimationTick();
        updateAttackBox();
        if(state == DEATH)
        {
            attackBox.x = hitBox.x - 25;
            attackBox.width = (int)(60 * SCALE);
            attackBox.y = hitBox.y - 30;
            if(!death)
            {
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.ICE_BORNE_DEATH);
                death = true;
            }
            knockbackFactor = 6;
            if(animationIndex == 12)
            {
                checkEnemyHit(attackBox, player);
            }
        }
    }

    private void updateAttackBox()
    {
        if(flipW() == -1)
        {
            attackBox.x = hitBox.x + attackBoxOffsetX - (8 * SCALE);
        }
        else
        {
            attackBox.x = hitBox.x - attackBoxOffsetX - (6 * SCALE);
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
                                int MAX_IDLE_DURATION = 150;
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
                case ATTACK ->
                {
                    if(animationIndex == 0)
                    {
                        attackChecked = false;
                    }

                    if(animationIndex == 9 && !attackChecked)
                    {
                        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.ICE_BORNE_ATTACK);
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
        if (isPlayerCloseForAttack(player, 2.1))
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
                int MAX_ATTACK_DELAY = 360;
                int MIN_ATTACK_DELAY = 180;
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
