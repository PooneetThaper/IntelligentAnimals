public abstract class Entity {
    protected World world;
    protected Location location;
    protected int age; // everything should have an age
    protected boolean isAlive; // if we're adding rocks, then i guess they're always alive?
    protected int clock;
    
    public Entity(World world, Location location) {
        this.world = world;
        world.addEntity(location, this);
        this.location = location;
        age = 0;
        isAlive = true;
        clock = world.getClock();
    }

    public World getWorld() {return world;}
    public Location getLocation() {return location;}
    public int getAge() {return age;}
    public boolean isAlive() {return isAlive;}
    public int getClock() {return clock;}

    public abstract void act();
    public abstract char getChar();

    // removes this Entity from the world
    public void removeSelfFromWorld() {
        // if it is already not alive, we can't move it because
        // it shouldn't be there in the first place
        if (!isAlive) {
            throw new IllegalStateException("Entity is not in the world");
        }
        // if this entity is not in the location it should be in,
        // something went horribly wrong
        Entity[][] map = world.getMap();
        int x = location.getX();
        int y = location.getY();
        if (map[x][y] != this) {
            // throw new IllegalStateException("Location loc does not contain this entity");
            //throw new IllegalStateException("Location " + location + " does not contain " + this);
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
        // world = null;
        // location = null;
        isAlive = false;
    }
}