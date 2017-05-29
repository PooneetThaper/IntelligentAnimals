import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Represents an Animal that is a subclass of Entity and implements the Living interface.
 */
public abstract class Animal extends Entity implements Living {
    /**
     * The farthest this Animal can see from its own location.
     */
    protected static final double MAX_SIGHT_DISTANCE = 10.0;

    /**
     * The maximum amount of energy this Animal can have before it stops going after food.
     */
    protected static final double MAX_ENERGY = 40;

    /**
     * The maximum age that this Animal can be before it dies.
     */
    protected final int lifespan;

    /**
     * The minimum amount of energy this Animal needs to give birth.
     */
    protected final int reproduceEnergy;

    /**
     * The current amount of energy this Animal has.
     */
    protected int energy;

    /**
     * The minimum age that this Animal must be before it can give birth.
     */
    protected int reproduceAge;

    /**
     * The minimum age that this Animal must be before it can act.
     */
    protected int cantMove = 0;

    /**
     * An Array of characters that stores the character representations of Entities that this Animal can eat.
     */
    protected char[] foodChars;

    /**
     * An Array of characters that stores the character representations of Entities that poses a threat to this
     * Animal.
     */
    protected char[] dangerChars;

    /**
     * An instance of Random which is used to randomize the actions of this Animal.
     */
    private Random rand = new Random();

    /**
     * Creates a new Animal that exists in a given World and is initially located at a given Location.
     * Its lifespan can be any integer between 15 and 17.
     * Its reproduceEnergy can be any integer between 27 and 29.
     * Its starting energy can be any integer between 10 and 14.
     * Its reproduceAge can be any integer between 7 and 9.
     * @param world The World in which this Animal exists.
     * @param location The initial Location where this Animal is located.
     */
    public Animal(World world, Location location) {
        super(world, location);
        lifespan = rand.nextInt(3) + 15;
        reproduceEnergy = rand.nextInt(3) + 27;
        energy = rand.nextInt(5) + 10;
        reproduceAge = rand.nextInt(3) + 7;
    }

    /**
     * Gets this Animal's current amount of energy.
     * @return this Animal's energy.
     */
    public int getEnergy() {return energy;}

    /**
     * Gets the best direction that this Animal should move in, as shown below.
     * Movement     1
     *            4 0 2
     *              3
     * @return a direction that this Animal should move in.
     */
    public int weighOptions() {return weighOptions(false);}

    /**
     * Allows this Animal to attempt to give birth and returns a boolean value of whether or not it was successful.
     * @return a boolean value of whether or not this Animal actually gave birth. True if it did give birth. False
     * otherwise.
     */
    public abstract boolean giveBirth();

    /**
     * Changes the location of this Animal depending on what direction it is heading towards.
     * @param direction An integer between 0 and 4 inclusively that represents the direction in which this Animal
     *                  is heading towards.
     *                  Movement     1
     *                             4 0 2
     *                               3
     */
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

