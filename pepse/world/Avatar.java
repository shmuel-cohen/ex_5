package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * Represents the main figure of the game.
 */
public class Avatar extends GameObject {
    // constants
    private static final float VELOCITY_X = 300;
    private static final float VELOCITY_Y = -300;
    private static final float GRAVITY = 500;
    private static final float MAX_SPEED_DOWN = 600;
    public static final int AVATAR_SIZE = 50;
    public static final int MAX_ENERGY = 100;
    public static final double ENERGY_CHANGE_VALUE = 0.5;
    public static final String AVATAR_TAG = "avatar";
    private static final float FLY_WAIT_FACTOR = 0.5f;
    private static final int REST_VELOCITY = 0;
    private static final int MIN_ENERGY = 0;
    public static final String FRONT_3 = "assets/front_3.png";
    public static final String FRONT_1 = "assets/front_1.png";
    public static final String FRONT_2 = "assets/front_2.png";
    public static final String WALK_RIGHT_1 = "assets/right_.png";
    public static final String WALK_RIGHT_2 = "assets/turen_right_2.png";
    public static final String WALK_RIGHT_3 = "assets/turen_right_4.png";
    public static final double TIME_BETWEEN_CLIPS = 0.2;
    public static final String JUMP_1 = "assets/jump_1.png";
    public static final String JUMP_2 = "assets/jump_2.png";
    // fields
    private static AnimationRenderable frontAnimation;
    private static AnimationRenderable wolkAnimation;
    private static AnimationRenderable flyAnimation;
    private final UserInputListener inputListener;
    private float energy = MAX_ENERGY;

    /**
     * constructor.
     * @param pos top left corner of the initial location.
     * @param inputListener a UserInputListener object.
     * @param animationRenderable an AnimationRenderable object.
     */
    public Avatar(Vector2 pos, UserInputListener inputListener, AnimationRenderable animationRenderable) {
        super(pos, Vector2.ONES.mult(AVATAR_SIZE), animationRenderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
    }

    /**
     * updates the avatar's renderer and velocity.
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            xVel -= VELOCITY_X;
            this.renderer().setRenderable(wolkAnimation);
            this.renderer().setIsFlippedHorizontally(true);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            xVel += VELOCITY_X;
            this.renderer().setRenderable(wolkAnimation);
            this.renderer().setIsFlippedHorizontally(false);
        }
        transform().setVelocityX(xVel);
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE)
                && inputListener.isKeyPressed(KeyEvent.VK_SHIFT)
                && energy > MIN_ENERGY) {
            energy -= ENERGY_CHANGE_VALUE;
            transform().setVelocityY(VELOCITY_Y);
            this.renderer().setRenderable(flyAnimation);
//            new ScheduledTask(this, FLY_WAIT_FACTOR, false,
//                    () -> physics().preventIntersectionsFromDirection(Vector2.ZERO));
            return;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == REST_VELOCITY)
            transform().setVelocityY(VELOCITY_Y);
        if (transform().getVelocity().x() == REST_VELOCITY && transform().getVelocity().y() >= REST_VELOCITY) {
            this.renderer().setRenderable(frontAnimation);
        }
        if (transform().getVelocity().y() > MAX_SPEED_DOWN) {
            transform().setVelocityY(MAX_SPEED_DOWN);
        }
        if (getVelocity().y() == REST_VELOCITY && energy < MAX_ENERGY){
            energy += ENERGY_CHANGE_VALUE;
        }
    }

    /**
     * creates an avatar object.
     * @param gameObjects game objects collection.
     * @param layer the layer to insert the avatar to.
     * @param topLeftCorner location of the avatar.
     * @param inputListener a UserInputListener object.
     * @param imageReader an ImageReader object.
     * @return the avatar.
     */
    public static Avatar create(GameObjectCollection gameObjects,
                                int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener,
                                ImageReader imageReader) {
        frontAnimation = new AnimationRenderable(new Renderable[]{imageReader.readImage(
                FRONT_3, true),
                imageReader.readImage(FRONT_1, true),
                imageReader.readImage(FRONT_2, true),
                imageReader.readImage(FRONT_1, true)}, TIME_BETWEEN_CLIPS);
        wolkAnimation = new AnimationRenderable(new Renderable[]{imageReader.readImage(
                WALK_RIGHT_1, true),
                imageReader.readImage(WALK_RIGHT_2, true),
                imageReader.readImage(WALK_RIGHT_1, true),
                imageReader.readImage(WALK_RIGHT_3, true)}, TIME_BETWEEN_CLIPS);
        flyAnimation = new AnimationRenderable(new Renderable[]{imageReader.readImage(
                JUMP_1, true),
                imageReader.readImage(JUMP_2, true),}, TIME_BETWEEN_CLIPS);
        Avatar avatar = new Avatar(topLeftCorner, inputListener, frontAnimation);
        gameObjects.addGameObject(avatar, layer);
        avatar.setTag(AVATAR_TAG);
        return avatar;
    }
}
