package pepse.world;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.trees.Tree;

import java.awt.*;

/**
 * Responsible for the creation and management of terrain.
 */
public class Terrain {
    public static final float GROUND_HEIGHT_BASE_FACTOR = 2F / 3F;
    // constants
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;
    private static final int DEEP_LAYER_ADDING = 5;
    public static final String GROUND_BLOCK_TAG = "ground";

    // fields
    private final float groundHeightAtXBase;
    private final Vector2 windowDimension;
    private final int seed;
    private final GameObjectCollection gameObjects;
    private final int surfaceLayer;
    private final Tree treeBuilder;
    private final int deepBlockLayer;

    /**
     * constructor.
     * @param gameObjects collection of game objects.
     * @param groundLayer the layer of the ground.
     * @param windowDimension the window's dimension.
     * @param seed random seed.
     */
    public Terrain(GameObjectCollection gameObjects, int groundLayer, Vector2 windowDimension, int seed){
        this.gameObjects = gameObjects;
        this.surfaceLayer = groundLayer;
        this.deepBlockLayer = groundLayer + DEEP_LAYER_ADDING;
        this.groundHeightAtXBase = GROUND_HEIGHT_BASE_FACTOR * windowDimension.y();
        this.windowDimension = windowDimension;
        this.seed = seed;
        this.treeBuilder = new Tree(gameObjects, seed, this::groundHeightAt);
    }

    /**
     * This method creates terrain in a given range of x-values.
     * @param minX The lower bound of the given range (will be rounded to a multiple of Block.SIZE).
     * @param maxX The upper bound of the given range (will be rounded to a multiple of Block.SIZE).
     */
    public void createInRange(int minX, int maxX){
        createBlocks(minX, maxX);
        createTrees(minX, maxX);
    }

    private void createTrees(int minX, int maxX) {
        treeBuilder.createInRange(minX, maxX);
    }

    /*
    create the ground blocks.
     */
    private void createBlocks(int minX, int maxX) {
        for (int x = minX - minX % Block.SIZE; x < maxX - maxX % Block.SIZE +1; x += Block.SIZE){
            // create surface blocks
            float y = (float) (Math.floor(groundHeightAt(x) / Block.SIZE) * Block.SIZE);
            Block block = new Block(new Vector2(x, y),
                    new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
            gameObjects.addGameObject(block, surfaceLayer);
            block.setTag(GROUND_BLOCK_TAG);
            block.addComponent((a) -> GameObjectKiller.removeObject(block, surfaceLayer));
            // create the deeper blocks
            for (int i = 0; i < TERRAIN_DEPTH; i++){
                y += Block.SIZE;
                Block ground  = new Block(new Vector2(x, y),
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
                gameObjects.addGameObject(ground, deepBlockLayer);
                ground.addComponent((a) -> GameObjectKiller.removeObject(ground, deepBlockLayer));
            }
        }
    }

    /**
     * This method returns the ground height at a given location.
     * @param x location.
     * @return The ground height at the given location.
     */
    public float groundHeightAt(float x ){
        double height =  (seed + 50)*Math.sin(x/(seed + 100)) +
                (seed + 100)*Math.sin(x/(seed + 180))+
                (seed + 80)*Math.cos(x/(seed + 150))+
                (seed + 10)*Math.pow(Math.cos(x/(seed+ 90)), 3) + groundHeightAtXBase;
        return (float)Math.max(Math.min(height, windowDimension.y() - 60), groundHeightAtXBase);
    }
}
