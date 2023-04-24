package pepse.world.trees;

import danogl.collisions.GameObjectCollection;

import java.util.Random;
import java.util.function.Function;

/**
 * Responsible for tree generation.
 */
public class Tree {
    public static final int TREE_SPARSITY = 500;
    public static final int CONST_ADD = 13;
    public static final int ADD_TREE = 0;
    private final int seed;
    private final GameObjectCollection gameObjects;
    private final Function<Float, Float> groundHeightFunc;

    /**
     * constructor.
     * @param gameObjects game objects collection.
     * @param seed seed for random control.
     * @param groundHeightFunc function that returns the ground height in a specific location.
     */
    public Tree(GameObjectCollection gameObjects, int seed, Function<Float, Float> groundHeightFunc){
        this.seed = seed;
        this.gameObjects = gameObjects;
        this.groundHeightFunc = groundHeightFunc;
    }

    /**
     * create trees in a range.
     * @param minX min boundary to create trees at.
     * @param maxX max boundary to create trees at.
     */
    public void createInRange(int minX, int maxX) {
        Random random = new Random(seed + CONST_ADD);
        System.out.println("seed  "+ seed);
        for (int x = minX; x < maxX; x++) {
            if(random.nextInt(TREE_SPARSITY) == ADD_TREE){
                SingleTree tree = new SingleTree(x, groundHeightFunc.apply((float) x), gameObjects);
            }
        }
    }
}
