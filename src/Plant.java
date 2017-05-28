import java.util.Random;

public class Plant extends Entity implements Living{
    Random rand = new Random();

    private final int lifespan;

    public Plant(World world, Location location) {
        super(world, location);
        lifespan = rand.nextInt(3) + 7; // lifespan can be from 7 to 9
    }

    public int getLifespan() {return lifespan;}

    public void act() {
        if (!isAlive) return;
        if (age > lifespan) {
            if (world.getShowAll()) {
                System.out.print(this);
                System.out.print("\tdied at ");
                System.out.print(location);
                System.out.println(String.format("\tAge: %d", age));
            }
            this.removeSelfFromWorld();
            return;
        }
        age++;
    }

    @Override
    public int getEnergy() {return lifespan - age;}

    @Override
    public char getChar(){
        return '*';
    }
}