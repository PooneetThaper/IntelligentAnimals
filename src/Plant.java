import java.util.Random;

/**
 * Represents a Plant that is a subclass of Entity and implements the Living interface.
 */
public class Plant extends Entity implements Living {
    /**
     * An instance of Random which is used to randomize the actions of this Animal.
     */
    Random rand = new Random();

    /**
     * The maximum age that this Plant can be before it dies.
     */
    private final int lifespan;

    /**
     * Creates a new Plant that exists in the given World and is located in the given Location. This Plant can
     * have a lifespan of anywhere between 15 and 18 cycles inclusively.
     * @param world The World in which this Plant exists.
     * @param location The Location where this Plant is located in its World.
     */
    public Plant(World world, Location location) {
        super(world, location);
        lifespan = rand.nextInt(4) + 15;
    }

    /**
     * Gets this Plant's lifespan.
     * @return this Plant's lifespan.
     */
    public int getLifespan() {return lifespan;}

    /**
     * Increments the age of this Plant if it is not already dead. If this Plant reached its lifespan, it removes
     * itself from the World.
     */
    public void act() {
        if (!isAlive) return;
        if (age > lifespan || getEnergy() <= 0) {
            if (world.getShowAll()) {
                System.out.printf("%s\tdied at %s\tAge: %d%n",
                    this, location, age);
            }
            removeSelfFromWorld();
            return;
        }
        age++;
    }

    /**
     * Gets the amount of energy that this Plant is worth when eaten: lifespan - age + 5
     * @return the amount of energy that this Plant is worth when eaten.
     */
    @Override
    public int getEnergy() {
        return lifespan - age + 5;
    }

    /**
     * Gets the Plant's character representation.
     * @return this Plant's character representation, '*'
     */
    @Override
    public char getChar() {return '*';}
}