import java.util.Random;

public class Omnivore extends Animal {
    private Random rand = new Random();

    public Omnivore(World world, Location location) {
        super(world, location);
        foodChars = new char[]{'&','*'};
        dangerChars = new char[]{'@'};
    }

    public Omnivore(Animal animal){
        super(animal.getWorld(),animal.getLocation());
        foodChars = new char[]{'&','*','@'};
        dangerChars = new char[]{'@'};
        energy = animal.getEnergy();
        age = animal.getAge();
    }

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
        o.cantMove = Math.abs(rand.nextInt() % 2) + 1;
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

    @Override
    public char getChar(){
        return '$';
    }
}
