import java.util.ArrayList;
import java.util.Random;

/**
 * Represents a World in which all of the entities that belong in the World exist.
 */
public class World {
    /**
     * A variable that keeps track of the number of cycles or iterations this World has run for.
     */
    private int clock = 0;

    /**
     * The dimensions of the 2D world. Looking at the world from a bird's eye view, LEN_X corresponds to how far the
     * world extends from left to right, and LEN_Y corresponds to how far the world extends from top to bottom.
     */
    private final int LEN_X, LEN_Y;

    /**
     * A 2D array that keeps track of this World's Entities and where they are located. Treating Map as a coordinate
     * system, the origin (0,0) is located at the upper-left corner of Map. The x-coordinate increases to the right,
     * and the y-coordinate increases downward. Some locations may be empty and entity-less.
     */
    private Entity[][] Map;

    /**
     * An ArrayList that keeps track of all of the Entities that are Animals in this World.
     */
    private ArrayList<Animal> Animals = new ArrayList<>();

    /**
     * An ArrayList that keeps track of all of the Entities that are Plants in this World.
     */
    private ArrayList<Plant> Plants = new ArrayList<>();

    /**
     * An instance of Random which is used to randomize the actions of this World.
     */
    private Random random;

    /**
     * A variable that is used to determine whether or not all of the actions of the Entities should be displayed
     * in the terminal as text. Used for testing purposes.
     */
    private boolean showAll = true;

    /**
     * Creates a new World of size lenX by lenY. The dimensions must be positive; otherwise, it will throw an
     * exception. If the given dimensions are valid, it initiates its Map field as an empty 2D array of the given
     * dimensions. It also initiates its random field to prepare for randomizing its actions. Additionally,
     * if this constructor is used, the showAll variable is true by default.
     * @param lenX The x-dimension of this World. Must be positive.
     * @param lenY The y-dimension of this World. Must be positive.
     */
    public World(int lenX, int lenY) {
        if (lenX <= 0 || lenY <= 0) {
            throw new IllegalArgumentException("The dimensions of the world must be positive.");
        }
        this.LEN_X = lenX;
        this.LEN_Y = lenY;
        Map = new Entity[lenX][lenY];
        random = new Random();
    }

    /**
     * Creates a new World of size lenX by lenY. The dimensions must be positive; otherwise, it will throw an
     * exception. If the given dimensions are valid, it initiates its Map field as an empty 2D array of the given
     * dimensions. It also initiates its random field to prepare for randomizing its actions. Additionally,
     * the showAll field takes on the provided valid.
     * @param lenX The x-dimension of this World. Must be positive.
     * @param lenY The y-dimension of this World. Must be positive.
     * @param showAll A boolean value used to determine whether or not all of the actions of this World's Entities
     *                should be displayed in the terminal as text.
     */
    public World(int lenX, int lenY, boolean showAll) {
        this(lenX, lenY);
        this.showAll = showAll;
    }

    /**
     * Gets the number of cycles or iterations this World has run for.
     * @return this World's clock.
     */
    public int getClock() {return clock;}

    /**
     * Gets the x-dimension of this World.
     * @return this World's LEN_X field, which is the x-dimension.
     */
    public int getXSize() {return LEN_X;}

    /**
     * Gets the y-dimension of this World.
     * @return this World's LEN_Y field, which is the y-dimension.
     */
    public int getYSize() {return LEN_Y;}

    /**
     * Gets a 2D array of the Entities located at every possible location of this world.
     * @return this World's Map where its Entities are stored in their respective locations.
     */
    public Entity[][] getMap() {return Map;}

    /**
     * Gets an ArrayList of all of the Animals that are still alive in this World.
     * @return this World's Animals as an ArrayList.
     */
    public ArrayList<Animal> getAnimals() {return Animals;}

    /**
     * Gets an ArrayList of all of the Plants that are still alive in this World.
     * @return this World's Plants as an ArrayList.
     */
    public ArrayList<Plant> getPlants() {return Plants;}

