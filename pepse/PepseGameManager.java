package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.GameObjectKiller;
import pepse.world.Sky;
import pepse.world.Terrain;

import java.awt.*;
import java.util.Random;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;

/**
 * The main class of the simulator.
 */
public class PepseGameManager extends GameManager {
    // Constants
    private static final Color YELLOW_COLOR_FOR_HALO = new Color(255, 255, 0, 20);
    public static final int DAY_CYCLE_LENGTH = 30;
    private static final float ADD_FACTOR = (1f / 2);
    // Fields
    private UserInputListener inputListener;
    private SoundReader soundReader;
    private ImageReader imageReader;
    private WindowController windowController;
    private Vector2 windowDimensions;
    private float windowDimensionsX;
    private int rightBounderies;
    private int leftBounderies;
    // objects
    private Terrain terrain;
    private Avatar avatar;
    // layers
    public static final int SKY_LAYER = Layer.BACKGROUND;
    public static final int SUN_LAYER = Layer.BACKGROUND + 1;
    public static final int SUN_HALO_LAYER = Layer.BACKGROUND + 10;
    public static final int TRUNK_LAYER = Layer.STATIC_OBJECTS - 2;
    public static final int LEAVES_LAYER = Layer.STATIC_OBJECTS - 1;
    public static final int TERRAIN_LAYER = Layer.STATIC_OBJECTS;
    public static final int AVATAR_LAYER = Layer.DEFAULT;
    public static final int NIGHT_LAYER = Layer.FOREGROUND;


    /**
     * Initialize fields and game objects.
     * The method will be called once when a GameGUIComponent is created,
     * and again after every invocation of windowController.resetGame().
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        // initialize fields
        this.inputListener = inputListener;
        this.soundReader = soundReader;
        this.imageReader = imageReader;
        this.windowController = windowController;
        this.windowDimensions = windowController.getWindowDimensions();
        this.windowDimensionsX =  windowDimensions.x();


        // create objects
        createSky();
        createNight();
        GameObject sun = createSun();
        createSunHalo(sun);
        createAvatar();
        createTerrain();

        new GameObjectKiller(gameObjects(), avatar, windowDimensionsX) ;

        //set layers collision
        gameObjects().layers().shouldLayersCollide(AVATAR_LAYER, TERRAIN_LAYER, true);
        gameObjects().layers().shouldLayersCollide(AVATAR_LAYER, TRUNK_LAYER, true);
        gameObjects().layers().shouldLayersCollide(LEAVES_LAYER, TERRAIN_LAYER, true);
    }

    private void createAvatar() {
        avatar = Avatar.create(gameObjects(), AVATAR_LAYER, Vector2.ZERO, inputListener, imageReader);
        setCamera(new Camera(avatar, Vector2.UP.mult(windowDimensions.y() / 6),
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
    }

    private void createSunHalo(GameObject sun) {
        SunHalo.create(gameObjects(), SUN_HALO_LAYER, sun, YELLOW_COLOR_FOR_HALO);
    }

    private GameObject createSun() {
        return Sun.create(gameObjects(), SUN_LAYER, windowDimensions, DAY_CYCLE_LENGTH);
    }

    private void createTerrain() {
        Random randInt = new Random();
        int seed = randInt.nextInt(60);
        terrain = new Terrain(gameObjects(), TERRAIN_LAYER, windowDimensions, seed);
        rightBounderies = (int)(windowDimensionsX * (1.5));
        leftBounderies =(int)(-0.5 * windowDimensionsX);
        terrain.createInRange(leftBounderies, rightBounderies);

    }

    private void createNight() {
        Night.create(gameObjects(), NIGHT_LAYER, windowDimensions, DAY_CYCLE_LENGTH);
    }

    /*
    creates the sky object
     */
    private void createSky() {
        Sky.create(gameObjects(), windowDimensions, SKY_LAYER);
    }

    /**
     * responsible for updating the simulation according to the avatar's location.
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (avatar.getCenter().x() + windowDimensionsX * ADD_FACTOR > rightBounderies) {
            terrain.createInRange(rightBounderies, rightBounderies + (int) (windowDimensionsX * ADD_FACTOR));
            rightBounderies += windowDimensionsX * ADD_FACTOR;
            leftBounderies += windowDimensionsX * ADD_FACTOR;
            return;
        }
        if (avatar.getCenter().x() - windowDimensionsX * ADD_FACTOR < leftBounderies) {
            terrain.createInRange(leftBounderies - (int) (windowDimensionsX * ADD_FACTOR), leftBounderies);
            rightBounderies -= windowDimensionsX * ADD_FACTOR;
            leftBounderies -= windowDimensionsX * ADD_FACTOR;
        }
    }

    /**
     * Runs the entire simulation.
     * @param args This argument should not be used.
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}
