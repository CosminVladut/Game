package entities;

import exceptions.UnexpectedEnemyTypeException;
import utils.Constants;
import utils.CustomLogger;

import java.util.HashMap;
import java.util.function.BiFunction;

public final class EnemyFactory
{
    private static final HashMap<Integer, BiFunction<Double, Double, ? extends Enemy>> factories = new HashMap<>();

    static
    {
        factories.put(Constants.EnemyConstants.SKELETON_MACER, (x, y) -> new SkeletonMacer(x, y, 2));
        factories.put(Constants.EnemyConstants.SHARDSOUL_SLAYER, (x, y) -> new ShardsoulSlayer(x, y, 2));
        factories.put(Constants.EnemyConstants.FLYING_DEMON, (x, y) -> new FlyingDemon(x, y, 3));
        factories.put(Constants.EnemyConstants.HUNTRESS, (x, y) -> new Huntress(x, y, 3));
        factories.put(Constants.EnemyConstants.NECROMANCER, (x, y) -> new Necromancer(x, y, 3));
        factories.put(Constants.EnemyConstants.LEAF_RANGER, (x, y) -> new LeafRanger(x, y, 3));
        factories.put(Constants.EnemyConstants.FIRE_WORM, (x, y) -> new FireWorm(x, y, 2));
        factories.put(Constants.EnemyConstants.FIRE_WIZARD, (x, y) -> new FireWizard(x, y, 3));
        factories.put(Constants.EnemyConstants.MINOTAUR, (x, y) -> new Minotaur(x, y, 3));
        factories.put(Constants.EnemyConstants.DEMON, (x, y) -> new Demon(x, y, 5));
        factories.put(Constants.EnemyConstants.FIRE_KNIGHT, (x, y) -> new FireKnight(x, y, 3));
        factories.put(Constants.EnemyConstants.ICE_BORNE, (x, y) -> new IceBorne(x, y, 2));
        factories.put(Constants.EnemyConstants.ICE_GOLEM, (x, y) -> new IceGolem(x, y, 5));
        factories.put(Constants.EnemyConstants.SNOW_KNIGHT, (x, y) -> new SnowKnight(x, y, 3));
        factories.put(Constants.EnemyConstants.WEREWOLF, (x, y) -> new Werewolf(x, y, 4));
        factories.put(Constants.EnemyConstants.VIKING, (x, y) -> new Viking(x, y, 3));
        factories.put(Constants.EnemyConstants.FINAL_BOSS, (x, y) -> new FinalBoss(x, y, 3));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Enemy> T createEnemy(int enemyType, double x, double y, Class<T> enemyClass)
    {
        BiFunction<Double, Double, ? extends Enemy> factory = factories.get(enemyType);
        if (factory != null && enemyClass.isInstance(factory.apply(x, y)))
        {
            return (T) factory.apply(x, y);
        }
        CustomLogger.logException("Tip de oponent necunoscut: " + enemyType, new UnexpectedEnemyTypeException());
        return null;
    }
}
