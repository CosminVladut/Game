package entities;

import audio.AudioPlayer;
import objects.Projectile;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import static main.Game.SCALE;
import static main.Game.TILES_SIZE;
import static utils.Constants.EnemyConstants.VIKING;
import static utils.Constants.EnemyConstants.Viking.*;
import static utils.Constants.Projectiles.*;

public final class Viking extends Enemy
{
    //AttackBox
    private int attackBoxOffsetX;

    private final Random random = new Random();

    private boolean death = false;

    private int attackDelayCounter;

    private int idleTimeCounter;

    private int blockTimeCounter;

    private int attackBoxOffsetY;

    public Viking(double x, double y, int tilesInHeight)
    {
        super(x, y, WIDTH, HEIGHT, tilesInHeight, VIKING);
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
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.VIKING_DEATH);
                playing.getGame().getAudioPlayer().playSong(playing.getLevelManager().getLevelIndex() + 1);
                death = true;
            }
        }
    }

    private void updateAttackBox()
    {
        if(flipW() == -1)
        {
            attackBox.x = hitBox.x + attackBoxOffsetX - (state == ATTACK1 ? -8 : (state == ATTACK2 ? -11 : (state == ATTACK3 ? -6 : 0))) * SCALE;
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
                    if (canSeePlayer(levelData, player, 10))
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
                case ATTACK1, ATTACK2, ATTACK3, ATTACK4 ->
                {
                    if(animationIndex == 0)
                    {
                        attackChecked = false;
                    }

                    if(animationTick == 0)
                    {
                        if(animationIndex == 3)
                        {
                            switch(state)
                            {
                                case ATTACK1, ATTACK2 ->
                                {
                                    playing.getObjectHandler().getProjectiles().add(new Projectile((int) (hitBox.x + (flipW() == -1 ? (int)(10 * SCALE) : (int)(-10 * SCALE))), (int) (hitBox.y + (int)(3 * SCALE)), -flipW(), false, ICE_SPIRAL, 2 * SCALE, ICE_SPIRAL_WIDTH, ICE_SPIRAL_HEIGHT));
                                    if(random.nextInt(2) == 0)
                                    {
                                        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.VIKING_ATTACK1);
                                    }
                                    else
                                    {
                                        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.VIKING_ATTACK2);
                                    }
                                }
                                case ATTACK3 ->
                                {
                                    playing.getObjectHandler().getProjectiles().add(new Projectile((int) (hitBox.x + (flipW() == -1 ? (int)(40 * SCALE) : (int)(-67 * SCALE))), (int) (hitBox.y + (int)(-26 * SCALE)), -flipW(), false, ICE_PILLAR, 0, ICE_PILLAR_WIDTH, ICE_PILLAR_HEIGHT));
                                    playing.getGame().getAudioPlayer().playEffect(AudioPlayer.VIKING_ATTACK3);
                                }
                            }
                        }
                        else
                        {
                            if(animationIndex == 5 && state == ATTACK4)
                            {
                                playing.getObjectHandler().getProjectiles().add(new Projectile((int) (hitBox.x + (flipW() == -1 ? (int)(20 * SCALE) : (int)(-115 * SCALE))), (int) (hitBox.y + (int)(22 * SCALE)), -flipW(), false, FROZEN_FLAME, 0.75 * SCALE, FROZEN_FLAME_WIDTH, FROZEN_FLAME_HEIGHT));
                                playing.getObjectHandler().getProjectiles().add(new Projectile((int) (hitBox.x + (flipW() == -1 ? (int)(-115 * SCALE) : (int)(20 * SCALE))), (int) (hitBox.y + (int)(22 * SCALE)), flipW(), false, FROZEN_FLAME, 0.75 * SCALE, FROZEN_FLAME_WIDTH, FROZEN_FLAME_HEIGHT));
                                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.VIKING_ATTACK4);
                            }
                        }
                    }

                    if((state == ATTACK4 ? animationIndex == 5: animationIndex == 3) && !attackChecked)
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
        if (isPlayerCloseForAttack(player, 4))
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
                newState(ATTACK1 + (random.nextInt(10) < 8 ? random.nextInt(3) : (random.nextInt(10) < 6 ? 4 : 3)));
                switch(state)
                {
                    case ATTACK1 ->
                    {
                        attackBox.height = (int)(47 * SCALE);
                        attackBox.width = (int)(33 * SCALE);
                        attackBoxOffsetX = (int)(6 * SCALE);
                        attackBoxOffsetY = 0;
                        knockbackFactor = 3;
                    }
                    case ATTACK2 ->
                    {
                        attackBox.height = (int)(47 * SCALE);
                        attackBox.width = (int)(31 * SCALE);
                        attackBoxOffsetX = (int)(3 * SCALE);
                        attackBoxOffsetY = 0;
                        knockbackFactor = 3;
                    }
                    case ATTACK3 ->
                    {
                        attackBox.height = (int)(47 * SCALE);
                        attackBox.width = (int)(35 * SCALE);
                        attackBoxOffsetX = (int)(10 * SCALE);
                        attackBoxOffsetY = 0;
                        knockbackFactor = 2;
                    }
                    case ATTACK4 ->
                    {
                        attackBox.height = (int)(16 * SCALE);
                        attackBox.width = (int)(100 * SCALE);
                        attackBoxOffsetX = (int)(25 * SCALE);
                        attackBoxOffsetY = (int)(-31 * SCALE);
                        knockbackFactor = 5;
                    }
                }

                if(state != BLOCK)
                {
                    int MAX_ATTACK_DELAY = 180;
                    int MIN_ATTACK_DELAY = 60;
                    attackDelayCounter = MIN_ATTACK_DELAY + random.nextInt(MAX_ATTACK_DELAY - MIN_ATTACK_DELAY + 1);
                }
                else
                {
                    int MAX_BLOCK_DURATION = 210;
                    int MIN_BLOCK_DURATION = 90;
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
