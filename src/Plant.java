import java.util.Random;

public class Plant extends Entity implements Living {
    Random rand = new Random();

    private final int lifespan;

    public Plant(World world, Location location) {
        super(world, location);
        lifespan = rand.nextInt(4) + 15; // lifespan can be from 15 to 18
    }

    public int getLifespan() {return lifespan;}

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

    @Override
    public int getEnergy() {
        // in case an herbivore eats the plant at its last cycle,
        // it still gets some energy
        return lifespan - age + 5;
    }

    @Override
    public char getChar() {
        return '*';
    }
}