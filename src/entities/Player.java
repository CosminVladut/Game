package entities;

import audio.AudioPlayer;
import gamestates.Playing;
import objects.Barrier;
import utils.CustomLogger;
import utils.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.util.ArrayList;

import static main.Game.SCALE;
import static main.Game.TILES_SIZE;
import static utils.Constants.ANIMATION_SPEED;
import static utils.Constants.GRAVITY;
import static utils.Constants.PlayerConstants.*;
import static utils.HelperMethods.*;

public final class Player extends Entity
{
    private static Player instance = null;
    private BufferedImage[][] animations;
    private boolean left, up, right, down, jump;
    private boolean moving = false, attacking = false;
    private int[][] levelData;
    private final int width = (int)(128 * SCALE);
    private final int height = (int)(66 * SCALE);
    private final double xDrawOffset = 56 * SCALE;
    private final double yDrawOffset = 18 * SCALE;

    //Pentru gravitatie
    private final double jumpSpeed = -10.25;
    private final double fallSpeedAfterCollision = 0.4 * SCALE;

    //Pentru UI
    private BufferedImage[] statusBar;
    private final int statusBarWidth = (int)(96 * SCALE);
    private final int statusBarHeight = (int)(64 * SCALE);
    private final int statusBarX = (int)(10 * SCALE);
    private final int statusBarY = (int)(10 * SCALE);

    private int progressIndex;
    private final int healthBarWidth= (int)(52 * SCALE);
    private final int healthBarHeight= (int)(6 * SCALE);
    private final int healthBarXStart= (int)(36 * SCALE);
    private final int healthBarYStart= (int)(3 * SCALE);
    private int healthWidth = healthBarWidth;

    private final int energyBarWidth = (int)(52 * SCALE);
    private final int energyBarHeight= (int)(6 * SCALE);
    private final int energyBarXStart= (int)(36 * SCALE);
    private final int energyBarYStart= (int)(3 * SCALE);
    private int energyWidth = energyBarWidth;
    private final int energyMaxValue = 200;
    private int energyValue = energyMaxValue;
    private final int energyGrowSpeed = 15;
    private int energyGrowTick;

    private int flipX = 0;
    private int flipW = 1;

    private int attackNumber = -1;
    private int numberOfCrystals = 5;
    private boolean attackChecked = false;
    private boolean canChangeHealth = true;
    private boolean canChangeAttackBox = true;
    private boolean crouching = false;
    private boolean healing = false;
    private boolean healSound = true;
    private boolean dropAttackSound = true;
    private boolean canDropAttack = false;
    private boolean wasAttack = false;
    private int framesUntilResetAttack = 0;
    private final Playing playing;

    public boolean isHit;

    private int tileY;
    private boolean dodging;
    private int dodgeTick;

    private boolean knockback = false;
    private int knockbackTick;
    private double knockbackSpeed;
    private int tileToSpawn;
    private double yBeforeAction = y;

    private boolean praying = false;
    private boolean talking = false;
    private boolean interacting = false;
    private boolean startCounting = false;
    private int framesUntilAbleToTalkAgain = 60;
    private boolean willCollide = false;
    private ArrayList<Barrier> barriers;
    private int potionsUsed = 0;
    private int damageTaken = 0;
    private double xOfSaveSpot = 0;

    private Player(double x, double y, int width, int height, Playing playing)
    {
        super(x, y, width, height, 3);
        this.playing = playing;
        state = IDLE;
        maxHealth = 100;
        currentHealth = maxHealth;
        walkSpeed = 1.75 * SCALE;
        loadAnimations();
        initHitBox(16, 47, 0, 0);
        initAttackBox();
        if(playing.getDatabaseHandler().getGameState().isContinueGame == 1)
        {
            flipX = playing.getDatabaseHandler().getPlayerState().flipX;
            flipW = playing.getDatabaseHandler().getPlayerState().flipW;
            damageTaken = playing.getDatabaseHandler().getPlayerState().damageTaken;
            potionsUsed = playing.getDatabaseHandler().getPlayerState().potionsUsed;
        }
        else
        {
            playing.getDatabaseHandler().updatePlayerState(null, null, flipX, flipW, damageTaken, potionsUsed);
        }
    }

