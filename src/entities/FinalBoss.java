package entities;

import audio.AudioPlayer;
import objects.Projectile;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import static main.Game.SCALE;
import static main.Game.TILES_SIZE;
import static utils.Constants.EnemyConstants.FINAL_BOSS;
import static utils.Constants.EnemyConstants.FinalBoss.*;
import static utils.Constants.Projectiles.*;

public final class FinalBoss extends Enemy
{
    //AttackBox
    private int attackBoxOffsetX;

    private final Random random = new Random();

    private boolean death = false;

    private int attackDelayCounter;

    private int idleTimeCounter;

    private int attackBoxOffsetY = 0;

    public FinalBoss(double x, double y, int tilesInHeight)
    {
        super(x, y, WIDTH, HEIGHT, tilesInHeight, FINAL_BOSS);
        changeWalkDir();
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
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.FINAL_BOSS_DEATH);
                playing.getGame().getAudioPlayer().playSong(playing.getLevelManager().getLevelIndex() + 1);
                death = true;
            }
        }
    }

    private void updateAttackBox()
    {
        if(flipW() == -1)
        {
            attackBox.x = hitBox.x + attackBoxOffsetX - (state == ATTACK2 ? 112 : (state == ATTACK3 ? 24 : 50)) * SCALE;
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
                    if (canSeePlayer(levelData, player, 20))
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
                    if (idleTimeCounter > 0 && !canSeePlayer(levelData, player, 20))
                    {
                        newState(IDLE);
                    }
                    else
                    {
                        if (canSeePlayer(levelData, player, 20))
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
                        switch(state)
                        {
                            case ATTACK1 ->
                            {
                                playing.getObjectHandler().getProjectiles().add(new Projectile((int) (hitBox.x + (flipW() == -1 ? hitBox.width : (int)(-67 * SCALE))), (int) (hitBox.y - 40), -flipW(), false, WIND_PROJECTILE, 1.5 * SCALE, WIND_PROJECTILE_WIDTH, WIND_PROJECTILE_HEIGHT));
                                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.FINAL_BOSS_ATTACK1);
                            }
                            case ATTACK2 ->
                            {
                                playing.getObjectHandler().getProjectiles().add(new Projectile((int) (hitBox.x + (flipW() == -1 ? (int)(67 * SCALE) : (int)(-67 * SCALE))), (int) (hitBox.y), -flipW(), false, WIND_VERTICAL_WAVE, 1.5 * SCALE, WIND_VERTICAL_WAVE_WIDTH, WIND_VERTICAL_WAVE_HEIGHT));
                                playing.getObjectHandler().getProjectiles().add(new Projectile((int) (hitBox.x + (flipW() == -1 ? (int)(-67 * SCALE) : (int)(67 * SCALE))), (int) (hitBox.y), flipW(), false, WIND_VERTICAL_WAVE, 1.5 * SCALE, WIND_VERTICAL_WAVE_WIDTH, WIND_VERTICAL_WAVE_HEIGHT));
                                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.FINAL_BOSS_ATTACK2);
                            }
                            case ATTACK3 ->
                            {
                                playing.getObjectHandler().getProjectiles().add(new Projectile((int) (hitBox.x + (flipW() == -1 ? (int)(60 * SCALE) : (int)(-67 * SCALE))), (int)(hitBox.y + (int)(26 * SCALE)), -flipW(), false, WIND_HORIZONTAL_WAVE, 1.25 * SCALE, WIND_HORIZONTAL_WAVE_WIDTH, WIND_HORIZONTAL_WAVE_HEIGHT));
                                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.FINAL_BOSS_ATTACK3);
                            }
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
                newState(ATTACK1 + random.nextInt(3));
                switch(state)
                {
                    case ATTACK1 ->
                    {
                        attackBox.width = (int)(80 * SCALE);
                        attackBox.height = (int)(47 * SCALE);
                        attackBoxOffsetX = (int)(50 * SCALE);
                        attackBoxOffsetY = 0;
                        knockbackFactor = 5;
                    }

                    case ATTACK2 ->
                    {
                        attackBox.width = (int)(143 * SCALE);
                        attackBox.height = (int)(51 * SCALE);
                        attackBoxOffsetX = (int)(58 * SCALE);
                        attackBoxOffsetY = (int)(-4 * SCALE);
                        knockbackFactor = 4;
                    }

                    case ATTACK3 ->
                    {
                        attackBox.width = (int)(53 * SCALE);
                        attackBox.height = (int)(86 * SCALE);
                        attackBoxOffsetX = (int)(54 * SCALE);
                        attackBoxOffsetY = (int)(-40 * SCALE);
                        knockbackFactor = 0;
                    }
                }
                int MAX_ATTACK_DELAY = 240;
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
