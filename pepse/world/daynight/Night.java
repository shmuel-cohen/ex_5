package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Darkens the entire window.
 */
public class Night {
    // Constants
    private static final Color BLACK_COLOR = new Color(0, 0, 0);
    private static final float MORNING_OPACITY = 0f;
    private static final Float MIDNIGHT_OPACITY = 0.5f;

    /**
     * This function creates a black rectangular game object that covers the entire game window and changes
     * its opaqueness in a cyclic manner, in order to resemble day-to-night transitions.
     *
     * @param gameObjects      - The collection of all participating game objects.
     * @param layer            - The number of the layer to which the created game object should be added.
     * @param windowDimensions - The dimensions of the windows.
     * @param cycleLength      - The amount of seconds it should take the created game object to complete a
     *                         full cycle.
     * @return A new game object representing day-to-night transitions.
     */
    public static GameObject create(GameObjectCollection gameObjects,
                                    int layer,
                                    Vector2 windowDimensions,
                                    float cycleLength){
        // create night object
        Renderable blackScreen = new RectangleRenderable(BLACK_COLOR);
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions, blackScreen);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(night, layer);
        night.setTag("night");

        // set opacity change
        new Transition<Float>( night, night.renderer()::setOpaqueness,
                MORNING_OPACITY, MIDNIGHT_OPACITY, Transition.CUBIC_INTERPOLATOR_FLOAT,
                cycleLength / 2, Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);

        return night;
    }
}
