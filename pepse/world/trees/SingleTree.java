package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.GameObjectKiller;

import java.awt.*;

/**
 * Represents a single tree, includes trunk and leaves.
 */
public class SingleTree{
    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    public static final int SAFE_UNDERGROUND_TREE = 10;
    public static final int MAX_TREE_SIZE = 350;
    public static final int LEAVES_FACTOR = 6;
    public static final int DISCOUNT_HEIGHT_FACTOR = 20;
    private final GameObjectCollection gameObjects;
    private final float height;
    private Vector2 trunkTopLeft;
    Renderable trunkRenderer = new RectangleRenderable(ColorSupplier.approximateColor(TRUNK_COLOR));

    /**
     * constructor.
     * @param x bottom-center location of the tree in x dimension.
     * @param y bottom-center location of the tree in y dimension.
     * @param gameObjects the game objects collection.
     */
    SingleTree(float x, float y, GameObjectCollection gameObjects){
        this.gameObjects = gameObjects;
        height = MAX_TREE_SIZE - (DISCOUNT_HEIGHT_FACTOR * (Math.round(x) % LEAVES_FACTOR));
        createTrunk(x, y, gameObjects);
        createLeaf();
    }

    private void createLeaf() {
        for (int y = (int) Math.floor(trunkTopLeft.y() - (height / LEAVES_FACTOR));
             y < Math.floor(trunkTopLeft.y() + (height / LEAVES_FACTOR)); y += Block.SIZE){
            for (int x = (int) Math.floor(trunkTopLeft.x() - (height / LEAVES_FACTOR));
                 x < trunkTopLeft.x() + (height / LEAVES_FACTOR); x += Block.SIZE){
                Vector2 locationOfLeaf = new Vector2(x, y );
                Leaf leaf  = new Leaf(locationOfLeaf,
                        new RectangleRenderable(ColorSupplier.approximateColor(LEAF_COLOR)));
                gameObjects.addGameObject(leaf, PepseGameManager.LEAVES_LAYER);
                leaf.addComponent((a) -> GameObjectKiller.removeObject(leaf,
                        PepseGameManager.LEAVES_LAYER));
            }
        }
    }

    private void createTrunk(float x, float y, GameObjectCollection gameObjects) {
        trunkTopLeft = new Vector2(x,y).add(Vector2.UP.mult(height));
        y = (float) Math.floor(y) +  height + SAFE_UNDERGROUND_TREE;
        for (float i = trunkTopLeft.y(); i < y + height; i+= Block.SIZE){
            Block trunkBlock;
            trunkBlock = new Block(new Vector2(x, i), trunkRenderer);
            gameObjects.addGameObject(trunkBlock, PepseGameManager.TRUNK_LAYER);
            trunkBlock.addComponent((a) -> GameObjectKiller.removeObject(trunkBlock,
                    PepseGameManager.TRUNK_LAYER));
        }
    }
}
