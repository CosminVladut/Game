package entities;

import audio.AudioPlayer;
import objects.Projectile;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import static main.Game.SCALE;
import static main.Game.TILES_SIZE;
import static utils.Constants.Directions.LEFT;
import static utils.Constants.EnemyConstants.WEREWOLF;
import static utils.Constants.EnemyConstants.Werewolf.*;
import static utils.Constants.Projectiles.*;
import static utils.HelperMethods.IsEntityOnFloor;

public final class Werewolf extends Enemy
{
    //AttackBox
    private int attackBoxOffsetX;

    private final Random random = new Random();

    private int attackDelayCounter;

    private int idleTimeCounter;

    private boolean jumpStarted = false;

    public Werewolf(double x, double y, int tilesInHeight)
    {
        super(x, y, WIDTH, HEIGHT, tilesInHeight, WEREWOLF);
        state = IDLE;
        walkSpeed = 0.5 * SCALE;
        initHitBox(40, 53, 0, (int)(10 * SCALE));
        initAttackBox();
        doesKnockBack = false;
    }

    private void initAttackBox()
    {
        attackBox = new Rectangle2D.Double(x, y, (int)(30 * SCALE), (int)(53 * SCALE));
    }

    public void update(int[][] levelData, Player player)
    {
        updateBehaviour(levelData, player);
        updateAnimationTick();
        updateAttackBox();
        if(!IsEntityOnFloor(hitBox, levelData, true) && state != JUMP)
        {
            changeWalkDir();
        }
        if(state == DEATH)
        {
            if(!death)
            {
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.WEREWOLF_DEATH);
                death = true;
            }
        }
    }

    private void updateAttackBox()
    {
        if(flipW() == -1)
        {
            attackBox.x = hitBox.x + attackBoxOffsetX - (state == ATTACK1 ? 19 : (state == ATTACK3 ? 5 : 0)) * SCALE;
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
                    if(shouldJump(player, levelData) && !jumpStarted)
                    {
                        jump(levelData);
                    }
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
                            walkSpeed = 0.5 * SCALE;
                            newState(WALK);
                        }
                    }
                }
                case WALK, RUN ->
                {
                    if(shouldJump(player, levelData) && !jumpStarted)
                    {
                        jump(levelData);
                    }
                    if (idleTimeCounter > 0 && !canSeePlayer(levelData, player, 12) && state != RUN)
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
                                int MIN_IDLE_DURATION = 90;
                                int MAX_IDLE_DURATION = 180;
                                idleTimeCounter = MIN_IDLE_DURATION + random.nextInt(MAX_IDLE_DURATION - MIN_IDLE_DURATION + 1);
                            }
                            else
                            {
                                if (state != WALK)
                                {
                                    walkSpeed = 0.5 * SCALE;
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

                    if(state == ATTACK1 && animationIndex < 4)
                    {
                        move(levelData);
                    }

                    if(state == ATTACK4 && animationIndex < 5)
                    {
                        move(levelData);
                    }

                    if(animationTick == 0)
                    {
                            switch(state)
                            {
                                case ATTACK1 ->
                                {
                                    if(animationIndex == 4)
                                    {
                                        playing.getObjectHandler().getProjectiles().add(new Projectile((int) (hitBox.x + (flipW() == -1 ? (int)(35 * SCALE) : (int)(-35 * SCALE))), (int) (hitBox.y + (int)(3 * SCALE)), -flipW(), false, FROZEN_SHOCKWAVE, 0, FROZEN_SHOCKWAVE_WIDTH, FROZEN_SHOCKWAVE_HEIGHT));
                                        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.WEREWOLF_ATTACK1);
                                    }
                                }
                                case ATTACK2 ->
                                {
                                    if(animationIndex == 2)
                                    {
                                        playing.getObjectHandler().getProjectiles().add(new Projectile((int) (hitBox.x + (flipW() == -1 ? (int)(35 * SCALE) : (int)(-35 * SCALE))), (int) (hitBox.y + (int)(3 * SCALE)), -flipW(), false, FROZEN_SHOCKWAVE, 0, FROZEN_SHOCKWAVE_WIDTH, FROZEN_SHOCKWAVE_HEIGHT));
                                        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.WEREWOLF_ATTACK2);
                                    }
                                }
                                case ATTACK3 ->
                                {
                                    if(animationIndex == 3)
                                    {
                                        playing.getObjectHandler().getProjectiles().add(new Projectile((int) (hitBox.x + (flipW() == -1 ? (int)(35 * SCALE) : (int)(-35 * SCALE))), (int) (hitBox.y + (int)(3 * SCALE)), -flipW(), false, FROZEN_SHOCKWAVE, 0, FROZEN_SHOCKWAVE_WIDTH, FROZEN_SHOCKWAVE_HEIGHT));
                                        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.WEREWOLF_ATTACK3);
                                    }
                                }
                                case ATTACK4 ->
                                {

                                    if(animationIndex == 5)
                                    {
                                        playing.getObjectHandler().getProjectiles().add(new Projectile((int) (hitBox.x + (flipW() == -1 ? (int)(35 * SCALE) : (int)(-35 * SCALE))), (int) (hitBox.y + (int)(3 * SCALE)), -flipW(), false, FROZEN_SHOCKWAVE, 0, FROZEN_SHOCKWAVE_WIDTH, FROZEN_SHOCKWAVE_HEIGHT));
                                        playing.getGame().getAudioPlayer().playEffect(AudioPlayer.WEREWOLF_ATTACK4);
                                    }
                                }
                            }
                    }

                    if((state == ATTACK1 ? animationIndex == 4 : (state == ATTACK2 ? animationIndex == 2 : (state == ATTACK3 ? animationIndex == 3 : animationIndex == 5))) && !attackChecked)
                    {
                        checkEnemyHit(attackBox, player);
                    }
                }
                case JUMP ->
                {
                    if(animationIndex == 10)
                    {
                        newState(IDLE);
                    }
                    jumpStarted = false;
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
        if(state != JUMP)
        {
            turnTowardsPlayer(player);
            Rectangle2D.Double hitBoxForAttacks = new Rectangle2D.Double(walkDir == LEFT ? hitBox.x - TILES_SIZE : hitBox.x + hitBox.width + TILES_SIZE, hitBox.y, hitBox.width, hitBox.height);
            int MAX_ATTACK_DELAY = 360;
            int MIN_ATTACK_DELAY = 240;
            if(isPlayerCloseForAttack(player, 4))
            {
                if(attackDelayCounter > 0)
                {
                    if(state != IDLE)
                    {
                        newState(IDLE);
                    }
                }
                else
                {
                    if(!IsEntityOnFloor(hitBoxForAttacks, levelData, true))
                    {
                        newState(ATTACK1 + random.nextInt(3));
                    }
                    else
                    {
                        newState(ATTACK2 + random.nextInt(2));
                    }

                    if(state == ATTACK1)
                    {
                        walkSpeed = 0.5 * SCALE;
                    }
                    switch(state)
                    {
                        case ATTACK1 ->
                        {
                            attackBox.width = (int) (60 * SCALE);
                            attackBoxOffsetX = (int) (15 * SCALE);
                        }
                        case ATTACK2 ->
                        {
                            attackBox.width = (int) (35 * SCALE);
                            attackBoxOffsetX = (int) (15 * SCALE);
                        }
                        case ATTACK3 ->
                        {
                            attackBox.width = (int) (40 * SCALE);
                            attackBoxOffsetX = (int) (15 * SCALE);
                        }
                    }
                    attackDelayCounter = MIN_ATTACK_DELAY + random.nextInt(MAX_ATTACK_DELAY - MIN_ATTACK_DELAY + 1);
                }
            }
            else
            {
                if(isPlayerCloseForAttack(player, 6))
                {
                    if(attackDelayCounter > 0)
                    {
                        if(state != IDLE)
                        {
                            newState(IDLE);
                        }
                    }
                    else
                    {
                        if(IsEntityOnFloor(hitBoxForAttacks, levelData, true))
                        {
                            newState(ATTACK4);
                            walkSpeed = 2 * SCALE;
                            attackBox.width = (int) (40 * SCALE);
                            attackBoxOffsetX = (int) (15 * SCALE);
                            attackDelayCounter = MIN_ATTACK_DELAY + random.nextInt(MAX_ATTACK_DELAY - MIN_ATTACK_DELAY + 1);
                        }
                    }
                }
                else
                {
                    if(state != RUN)
                    {
                        walkSpeed = 1.75 * SCALE;
                        newState(RUN);
                    }
                    move(levelData);
                }
            }
        }
    }

    private void jump(int[][] levelData)
    {
        newState(JUMP);
        walkSpeed = 4 * SCALE;
        inAir = true;
        jumpStarted = true;
        airSpeed = -3;
        move(levelData);
    }

    private boolean shouldJump(Player player, int[][] levelData)
    {
        Rectangle2D.Double hitBoxForJump = new Rectangle2D.Double(walkDir == LEFT ? hitBox.x - TILES_SIZE : hitBox.x + hitBox.width, hitBox.y, hitBox.width, hitBox.height);
        return canSeePlayer(levelData, player, 12) && !IsEntityOnFloor(hitBoxForJump, levelData, true);
    }
}
