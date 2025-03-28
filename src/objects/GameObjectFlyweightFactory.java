package objects;

import java.util.HashMap;

public final class GameObjectFlyweightFactory
{
    private static final HashMap<String, GameObjectFlyweight> gameObjectCache = new HashMap<>();

    public static GameObjectFlyweight getGameObjectFlyweight(int objectType, int xDrawOffset, int yDrawOffset)
    {
        String key = objectType + "_" + xDrawOffset + "_" + yDrawOffset;

        if(!gameObjectCache.containsKey(key))
        {
            gameObjectCache.put(key, new GameObjectFlyweight(objectType, xDrawOffset, yDrawOffset));
        }
        return gameObjectCache.get(key);
    }
}
