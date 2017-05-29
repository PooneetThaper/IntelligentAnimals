import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Represents a Carnivore that is a subclass of Animal.
 */
public class Carnivore extends Animal {
    /**
     * An instance of Random which is used to randomize the actions of this Animal.
     */
    private Random rand = new Random();

    /**
     * Creates a new Carnivore that exists in a given World and is initially located at a given Location.
     * Carnivores can eat Herbivores and Omnivores. Its other fields are initialized using its superclass's
     * constructor.
     * @param world The World in which this Carnivore exists.
     * @param location The initial Location where this Carnivore is located.
     */
    public Carnivore(World world, Location location) {
        super(world, location);
        foodChars = new char[]{'&','$'};
        dangerChars = new char[]{};
    }

    /**
     * Causes this Carnivore to try to give birth to another Carnivore if the conditions are met. This Carnivore
     * must find the best location for it to give birth in order to do so. Its offspring would then be born in that
     * location with a certain amount of initial energy, which would be taken from this parent Carnivore. The baby
     * Carnivore would not be allowed to move until it reaches a random age between 3 and 5. In addition, this
     * parent Carnivore cannot give birth for another 3 to 5 moves.
     * @return a boolean value of whether or not this Carnivore successfully gave birth. True if it did give birth.
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
        Carnivore c = new Carnivore(world, birthPlace);
        c.cantMove = Math.abs(rand.nextInt() % 3) + 3;
        energy -= c.energy;
        // increase age when carnivores can reproduce so they won't
        // multiply too quickly after every turn
        reproduceAge += rand.nextInt(3) + 3;
        
        if (world.getShowAll()) {
            System.out.printf("%s\tgave birth at %s\tNew Energy: %d%n",
                this, birthPlace, energy);
        }
        return true;
    }

    /**
     * Gets the Carnivore's character representation.
     * @return this Carnivore's character representation, '@'
     */
    @Override
    public char getChar(){
        return '@';
    }
}