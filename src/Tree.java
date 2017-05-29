import java.util.Random;
import java.util.ArrayList;

public class Tree extends Plant implements Obstacle {
    
    Random rand = new Random();
    private final int lifespan;

    public Tree(World world, Location location) {
        super(world, location);
        lifespan = rand.nextInt(51) + 500; // lifespan can be from 500 to 550
    }

    public int getLifespan() {return lifespan;}

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
        // 10% of trying to drop food
        if (Math.random() < 0.1) {
            dropFood();
        }
        age++;
    }

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

    @Override
    public char getChar() {
        return 'T';
    }
}