    /**
     * Gets the best Location where this Animal's baby can be initialized to. If there is not valid location for the
     * baby, then it returns null.
     * @param direction An integer between 0 and 4 inclusively that represents the direction in which this Animal
     *                  might give birth in.
     *                  Movement     1
     *                             4 0 2
     *                               3
     * @return the best location where this Animal's baby should be born in. If null is returned, then there is no
     * such location and this Animal does not give birth successfully.
     */
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
        if (birthPlace != null && !world.isEmpty(birthPlace)) {
            birthPlace = null;
        }
        return birthPlace;
    }

    /**
     * Determines whether or not this Animal can move depending on its age. This Animal must have an age that is
     * greater than or equal to its cantMove field to return true.
     * @return a boolean value of whether or not this Animal can move. True if this Animal can move. False otherwise.
     */
    public boolean canMove() {
        if (age < cantMove) {
            if (world.getShowAll()) {
                System.out.printf("%s\tat %s can't move yet.\tAge: %d, Energy: %d%n",
                    this, location, age, energy);
            }
            return false;
        }
        return true;
    }

    /**
     * Determines which direction this Animal should go in for its next move.
     * @param noStay A boolean value that discourages this Animal from staying in its current location if it is true.
     * @return the best direction that this Animal should go in.
     */
    public int weighOptions(boolean noStay) {
        int locationX = location.getX();
        int locationY = location.getY();
        //Use full list of all carnivores and plants in a certain area
        ArrayList<Location> nearestDangers = world.exhaustiveBFS(location, dangerChars, MAX_SIGHT_DISTANCE , true);
        ArrayList<Location> nearestFood = world.exhaustiveBFS(location, foodChars, MAX_SIGHT_DISTANCE, true);
        double[] possibleMoves = new double[5];

        // Consider no move
        if (nearestDangers.size() > 0){
            possibleMoves[0] += Location.distance(location, nearestDangers.get(0));
            possibleMoves[0] += Location.distance(location, nearestDangers);
        }

        if (this.energy < MAX_ENERGY) {
            if (nearestFood.size() > 0){
                possibleMoves[0] -= Location.distance(location, nearestFood.get(0));
                possibleMoves[0] -= Location.distance(location, nearestFood);
            }
        }
        if (noStay) possibleMoves[0] -= 20;

        // Consider up
        Location upLocation = new Location(locationX, locationY - 1);
        if (world.isValid(upLocation)) {
            if (nearestDangers.size() > 0){
                possibleMoves[1] += Location.distance(upLocation, nearestDangers.get(0));
                possibleMoves[1] += Location.distance(upLocation, nearestDangers);
            }

            if (this.energy < MAX_ENERGY) {
                if (nearestFood.size() > 0){
                    possibleMoves[1] -= Location.distance(upLocation, nearestFood.get(0));
                    possibleMoves[1] -= Location.distance(upLocation, nearestFood);
                }
            }
            if (!world.isEmpty(upLocation)) {
                if (new String(foodChars).indexOf(world.getLocationChar(upLocation)) != -1){
                    if (this.energy < MAX_ENERGY) possibleMoves[1] += 15;
                }
                else possibleMoves[1] -= 15;
            }
        }else possibleMoves[1] -= 15;

        // Consider right
        Location rightLocation = new Location(locationX + 1,locationY);
        if (world.isValid(rightLocation)) {
            if (nearestDangers.size() > 0){
                possibleMoves[2] += Location.distance(rightLocation, nearestDangers.get(0));
                possibleMoves[2] += Location.distance(rightLocation, nearestDangers);
            }
            if (this.energy < MAX_ENERGY) {
                if (nearestFood.size() > 0){
                    possibleMoves[2] -= location.distance(rightLocation, nearestFood.get(0));
                    possibleMoves[2] -= location.distance(rightLocation, nearestFood);
                }
            }
            if (!world.isEmpty(rightLocation)) {
                if (new String(foodChars).indexOf(world.getLocationChar(rightLocation)) != -1){
                    if (this.energy < MAX_ENERGY) possibleMoves[2] += 15;
                }
                else possibleMoves[2] -= 15;
            }
        }else possibleMoves[2] -= 15;

        // Consider down
        Location downLocation = new Location(locationX,locationY + 1);
        if (world.isValid(downLocation)) {
            if (nearestDangers.size() > 0){
                possibleMoves[3] += Location.distance(downLocation, nearestDangers.get(0));
                possibleMoves[3] += Location.distance(downLocation, nearestDangers);
            }
            if (this.energy < MAX_ENERGY) {
                if (nearestFood.size() > 0){
                    possibleMoves[3] -= Location.distance(downLocation, nearestFood.get(0));
                    possibleMoves[3] -= Location.distance(downLocation, nearestFood);
                }
            }
            if (!world.isEmpty(downLocation)) {
                if (new String(foodChars).indexOf(world.getLocationChar(downLocation)) != -1){
                    if (this.energy < MAX_ENERGY) possibleMoves[3] += 15;
                }
                else possibleMoves[3] -= 15;
            }
        }else possibleMoves[3] -= 15;

        // Consider left
        Location leftLocation = new Location(locationX - 1, locationY);
        if (world.isValid(leftLocation)) {
            if (nearestDangers.size() > 0){
                possibleMoves[4] += Location.distance(leftLocation, nearestDangers.get(0));
                possibleMoves[4] += Location.distance(leftLocation, nearestDangers);
            }
            if (this.energy < MAX_ENERGY) {
                if (nearestFood.size()>0){
                    possibleMoves[4] -= Location.distance(leftLocation, nearestFood.get(0));
                    possibleMoves[4] -= Location.distance(leftLocation, nearestFood);
                }
            }
            if (!world.isEmpty(leftLocation)) {
                if (new String(foodChars).indexOf(world.getLocationChar(leftLocation)) != -1){
                    if (this.energy < MAX_ENERGY) possibleMoves[4] += 15;
                }
                else possibleMoves[4] -= 15;
            }
        } else possibleMoves[4] -= 15;

        /*
        System.out.print(this);
        System.out.print(Location);
        System.out.printf(" [%f,%f,%f,%f,%f]%n",possibleMoves[0],possibleMoves[1],possibleMoves[2],possibleMoves[3],possibleMoves[4]);
        */

        int maxIndex = 0;
        for (int i = 1 ; i < 5; i++){
            if (possibleMoves[i] > possibleMoves[maxIndex]) maxIndex = i;
        }
        return maxIndex;
    }

    /**
     * Causes this Animal to eat another Living Entity in the World that they belong to. This Animal gains the
     * amount of energy that the eaten Living Entity has. The eaten Living Entity also removes itself from the
     * World since it is no longer alive.
     * @param eaten An Entity that is Living, which this Animal eats and gains energy from.
     */
    public void eat(Living eaten) {
        energy += (eaten).getEnergy();
        eaten.removeSelfFromWorld();
        if (world.getShowAll()) {
            System.out.printf("%s\tate %s at %s\tNew Energy: %d%n", 
                this, eaten, location, energy);
        }
    }

    private boolean tryToEat(Entity[][] map) {
        int currentX = location.getX();
        int currentY = location.getY();
        char currentEntity = map[currentX][currentY].getChar();

        // Is this animal allowed to eat the entity at the current location
        if (new String(foodChars).indexOf(currentEntity) == -1) {
            if (world.getShowAll()) {
                Entity e = map[currentX][currentY];
                System.out.printf("%s\tcan't eat %s%n", this, e);
            }
            return false;
        }
        Living eaten;
        switch (currentEntity) {
            case '&':
                eaten = (Herbivore)map[currentX][currentY];
                eat(eaten);
                break;
            case '*':
                eaten = (Plant)map[currentX][currentY];
                eat(eaten);
                break;
            case '$':
                eaten = (Omnivore)map[currentX][currentY];
                eat(eaten);
                break;
        }
        return true;
    }

    /**
     * A method that tells this Animal to do something. It makes sures that this Animal is alive before proceeding.
     * If this Animal reached its lifespan or has no more energy, then it dies by removing itself from its World.
     * If this Animal is not yet able to move, it does nothing. Otherwise, this Animal will see if it is capable
     * of giving birth to another Animal. If it is, then it does so. Otherwise, this Animal sees if it can move to
     * another Location where it may or may not eat food. If this Animal has less than 8 energy units, then it
     * changes to an Omnivore, if it already isn't one in order to have a wider range of Animals to eat. This makes
     * the Animals more adaptable and capable of surviving longer to create a sustainable World.
     */
    public void act() {
        // animal should not do anything if it's not alive
        if (!isAlive) return;

        // animal should die if it is too old or if it has no energy
        if (age > lifespan || energy < 0) {
            if (world.getShowAll()) {
                System.out.printf("%s\tdied at %s\tAge: %d, Energy: %d%n", 
                    this, location, age, energy);
            }
            this.removeSelfFromWorld();
            return;
        }

        // if animal is a newborn and can't move, it should just age up and lose energy
        if (!canMove()) {
            if (clock != world.getClock()) {
                age++;
                clock = world.getClock();
            }
            return;
        }

        // animal should try to give birth if it can
        if (age >= reproduceAge && energy > reproduceEnergy) {
            // if it gave birth successfully, it can't move this turn
            if (giveBirth()) {
                if (clock != world.getClock()) {
                    age++;
                    clock = world.getClock();
                }
                return;
            }
        }

        // otherwise, try to move
        Entity[][] map = world.getMap();
        Location start = new Location(location);
        int startX = start.getX();
        int startY = start.getY();

        move(weighOptions());
        Location end = new Location(location);
        int endX = location.getX();
        int endY = location.getY();

        if (world.getShowAll()) {
            System.out.printf("%s\t%s -> %s \tAge: %d, Energy: %d%n", this, start, end, age, energy);
        }

        boolean shouldMove = false;
        if (world.isEmpty(end)) {
            shouldMove = true;
        }
        else {
            // end location is not empty and this animal is not located there
            if (map[endX][endY] != this) {
                shouldMove = tryToEat(map);
            }
        }

        if (shouldMove) {
            map[endX][endY] = this;
        }
        else {
            // if it shouldn't move, then the animal's location should be the same as where it started
            end = new Location(start);
        }

        if (!start.equals(end)) {
            map[startX][startY] = null;
        }

        // no matter what happens, make sure to set the location to the end
        // in case the move method changed the location when it shouldn't have
        location = end;

        if (clock != world.getClock()) {
            age++;
            clock = world.getClock();
        }
        energy--;
        if (energy < 8 && !(this instanceof Omnivore)) {
            desperate();
        }
    }

    /**
     * Replaces this Animal with an Omnivore, which can eat a wider range of Entities.
     */
    private void desperate() {
        removeSelfFromWorld();
        Omnivore o = new Omnivore(this);
        if (world.getShowAll()) {
            System.out.printf("%s\tbecame desperate and became an Omnivore at %s\tEnergy: %d%n",
                this, location, energy);
        }
    }
}