    /**
     * Gets a boolean value that determines whether or not this World's Entities should display their actions
     * as text in the terminal.
     * @return this World's showAll field.
     */
    public boolean getShowAll() {return showAll;}

    /**
     * Returns a random permutation of a given ArrayList of Animals.
     * @param A An ArrayList of Animals.
     * @return A random permutation of the given ArrayList of Animals.
     */
    public ArrayList<Animal> randomPermute(ArrayList<Animal> A) {
        ArrayList<Animal> retval = new ArrayList<>();
        while (A.size() > 0) {
            int index = ((Math.abs(random.nextInt())) % A.size());
            retval.add(A.get(index));
            A.remove(index);
        }
        return retval;
    }

    // Adding living things methods

    /**
     * Initializes this World with a given number of Carnivores, Herbivores, Plants, Rocks, and Trees. The Entities
     * are placed in random Locations in this World.
     * @param numCarnivores The provided number of Carnivores this World will start with.
     * @param numHerbivores The provided number of Herbivores this World will start with.
     * @param numPlants The provided number of Plants this World will start with.
     * @param numRocks The provided number of Rocks this World will start with.
     * @param numTrees The provided number of Trees this World will start with.
     */
    public void initialSpawn(int numCarnivores, int numHerbivores, int numPlants, int numRocks, int numTrees) {
        while (numRocks > 0) {
            int x = Math.abs(random.nextInt()) % LEN_X;
            int y = Math.abs(random.nextInt()) % LEN_Y;
            Location loc = new Location(x, y);
            if (isEmpty(loc)) {
                Rock r = new Rock(this, loc);
                numRocks--;
            }
        }
        while (numTrees > 0) {
            int x = Math.abs(random.nextInt()) % LEN_X;
            int y = Math.abs(random.nextInt()) % LEN_Y;
            Location loc = new Location(x, y);
            if (isEmpty(loc)) {
                Tree t = new Tree(this, loc);
                numTrees--;
            }
        }
        while (numCarnivores > 0) {
            int x = Math.abs(random.nextInt()) % LEN_X;
            int y = Math.abs(random.nextInt()) % LEN_Y;
            Location loc = new Location(x, y);
            if (isEmpty(loc)) {
                Carnivore c = new Carnivore(this, loc);
                numCarnivores--;
            }
        }
        while (numHerbivores > 0) {
            int x = Math.abs(random.nextInt()) % LEN_X;
            int y = Math.abs(random.nextInt()) % LEN_Y;
            Location loc = new Location(x, y);
            if (isEmpty(loc)) {
                Herbivore h = new Herbivore(this, loc);
                numHerbivores--;
            }
        }
        while (numPlants > 0) {
            int x = Math.abs(random.nextInt()) % LEN_X;
            int y = Math.abs(random.nextInt()) % LEN_Y;
            Location loc = new Location(x, y);
            if (isEmpty(loc)) {
                Plant p = new Plant(this, loc);
                numPlants--;
            }
        }
    }

    /**
     * Places a given Entity at a given Location in this World. If the Entity is an Animal or a Plant, it will also
     * add it to the respective ArrayList.
     * @param loc The given Location where the Entity should be placed in. Must be valid; otherwise, it will throw
     *            an exception.
     * @param e The given Entity which is to be placed in the given Location. Cannot be null; otherwise, it will
     *          throw an exception.
     */
    public void addEntity(Location loc, Entity e) {
        if (!isValid(loc)) {
            throw new IllegalArgumentException("Location loc is not in the world");
        }
        if (e == null) {
            throw new IllegalArgumentException("Entity e cannot be null");
        }
        // add Entity e to the world
        Map[loc.getX()][loc.getY()] = e;

        if (e instanceof Animal) {
            Animals.add((Animal)e);
        }
        else if (e instanceof Plant) {
            Plants.add((Plant)e);
        }
    }

    /**
     * Places new plants at random, empty locations in this World.
     */
    public void spawnPlant() {
        int x = Math.abs(random.nextInt()) % LEN_X;
        int y = Math.abs(random.nextInt()) % LEN_Y;
        Location loc = new Location(x, y);
        if (isEmpty(loc)) {
            Plant added = new Plant(this, loc);
            if (showAll) {
                System.out.printf("%s\tspawned at %s%n", added, loc);
            }
        }
    }

