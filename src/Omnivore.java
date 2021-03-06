import java.util.Random;

/**
 * Represents an Omnivore that is a subclass of Animal.
 */
public class Omnivore extends Animal {
    /**
     * An instance of Random which is used to randomize the actions of this Animal.
     */
    private Random rand = new Random();

    /**
     * Creates a new Omnivore that exists in a given World and is initially located at a given Location.
     * Omnivores eat Herbivores and Plants. They avoid Carnivores. Its other fields are initialized using its
     * superclass's constructor.
     * @param world The World in which this Herbivore exists.
     * @param location The initial Location where this Herbivore is located.
     */
    public Omnivore(World world, Location location) {
        super(world, location);
        foodChars = new char[]{'&','*'};
        dangerChars = new char[]{'@'};
    }

    /**
     * Creates a new Omnivore that replaces the Animal that became desperate.
     * Omnivores eat Herbivores and Plants. They also avoid Carnivores. This Omnivore's energy and age are
     * initialized so that they are the same as the Animal that it replaced.
     * @param animal The Animal that became desperate enough to eat Entities that it previously was not able to eat.
     */
    public Omnivore(Animal animal){
        super(animal.getWorld(),animal.getLocation());
        foodChars = new char[]{'&','*'};
        dangerChars = new char[]{'@'};
        energy = animal.getEnergy();
        age = animal.getAge();
    }

    /**
     * Causes this Omnivore to try to give birth to another Omnivore if the conditions are met. This Omnivore
     * must find the best location for it to give birth in order to do so. Its offspring would then be born in that
     * location with a certain amount of initial energy, which would be taken from this parent Omnivore. The baby
     * Omnivore would not be allowed to move until it reaches a random age between 3 and 5. In addition, this
     * parent Omnivore cannot give birth for another 1 to 2 moves.
     * @return a boolean value of whether or not this Omnivore successfully gave birth. True if it did give birth.
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
        Omnivore o = new Omnivore(world, birthPlace);
        o.cantMove = Math.abs(rand.nextInt() % 3) + 3;
        energy -= o.energy;
        // increase age when carnivores can reproduce so they won't
        // multiply too quickly after every turn
        reproduceAge += rand.nextInt(2) + 1;

        if (world.getShowAll()) {
            System.out.printf("%s\tgave birth at %s\tNew Energy: %d%n",
                this, birthPlace, energy);
        }
        return true;
    }

    /**
     * Gets the Omnivore's character representation.
     * @return this Omnivore's character representation, '$'
     */
    @Override
    public char getChar(){
        return '$';
    }
}
