import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public abstract class Animal extends Entity implements Living {
    protected static final double MAX_SIGHT_DISTANCE = 10.0;
    protected static final double MAX_ENERGY = 20; // energy at which animal stops going after food
    protected final int lifespan; // age when the animal dies
    protected final int reproduceEnergy; // energy given to its offspring when it gives birth
    protected int energy; // current energy
    protected int reproduceAge;
    protected int cantMove = 0;
    protected char[] foodChars; // Characters for stuff that this animal can eat
    protected char[] dangerChars; // Characters for stuff that posses a threat to this animal
    private Random rand = new Random();
    
    public Animal(World world, Location location) {
        super(world, location);
        lifespan = rand.nextInt(3) + 15; // lifespan can be from 15 to 17
        reproduceEnergy = rand.nextInt(3) + 7; // reproduceEnergy can be from 7 to 9
        energy = rand.nextInt(6) + 6; // initial energy can be from 6 to 11
        reproduceAge = rand.nextInt(3) + 7; // reproduceAge can be from 8 to 10
    }
    
    public int getEnergy() {return energy;}
    public int weighOptions() {return weighOptions(false);}
    public abstract void giveBirth();

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

    public int weighOptions(boolean noStay) {
        int locationX = location.getX();
        int locationY = location.getY();
        //Use full list of all carnivores and plants in a certain area
        ArrayList<Location> nearestDangers = world.exhaustiveBFS(location, dangerChars, MAX_SIGHT_DISTANCE - 1, true);
        ArrayList<Location> nearestPlant = world.exhaustiveBFS(location, foodChars, MAX_SIGHT_DISTANCE, true);
        double[] possibleMoves = new double[5];

        // Consider no move
        if (nearestDangers.size() > 0){
            possibleMoves[0] += Location.distance(location, nearestDangers.get(0));
            possibleMoves[0] += Location.distance(location, nearestDangers);
        }

        if (this.energy < MAX_ENERGY) {
            if (nearestPlant.size() > 0){
                possibleMoves[0] -= Location.distance(location, nearestPlant.get(0));
                possibleMoves[0] -= Location.distance(location, nearestPlant);
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
                if (nearestPlant.size() > 0){
                    possibleMoves[1] -= Location.distance(upLocation, nearestPlant.get(0));
                    possibleMoves[1] -= Location.distance(upLocation, nearestPlant);
                }
            }
            if (!world.isEmpty(upLocation)) {
                if (world.isPlant(upLocation)){
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
                if (nearestPlant.size() > 0){
                    possibleMoves[2] -= location.distance(rightLocation, nearestPlant.get(0));
                    possibleMoves[2] -= location.distance(rightLocation, nearestPlant);
                }

            }
            if (!world.isEmpty(rightLocation)) {
                if (world.isPlant(rightLocation)){
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
                if (nearestPlant.size() > 0){
                    possibleMoves[3] -= Location.distance(downLocation, nearestPlant.get(0));
                    possibleMoves[3] -= Location.distance(downLocation, nearestPlant);
                }
            }
            if (!world.isEmpty(downLocation)) {
                if (world.isPlant(downLocation)){
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
                if (nearestPlant.size()>0){
                    possibleMoves[4] -= Location.distance(leftLocation, nearestPlant.get(0));
                    possibleMoves[4] -= Location.distance(leftLocation, nearestPlant);
                }
            }
            if (!world.isEmpty(leftLocation)) {
                if (world.isPlant(leftLocation)){
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

    public void eat(Living eaten) {
    // public boolean eat(Living eaten) {
    //     char eatenAsChar = ((Entity)eaten).getChar();
    //     // Is this animal allowed to eat the entity at the current location
    //     if (new String(foodChars).indexOf(eatenAsChar) == -1) {
    //         if (world.getShowAll()) {
    //             System.out.printf("%s\tcan't eat %s%n", this, eaten);
    //         }
    //         return false;
    //     }
        energy += (eaten).getEnergy();
        eaten.removeSelfFromWorld();
        if (world.getShowAll()) {
            System.out.printf("%s\tate %s at %s\tNew Energy: %d%n", 
                this, eaten, location, energy);
        }
        // return true;
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

    public void act() {
        if (!isAlive) return;
        if (age > lifespan || energy < 0) {
            if (world.getShowAll()) {
                System.out.printf("%s\tdied at %s\tAge: %d, Energy: %d%n", 
                    this, location, age, energy);
            }
            this.removeSelfFromWorld();
            return;
        }

        Entity[][] map = world.getMap();
        Location start = new Location(location);
        int startX = start.getX();
        int startY = start.getY();

        move(weighOptions());
        Location end = new Location(location);
        int endX = location.getX();
        int endY = location.getY();

        if (world.getShowAll()) {
            System.out.printf("%s\t%s -> %s%n", this, start, end);
        }

        boolean shouldMove = false;
        if (world.isEmpty(end)) {
            shouldMove = true;
        }
        else {
            // end location is not empty and this animal is not located there
            if (map[endX][endY] != this) {
                shouldMove = tryToEat(map);
                // Entity e = map[endX][endY];
                // shouldMove = eat((Living)e);
            }
        }

        if (shouldMove) {
            map[endX][endY] = this;
        }
        else {
            // if it shouldn't move, then the animal's location should 
            // be the same as where it started
            end = new Location(start);
        }

        if (!start.equals(end)) {
            map[startX][startY] = null;
        }

        // no matter what happens, make sure to set the location to the end
        //  in case the move method changed the location when it shouldn't have
        location = end;

        if (clock != world.getClock()) {
            age++;
            energy--;
            clock = world.getClock();
            if (energy < 5 && !(this instanceof Omnivore)) desperate();
        }
    }

    private void desperate() {
        removeSelfFromWorld();
        Omnivore o = new Omnivore(this);
        if (world.getShowAll()) {
            System.out.printf("%s\tbecame desperate and became an Omnivore at %s\tEnergy: %d%n",
                this, location, energy);
        }
    }
}