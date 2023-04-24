package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.function.Consumer;

/**
 * Represents the simulation's sun.
 */
public class Sun {
    private static final float SUN_SIZE = 80;
    public static final String SUN_TAG = "sun";

    /**
     * create a sun object.
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength){
        Vector2 centerScreen = new Vector2(windowDimensions.x()/2, windowDimensions.y()/2);
        GameObject sun = new GameObject(
                new Vector2(centerScreen.x() + (float) (windowDimensions.x()/2.5), centerScreen.y()),
                new Vector2(SUN_SIZE, SUN_SIZE),
                new OvalRenderable(Color.YELLOW));
        gameObjects.addGameObject(sun, layer);
        sun.setTag(SUN_TAG);

        // sun location handling
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        Consumer<Float> setSunLocation = (x) -> sun.setCenter(
                new Vector2((float)(centerScreen.x() + Math.cos(x) * (windowDimensions.x()/2.5)),
                        (float)(centerScreen.y() + Math.sin(x) * (windowDimensions.y()/2.5))));
        new Transition<>( sun, setSunLocation, 1.5f * (float) Math.PI,
                -0.5f * (float) Math.PI, Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength , Transition.TransitionType.TRANSITION_LOOP,
                null);

        return sun;
    }
}