    // Pathfinding Methods

    /**
     * Searches for all instances of the entity matching the given character target within a given distance maxDist.
     * Starts the breadth first search at the given Location start and searches in a flood fill as long as the distance
     * of the current location from the start location is less than maxDist. If boolean exhaustive is false, the search
     * stops at the first found instance. Otherwise, returns all found instances in the given area.
     * @param start The starting location of the breadth first search
     * @param target The character of the type that is being searched for
     * @param maxDist The maximum distance that the search is allowed to look
     * @param exhaustive Whether or not to find all instances (true) or only one (false)
     * @return An array of found instances given the parameters and the state of this World
     */
    public ArrayList<Location> exhaustiveBFS(Location start, char target, double maxDist, boolean exhaustive) {
        return exhaustiveBFS(start,new char[]{target}, maxDist, exhaustive);
    }

    /**
     * Searches for all instances of the entity matching the any of the given characters in array targets within a
     * given distance maxDist. Starts the breadth first search at the given Location start and searches in a flood fill
     * as long as the distance of the current location from the start location is less than maxDist. If boolean
     * exhaustive is false, the search stops at the first found instance. Otherwise, returns all found instances in the given area.
     * @param start The starting location of the breadth first search
     * @param targets The array of characters of the types that are being searched for
     * @param maxDist The maximum distance that the search is allowed to look
     * @param exhaustive Whether or not to find all instances (true) or only one (false)
     * @return An array of found instances given the parameters and the state of this World
     */
    public ArrayList<Location> exhaustiveBFS(Location start, char[] targets, double maxDist, boolean exhaustive) {
        ArrayList<Location> found = new ArrayList<>();
        Queue<Location> seen = new Queue<>();
        Queue<Location> queue = new Queue<>();
        queue.enqueue(start);
        while (!queue.isEmpty()) {
            Location current = queue.dequeue();
            int currentX = current.getX();
            int currentY = current.getY();

            if (Map[currentX][currentY] != null) {
                for (char target : targets){
                    if (Map[currentX][currentY].getChar() == target) {
                        found.add(current);
                        if (!exhaustive) {
                            break;
                        }
                    }
                }
            }

            Location upLocation = new Location(currentX, currentY - 1);
            if (validLocation(upLocation, start, maxDist, ' ') && !(Map[currentX][currentY-1] instanceof Obstacle) && !queue.contains(upLocation) && !seen.contains(upLocation)) {
                queue.enqueue(upLocation);
            }

            Location rightLocation = new Location(currentX + 1, currentY);
            if (validLocation(rightLocation, start, maxDist, ' ') && !(Map[currentX+1][currentY] instanceof Obstacle) && !queue.contains(rightLocation) && !seen.contains(rightLocation)) {
                queue.enqueue(rightLocation);
            }

            Location downLocation = new Location(currentX, currentY + 1);
            if (validLocation(downLocation, start, maxDist, ' ') && !(Map[currentX][currentY+1] instanceof Obstacle) && !queue.contains(downLocation) && !seen.contains(downLocation)) {
                queue.enqueue(downLocation);
            }

            Location leftLocation = new Location(currentX - 1, currentY);
            if (validLocation(leftLocation, start, maxDist, ' ') && !(Map[currentX-1][currentY] instanceof Obstacle) && !queue.contains(leftLocation) && !seen.contains(leftLocation)) {
                queue.enqueue(leftLocation);
            }
            seen.enqueue(current);
        }
        return found;
    }

    /**
     * Determines if the given location (next) is valid for breadth first search given the start location, maxDistance,
     * and forbidden characters.
     * @param next The current location in consideration for search
     * @param start The start location of the search
     * @param maxDistance The maximum distance the search is allowed to look
     * @param forbidden The characters that can't be move onto
     * @return
     */
    private boolean validLocation(Location next, Location start, double maxDistance, char forbidden) {
        int nextX = next.getX();
        int nextY = next.getY();
        return validLocation(nextX, nextY, start, maxDistance, forbidden);
    }

