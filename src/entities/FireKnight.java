package entities;

import audio.AudioPlayer;
import objects.Projectile;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import static main.Game.SCALE;
import static main.Game.TILES_SIZE;
import static utils.Constants.EnemyConstants.FIRE_KNIGHT;
import static utils.Constants.EnemyConstants.FireKnight.*;
import static utils.Constants.Projectiles.*;

public final class FireKnight extends Enemy
{
    //AttackBox
    private int attackBoxOffsetX;

    private final Random random = new Random();

    private boolean death = false;

    private int attackDelayCounter;

    private int idleTimeCounter;

    private int blockTimeCounter;

    private int attackBoxOffsetY;

    public FireKnight(double x, double y, int tilesInHeight)
    {
        super(x, y, WIDTH, HEIGHT, tilesInHeight, FIRE_KNIGHT);
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
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.FIRE_KNIGHT_DEATH);
                playing.getGame().getAudioPlayer().playSong(playing.getLevelManager().getLevelIndex() + 1);
                death = true;
            }
        }
    }

    private void updateAttackBox()
    {
        if(flipW() == -1)
        {
            attackBox.x = hitBox.x + attackBoxOffsetX - (state == ATTACK1 ? 16 : (state == ATTACK3 ? 38 : (state == ATTACK4 ? 10 : 90))) * SCALE;
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
                    if(animationIndex == 0 || (state == ATTACK2 && animationTick == 0 && animationIndex == 7))
                    {
                        attackChecked = false;
                    }

                    if(animationTick == 0)
                    {
                        if(animationIndex == 4)
                        {
                            switch(state)
                            {
                                case ATTACK1 ->
                                {
                                    playing.getObjectHandler().getProjectiles().add(new Projectile((int) (hitBox.x + (flipW() == -1 ? (int)(67 * SCALE) : (int)(-67 * SCALE))), (int) (hitBox.y + (int)(3 * SCALE)), -flipW(), false, FIRE_SPIRAL, 3 * SCALE, FIRE_SPIRAL_WIDTH, FIRE_SPIRAL_HEIGHT));
                                    playing.getGame().getAudioPlayer().playEffect(AudioPlayer.FIRE_KNIGHT_ATTACK1);
                                }
                                case ATTACK2 -> playing.getGame().getAudioPlayer().playEffect(AudioPlayer.FIRE_KNIGHT_ATTACK2);
                                case ATTACK3 -> playing.getGame().getAudioPlayer().playEffect(AudioPlayer.FIRE_KNIGHT_ATTACK3);
                            }
                        }
                        else
                        {
                            if(state == ATTACK4 && animationIndex == 1)
                            {
                                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.FIRE_KNIGHT_ATTACK4);
                            }
                        }
                    }

                    if((state == ATTACK1 || state == ATTACK3 ? animationIndex == 4 : (state == ATTACK2 ? animationIndex == 7 || animationIndex == 3 : animationIndex == 12)) && !attackChecked)
                    {
                        checkEnemyHit(attackBox, player);
                    }
                }
                case ROLL -> move(levelData);
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

        if(state == ROLL && animationIndex == 7)
        {
            newState(IDLE);
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
                newState(ATTACK1 + (random.nextInt(5) != 0 ? random.nextInt(4) : (random.nextInt(4) < 2 ? 5 : 4)));
                switch(state)
                {
                    case ATTACK1 ->
                    {
                        attackBox.height = (int)(67 * SCALE);
                        attackBox.width = (int)(55 * SCALE);
                        attackBoxOffsetX = (int)(41 * SCALE);
                        attackBoxOffsetY = (int)(20 * SCALE);
                        knockbackFactor = 2;
                    }
                    case ATTACK2 ->
                    {
                        attackBox.height = (int)(52 * SCALE);
                        attackBox.width = (int)(132 * SCALE);
                        attackBoxOffsetX = (int)(45 * SCALE);
                        attackBoxOffsetY = (int)(5 * SCALE);
                        knockbackFactor = 0;
                    }
                    case ATTACK3 ->
                    {
                        attackBox.height = (int)(60 * SCALE);
                        attackBox.width = (int)(80 * SCALE);
                        attackBoxOffsetX = (int)(55 * SCALE);
                        attackBoxOffsetY = (int)(12 * SCALE);
                        knockbackFactor = 6;
                    }
                    case ATTACK4 ->
                    {
                        attackBox.height = (int)(60 * SCALE);
                        attackBox.width = (int)(50 * SCALE);
                        attackBoxOffsetX = (int)(53 * SCALE);
                        attackBoxOffsetY = (int)(12 * SCALE);
                        knockbackFactor = 12;
                    }
                }

                if(state != BLOCK)
                {
                    int MAX_ATTACK_DELAY = 480;
                    int MIN_ATTACK_DELAY = 300;
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
