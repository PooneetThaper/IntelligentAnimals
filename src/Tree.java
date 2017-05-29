import java.util.Random;
import java.util.ArrayList;

/**
 * Represents a Tree that is a subclass of Plant and implements the Obstacle interface.
 */
public class Tree extends Plant implements Obstacle {
    /**
     * An instance of Random which is used to randomize the actions of this Animal.
     */
    Random rand = new Random();

    /**
     * The maximum age that this Plant can be before it dies.
     */
    private final int lifespan;

    /**
     * Creates a new Tree that exists in the given World and is located in the given Location. This Tree can
     * have a lifespan of anywhere between 500 and 550 cycles inclusively.
     * @param world The World in which this Plant exists.
     * @param location The Location where this Plant is located in its World.
     */
    public Tree(World world, Location location) {
        super(world, location);
        lifespan = rand.nextInt(51) + 500; // lifespan can be from 500 to 550
    }

    /**
     * Increments this Tree's age if it is not already dead. It also has a chance to drop food around it (20%
     * chance). When this Tree reaches its lifespan, a new Tree is created in a random, empty Location in the World,
     * and then this Tree dies and removes itself from the World.
     */
    @Override
    public void act() {
        if (!isAlive) return;
        if (age > lifespan) {
            if (world.getShowAll()) {
                System.out.printf("%s\tdied at %s\tAge: %d%n",
                    this, location, age);
            }
            // before dying, let another tree sprout elsewhere in the world
            boolean newTreeHasSprouted = false;
            while (!newTreeHasSprouted) {
                int newX = rand.nextInt(world.getXSize());
                int newY = rand.nextInt(world.getYSize());
                Location newLoc = new Location(newX, newY);

                if (world.isEmpty(newLoc)) {
                    Tree newTree = new Tree(world, newLoc);
                    newTreeHasSprouted = true;
                    if (world.getShowAll()) {
                        System.out.printf("%s sprouted at %s%n", newTree, newLoc);
                    }
                }
            }
            removeSelfFromWorld();
            return;
        }
        // 20% of trying to drop food
        if (Math.random() < 0.2) {
            dropFood();
        }
        age++;
    }

    /**
     * Creates a new Plant at some random Location around this Tree if there exists at least one empty Location.
     */
    public void dropFood() {
        ArrayList<Location> adjacentSpots = new ArrayList<Location>();
        int locationX = this.location.getX();
        int locationY = this.location.getY();
        for (int i = -1; i <= 1; i++) {
            Location above = new Location(locationX + i, locationY + 1);
            Location below = new Location(locationX + i, locationY - 1);
            adjacentSpots.add(above);
            adjacentSpots.add(below);
            if (i != 0) {
                Location sameRow = new Location(locationX + i, locationY);
                adjacentSpots.add(sameRow);
            }
        }

        for (int i = adjacentSpots.size() - 1; i >= 0; i--) {
            Location loc = adjacentSpots.get(i);
            if (!(world.isValid(loc) && world.isEmpty(loc))) {
                adjacentSpots.remove(loc);
            }
        }
        
        int numEmptySpots = adjacentSpots.size();
        if (numEmptySpots > 0) {
            int randomSpot = rand.nextInt(numEmptySpots);
            Plant food = new Plant(world, adjacentSpots.get(randomSpot));
            if (world.getShowAll()) {
                System.out.printf("%s\tdropped food at %s%n", this, adjacentSpots.get(randomSpot));
            }
        }
    }

    /**
     * Gets the Tree's character representation.
     * @return this Tree's character representation, 'T'
     */
    @Override
    public char getChar() {
        return 'T';
    }
}