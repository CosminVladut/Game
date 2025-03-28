package entities;

import audio.AudioPlayer;
import objects.Projectile;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import static main.Game.SCALE;
import static main.Game.TILES_SIZE;
import static utils.Constants.EnemyConstants.LEAF_RANGER;
import static utils.Constants.EnemyConstants.LeafRanger.*;
import static utils.Constants.Projectiles.*;

public final class LeafRanger extends Enemy
{
    //AttackBox
    private int attackBoxOffsetX;

    private final Random random = new Random();

    private boolean death = false;

    private int attackDelayCounter;

    private int idleTimeCounter;

    private int blockTimeCounter;

    private int attackBoxOffsetY;

    public LeafRanger(double x, double y, int tilesInHeight)
    {
        super(x, y, WIDTH, HEIGHT, tilesInHeight, LEAF_RANGER);
        state = IDLE;
        walkSpeed = 1.8 * SCALE;
        initHitBox(22, 47, 0, 0);
        initAttackBox();
        doesKnockBack = false;
    }

    private void initAttackBox()
    {
        attackBox = new Rectangle2D.Double(x, y + attackBoxOffsetY, (int)(61 * SCALE), (int)(64 * SCALE));
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
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.LEAF_RANGER_DEATH);
                playing.getGame().getAudioPlayer().playSong(playing.getLevelManager().getLevelIndex() + 1);
                death = true;
            }
        }
    }

    private void updateAttackBox()
    {
        if(flipW() == -1)
        {
            attackBox.x = hitBox.x + attackBoxOffsetX - (state == ATTACK1 ? (57 * SCALE) : (30 * SCALE));
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

        if(playing.getObjectHandler().intersetsTouchBox(hitBox))
        {
            if(state != IDLE)
            {
                newState(IDLE);
            }
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
                    if (canSeePlayer(levelData, player, 14))
                    {
                        ifPlayerSeen(levelData, player);
                    }
                    else
                    {
                        if (idleTimeCounter > 0 || playing.getObjectHandler().intersetsTouchBox(hitBox))
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
                    if (idleTimeCounter > 0 && !canSeePlayer(levelData, player, 14))
                    {
                        newState(IDLE);
                    }
                    else
                    {
                        if (canSeePlayer(levelData, player, 14))
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
                case ATTACK1, ATTACK2, ATTACK3, ATTACK4 ->
                {
                    if(animationIndex == 0)
                    {
                        attackChecked = false;
                    }

                    if(animationTick == 0)
                    {
                        switch(state)
                        {
                            case ATTACK1 ->
                            {
                                if(animationIndex == 4)
                                {
                                    playing.getGame().getAudioPlayer().playEffect(AudioPlayer.LEAF_RANGER_ATTACK1);
                                }
                            }
                            case ATTACK2 ->
                            {
                                if(animationIndex == 10)
                                {
                                    playing.getObjectHandler().getProjectiles().add(new Projectile((int) (player.getHitBox().x - player.getHitBox().width / 2), (int) (hitBox.y + (int)(-67 * SCALE)), -flipW(), false, ROOTS, 0, ROOTS_WIDTH, ROOTS_HEIGHT));
                                    playing.getGame().getAudioPlayer().playEffect(AudioPlayer.LEAF_RANGER_ATTACK2);
                                }
                            }
                            case ATTACK3 ->
                            {
                                if(animationIndex == 8)
                                {
                                    playing.getObjectHandler().getProjectiles().add(new Projectile((int) (hitBox.x), (int) (hitBox.y + (int)(13 * SCALE)), -flipW(), false, ARROW, 4 * SCALE, ARROW_WIDTH, ARROW_HEIGHT));
                                    playing.getGame().getAudioPlayer().playEffect(AudioPlayer.LEAF_RANGER_ATTACK3);
                                }
                            }
                            case ATTACK4 ->
                            {
                                if(animationIndex == 9)
                                {
                                    playing.getObjectHandler().getProjectiles().add(new Projectile((int) (hitBox.x + (flipW() == -1 ? (int)(11 * SCALE) : (int)(-245 * SCALE))), (int) (hitBox.y + (int)(13 * SCALE)), -flipW(), false, GREEN_RAY, 0, GREEN_RAY_WIDTH, GREEN_RAY_HEIGHT));
                                    playing.getGame().getAudioPlayer().playEffect(AudioPlayer.LEAF_RANGER_ATTACK4);
                                }
                            }
                        }
                    }

                    if(((state == ATTACK1 && animationIndex == 4) || (state == ATTACK4 && animationIndex == 9)) && !attackChecked)
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
            if(state == BLOCK && animationIndex == 7)
            {
                newState(BLOCK);
                animationIndex = 7;
            }
            --blockTimeCounter;
        }
    }

    private void ifPlayerSeen(int[][] levelData, Player player)
    {
        turnTowardsPlayer(player);
        int MAX_ATTACK_DELAY = 180;
        int MIN_ATTACK_DELAY = 120;
        if (isPlayerCloseForAttack(player, 4.5))
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
                newState(ATTACK1 + (random.nextInt(5) != 0 ? random.nextInt(2) : 4));
                attackBox.height = (int)(40 * SCALE);
                attackBox.width = (int)(90 * SCALE);
                attackBoxOffsetX = (int)(60 * SCALE);
                attackBoxOffsetY = (int)(-2 * SCALE);
                knockbackFactor = 7;

                if(state != BLOCK)
                {
                    attackDelayCounter = MIN_ATTACK_DELAY + random.nextInt(MAX_ATTACK_DELAY - MIN_ATTACK_DELAY + 1);
                }
                else
                {
                    int MAX_BLOCK_DURATION = 180;
                    int MIN_BLOCK_DURATION = 60;
                    blockTimeCounter = MIN_BLOCK_DURATION + random.nextInt(MAX_BLOCK_DURATION - MIN_BLOCK_DURATION + 1);
                }
            }
        }
        else
        {
            if(isPlayerCloseForAttack(player, 10))
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
                    newState(ATTACK2 + random.nextInt(3));
                    attackBox.width = 0;
                    attackBox.height = 0;
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
