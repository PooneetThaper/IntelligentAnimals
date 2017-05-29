import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Carnivore extends Animal {
    private Random rand = new Random();

    public Carnivore(World world, Location location) {
        super(world, location);
        foodChars = new char[]{'&','$'};
        dangerChars = new char[]{};
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

    @Override
    public char getChar(){
        return '@';
    }
}