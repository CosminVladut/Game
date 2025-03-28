package entities;

import audio.AudioPlayer;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import static main.Game.SCALE;
import static main.Game.TILES_SIZE;
import static utils.Constants.EnemyConstants.SKELETON_MACER;
import static utils.Constants.EnemyConstants.SkeletonMacer.*;

public final class SkeletonMacer extends Enemy
{
    //AttackBox
    private int attackBoxOffsetX;

    private final Random random = new Random();

    private boolean death = false;

    private int attackDelayCounter;

    private int idleTimeCounter;

    public SkeletonMacer(double x, double y, int tilesInHeight)
    {
        super(x, y, WIDTH, HEIGHT, tilesInHeight, SKELETON_MACER);
        state = IDLE;
        walkSpeed = 0.5 * SCALE;
        initHitBox(18, 31, 0, 0);
        initAttackBox();
        doesKnockBack = false;
    }

    private void initAttackBox()
    {
        attackBox = new Rectangle2D.Double(x, y, (int)(41 * SCALE), (int)(31 * SCALE));
        attackBoxOffsetX = (int)(13 * SCALE);
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
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.SKELETON_MACER_DEATH);
                death = true;
            }
        }
    }

    private void updateAttackBox()
    {
        if(flipW() == -1)
        {
            attackBox.x = hitBox.x + attackBoxOffsetX - (13 * SCALE);
        }
        else
        {
            attackBox.x = hitBox.x - attackBoxOffsetX - (10 * SCALE);
        }
        attackBox.y = hitBox.y;
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
                    if (canSeePlayer(levelData, player, 3.5))
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
                    if (idleTimeCounter > 0 && !canSeePlayer(levelData, player, 3.5))
                    {
                        newState(IDLE);
                    }
                    else
                    {
                        if (canSeePlayer(levelData, player, 3.5))
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
                case ATTACK1, ATTACK2 ->
                {
                    if(animationIndex == 0)
                    {
                        attackChecked = false;

                    }

                    if(animationIndex == 4 && !attackChecked)
                    {
                        if (random.nextInt(2) == 0)
                        {
                            playing.getGame().getAudioPlayer().playEffect(AudioPlayer.SKELETON_MACER_ATTACK1);
                        }
                        else
                        {
                            playing.getGame().getAudioPlayer().playEffect(AudioPlayer.SKELETON_MACER_ATTACK2);
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
                newState(ATTACK1 + random.nextInt(2));
                int MAX_ATTACK_DELAY = 240;
                int MIN_ATTACK_DELAY = 60;
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
