package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.Random;
import java.util.function.Consumer;

/**
 * Represents a leaf.
 */
public class Leaf extends Block {
    private static final float DROP_VELOCITY = 30;
    private static final float FADEOUT_TIME = 20;
    private static final float LEAF_SMALLEST = 28;
    private static final int RELIVE_LEAF = 30;
    private static final float MOVE_FROM_WIDE = 20;
    private static final float LEAF_MASS = 1;
    private static final float FADE_IN_TIME = 0;
    private static final float WIND_FACTOR = 0.5f;

    private Transition<Float> horizontalMovement;

    /**
     * Construct a new Block instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Leaf(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, renderable);
        Random random = new Random();
        physics().setMass(LEAF_MASS);
        new ScheduledTask(this, random.nextFloat() % WIND_FACTOR,
                false, ()->addTransition(this));
        //drop and fade of leaf
        new ScheduledTask(this, (float) random.nextInt(RELIVE_LEAF),
                false, ()-> addDroppingOut(this, topLeftCorner, random));
    }

    private void addDroppingOut(Leaf leaf, Vector2 locationOfLeaf, Random random) {
        leaf.transform().setVelocityY(DROP_VELOCITY);
        //set horizontal movement
        horizontalMovement = new Transition<>( leaf, (velocity) ->
                leaf.transform().setVelocityX(velocity),
                -MOVE_FROM_WIDE, MOVE_FROM_WIDE, Transition.LINEAR_INTERPOLATOR_FLOAT,
                2f , Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);

        leaf.renderer().fadeOut(FADEOUT_TIME,() -> {
            leaf.setVelocity(Vector2.ZERO);
            leaf.setTopLeftCorner(locationOfLeaf);
            leaf.renderer().fadeIn(FADE_IN_TIME);
            new ScheduledTask(leaf, (float) random.nextInt(RELIVE_LEAF), false,
                                                    ()-> addDroppingOut(leaf, locationOfLeaf, random));
        });
    }

    private void addTransition(Leaf leaf) {
        Consumer<Float> moveLeaf = (angle) ->
                leaf.renderer().setRenderableAngle(angle);

        //move transition
        new Transition<>( leaf, moveLeaf,
                0f, 10f, Transition.LINEAR_INTERPOLATOR_FLOAT,
                0.5f , Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);

        //change size transition
        new Transition<>( leaf, leaf::setDimensions,
                new Vector2(LEAF_SMALLEST,LEAF_SMALLEST), new Vector2(30,30), Transition.LINEAR_INTERPOLATOR_VECTOR,
                1 , Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);

    }

    /**
     * Called on the first frame of a collision.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        removeComponent(horizontalMovement);
    }
}
