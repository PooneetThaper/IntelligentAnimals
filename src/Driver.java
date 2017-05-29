import java.util.Random;
import java.util.Scanner;

/**
 * The main class that drives the program.
 */
public class Driver {
    /**
     * Prompts for world dimensions and iterations. Initializes world and starts simulation.
     * @param args Command line arguments
     */
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
        // World w = new World(xDim, yDim, true);
        System.out.println(w); // Blank map

        //Start off with about 2.9 percent as Carnivores, 20 percent as Herbivores, and 50 percent as plants
        System.out.print("Generating living creatures (2.9% carnivores, 20% herbivores, 50% plants, 2.9% rocks, and 1.5% trees of all spaces)\n");
        w.initialSpawn(xDim * yDim / 35,  xDim * yDim / 5, xDim * yDim / 5, xDim * yDim / 35, xDim * yDim / 67);
        System.out.println(w); // Initial map (Clock = 0)

        // Loop will continue until all animals are dead or until iterations are up
        while (numIterations > 0 && w.hasAnimals()){
            w.update();
            System.out.println(w);
            numIterations--;
        }
    }
}