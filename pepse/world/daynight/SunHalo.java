package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the sun halo.
 */
public class SunHalo {

    private static final float SIZE_OF_SUN_HALO = 160;
    private static final String SUN_HALO_TAG = "sun halo";

    /**
     * creates a sunHalo object.
     * @param gameObjects The collection of all the participating game objects.
     * @param layer the layer to put the sun halo into.
     * @param sun the sun object to create halo to.
     * @param color the halo color.
     * @return the sun halo created.
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            GameObject sun,
            Color color){
        GameObject sunHalo = new GameObject(sun.getCenter(),
                new Vector2(SIZE_OF_SUN_HALO, SIZE_OF_SUN_HALO),
                new OvalRenderable(color));
        gameObjects.addGameObject(sunHalo, layer);
        sun.setTag(SUN_HALO_TAG);
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.addComponent((x) -> sunHalo.setCenter(sun.getCenter()));
        return sun;
    }
}