    /**
     * Determines if the given location (next) is valid for breadth first search given the start location, maxDistance,
     * and forbidden characters.
     * @param x The x coordinate of the current location in consideration for search
     * @param y The y coordinate of the current location in consideration for search
     * @param start The start location of the search
     * @param maxDistance The maximum distance the search is allowed to look
     * @param forbidden The characters that can't be move onto
     * @return
     */
    private boolean validLocation(int x, int y, Location start, double maxDistance, char forbidden) {
        boolean notBlocked = true;
        int startX = start.getX();
        int startY = start.getY();
        if (Map[startX][startY] != null) {
            if (Map[startX][startY].getChar() == forbidden) {
                notBlocked = false;
            }
        }
        return (isValid(x, y) && (Location.distance(x, y, start) <= maxDistance) && notBlocked);
    }

    // Update methods

    /**
     * Iterates through a random permutation of the ArrayList of Animals so that each Animal can have an equally
     * likely chance of acting next (Animals that were added to the ArrayList first are not prioritized over other
     * Animals). Each Animal gets to act at least once during this World's clock cycle. Carnivores are more likely
     * to act twice in one clock cycle (70%). All animals have an additional chance of acting one more time (30%).
     */
    public void updateAnimals() {
        //Randomly permute list each time
        Animals = randomPermute(Animals);

        for (int i = 0; i < Animals.size(); i++) {
            Animal currentAnimal = Animals.get(i);

            // All animals that can move get one move
            currentAnimal.act();
            // Carnivores are likely to get a second move
            if (currentAnimal instanceof Carnivore) {
                if (currentAnimal.getAge() % 2 == 1 && (Math.abs(random.nextInt()) % 10 > 3)) {
                    currentAnimal.act();
                }
            }
            // All animals have the chance to have a bonus move (about 3/10)
            if (Math.abs(random.nextInt()) % 10 > 7) {
                currentAnimal.act();
            }
        }
    }

    /**
     * Iterates through the ArrayList of Plants and allows each Plant to perform an action.
     */
    public void updatePlants() {
        for (int i = 0; i < Plants.size(); i++){
            Plant currentPlant = Plants.get(i);
            currentPlant.act();
        }
    }

    /**
     * Updates this World. It increments its clock before having all of its Entities update themselves. It also
     * spawns new plants to provide food for the Animals that can eat them, thus keeping the World sustainable.
     */
    public void update() {
        clock++;
        System.out.println("Current clock: " + clock);
        updateAnimals();
        updatePlants();

        int numNewPlants = ((LEN_X * LEN_Y - Animals.size() - Plants.size()) / 50);
        // About 2% of empty space of new plants to keep simulation going

        for(int i = 0; i < numNewPlants; i++){
            spawnPlant();
        }
    }

    //Conditional Methods

    /**
     * Determines whether or not the given Location in this World is empty.
     * @param loc The given Location in which one wants to check if it is empty.
     * @return a boolean value of whether or not the given Location in this World is empty. True if it is empty.
     * False otherwise.
     */
    public boolean isEmpty(Location loc) {
        return isEmpty(loc.getX(), loc.getY());
    }

    /**
     * Determines whether or not the given coordinates in this World are empty.
     * @param x The x-coordinates of this World.
     * @param y The y-coordinates of this World.
     * @return a boolean value of whether or not the given coordinates of this World is empty. True if it is empty.
     * False otherwise.
     */
    public boolean isEmpty(int x, int y) {
        return Map[x][y] == null;
    }

    /**
     * Determines whether or not the given Location contains an Entity that is a Carnivore.
     * @param loc The given Location in which one wants to check if a Carnivore exists.
     * @return a boolean value of whether or not the given Location contains a Carnivore.
     */
    public boolean isCarnivore(Location loc) {
        return isCarnivore(loc.getX(), loc.getY());
    }

