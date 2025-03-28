package entities;

import audio.AudioPlayer;
import objects.Projectile;

import java.awt.geom.Rectangle2D;
import java.util.Random;

import static main.Game.SCALE;
import static main.Game.TILES_SIZE;
import static utils.Constants.EnemyConstants.*;
import static utils.Constants.EnemyConstants.Necromancer.*;
import static utils.Constants.Projectiles.*;
import static utils.HelperMethods.CanSpawnEnemyOnSide;

public final class Necromancer extends Enemy
{
    private final Random random = new Random();

    private boolean death = false;

    private int attackDelayCounter;

    private int enemy;
    private int direction;
    private Rectangle2D.Double playerHitBox;
    private int playerTilesToSpawn;

    public Necromancer(double x, double y, int tilesInHeight)
    {
        super(x, y, WIDTH, HEIGHT, tilesInHeight, NECROMANCER);
        state = WALK;
        walkSpeed = 0.5 * SCALE;
        initHitBox(18, 47, 0, 0);
        initAttackBox();
        doesKnockBack = false;
    }

    private void initAttackBox()
    {
        attackBox = new Rectangle2D.Double(x, y, 0, 0);
    }

    public void update(int[][] levelData, Player player)
    {
        updateBehaviour(levelData, player);
        updateAnimationTick();
        if(state == DEATH)
        {
            if(!death)
            {
                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.NECROMANCER_DEATH);
                death = true;
            }
        }
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
                case WALK ->
                {
                    if (canSeePlayer(levelData, player, 10))
                    {
                        ifPlayerSeen(levelData, player);
                    }
                    else
                    {
                        if (state != WALK)
                        {
                            newState(WALK);
                        }
                        move(levelData);
                    }
                    tileY = (int)(hitBox.y / TILES_SIZE);
                }
                case ATTACK1, ATTACK2 ->
                {
                    if(animationTick == 0)
                    {
                        if(state == ATTACK1 && animationIndex == 8)
                        {
                            playing.getObjectHandler().getProjectiles().add(new Projectile((int) (player.getHitBox().x - player.getHitBox().width / 2), (int) (player.getHitBox().y + (int)(9 * SCALE)), -flipW(), false, EXPLOSION, 0 , EXPLOSION_WIDTH, EXPLOSION_HEIGHT));
                            playing.getGame().getAudioPlayer().playEffect(AudioPlayer.NECROMANCER_ATTACK1);
                        }
                        else
                        {
                            if(state == ATTACK2 && animationIndex == 11)
                            {
                                playing.getEnemyHandler().addEnemy(playerHitBox, playerTilesToSpawn, enemy, direction);
                                playing.getGame().getAudioPlayer().playEffect(AudioPlayer.NECROMANCER_ATTACK2);
                            }
                        }
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
        if (isPlayerCloseForAttack(player, 8))
        {
            if (attackDelayCounter > 0)
            {
                if(state != WALK)
                {
                    newState(WALK);
                }
            }
            else
            {
                double chance = random.nextDouble();
                enemy = chance < 0.1 ? HUNTRESS : (chance < 0.2 ? FLYING_DEMON : (chance < 0.3 ? SHARDSOUL_SLAYER : SKELETON_MACER));
                playerHitBox = player.hitBox;
                playerTilesToSpawn = player.getTilesToSpawn();
                switch(enemy)
                {
                    case SKELETON_MACER -> direction = CanSpawnEnemyOnSide(playerHitBox, (int)(18 * SCALE), (int)(31 * SCALE), playerTilesToSpawn, 2, playing.getLevelManager().getCurrentLevel().getLevelData());
                    case SHARDSOUL_SLAYER -> direction = CanSpawnEnemyOnSide(playerHitBox, (int)(24 * SCALE), (int)(31 * SCALE), playerTilesToSpawn, 2, playing.getLevelManager().getCurrentLevel().getLevelData());
                    case FLYING_DEMON -> direction = CanSpawnEnemyOnSide(playerHitBox, (int)(43 * SCALE), (int)(47.5 * SCALE), playerTilesToSpawn, 3, playing.getLevelManager().getCurrentLevel().getLevelData());
                    case HUNTRESS -> direction = CanSpawnEnemyOnSide(playerHitBox, (int)(22 * SCALE), (int)(47 * SCALE), playerTilesToSpawn, 3, playing.getLevelManager().getCurrentLevel().getLevelData());
                }
                if(direction != 3)
                {
                    if(direction == 0)
                    {
                        direction = random.nextInt(2) + 1;
                    }
                    if(!player.inAir)
                    {
                        newState(ATTACK1 + (random.nextInt(30) < 10 ? 1 : 0));
                    }
                    else
                    {
                        newState(ATTACK1);
                    }
                }
                else
                {
                    newState(ATTACK1);
                }
                int MAX_ATTACK_DELAY = 480;
                int MIN_ATTACK_DELAY = 300;
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
