/**
 * Represents an Entity that exists in the world.
 */
public abstract class Entity {
    /**
     * The world in which the Entity exists in.
     */
    protected World world;

    /**
     * The location in the world where the Entity is located.
     */
    protected Location location;

    /**
     * The number of cycles in which the Entity has existed in the world for.
     */
    protected int age;

    /**
     * A boolean value that states whether or not an Entity is still alive.
     */
    protected boolean isAlive;

    /**
     * A variable used to keep track of whether or not the Entity has moved during the world's current cycle.
     */
    protected int clock;

    /**
     * Creates a new Entity in the given world and location. Initiates the Entity's age to 0, its isAlive field to
     * true, and its clock to its world's clock.
     * @param world The world in which the Entity exists in.
     * @param location The location in the world where the Entity is located.
     */
    public Entity(World world, Location location) {
        this.world = world;
        world.addEntity(location, this);
        this.location = location;
        age = 0;
        isAlive = true;
        clock = world.getClock();
    }

    /**
     * Gets the world where the Entity exists in.
     * @return this Entity's world.
     */
    public World getWorld() {return world;}

    /**
     * Gets the location where the Entity is located in the world.
     * @return this Entity's location.
     */
    public Location getLocation() {return location;}

    /**
     * Gets the number of cycles in which the Entity has existed in the world for.
     * @return this Entity's age.
     */
    public int getAge() {return age;}

    /**
     * Gets the living status of the Entity.
     * @return this Entity's isAlive field. If it is true, then the Entity is still alive. Otherwise, it is not.
     */
    public boolean isAlive() {return isAlive;}

    /**
     * Gets the clock of the Entity, which is used to determine whether or not the Entity moved during the current
     * cycle or iteration.
     * @return this Entity's clock.
     */
    public int getClock() {return clock;}

    /**
     * All non-abstract subclasses should have an act() method which allows them to do something during each cycle
     * or iteration.
     */
    public abstract void act();

    /**
     * All non-abstract subclasses should have a getChar() method.
     * @return the character representation of this Entity in the world when printed in the terminal.
     */
    public abstract char getChar();

    /**
     * Removes the Entity from the world. It removes itself from its world's map so it can no longer be referenced
     * by the world. It also sets its isAlive status to false.
     */
    public void removeSelfFromWorld() {
        if (!isAlive) {
            throw new IllegalStateException("Entity is not in the world");
        }
        Entity[][] map = world.getMap();
        int x = location.getX();
        int y = location.getY();
        if (map[x][y] != this) {
            System.out.println("Location " + location + " does not contain " + this);
        }else{
            map[x][y] = null;
        }
        if (this instanceof Animal) {
            world.getAnimals().remove(this);
        }
        else if (this instanceof Plant) {
            world.getPlants().remove(this);
        }
        map[x][y] = null;
        isAlive = false;
    }
}