    /**
     * Determines whether or not the given coordinates contain an Entity that is a Carnivore.
     * @param x The x-coordinates of this World.
     * @param y The y-coordinates of this World.
     * @return a boolean value of whether or not the given coordinates of this World contains a Carnivore. True if
     * if todes. False otherwise.
     */
    public boolean isCarnivore(int x, int y) {
        return Map[x][y] instanceof Carnivore;
    }

    /**
     * Determines whether or not the given Location contains an Entity that is a Herbivore.
     * @param loc The given Location in which one wants to check if a Herbivore exists.
     * @return a boolean value of whether or not the given Location contains a Herbivore.
     */
    public boolean isHerbivore(Location loc) {
        return isHerbivore(loc.getX(), loc.getY());
    }

    /**
     * Determines whether or not the given coordinates contain an Entity that is a Herbivore.
     * @param x The x-coordinates of this World.
     * @param y The y-coordinates of this World.
     * @return a boolean value of whether or not the given coordinates of this World contains a Herbivore. True if
     * it does. False otherwise.
     */
    public boolean isHerbivore(int x, int y) {
        return Map[x][y] instanceof Herbivore;
    }

    /**
     * Determines whether or not the given Location contains an Entity that is a Plant.
     * @param loc The given Location in which one wants to check if a Plant exists.
     * @return a boolean value of whether or not the given Location contains a Plant.
     */
    public boolean isPlant(Location loc) {
        return isPlant(loc.getX(), loc.getY());
    }

    /**
     * Determines whether or not the given coordinates contain an Entity that is a Plant.
     * @param x The x-coordinate of this World.
     * @param y The y-coordinate of this World.
     * @return a boolean value of whether or not the given coordinates of this World contains a Plant. True if it
     * does. False otherwise.
     */
    public boolean isPlant(int x, int y) {
        return Map[x][y] instanceof Plant;
    }

    /**
     * Determines whether or not the given Location is valid in this world. The Location is valid if it is located
     * within the boundaries of this World.
     * @param loc The given Location in which one wants to check the validity of.
     * @return a boolean value of whether or not the given Location is valid. True if it is valid. False otherwise.
     */
    public boolean isValid(Location loc) {
        return (isValid(loc.getX(),loc.getY()));
    }

    /**
     * Determines whether or not the given coordinates are valid in this World. They are valid if they are
     * located within the boundaries of this World.
     * @param x The x-coordinate of this World.
     * @param y The y-coordinate of this World.
     * @return a boolean value of whether or not the given coordinates are a valid Location in this World. True if
     * they are valid. False otherwise.
     */
    public boolean isValid(int x, int y) {
        return (x >= 0 && x < LEN_X) && (y >= 0 && y < LEN_Y);
    }

    /**
     * Determines whether or not there are Animals in this World.
     * @return a boolean value of whether or not there exists Animals in this World. True if there are still Animals
     * in this World. False otherwise.
     */
    public boolean hasAnimals() {return Animals.size() > 0;}

    /**
     * Gets the character representation of an Entity that is located at a given Location in this World.
     * @param l The given Location where an Entity is located.
     * @return the character representation of the Entity at the given Location.
     */
    public char getLocationChar(Location l){
        return Map[l.getX()][l.getY()].getChar();
    }

    /**
     * Creates a String that represents what this World's Map looks like. It contains the character representations
     * of the Entities that are located at each location in the world. If there is no Entity in a location, a '.' is
     * printed instead.
     * @return the String representation of the Map of this World.
     */
    @Override
    public String toString() {
        String s = " ";
        for (int i = 0; i < LEN_X; i++){
            s += " " + Integer.toString(i % 10);
        }
        s += "\n";

        for (int i = 0; i < LEN_Y; i++) {
            s += Integer.toString(i % 10) + " ";
            for (int j = 0; j < LEN_X; j++) {
                if (Map[j][i] != null) {
                    s += Character.toString(Map[j][i].getChar()) + " ";
                }
                else {
                    s += ". ";
                }
            }
            s += "\n";
        }
        return s;
    }
}