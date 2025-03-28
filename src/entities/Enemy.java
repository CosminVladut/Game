package entities;

import gamestates.Playing;
import utils.Constants;

import java.awt.geom.Rectangle2D;

import static main.Game.SCALE;
import static main.Game.TILES_SIZE;
import static utils.Constants.ANIMATION_SPEED;
import static utils.Constants.Directions.LEFT;
import static utils.Constants.Directions.RIGHT;
import static utils.Constants.EnemyConstants.*;
import static utils.Constants.GRAVITY;
import static utils.HelperMethods.*;

public abstract class Enemy extends Entity
{
    protected boolean canSetPlaying = true;
    protected Playing playing;
    protected final int enemyType;
    protected boolean firstUpdate = true;
    protected boolean death = false;
    protected boolean inAir;
    protected int walkDir = LEFT;
    protected int tileY;
    protected final double attackDistance = TILES_SIZE;
    protected boolean active = true;
    protected boolean attackChecked;
    protected boolean doesKnockBack = false;
    protected double knockbackFactor = 0;
    protected boolean isSpawned = false;
    protected int zone = 0;

    public Enemy(double x, double y, int width, int height, int tilesInHeight, int enemyType)
    {
        super(x, y, width, height, tilesInHeight);
        this.enemyType = enemyType;
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
    }

    protected void firstUpdateCheck(int[][] levelData)
    {
        if(!IsEntityOnFloor(hitBox, levelData, true) && !(this instanceof Necromancer) && !(this instanceof FlyingDemon))
        {
            inAir = true;
        }
        firstUpdate = false;
    }

    protected void updateInAir(int[][] levelData)
    {
        if(CanMoveHere(hitBox.x + walkSpeed, hitBox.y + airSpeed, hitBox.width, hitBox.height, levelData, true))
        {
            hitBox.y += airSpeed;
            airSpeed += GRAVITY / 2;
            hitBox.x += walkDir == LEFT ? -walkSpeed : walkSpeed;
        }
        else
        {
            inAir = false;
            airSpeed = 0;
            while(CanMoveHere(hitBox.x, hitBox.y + 0.01, hitBox.width, hitBox.height, levelData, true))
            {
                hitBox.y += 0.01;
            }
        }
    }

