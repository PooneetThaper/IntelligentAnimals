/**
 * Represents a Rock that is a subclass of Entity and implements the Obstacle interface.
 */
public class Rock extends Entity implements Obstacle {

    /**
     * Creates a new Rock that exists in the given World and is located in a given Location.
     * @param world The World in which this Rock exists.
     * @param location The Location in which this Rock is located in the World.
     */
    public Rock(World world, Location location) {
        super(world, location);
    }

    /**
     * Gets the Rock's character representation.
     * @return this Rock's character representation, '?'
     */
    @Override
    public char getChar(){return '?';}

    /**
     * Does not do anything is it is a rock.
     */
    public void act(){
        // Its a rock... it doesn't really do anything...
        // (but need this because its an entity)
        return;
    }
}
