import java.util.ArrayList;
import java.util.Random;

public class World {
    private int clock = 0;
    private int lenX, lenY;
    private Entity[][] Map;
    private ArrayList<Animal> Animals = new ArrayList<>();
    private ArrayList<Plant> Plants = new ArrayList<>();
    private Random random;
    private boolean showAll = true;

    public World(int lenX, int lenY) {
        this.lenX = lenX;
        this.lenY = lenY;
        Map = new Entity[lenX][lenY];
        random = new Random();
    }

    public World(int lenX, int lenY, boolean showAll) {
        this.lenX = lenX;
        this.lenY = lenY;
        Map = new Entity[lenX][lenY];
        random = new Random();
        this.showAll = showAll;
    }

    public int getClock() {return clock;}
    public int getXSize() {return lenX;}
    public int getYSize() {return lenY;}
    public Entity[][] getMap() {return Map;}
    public ArrayList<Animal> getAnimals() {return Animals;}
    public ArrayList<Plant> getPlants() {return Plants;}
    public boolean getShowAll() {return showAll;}

    public ArrayList<Animal> randomPermute(ArrayList<Animal> A){
        ArrayList<Animal> retval = new ArrayList<>();
        while(A.size() > 0){
            int index = ((Math.abs(random.nextInt())) % A.size());
            retval.add(A.get(index));
            A.remove(index);
        }
        return retval;
    }

    // Adding living things methods

    public void initialSpawn(int numCarnivores, int numHerbivores, int numPlants) {
        while (numCarnivores > 0) {
            int x = Math.abs(random.nextInt()) % lenX;
            int y = Math.abs(random.nextInt()) % lenY;
            Location loc = new Location(x, y);
            if (isEmpty(loc)) {
                Carnivore c = new Carnivore(this, loc);
                numCarnivores--;
            }
        }
        while (numHerbivores > 0) {
            int x = Math.abs(random.nextInt()) % lenX;
            int y = Math.abs(random.nextInt()) % lenY;
            Location loc = new Location(x, y);
            if (isEmpty(loc)) {
                Herbivore h = new Herbivore(this, loc);
                numHerbivores--;
            }
        }
        while (numPlants > 0) {
            int x = Math.abs(random.nextInt()) % lenX;
            int y = Math.abs(random.nextInt()) % lenY;
            Location loc = new Location(x, y);
            if (isEmpty(loc)) {
                Plant p = new Plant(this, loc);
                numPlants--;
            }
        }
    }

    // places the Entity e at the given Location loc
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

    // public Carnivore addCarnivore(Location location) {
    //     Animal c = new Carnivore(this, location);
    //     Map[location.getX()][location.getY()] = c;
    //     Animals.add(c);
    //     return (Carnivore) c;
    // }

    // public Herbivore addHerbivore(Location location) {
    //     Animal h = new Herbivore(this, location);
    //     Map[location.getX()][location.getY()] = h;
    //     Animals.add(h);
    //     return (Herbivore) h;
    // }

    // public Plant addPlant(Location location) {
    //     Plant p = new Plant(this, location);
    //     Map[location.getX()][location.getY()] = p;
    //     Plants.add(p);
    //     return p;
    // }

    public void spawnPlant(){
        int x = Math.abs(random.nextInt()) % lenX;
        int y = Math.abs(random.nextInt()) % lenY;
        Location loc = new Location(x, y);
        if (isEmpty(loc)) {
            Plant added = new Plant(this, loc);
            if (showAll){
                System.out.print(added);
                System.out.print("\tspawned at ");
                System.out.println(loc);
            }
        }
    }

    // Pathfinding Methods

