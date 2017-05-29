import java.util.Random;
import java.util.Scanner;

public class Driver {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int xDim = 0;
        int yDim = 0;
        int numIterations = 0;
        
        System.out.print("Input desired x dimension for simulation: ");
        if (in.hasNextInt()) {
            xDim = in.nextInt();
        }

        System.out.print("Input desired y dimension for simulation: ");
        if (in.hasNextInt()) {
            yDim = in.nextInt();
        }

        System.out.print("Input desired number of iterations for simulation: ");
        if (in.hasNextInt()) {
            numIterations = in.nextInt();
        }
        
        in.close();

        // By default World shows all events verbosely
        // To prevent this, add a parameter false after the current parameters of constructor
        World w = new World(xDim, yDim, false);
        //World w = new World(xDim, yDim, false);
        System.out.println(w); // Blank map

        //Start off with about 2.9 percent as Carnivores, 20 percent as Herbivores, and 50 percent as plants
        System.out.print("Generating living creatures (2.9% carnivores, 20% herbivores, 50% plants, and 2.9% rocks of all spaces)\n");
        w.initialSpawn(xDim * yDim / 35,  xDim * yDim / 5, xDim * yDim / 5, xDim * yDim / 35);
        System.out.println(w); // Initial map (Clock = 0)

        // DOCUMENTATION:

        // World behavior:
        // World spawns given number of carnivores, herbivores, and plants at start
        // World updates all animals and plants each clock cycle
        // World holds all animals in an ArrayList called Animals and all plants in an ArrayList called Plants
        // World holds map as a 2-D array of Entities
        // ArrayList Animals is randomized each clock cycle to allow random update order
        // Breadth first search (BFS and exhaustiveBFS) is provided for animals to find entities of a certain type within a certain distance
        //          BFS (only returns first found) and exhaustiveBFS (returns all found)
        //          Use a generic queue with generic nodes on type coordinate
        // Conditional functions (ex isEmpty, isPlant, etc.) are provided for animals to ask about their surroundings

        // Animal behavior:
        // Animals that spawn at the start begin with age 0 or 1 randomly (to randomize carnivore double movement)
        // Animals will try to eat if their energy is less than 8 and gain 5 energy each time they eat
        // Animals will give birth if energy is greater than 9 and age > 4 and lose 3 energy each time they give birth
        // Animals will give birth on a spot next to themselves that they would have decided as the optimal next spot
        // Animals will not give birth immediately if they are not able to find a good spot
        //          (ie best move for current animal currently is to not move even with penalty)
        // Animals will die if their age is greater than 15 or if their energy has dropped below 0
        // Animals contain a reference to their world and call on it to know about their surroundings
        // Animals each do their own calculations using the BFS/exhaustive BFS and decide on a direction to move
        //          (0: don't move, 1: move up, 2: move right, 3: move down, 4: move left)

        // Carnivore behavior:
        // Carnivores can move twice if their age is an odd number
        // Carnivores see all herbivores within 7.0 units of themselves
        // Carnivores decide which direction to move by trying to
        //         minimize the average distance to all the herbivores seen as well as to the nearest herbivore
        // Carnivores do not consider the average distance to herbivores if the carnivore's energy >= 6

        // Herbivore behavior:
        // Herbivores see all carnivores and plants within 6.0 units of themselves
        // Herbivores decide which direction to move by trying to minimize the average distance to
        //          all the plants seen as well as the nearest plant and maximize the distance to all
        //          carnivores seen as well as the nearest carnivore
        // Herbivores do not consider the average distance to plants if the herbivore's energy >= 6
        // Herbivores always consider the average distance to carnivores

        // Plant behavior:
        // Plants will spawn at a random location every 2 clocks
        // Plants will die if their age is greater than 30


        // Loop will continue until all animals are dead or until iterations are up
        while (numIterations > 0 && w.hasAnimals()){
            w.update();
            System.out.println(w);
            numIterations--;
        }
    }
}