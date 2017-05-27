import java.util.ArrayList;
import java.util.Random;

public abstract class Animal extends Entity {
    protected static final double MAX_SIGHT_DISTANCE = 10.0;
    protected static final double MAX_ENERGY = 20; // energy at which animal stops going after food
    protected final int lifespan; // age when the animal dies
    protected final int reproduceEnergy; // energy given to its offspring when it gives birth
    protected double energy; // current energy
    protected int reproduceAge;
    protected int cantMove = 0;
    private Random rand = new Random();
    
    public Animal(World world, Location location) {
        super(world, location);
        lifespan = rand.nextInt(3) + 15; // lifespan can be from 15 to 17
        reproduceEnergy = rand.nextInt(3) + 7; // reproduceEnergy can be from 7 to 9
        energy = rand.nextInt(3) + 4; // initial energy can be from 4 to 6
        reproduceAge = rand.nextInt(3) + 8; // reproduceAge can be from 8 to 10
    }
    
    public double getEnergy() {return energy;}
    
    public abstract int weighOptions();
    public abstract int weighOptions(boolean noStay);
    public abstract void giveBirth();
    public abstract void act();

    // Movement         1
    //                4 0 2
    //                  3

    //Decide the best move based on search algorithm
    //public abstract int bestMove();

    //Change state of
    public void move(int direction) {
        //System.out.print(this);
        //System.out.println(" wants to move in direction " + direction);
        int locationX = location.getX();
        int locationY = location.getY();
        switch (direction) {
            case 1:
                if (locationY > 0) {
                    location.yUp();
                }
                break;
            case 2:
                if (locationX < (world.getXSize() - 1)) {
                    location.xRight();
                }
                break;
            case 3:
                if (locationY < (world.getYSize() - 1)) {
                    location.yDown();
                }
                break;
            case 4:
                if (locationX > 0) {
                    location.xLeft();
                }
                break;
        }
    }

    public Location getBestBirthPlace(int direction) {
        Location birthPlace = null;
        int locationX = location.getX();
        int locationY = location.getY();
        switch (direction) {
            case 1:
                if (locationY > 0 && world.isValid(locationX, locationY - 1)) {
                    birthPlace = new Location(locationX, locationY - 1);
                }
                break;
            case 2:
                if (locationX < (world.getXSize() - 1) && world.isValid(locationX + 1, locationY)) {
                    birthPlace = new Location(locationX + 1, locationY);
                }
                break;
            case 3:
                if (locationY < (world.getYSize() - 1) && world.isValid(locationX, locationY + 1)) {
                    birthPlace = new Location(locationX, locationY + 1);
                }
                break;
            case 4:
                if (locationX > 0 && world.isValid(locationX - 1, locationY)) {
                    birthPlace = new Location(locationX - 1, locationY);
                }
                break;
        }
        return birthPlace;
    }

    public boolean canMove() {
        return age >= cantMove;
    }
}