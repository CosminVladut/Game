package entities;

import audio.AudioPlayer;
import objects.Projectile;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import static main.Game.SCALE;
import static main.Game.TILES_SIZE;
import static utils.Constants.EnemyConstants.MINOTAUR;
import static utils.Constants.EnemyConstants.Minotaur.*;
import static utils.Constants.Projectiles.*;

public final class Minotaur extends Enemy
{
    //AttackBox
    private int attackBoxOffsetX;

    private final Random random = new Random();

    private boolean death = false;

    private int attackDelayCounter;

    private int idleTimeCounter;

    private int blockTimeCounter;

    private int attackBoxOffsetY;

    public Minotaur(double x, double y, int tilesInHeight)
    {
        super(x, y, WIDTH, HEIGHT, tilesInHeight, MINOTAUR);
        state = IDLE;
        walkSpeed = 1.8 * SCALE;
        initHitBox(30, 47, 0, 0);
        initAttackBox();
        doesKnockBack = true;
    }

    private void initAttackBox()
    {
        attackBox = new Rectangle2D.Double(x, y + attackBoxOffsetY, (int)(40 * SCALE), (int)(60 * SCALE));
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
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.MINOTAUR_DEATH);
                death = true;
            }
        }
    }

    private void updateAttackBox()
    {
        if(flipW() == -1)
        {
            attackBox.x = hitBox.x + attackBoxOffsetX - (state == ATTACK2 ? (53 * SCALE) : 0);
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
                case ATTACK1, ATTACK2 ->
                {
                    if(animationIndex == 0)
                    {
                        attackChecked = false;
                    }

                    if(animationTick == 0)
                    {
                        if(state == ATTACK1 && animationIndex == 4)
                        {
                            playing.getObjectHandler().getProjectiles().add(new Projectile((int) (hitBox.x), (int) (hitBox.y - (int)(13 * SCALE)), -flipW(), false, VERTICAL_FIRE, 3 * SCALE, VERTICAL_FIRE_WIDTH, VERTICAL_FIRE_HEIGHT));
                            playing.getGame().getAudioPlayer().playEffect(AudioPlayer.MINOTAUR_ATTACK1);
                        }
                        else
                        {
                            if(state == ATTACK2)
                            {
                                switch(animationIndex)
                                {
                                    case 3 -> playing.getGame().getAudioPlayer().playEffect(AudioPlayer.MINOTAUR_ATTACK2);
                                    case 4 -> playing.getObjectHandler().getProjectiles().add(new Projectile((int) (hitBox.x), (int) (hitBox.y + (int)(27 * SCALE)), flipW(), false, HORIZONTAL_FIRE, 2 * SCALE, HORIZONTAL_FIRE_WIDTH, HORIZONTAL_FIRE_HEIGHT));
                                    case 5 -> playing.getObjectHandler().getProjectiles().add(new Projectile((int) (hitBox.x), (int) (hitBox.y + (int)(27 * SCALE)), -flipW(), false, HORIZONTAL_FIRE, 2 * SCALE, HORIZONTAL_FIRE_WIDTH, HORIZONTAL_FIRE_HEIGHT));
                                }
                            }
                        }
                    }

                    if((state == ATTACK1 ? animationIndex == 4 : animationIndex == 3) && !attackChecked)
                    {
                        checkEnemyHit(attackBox, player);
                    }
                }
            }
        }

        if (attackDelayCounter > 0)
        {
            --attackDelayCounter;
        }

        if(blockTimeCounter > 0)
        {
            if(state == BLOCK && animationIndex == 4)
            {
                newState(BLOCK);
            }
            --blockTimeCounter;
        }
    }

    private void ifPlayerSeen(int[][] levelData, Player player)
    {
        turnTowardsPlayer(player);
        if (isPlayerCloseForAttack(player, 5))
        {
            if (attackDelayCounter > 0 || blockTimeCounter > 0)
            {
                if(state != IDLE)
                {
                    newState(IDLE);
                }
            }
            else
            {
                newState(ATTACK1 + (random.nextInt(5) != 0 ? random.nextInt(2) : 2));
                if(state == ATTACK1)
                {
                    attackBox.height = (int)(60 * SCALE);
                    attackBox.width = (int)(40 * SCALE);
                    attackBoxOffsetX = (int)(14 * SCALE);
                    attackBoxOffsetY = (int)(12 * SCALE);
                    knockbackFactor = 3;
                }
                else
                {
                    attackBox.height = (int)(21 * SCALE);
                    attackBox.width = (int)(94 * SCALE);
                    attackBoxOffsetX = (int)(20 * SCALE);
                    attackBoxOffsetY = (int)(-27 * SCALE);
                    knockbackFactor = 5;
                }

                if(state != BLOCK)
                {
                    int MAX_ATTACK_DELAY = 420;
                    int MIN_ATTACK_DELAY = 180;
                    attackDelayCounter = MIN_ATTACK_DELAY + random.nextInt(MAX_ATTACK_DELAY - MIN_ATTACK_DELAY + 1);
                }
                else
                {
                    int MAX_BLOCK_DURATION = 240;
                    int MIN_BLOCK_DURATION = 120;
                    blockTimeCounter = MIN_BLOCK_DURATION + random.nextInt(MAX_BLOCK_DURATION - MIN_BLOCK_DURATION + 1);
                }
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