    public void setSpawn(Point spawn)
    {
        x = spawn.x;
        y = spawn.y;
        hitBox.x = x;
        hitBox.y = y;
        playing.getDatabaseHandler().updatePlayerState(x, y, null, null, null, null);
    }

    private void initAttackBox()
    {
        attackBox = new Rectangle2D.Double(x, y, (int)(18 * SCALE), (int)(47 * SCALE));
    }

    public static Player createPlayer(double x, double y, int width, int height, Playing playing)
    {
        if(instance == null)
        {
            instance = new Player(x, y, width, height, playing);
        }
        return instance;
    }

    public void update()
    {
        updateHealthBar();
        updateEnergyBar();
        willCollide = false;

        progressIndex = playing.getLevelManager().getLevelIndex() != 4 && playing.getLevelManager().getLevelIndex() != 0 ? playing.getLevelManager().getLevelIndex() - 1 : progressIndex;
        progressIndex = playing.getLevelManager().getLevelIndex() != 4 && playing.getLevelManager().getLevelIndex() != 0 ? playing.getLevelManager().getLevelIndex() - 1 : progressIndex;

        if(!inAir)
        {
            tileToSpawn = (int)((hitBox.y + hitBox.height + TILES_SIZE) / TILES_SIZE);
            playing.checkObjectNear(hitBox);
        }

        playing.getObjectHandler().checkTouchBoxTouched(hitBox);

        if(startCounting)
        {
            --framesUntilAbleToTalkAgain;
        }

        if(framesUntilAbleToTalkAgain == 0)
        {
            startCounting = false;
            framesUntilAbleToTalkAgain = 60;
        }

        if(wasAttack)
        {
            ++framesUntilResetAttack;
        }

        if(framesUntilResetAttack == 60)
        {
            attackNumber = -1;
            wasAttack = false;
            framesUntilResetAttack = 0;
        }

        if(currentHealth <= 0)
        {
              if(state != DEATH)
              {
                  state = DEATH;
                  animationTick = 0;
                  animationIndex = 0;
                  playing.setPlayerDying(true);
                  playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DEATH);

                  if (!IsEntityOnFloor(hitBox, levelData, false))
                  {
                      inAir = true;
                      canChangeHealth = true;
                      dodging = false;
                      crouching = false;
                      hitBox.height = (int)(47 * SCALE);
                      hitBox.y = y;
                      attackBox.y = hitBox.y;
                      attackBox.height = hitBox.height;
                      airSpeed = 0;
                  }
              }
              else
              {
                  healing = false;
                  jump = false;
                  if(animationIndex == GetSpriteAmount(DEATH) - 1 && animationTick >= ANIMATION_SPEED - 1)
                  {
                      playing.setGameOver(true);
                      playing.getGame().getAudioPlayer().stopSong();
                  }
                  else
                  {
                      updateAnimationTick();
                      if (inAir)
                      {
                          if (CanMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, levelData, false))
                          {
                              hitBox.y += airSpeed;
                              airSpeed += GRAVITY;
                          }
                          else
                          {
                              inAir = false;
                          }
                      }
                  }
              }
              return;
        }

        if(!praying && !talking)
        {
            updateAttackBox();
            checkFiresTouched();
            if(moving)
            {
                checkSpikesTouched();
                tileY = (int) (hitBox.y / TILES_SIZE);
                if(dodging)
                {
                    attacking = false;
                    ++dodgeTick;
                    if(dodgeTick >= 15)
                    {
                        dodgeTick = 0;
                        dodging = false;
                        canChangeHealth = true;
                    }
                }
            }

            if(attacking)
            {
                dodging = false;
                healing = false;
                if(!inAir)
                {
                    moving = false;
                }
                setAnimations();
                checkAttack();
                if(inAir || knockback)
                {
                    crouching = false;
                    updatePosition();
                }
            }
            else
            {
                updatePosition();
            }
        }

        updateAnimationTick();
        setAnimations();
    }

    private void checkSpikesTouched()
    {
        playing.checkSpikesTouched(this);
    }

    private void checkFiresTouched()
    {
        playing.checkFiresTouched(this);
    }

    private void checkAttack()
    {
        if(attackChecked || (state != ATTACKS4 && state != DROP_ATTACK ? animationIndex != 1 : animationIndex != 3))
        {
            return;
        }

        attackChecked = true;
        playing.checkEnemyHit(attackBox);
        if(state != DROP_ATTACK)
        {
            if(state != ATTACKS4)
            {
                playing.getGame().getAudioPlayer().playAttackSound();
            }
            else
            {
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.LAST_HIT);
            }
        }
    }

    public void changeEnergy(int value)
    {
        energyValue += value;
        if(energyValue >= energyMaxValue)
        {
            energyValue = energyMaxValue;
        }
        else
        {
            if(energyValue <= 0)
            {
                energyValue = 0;
            }
        }
    }

    public void updateAttackBox()
    {
        if(canChangeAttackBox || knockback)
        {
            if (right && left)
            {
                if (flipW == 1)
                {
                    attackBox.x = hitBox.x + hitBox.width + (int) (10 * SCALE);
                }
                else
                {
                    attackBox.x = hitBox.x - hitBox.width - (int) (12 * SCALE);
                }
            }
            else
            {
                if ((right || (dodging && flipW == 1)) || (attackBox.x == hitBox.x - 30 && state != DROP_ATTACK && flipW == 1) || (knockback && flipW == 1))
                {
                    attackBox.x = hitBox.x + hitBox.width + (int) (10 * SCALE);
                }
                else
                {
                    if ((left || (dodging && flipW == -1)) || (attackBox.x == hitBox.x - 30 && state != DROP_ATTACK && flipW == -1) || (knockback && flipW == -1))
                    {
                        attackBox.x = hitBox.x - hitBox.width - (int) (12 * SCALE);
                    }
                }
            }
        }
        if(state != DROP_ATTACK)
        {
            attackBox.width = (int)(18 * SCALE);
        }
        attackBox.y = hitBox.y;
    }

    private void updateHealthBar()
    {
        healthWidth = (int)((currentHealth / (double)maxHealth) * healthBarWidth);
    }

    private void updateEnergyBar()
    {
        energyWidth = (int)((energyValue / (double)energyMaxValue) * energyBarWidth);
        ++energyGrowTick;
        if(energyGrowTick >= energyGrowSpeed)
        {
            energyGrowTick = 0;
            changeEnergy(7);
        }
    }

    public void render(Graphics g, int levelOffset)
    {
        if(!crouching)
        {
            g.drawImage(animations[state][animationIndex], (int) (hitBox.x - xDrawOffset) - levelOffset + flipX, (int) (hitBox.y - yDrawOffset), width * flipW, height, null);
        }
        else
        {
            g.drawImage(animations[state][animationIndex], (int) (hitBox.x - xDrawOffset) - levelOffset + flipX, (int) (hitBox.y - 30 - yDrawOffset), width * flipW, height, null);
        }

//        drawHitBox(g, levelOffset);
//        drawAttackBox(g, levelOffset);
        drawUI(g);
    }

    public void drawUI(Graphics g)
    {
        g.drawImage(statusBar[progressIndex], statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        try
        {
            g.setColor(new Color(255, 255, 255, 255));
            g.drawString(String.valueOf(numberOfCrystals), 133, 130);
        }
        catch(IllegalArgumentException e)
        {
            CustomLogger.logException("Culoare invalida.", e);
        }
        catch(NullPointerException e)
        {
            CustomLogger.logException("Sir de caractere nul.", e);
        }
        g.setColor(Color.RED);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
        g.setColor(Color.YELLOW);
        g.fillRect(energyBarXStart + statusBarX, energyBarYStart + statusBarY * 2, energyWidth, energyBarHeight);
    }

    private void loadAnimations()
    {
        BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_SPRITESHEET);
        animations = new BufferedImage[17][12];

        for(int i = 0; i < animations.length; ++i)
        {
            for(int j = 0; j < animations[i].length; ++j)
            {
                try
                {
                    animations[i][j] = image.getSubimage(j * 128, i * 66, 128, 66);
                }
                catch(RasterFormatException e)
                {
                    CustomLogger.logException("Partea asta nu exista in spritesheet-ul jucatorului.", e);
                }
            }
        }
        
        image = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
        statusBar = new BufferedImage[4];
        for(int i = 0; i < statusBar.length; ++i)
        {
            try
            {
                statusBar[i] = image.getSubimage(i * 96, 0, 96, 64);
            }
            catch(RasterFormatException e)
            {
                CustomLogger.logException("Partea asta nu exista in bara de status.", e);
            }
        }
    }

    public void loadLevelData(int[][] levelData)
    {
        this.levelData = levelData;
        if(!IsEntityOnFloor(hitBox, levelData, false))
        {
            inAir = true;
            canChangeHealth = true;
            dodging = false;
            crouching = false;
            hitBox.height = (int)(47 * SCALE);
            hitBox.y = y;
            attackBox.y = hitBox.y;
            attackBox.height = hitBox.height;
        }
    }

    public void resetBooleans()
    {
        left = right = up = down = false;
    }

    public void setAttack(boolean value)
    {
        attacking = value;
        if(attacking && !inAir)
        {
            canChangeAttackBox = false;
        }
    }

    public void incrementAttackNumber()
    {
        if(crouching)
        {
            attackNumber = (attackNumber + 1) % 2;
        }
        else
        {
            attackNumber = (attackNumber + 1) % 4;
        }
    }

    private void setAnimations()
    {
        int startAnimation = state;

        if(!praying)
        {
            if(!talking)
            {
                if(attacking)
                {
                    healing = false;
                    if(crouching)
                    {
                        switch(attackNumber)
                        {
                            case 0 -> state = CROUCH_ATTACKS1;
                            case 1 -> state = CROUCH_ATTACKS2;
                        }
                    }
                    else
                    {
                        if(state != DROP_ATTACK)
                        {
                            switch(attackNumber)
                            {
                                case 0 -> state = ATTACKS1;
                                case 1 -> state = ATTACKS2;
                                case 2 -> state = ATTACKS3;
                                case 3 -> state = ATTACKS4;
                            }
                        }
                        if(canDropAttack && down && inAir)
                        {
                            if(dropAttackSound)
                            {
                                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DROP_ATTACK);
                                dropAttackSound = false;
                                attackBox.width = 60 + hitBox.width;
                            }
                            state = DROP_ATTACK;
                        }
                    }
                }
                else
                {
                    if(!crouching)
                    {
                        if(moving)
                        {
                            if(!dodging)
                            {
                                up = false;
                                state = RUN;
                            }
                        }
                        else
                        {
                            state = IDLE;
                        }


                        if(inAir)
                        {
                            up = false;
                            if(airSpeed < 0)
                            {
                                state = JUMP;
                            }
                            else
                            {
                                state = FALL;
                            }
                        }

                        if(moving && !inAir)
                        {
                            attackNumber = -1;
                        }
                    }
                    else
                    {
                        state = CROUCH_IDLE;
                    }

                    if(healing && !inAir && !dodging && !isHit)
                    {
                        state = HEAL;
                        attackNumber = -1;
                        hitBox.y = crouching ? yBeforeAction - 30 : yBeforeAction;
                        crouching = false;
                        up = false;
                    }

                    if(up)
                    {
                        if(crouching)
                        {
                            state = IDLE;
                            if(!inAir)
                            {
                                hitBox.height = (int) (47 * SCALE);
                                hitBox.y = yBeforeAction;
                                attackBox.y = hitBox.y;
                                attackBox.height = hitBox.height;
                            }
                        }
                        crouching = false;
                        attackNumber = -1;
                    }

                    if(dodging && !inAir && !healing)
                    {
                        state = ROLL;
                        attackNumber = -1;
                    }

                    if(isHit)
                    {
                        state = TAKE_HIT;
                        canDropAttack = false;
                    }
                }
            }
            else
            {
                state = IDLE;
                resetBooleans();
                isHit = false;
                inAir = false;
                attacking = false;
                moving = false;
                dodging = false;
                canChangeHealth = true;
                canChangeAttackBox = false;
                healSound = false;
                healing = false;
                canDropAttack = false;
                dropAttackSound = true;
                knockback = false;
                dodgeTick = 0;
                airSpeed = 0;
                while(CanMoveHere(hitBox.x, hitBox.y + 0.01, hitBox.width, hitBox.height, levelData, false))
                {
                    hitBox.y += 0.01;
                }
            }
        }
        else
        {
            if(crouching && animationIndex == 0 && animationTick == 0)
            {
                hitBox.y = hitBox.y + 30;
            }

            state = PRAY;
            resetBooleans();
            isHit = false;
            inAir = false;
            attacking = false;
            moving = false;
            dodging = false;
            canChangeHealth = true;
            canChangeAttackBox = false;
            healSound = false;
            healing = false;
            canDropAttack = false;
            dropAttackSound = true;
            knockback = false;
            numberOfCrystals = 5;
            attackNumber = -1;
            currentHealth = maxHealth;
            energyValue = energyMaxValue;
            dodgeTick = 0;
            airSpeed = 0;
            x = xOfSaveSpot;
            y = hitBox.y;
            playing.getDatabaseHandler().updatePlayerState(xOfSaveSpot, hitBox.y, 0, 1, null, null);
        }

        if(startAnimation != state)
        {
            resetAnimationTick();
        }
    }

    private void resetAnimationTick()
    {
        animationTick = 0;
        animationIndex = 0;
    }

    private void updatePosition()
    {
        moving = false;
        if(knockback)
        {
            airSpeed = 0;
            if (knockbackTick < 5)
            {
                updateXPos(knockbackSpeed);
                ++knockbackTick;
            }
            else
            {
                canChangeHealth = true;
                knockback = false;
            }
        }

        double xSpeed = 0;

        if(!knockback)
        {
            if(jump && !dodging && !healing)
            {
                jump();
            }

            if(!inAir && !dodging)
            {
                if((!left && !right) || (left && right))
                {
                    return;
                }
            }

            if(left && !right)
            {
                if(!crouching)
                {
                    xSpeed -= walkSpeed;
                }
                else
                {
                    xSpeed = 0;
                }
                flipX = width;
                flipW = -1;
            }

            if (right && !left)
            {
                if(!crouching)
                {
                    xSpeed += walkSpeed;
                }
                else
                {
                    xSpeed = 0;
                }
                flipX = 0;
                flipW = 1;
            }

            if(dodging && !inAir)
            {
                if(flipW == -1)
                {
                    xSpeed = -walkSpeed;
                }
                else
                {
                    xSpeed = walkSpeed;
                }
                xSpeed *= 3;
            }

            if(!inAir && (isHit || healing))
            {
                xSpeed = 0;
            }
        }

        if(!inAir)
        {
            if(!IsEntityOnFloor(hitBox, levelData, false))
            {
                inAir = true;
                canChangeHealth = true;
                hitBox.height = (int)(47 * SCALE);
                dodging = false;
                crouching = false;
            }
        }

        if(inAir)
        {
            if(state == DROP_ATTACK)
            {
                xSpeed = 0;
                airSpeed += 3.5 * GRAVITY;
                attackBox.x = hitBox.x - 30;
                down = true;
            }

            if(CanMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, levelData, false))
            {
                hitBox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
            }
            else
            {
                canChangeAttackBox = false;
                canDropAttack = false;
                down = false;
                if(airSpeed > 0)
                {
                    while(CanMoveHere(hitBox.x, hitBox.y + 0.01, hitBox.width, hitBox.height, levelData, false))
                    {
                        hitBox.y += 0.01;
                    }
                    resetInAir();
                    attackNumber = -1;
                }
                else
                {
                    airSpeed = fallSpeedAfterCollision;
                }
                updateXPos(xSpeed);
            }
        }
        else
        {
            canChangeAttackBox = true;
            updateXPos(xSpeed);
        }

        moving = true;
    }

    private void jump()
    {
        if(inAir)
        {
            return;
        }

        if(energyValue >= 20)
        {
            changeEnergy(-20);
            playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
            inAir = true;
            canChangeHealth = true;
            dodging = false;
            airSpeed = jumpSpeed;
            hitBox.height = (int)(47 * SCALE);
            hitBox.y = crouching ? yBeforeAction - 30 : yBeforeAction;
            attackBox.y = hitBox.y;
            attackBox.height = hitBox.height;
        }
        crouching = false;
    }

    public void dodge()
    {
        if(dodging || inAir || attacking || healing || praying || talking)
        {
            return;
        }

        if(energyValue >= 40)
        {
            dodging = true;
            canChangeHealth = false;
            changeEnergy(-40);
        }
    }

    private void resetInAir()
    {
        inAir = false;
        airSpeed = 0;
    }

    public void resetAll()
    {
        resetBooleans();
        isHit = false;
        inAir = false;
        attacking = false;
        moving = false;
        dodging = false;
        praying = false;
        talking = false;
        interacting = false;
        canChangeHealth = true;
        canChangeAttackBox = true;
        crouching = false;
        healing = false;
        canDropAttack = false;
        healSound = true;
        dropAttackSound = true;
        knockback = false;
        numberOfCrystals = 5;
        attackNumber = -1;
        state = IDLE;
        currentHealth = maxHealth;
        energyValue = energyMaxValue;

        dodgeTick = 0;
        airSpeed = 0;
        attackBox.x = hitBox.x + hitBox.width + (int)(10 * SCALE);
        attackBox.y = hitBox.y;
        hitBox.height = (int)(47 * SCALE);
        attackBox.height = hitBox.height;
        hitBox.x = x;
        hitBox.y = y;
        if(playing.getLevelManager().getLevelIndex() == playing.getLevelManager().getHowManyLevels() - 1)
        {
            flipX = width;
            flipW = -1;
        }
        else
        {
            flipX = 0;
            flipW = 1;
        }
        if(!IsEntityOnFloor(hitBox, levelData, false))
        {
            inAir = true;
            dodging = false;
        }
        playing.getDatabaseHandler().updatePlayerState(x, y, flipX, flipW, null, null);
    }

    public void updateXPos(double xSpeed)
    {
        for(Barrier barrier : barriers)
        {
            if(barrier.isActive() && barrier.willCollide(hitBox, xSpeed))
            {
                willCollide = true;
                break;
            }
        }

        if(CanMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, levelData, false) && !willCollide)
        {
            hitBox.x += xSpeed;
        }
        else
        {
            hitBox.x = GetEntityXPosNextToWall(hitBox);
            if(dodging)
            {
                dodging = false;
                canChangeHealth = true;
                dodgeTick = 0;
            }
        }
    }

    public void changeHealth(int value, boolean fromRight, boolean isKnockback, double factor)
    {
        if(canChangeHealth)
        {
            currentHealth += value;

            if(value < 0)
            {
                damageTaken -= value;
                playing.getDatabaseHandler().updatePlayerState(null, null, null, null, damageTaken, null);
            }

            if (currentHealth <= 0)
            {
                currentHealth = 0;
                //gameOver();
            }
            else
            {
                if (currentHealth >= maxHealth)
                {
                    currentHealth = maxHealth;
                }
            }

            if(value == -7)
            {
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.BURN);
            }

            if(!healing && value != 0 && value != -7)
            {
                isHit = true;
                yBeforeAction = hitBox.y;
                knockback = isKnockback;
                if(knockback)
                {
                    canChangeHealth = false;
                    knockbackTick = 0;
                    knockbackSpeed = Math.abs(factor) * walkSpeed;
                    if (fromRight)
                    {
                        knockbackSpeed = -knockbackSpeed;
                    }
                }
                if(!inAir)
                {
                    hitBox.height = (int) (47 * SCALE);
                    hitBox.y = yBeforeAction;
                    attackBox.y = hitBox.y;
                    attackBox.height = hitBox.height;
                }
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.TAKE_HIT);
                if(crouching)
                {
                    hitBox.y = yBeforeAction - 30;
                }
                crouching = false;
            }
        }
    }

    public void kill()
    {
        currentHealth = 0;
    }

    public boolean getLeft()
    {
        return left;
    }

    public boolean getRight()
    {
        return right;
    }

    public boolean getUp()
    {
        return up;
    }

    public int getFlipW()
    {
        return flipW;
    }

    public int getEnergy()
    {
        return energyValue;
    }

    public int getDamageTaken()
    {
        return damageTaken;
    }

    public int getPotionsUsed()
    {
        return potionsUsed;
    }

    public void setRight(boolean value)
    {
        right = value;
    }

    public void setUp(boolean value)
    {
        yBeforeAction = hitBox.y - 30;
        up = value;
    }

    public void setLeft(boolean value)
    {
        left = value;
    }

    public void setJump(boolean value)
    {
        yBeforeAction = hitBox.y;
        jump = value;
    }

    public void setDown(boolean value)
    {
        down = value;
    }

    public void setPraying(boolean value)
    {
        if(!talking)
        {
            praying = value;
        }
    }

    public void setTalking(boolean value)
    {
        if(talking && !value)
        {
            startCounting = true;
        }
        talking = value;
    }

    public void setInteracting(boolean value)
    {
        interacting = value;
    }

    public void setCrouching(boolean value)
    {
        if(!praying && !talking)
        {
            yBeforeAction = hitBox.y;
            if(!inAir && !crouching && value)
            {
                hitBox.height = (int) (37 * SCALE);
                hitBox.y = yBeforeAction + 30;
                attackBox.y = hitBox.y;
                attackBox.height = hitBox.height;
            }
            crouching = value;
            down = true;
            if(inAir)
            {
                crouching = false;
            }
        }
    }

    public void setXOfSaveSpot(double value)
    {
        xOfSaveSpot = value;
    }

    public int getTilesToSpawn()
    {
        return tileToSpawn;
    }

    public void setHeal(boolean value)
    {
        healing = value;

        if(numberOfCrystals == 0)
        {
            healing = false;
            return;
        }

        if(healSound && !inAir && !attacking)
        {
            yBeforeAction = crouching ? hitBox.y - 30 : hitBox.y;
            --numberOfCrystals;
            ++potionsUsed;
            playing.getDatabaseHandler().updatePlayerState(null, null, null, null, null, potionsUsed);
            playing.getGame().getAudioPlayer().playEffect(AudioPlayer.HEAL);
            changeHealth(30, false, false, 0);
        }
        healSound = false;
    }

    public void setBarriers(ArrayList<Barrier> value)
    {
        barriers = value;
    }

    public boolean pressedInteract()
    {
        return interacting;
    }

    public boolean isDodging()
    {
        return dodging;
    }

    public boolean isHealing()
    {
        return healing;
    }

    public boolean isPraying()
    {
        return praying;
    }

    public boolean isTalking()
    {
        return talking;
    }

    public boolean isCountStarted()
    {
        return startCounting;
    }

    public int getTileY()
    {
        return tileY;
    }

    private void updateAnimationTick()
    {
        ++animationTick;

        if(animationTick >= ANIMATION_SPEED)
        {
            animationTick = 0;
            ++animationIndex;

            if(animationIndex >= GetSpriteAmount(state))
            {
                if(state != JUMP)
                {
                    animationIndex = 0;
                }
                else
                {
                    animationIndex = GetSpriteAmount(state) - 1;
                }
                attacking = false;
                attackChecked = false;
                isHit = false;
                healing = false;
                canChangeAttackBox = true;
                healSound = true;
                dropAttackSound = true;
                canDropAttack = true;
                praying = false;
                if(state == ATTACKS1 || state == ATTACKS2 || state == ATTACKS3 || state == ATTACKS4)
                {
                    wasAttack = true;
                }
            }
            else
            {
                if(state == ATTACKS1 || state == ATTACKS2 || state == ATTACKS3 || state == ATTACKS4)
                {
                    canDropAttack = false;
                    framesUntilResetAttack = 0;
                }
            }
        }
    }
}
