import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Pthaper on 3/30/17.
 */
public class World {
    private int clock = 0;
    private int lenX, lenY;
    private Entity[][] Map;
    private ArrayList<Animal> Animals = new ArrayList<>();
    private ArrayList<Plant> Plants = new ArrayList<>();
    public Random random;
    public boolean showAll = true;

    public World(int lenX, int lenY, boolean showAll){
        this.showAll = showAll;
        this.lenX = lenX;
        this.lenY = lenY;
        Map = new Entity[lenX][lenY];
        random = new Random();

    }

    public World(int lenX, int lenY) {
        this.lenX = lenX;
        this.lenY = lenY;
        Map = new Entity[lenX][lenY];
        random = new Random();
    }

    public int getXSize() {
        return lenX;
    }

    public int getYSize() {
        return lenY;
    }

    public void printMap() {
        System.out.println();
        boolean showRowColNums = true;
        if (showRowColNums) {
            System.out.print("  ");
            for (int i = 0; i < lenX; i++){
                System.out.printf("%d ",i%10);
            }
            System.out.print("\n");
        }
        for (int i = 0; i < lenY; i++) {
            if (showRowColNums) System.out.printf("%d ",i%10);
            for (int j = 0; j < lenX; j++) {
                if (Map[j][i] != null) {

                    System.out.print(Map[j][i].getChar());
                } else {
                    System.out.print(".");
                }
                System.out.print(" ");
            }
            System.out.println("");
        }
        System.out.println("");
    }

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
            Coordinate next = new Coordinate((Math.abs(random.nextInt()) % lenX), (Math.abs(random.nextInt()) % lenY));
            if (isEmpty(next)) {
                addCarnivore(next, Math.abs((random.nextInt()) % 3));
                numCarnivores--;
            }
        }

        while (numHerbivores > 0) {
            Coordinate next = new Coordinate(Math.abs((random.nextInt()) % lenX), (Math.abs(random.nextInt()) % lenY));
            if (isEmpty(next)) {
                addHerbivore(next, Math.abs((random.nextInt()) % 3));
                numHerbivores--;
            }
        }

        while (numPlants > 0) {
            Coordinate next = new Coordinate(Math.abs((random.nextInt()) % lenX), (Math.abs(random.nextInt()) % lenY));
            if (isEmpty(next)) {
                addPlant(next, Math.abs((random.nextInt()) % 7));
                numPlants--;
            }
        }

    }

    public Carnivore addCarnivore(Coordinate location) {
        Animal c = new Carnivore(this, location);
        Map[location.x][location.y] = c;
        Animals.add(c);
        return (Carnivore) c;
    }

    public Carnivore addCarnivore(Coordinate location, int age) {
        Animal c = new Carnivore(this, location, age);
        Map[location.x][location.y] = c;
        Animals.add(c);
        return (Carnivore) c;
    }

    public Herbivore addHerbivore(Coordinate location) {
        Animal h = new Herbivore(this, location);
        Map[location.x][location.y] = h;
        Animals.add(h);
        return (Herbivore) h;
    }

    public Herbivore addHerbivore(Coordinate location, int age) {
        Animal h = new Herbivore(this, location, age);
        Map[location.x][location.y] = h;
        Animals.add(h);
        return (Herbivore) h;
    }

    public Plant addPlant(Coordinate location) {
        Plant p = new Plant(this, location);
        Map[location.x][location.y] = p;
        Plants.add(p);
        return p;
    }

    public Plant addPlant(Coordinate location, int age) {
        Plant p = new Plant(this, location, age);
        Map[location.x][location.y] = p;
        Plants.add(p);
        return p;
    }

    public void spawnPlant(){
        Coordinate next = new Coordinate(Math.abs((random.nextInt()) % lenX), (Math.abs(random.nextInt()) % lenY));
        if (isEmpty(next)) {
            Plant added = addPlant(next);
            if (showAll){
                System.out.print(added);
                System.out.print("\tspawed at ");
                System.out.println(next);
            }
        }
    }

    // Pathfinding Methods

    public ArrayList<Coordinate> exhaustiveBFS(Coordinate start, char target, double maxDist, boolean exhaustive) {
        ArrayList<Coordinate> found = new ArrayList<>();
        Queue<Coordinate> seen = new Queue<>();
        Queue<Coordinate> queue = new Queue<>();
        queue.enqueue(start);
        while (!queue.isEmpty()) {
            Coordinate current = queue.dequeue();

            if (Map[current.x][current.y] != null) {

                if (Map[current.x][current.y].getChar() == target) {
                    found.add(current);
                    if (!exhaustive) break;
                }

            }

            Coordinate upLocation = new Coordinate(current.x, current.y - 1);
            if (validCoordinate(upLocation, start, maxDist, ' ') && !queue.contains(upLocation) && !seen.contains(upLocation)) {
                queue.enqueue(upLocation);
            }

            Coordinate rightLocation = new Coordinate(current.x + 1, current.y);
            if (validCoordinate(rightLocation, start, maxDist, ' ') && !queue.contains(rightLocation) && !seen.contains(rightLocation)) {
                queue.enqueue(rightLocation);
            }

            Coordinate downLocation = new Coordinate(current.x, current.y + 1);
            if (validCoordinate(downLocation, start, maxDist, ' ') && !queue.contains(downLocation) && !seen.contains(downLocation)) {
                queue.enqueue(downLocation);
            }

            Coordinate leftLocation = new Coordinate(current.x - 1, current.y);
            if (validCoordinate(leftLocation, start, maxDist, ' ') && !queue.contains(leftLocation) && !seen.contains(leftLocation)) {
                queue.enqueue(leftLocation);
            }

            seen.enqueue(current);
        }
        return found;
    }

    private boolean validCoordinate(Coordinate next, Coordinate start, double maxDistance, char forbidden){
        return validCoordinate(next.x, next.y, start, maxDistance, forbidden);
    }

    private boolean validCoordinate(int x, int y, Coordinate start, double maxDistance, char forbidden) {
        boolean notBlocked = true;
        if (Map[start.x][start.y] != null) {
            if (Map[start.x][start.y].getChar() == forbidden) notBlocked = false;
        }
        return (isValid(x,y) && (Coordinate.distance(x, y, start) <= maxDistance) && notBlocked);
    }

    // Update methods

    public void updateAnimal(Animal currentAnimal){
        Coordinate start = new Coordinate(currentAnimal.getLocation());
        if (currentAnimal.getAge() > 30 || currentAnimal.getEnergy() < 0) {
            if (showAll) {
                System.out.print(currentAnimal);
                System.out.print("\tdied at ");
                System.out.print(start);
                System.out.println(String.format("\tAge: %d, Energy: %d",currentAnimal.getAge(),currentAnimal.getEnergy()));
            }
            Map[start.x][start.y] = null;
            Animals.remove(currentAnimal);
            return;
        }
        currentAnimal.move(currentAnimal.weighOptions());
        Coordinate end = currentAnimal.getLocation();
        if (showAll) {
            System.out.print(currentAnimal);
            System.out.println("\t" + start + " -> " + end);
        }
        if (Map[end.x][end.y]!= null && Map[end.x][end.y] != currentAnimal) {
            Entity eaten = Map[end.x][end.y];
            if ( (eaten instanceof Herbivore && currentAnimal instanceof Carnivore)
                    || (eaten instanceof Plant && currentAnimal instanceof Herbivore)) {
                if (eaten instanceof Herbivore) Animals.remove(eaten);
                else if (eaten instanceof Plant) Plants.remove(eaten);

                if (showAll) {
                    System.out.print(currentAnimal);
                    System.out.print("\tate ");
                    System.out.print(eaten);
                    System.out.print(" at ");
                    System.out.print(end);
                    System.out.println(String.format("\tNew Energy: %d",currentAnimal.getEnergy() + 4));
                }

                Map[end.x][end.y] = null;
                currentAnimal.ateFood();
            }
        }
        Map[end.x][end.y] = currentAnimal;

        if (start.x != end.x || start.y != end.y) Map[start.x][start.y] = null;
    }

    public void updateAnimals() {
        //Randomly permute list each time
        Animals = randomPermute(Animals);

        for (int i = 0; i < Animals.size(); i++) {
            Animal currentAnimal = Animals.get(i);
            currentAnimal.update();
            if (!currentAnimal.canMove()){
                //Can this animal move yet after being born
                if(showAll){
                    System.out.print(currentAnimal);
                    System.out.print("\tcant move yet. ");
                    System.out.println(String.format("\tAge: %d, Energy: %d",currentAnimal.getAge(),currentAnimal.getEnergy()));
                }
                continue;
            }

            // All animals that can move get one move
            updateAnimal(currentAnimal);
            // Carnivores are likely to get a second move
            if (currentAnimal instanceof Carnivore) {
                if (currentAnimal.getAge() % 2 == 1 && (Math.abs(random.nextInt()) % 10 > 3)) updateAnimal(currentAnimal);
            }
            // All animals have the chance to have a bonus move (about 3/10)
            if (Math.abs(random.nextInt()) % 10 > 6) updateAnimal(currentAnimal);
        }

    }

    public void updatePlants(){
        for (int i = 0 ; i < Plants.size(); i++){
            Plant currentPlant = Plants.get(i);
            currentPlant.update();
            if (currentPlant.getAge() > 50) {
                Coordinate currentLocation = currentPlant.getLocation();
                if (showAll) {
                    System.out.print(currentPlant);
                    System.out.print("\tdied at ");
                    System.out.print(currentLocation);
                    System.out.println(String.format("\tAge: %d",currentPlant.getAge()));
                }
                Map[currentLocation.x][currentLocation.y] = null;
                Plants.remove(currentPlant);
            }
        }
    }

    public void update(){
        clock++;
        System.out.println("Current clock: " + clock);
        updateAnimals();
        updatePlants();

        int numNewPlants = ((lenX * lenY - Animals.size() - Plants.size()) / 50);
        // About 2 % of empty space of new plants to keep simulation going

        for(int i = 0; i < numNewPlants; i++){
            spawnPlant();
        }


    }

    //Conditional Methods

    public boolean isEmpty(Coordinate c) {
        return isEmpty(c.x, c.y);
    }

    public boolean isEmpty(int x, int y) {
        if (Map[x][y] == null) return true;
        return false;
    }

    public boolean isCarnivore(Coordinate c) {
        return isCarnivore(c.x, c.y);
    }

    public boolean isCarnivore(int x, int y) {
        if (Map[x][y] instanceof Carnivore) return true;
        else return false;
    }

    public boolean isHerbivore(Coordinate c) {
        return isHerbivore(c.x, c.y);
    }

    public boolean isHerbivore(int x, int y) {
        if (Map[x][y] instanceof Herbivore) return true;
        else return false;
    }

    public boolean isPlant(Coordinate c) {
        return isPlant(c.x, c.y);
    }

    public boolean isPlant(int x, int y) {
        if (Map[x][y] instanceof Plant) return true;
        else return false;
    }

    public boolean isValid(Coordinate c) {
        return (isValid(c.x,c.y));
    }

    public boolean isValid(int x, int y) {
        return ((x >= 0 && x < lenX) && (y >= 0 && y < lenY));
    }

    public boolean hasAnimals(){ return (Animals.size() > 0);}
}