    protected void move(int[][] levelData)
    {
        double xSpeed;

        if(walkDir == LEFT)
        {
            xSpeed = -walkSpeed;
        }
        else
        {
            xSpeed = walkSpeed;
        }

        if(CanMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, levelData, true))
        {
            if(IsOkToWalkOn(hitBox, xSpeed, levelData))
            {
                hitBox.x += xSpeed;
            }
        }
        else
        {
            changeWalkDir();
        }
    }

    protected void turnTowardsPlayer(Player player)
    {
        if(player.hitBox.x > hitBox.x)
        {
            walkDir = RIGHT;
        }
        else
        {
            walkDir = LEFT;
        }
    }

    protected boolean canSeePlayer(int[][] levelData, Player player, double factor)
    {
        int playerTileY = (int)(player.getHitBox().y / TILES_SIZE);

        if(Math.abs(playerTileY - tileY) <= 3)
        {
            if(isPlayerInRange(player, factor))
            {
                return IsSightClear(levelData, hitBox, player.hitBox);
            }
        }

        return false;
    }

    protected boolean isPlayerInRange(Player player, double factor)
    {
        double absoluteValue = Math.abs(player.hitBox.x - hitBox.x + (flipW() == -1 ? - player.hitBox.width - attackBox.x + hitBox.x : 0));
        return absoluteValue <= attackDistance * Math.abs(factor);
    }

    protected boolean isPlayerCloseForAttack(Player player, double factor)
    {
        double absoluteValue = Math.abs(player.hitBox.x - hitBox.x + (flipW() == -1 && attackBox.x + attackBox.width > player.getHitBox().x + 5 ? - player.hitBox.width - attackBox.x + hitBox.x : 0));
        return absoluteValue <= attackDistance * Math.abs(factor);
    }

    public boolean isActive()
    {
        return active;
    }

    protected void newState(int state)
    {
        this.state = state;
        animationTick = 0;
        animationIndex = 0;
    }

    public void setPlaying(Playing playing)
    {
        if(canSetPlaying)
        {
            this.playing = playing;
            canSetPlaying = false;
        }
    }

    public void setSpawned(boolean value)
    {
        isSpawned = value;
    }

    protected void changeWalkDir()
    {
        if(walkDir == LEFT)
        {
            walkDir = RIGHT;
        }
        else
        {
            walkDir = LEFT;
        }
    }

    protected void updateAnimationTick()
    {
        ++animationTick;
        if(animationTick >= ANIMATION_SPEED)
        {
            animationTick = 0;
            ++animationIndex;
            if(animationIndex >= GetSpriteAmountEnemy(enemyType, state))
            {
                animationIndex = 0;
                switch(enemyType)
                {
                    case SKELETON_MACER ->
                    {
                        switch(state)
                        {
                            case Constants.EnemyConstants.SkeletonMacer.ATTACK1, Constants.EnemyConstants.SkeletonMacer.ATTACK2, Constants.EnemyConstants.SkeletonMacer.TAKE_HIT -> newState(Constants.EnemyConstants.SkeletonMacer.IDLE);
                            case Constants.EnemyConstants.SkeletonMacer.DEATH ->
                            {
                                active = false;
                                zone = -1;
                            }
                        }
                    }

                    case SHARDSOUL_SLAYER ->
                    {
                        switch(state)
                        {
                            case Constants.EnemyConstants.ShardsoulSlayer.ATTACK, Constants.EnemyConstants.ShardsoulSlayer.TAKE_HIT -> newState(Constants.EnemyConstants.ShardsoulSlayer.IDLE);
                            case Constants.EnemyConstants.ShardsoulSlayer.DEATH ->
                            {
                                active = false;
                                zone = -1;
                            }
                        }
                    }

                    case FLYING_DEMON ->
                    {
                        switch(state)
                        {
                            case Constants.EnemyConstants.FlyingDemon.ATTACK, Constants.EnemyConstants.FlyingDemon.TAKE_HIT -> newState(Constants.EnemyConstants.FlyingDemon.IDLE);
                            case Constants.EnemyConstants.FlyingDemon.DEATH ->
                            {
                                active = false;
                                zone = -1;
                            }
                        }
                    }

                    case HUNTRESS ->
                    {
                        switch(state)
                        {
                            case Constants.EnemyConstants.Huntress.ATTACK1, Constants.EnemyConstants.Huntress.ATTACK2, Constants.EnemyConstants.Huntress.ATTACK3, Constants.EnemyConstants.Huntress.TAKE_HIT -> newState(Constants.EnemyConstants.Huntress.IDLE);
                            case Constants.EnemyConstants.Huntress.DEATH ->
                            {
                                active = false;
                                zone = -1;
                            }
                        } 
                    }

                    case NECROMANCER ->
                    {
                        switch(state)
                        {
                            case Constants.EnemyConstants.Necromancer.ATTACK1, Constants.EnemyConstants.Necromancer.ATTACK2, Constants.EnemyConstants.Necromancer.TAKE_HIT -> newState(Constants.EnemyConstants.Necromancer.WALK);
                            case Constants.EnemyConstants.Necromancer.DEATH ->
                            {
                                active = false;
                                zone = -1;
                            }
                        }
                    }

                    case LEAF_RANGER ->
                    {
                        switch(state)
                        {
                            case Constants.EnemyConstants.LeafRanger.ATTACK1, Constants.EnemyConstants.LeafRanger.ATTACK2, Constants.EnemyConstants.LeafRanger.ATTACK3, Constants.EnemyConstants.LeafRanger.ATTACK4, Constants.EnemyConstants.LeafRanger.BLOCK, Constants.EnemyConstants.LeafRanger.TAKE_HIT -> newState(Constants.EnemyConstants.LeafRanger.IDLE);
                            case Constants.EnemyConstants.LeafRanger.DEATH ->
                            {
                                active = false;
                                zone = -1;
                            }
                        }
                    }

                    case DEMON ->
                    {
                        switch(state)
                        {
                            case Constants.EnemyConstants.Demon.ATTACK, Constants.EnemyConstants.Demon.TAKE_HIT -> newState(Constants.EnemyConstants.Demon.IDLE);
                            case Constants.EnemyConstants.Demon.DEATH ->
                            {
                                active = false;
                                zone = -1;
                            }
                        }
                    }

                    case FIRE_WORM ->
                    {
                        switch(state)
                        {
                            case Constants.EnemyConstants.FireWorm.ATTACK, Constants.EnemyConstants.FireWorm.TAKE_HIT -> newState(Constants.EnemyConstants.FireWorm.IDLE);
                            case Constants.EnemyConstants.FireWorm.DEATH ->
                            {
                                active = false;
                                zone = -1;
                            }
                        }
                    }

                    case FIRE_WIZARD ->
                    {
                        switch(state)
                        {
                            case Constants.EnemyConstants.FireWizard.ATTACK, Constants.EnemyConstants.FireWizard.TAKE_HIT -> newState(Constants.EnemyConstants.FireWizard.IDLE);
                            case Constants.EnemyConstants.FireWizard.DEATH ->
                            {
                                active = false;
                                zone = -1;
                            }
                        }
                    }

                    case MINOTAUR ->
                    {
                        switch(state)
                        {
                            case Constants.EnemyConstants.Minotaur.ATTACK1, Constants.EnemyConstants.Minotaur.ATTACK2, Constants.EnemyConstants.Minotaur.BLOCK, Constants.EnemyConstants.Minotaur.TAKE_HIT -> newState(Constants.EnemyConstants.Minotaur.IDLE);
                            case Constants.EnemyConstants.Minotaur.DEATH ->
                            {
                                active = false;
                                zone = -1;
                            }
                        }
                    }

                    case FIRE_KNIGHT ->
                    {
                        switch(state)
                        {
                            case Constants.EnemyConstants.FireKnight.ATTACK1, Constants.EnemyConstants.FireKnight.ATTACK2, Constants.EnemyConstants.FireKnight.ATTACK3, Constants.EnemyConstants.FireKnight.ATTACK4, Constants.EnemyConstants.FireKnight.BLOCK, Constants.EnemyConstants.FireKnight.TAKE_HIT -> newState(Constants.EnemyConstants.FireKnight.IDLE);
                            case Constants.EnemyConstants.FireKnight.DEATH ->
                            {
                                active = false;
                                zone = -1;
                            }
                        }
                    }

                    case ICE_GOLEM ->
                    {
                        switch(state)
                        {
                            case Constants.EnemyConstants.IceGolem.ATTACK, Constants.EnemyConstants.IceGolem.TAKE_HIT -> newState(Constants.EnemyConstants.IceGolem.IDLE);
                            case Constants.EnemyConstants.IceGolem.DEATH ->
                            {
                                active = false;
                                zone = -1;
                            }
                        }
                    }

                    case ICE_BORNE ->
                    {
                        switch(state)
                        {
                            case Constants.EnemyConstants.IceBorne.ATTACK, Constants.EnemyConstants.IceBorne.TAKE_HIT -> newState(Constants.EnemyConstants.IceBorne.IDLE);
                            case Constants.EnemyConstants.IceBorne.DEATH ->
                            {
                                active = false;
                                zone = -1;
                            }
                        }
                    }

                    case SNOW_KNIGHT ->
                    {
                        switch(state)
                        {
                            case Constants.EnemyConstants.SnowKnight.ATTACK1, Constants.EnemyConstants.SnowKnight.ATTACK2, Constants.EnemyConstants.SnowKnight.ATTACK3, Constants.EnemyConstants.SnowKnight.TAKE_HIT -> newState(Constants.EnemyConstants.SnowKnight.IDLE);
                            case Constants.EnemyConstants.SnowKnight.DEATH ->
                            {
                                active = false;
                                zone = -1;
                            }
                        }
                    }

                    case WEREWOLF ->
                    {
                        switch(state)
                        {
                            case Constants.EnemyConstants.Werewolf.ATTACK1, Constants.EnemyConstants.Werewolf.ATTACK2, Constants.EnemyConstants.Werewolf.ATTACK3, Constants.EnemyConstants.Werewolf.ATTACK4, Constants.EnemyConstants.Werewolf.TAKE_HIT -> newState(Constants.EnemyConstants.Werewolf.IDLE);
                            case Constants.EnemyConstants.Werewolf.DEATH ->
                            {
                                active = false;
                                zone = -1;
                            }
                        }
                    }

                    case VIKING ->
                    {
                        switch(state)
                        {
                            case Constants.EnemyConstants.Viking.ATTACK1, Constants.EnemyConstants.Viking.ATTACK2, Constants.EnemyConstants.Viking.ATTACK3, Constants.EnemyConstants.Viking.ATTACK4, Constants.EnemyConstants.Viking.BLOCK, Constants.EnemyConstants.Viking.TAKE_HIT -> newState(Constants.EnemyConstants.Viking.IDLE);
                            case Constants.EnemyConstants.Viking.DEATH ->
                            {
                                active = false;
                                zone = -1;
                            }
                        }
                    }

                    case FINAL_BOSS ->
                    {
                        switch(state)
                        {
                            case Constants.EnemyConstants.FinalBoss.ATTACK1, Constants.EnemyConstants.FinalBoss.ATTACK2, Constants.EnemyConstants.FinalBoss.ATTACK3, Constants.EnemyConstants.FinalBoss.TAKE_HIT -> newState(Constants.EnemyConstants.FinalBoss.IDLE);
                            case Constants.EnemyConstants.FinalBoss.DEATH ->
                            {
                                active = false;
                                zone = -1;
                            }
                        }
                    }
                }
            }
        }
    }

    public void hurt(int damage, boolean flick, int deathState, int takeHitState)
    {
        currentHealth -= damage;
        if(currentHealth <= 0)
        {
            newState(deathState);
        }
        else
        {
            if(flick)
            {
                newState(takeHitState);
            }
        }
    }

    public void resetEnemy(int idleState)
    {
        hitBox.x = x;
        hitBox.y = y + ((this instanceof Werewolf) || (this instanceof Huntress) ? (int)(10 * SCALE) : 0);
        firstUpdate = true;
        currentHealth = maxHealth;
        newState(idleState);
        active = true;
        airSpeed = 0;
    }

    protected void checkEnemyHit(Rectangle2D.Double attackBox, Player player)
    {
       if(attackBox.intersects(player.hitBox))
       {
           player.changeHealth(-GetEnemyDamage(enemyType, state), walkDir == LEFT && player.getFlipW() != -1, doesKnockBack, knockbackFactor);

       }
       attackChecked = true;
    }

    public void update(int[][] levelData, Player player)
    {

    }

    public int getZone()
    {
        return zone;
    }

    public int flipX()
    {
        if(walkDir == RIGHT)
        {
            return width;
        }
        return 0;
    }

    public int flipW()
    {
        if(walkDir == RIGHT)
        {
            return -1;
        }
        return 1;
    }
}
