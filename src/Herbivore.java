import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Represents a Herbivore that is a subclass of Animal.
 */
public class Herbivore extends Animal {
    /**
     * An instance of Random which is used to randomize the actions of this Animal.
     */
    private Random rand = new Random();

    /**
     * Creates a new Herbivore that exists in a given World and is initially located at a given Location.
     * Herbivores eat Plants and avoid both Carnivores and Omnivores. Its other fields are initialized using its
     * superclass's constructor.
     * @param world The World in which this Herbivore exists.
     * @param location The initial Location where this Herbivore is located.
     */
    public Herbivore(World world, Location location) {
        super(world, location);
        foodChars = new char[]{'*'};
        dangerChars = new char[]{'@','$'};
    }

    /**
     * Causes this Herbivore to try to give birth to another Herbivore if the conditions are met. This Herbivore
     * must find the best location for it to give birth in order to do so. Its offspring would then be born in that
     * location with a certain amount of initial energy, which would be taken from this parent Herbivore. The baby
     * Herbivore would not be allowed to move until it reaches a random age between 3 and 5. In addition, this
     * parent Herbivore cannot give birth for another 2 to 3 moves.
     * @return a boolean value of whether or not this Herbivore successfully gave birth. True if it did give birth.
     * False otherwise.
     */
    @Override
    public boolean giveBirth() {
        //Choose the best spot to give birth to new baby
        Location birthPlace = super.getBestBirthPlace(this.weighOptions(true));
        //Not in the best position to give birth;
        if (birthPlace == null) {
            if (world.getShowAll()) {
                System.out.printf("%s\ttried to gve birth but did not find optimal spot%n", this);
            }
            return false;
        }
        Herbivore h = new Herbivore(world, birthPlace);
        h.cantMove = Math.abs(rand.nextInt() % 3) + 3;
        energy -= h.energy;
        reproduceAge += rand.nextInt(2) + 2;
        
        if (world.getShowAll()) {
            System.out.printf("%s\tgave birth at %s\tNew Energy: %d%n",
                this, birthPlace, energy);
        }
        return true;
    }

    /**
     * Gets the Herbivore's character representation.
     * @return this Herbivore's character representation, '&'
     */
    @Override
    public char getChar(){
        return '&';
    }
}