    public ArrayList<Location> exhaustiveBFS(Location start, char target, double maxDist, boolean exhaustive) {
        ArrayList<Location> found = new ArrayList<>();
        Queue<Location> seen = new Queue<>();
        Queue<Location> queue = new Queue<>();
        queue.enqueue(start);
        while (!queue.isEmpty()) {
            Location current = queue.dequeue();
            int currentX = current.getX();
            int currentY = current.getY();

            if (Map[currentX][currentY] != null) {
                if (Map[currentX][currentY].getChar() == target) {
                    found.add(current);
                    if (!exhaustive) {
                        break;
                    }
                }
            }

            Location upLocation = new Location(currentX, currentY - 1);
            if (validLocation(upLocation, start, maxDist, ' ') && !queue.contains(upLocation) && !seen.contains(upLocation)) {
                queue.enqueue(upLocation);
            }

            Location rightLocation = new Location(currentX + 1, currentY);
            if (validLocation(rightLocation, start, maxDist, ' ') && !queue.contains(rightLocation) && !seen.contains(rightLocation)) {
                queue.enqueue(rightLocation);
            }

            Location downLocation = new Location(currentX, currentY + 1);
            if (validLocation(downLocation, start, maxDist, ' ') && !queue.contains(downLocation) && !seen.contains(downLocation)) {
                queue.enqueue(downLocation);
            }

            Location leftLocation = new Location(currentX - 1, currentY);
            if (validLocation(leftLocation, start, maxDist, ' ') && !queue.contains(leftLocation) && !seen.contains(leftLocation)) {
                queue.enqueue(leftLocation);
            }

            seen.enqueue(current);
        }
        return found;
    }

    private boolean validLocation(Location next, Location start, double maxDistance, char forbidden) {
        int nextX = next.getX();
        int nextY = next.getY();
        return validLocation(nextX, nextY, start, maxDistance, forbidden);
    }

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

    public void updateAnimals() {
        //Randomly permute list each time
        Animals = randomPermute(Animals);

        for (int i = 0; i < Animals.size(); i++) {
            Animal currentAnimal = Animals.get(i);
            currentAnimal.act();
            if (!currentAnimal.canMove()){
                //Can this animal move yet after being born
                if(showAll){
                    System.out.print(currentAnimal);
                    System.out.print("\tcant move yet. ");
                    System.out.println(String.format("\tAge: %d, Energy: %d",currentAnimal.getAge(), currentAnimal.getEnergy()));
                }
                continue;
            }

            // All animals that can move get one move
            currentAnimal.act();
            // Carnivores are likely to get a second move
            if (currentAnimal instanceof Carnivore) {
                if (currentAnimal.getAge() % 2 == 1 && (Math.abs(random.nextInt()) % 10 > 3)) {currentAnimal.act();
                }
            }
            // All animals have the chance to have a bonus move (about 3/10)
            if (Math.abs(random.nextInt()) % 10 > 6) {
                currentAnimal.act();
            }
        }

    }

    public void updatePlants() {
        for (int i = 0; i < Plants.size(); i++){
            Plant currentPlant = Plants.get(i);
            currentPlant.act();
        }
    }

    public void update() {
        clock++;
        System.out.println("Current clock: " + clock);
        updateAnimals();
        updatePlants();

        int numNewPlants = ((lenX * lenY - Animals.size() - Plants.size()) / 50);
        // About 2% of empty space of new plants to keep simulation going

        for(int i = 0; i < numNewPlants; i++){
            spawnPlant();
        }
    }

    //Conditional Methods

    public boolean isEmpty(Location loc) {
        return isEmpty(loc.getX(), loc.getY());
    }

    public boolean isEmpty(int x, int y) {
        return Map[x][y] == null;
    }

    public boolean isCarnivore(Location loc) {
        return isCarnivore(loc.getX(), loc.getY());
    }

    public boolean isCarnivore(int x, int y) {
        return Map[x][y] instanceof Carnivore;
    }

    public boolean isHerbivore(Location loc) {
        return isHerbivore(loc.getX(), loc.getY());
    }

    public boolean isHerbivore(int x, int y) {
        return Map[x][y] instanceof Herbivore;
    }

    public boolean isPlant(Location loc) {
        return isPlant(loc.getX(), loc.getY());
    }

    public boolean isPlant(int x, int y) {
        return Map[x][y] instanceof Plant;
    }

    public boolean isValid(Location loc) {
        return (isValid(loc.getX(),loc.getY()));
    }

    public boolean isValid(int x, int y) {
        return (x >= 0 && x < lenX) && (y >= 0 && y < lenY);
    }

    public boolean hasAnimals() {return Animals.size() > 0;}

    @Override
    public String toString() {
        String s = " ";
        for (int i = 0; i < lenX; i++){
            s += " " + Integer.toString(i % 10);
        }
        s += "\n";

        for (int i = 0; i < lenY; i++) {
            s += Integer.toString(i % 10) + " ";
            for (int j = 0; j < lenX; j++) {
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