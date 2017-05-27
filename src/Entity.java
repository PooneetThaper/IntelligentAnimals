public abstract class Entity {
    protected World world;
    protected Location location;
    protected int age; // everything should have an age
    protected boolean isAlive; // if we're adding rocks, then i guess they're always alive?
    
    public Entity(World world, Location location) {
        this.world = world;
        world.addEntity(location, this);
        this.location = location;
        age = 0;
        isAlive = true;
    }

    public int getAge() {return age;}

    public abstract void act();

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
            throw new IllegalStateException("Location loc does not contain this entity");
        }
        if (this instanceof Animal) {
            world.getAnimals().remove(this);
        }
        else if (this instanceof Plant) {
            world.getPlants().remove(this);
        }
        map[x][y] = null;
        world = null;
        location = null;
        isAlive = false;
    }
    
    public abstract char getChar();
}