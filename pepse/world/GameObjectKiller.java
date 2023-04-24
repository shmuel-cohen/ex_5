package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;

/**
 * Responsible to remove a gameObject if it's too far from the avatar.
 */
public class GameObjectKiller {
    private static final float OUT_OF_SCREEN = (1 + 3f/4);
    private static GameObjectCollection gameObjects;
    private static Avatar avatar;
    private static float windowDimensionX;

    /**
     * constructor.
     * @param gameObjects the gameObjects collection to remove objects from.
     * @param avatar the avatar instance of the simulation.
     * @param windowDimensionX the window width.
     */
    public GameObjectKiller(GameObjectCollection gameObjects, Avatar avatar, float windowDimensionX){
        GameObjectKiller.gameObjects = gameObjects;
        GameObjectKiller.avatar = avatar;
        GameObjectKiller.windowDimensionX = windowDimensionX;
    }

    /**
     * remove the gameObject if it's too far from the avatar.
     * @param gameObject object to remove.
     * @param layerObj the object layer.
     */
    public static void removeObject(GameObject gameObject, int layerObj){
        if (Math.abs(gameObject.getCenter().x() - avatar.getCenter().x()) > windowDimensionX * OUT_OF_SCREEN){
            gameObjects.removeGameObject(gameObject, layerObj);
        }
    }
}
