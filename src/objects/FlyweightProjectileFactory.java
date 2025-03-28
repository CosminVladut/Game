package objects;

import java.util.HashMap;

public final class FlyweightProjectileFactory
{
    private static final HashMap<String, ProjectileFlyweight> projectileCache = new HashMap<>();

    public static ProjectileFlyweight getProjectileFlyweight(int projectileType, double speed, int width, int height)
    {
        String key = projectileType + "_" + speed + "_" + width + "_" + height;
        if(!projectileCache.containsKey(key))
        {
            projectileCache.put(key, new ProjectileFlyweight(projectileType, width, height, speed));
        }
        return projectileCache.get(key);
    }
}
