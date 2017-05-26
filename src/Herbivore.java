import java.util.ArrayList;

/**
 * Created by Pthaper on 3/30/17.
 */
public class Herbivore extends Animal {
    //Breath first search needed
    //Calculating avg dist from all things of some type in some range of current herbivore

    public Herbivore(World world, Coordinate location, int age){
        EncapsulatingWorld = world;
        Location = location;
        super.age = age;
        energyBoost();
    }

    public Herbivore(World world, Coordinate location){
        EncapsulatingWorld = world;
        Location = location;
        cantMove = ((Math.abs(EncapsulatingWorld.random.nextInt()) % 2) + 1);
        // Cant move for a random period of time after birth (either 1 or 2 clocks)
        energyBoost();
    }

    void energyBoost(){
        //Add a random bonus amount of energy (more possible for herbivore than carnivore)
        energy += (Math.abs(EncapsulatingWorld.random.nextInt()) % 7);
    }

    @Override
    public int weighOptions(){
        return weighOptions(false);
    }

    @Override
    public int weighOptions(boolean noStay){

        //Use full list of all carnivores and plants in a certain area
        ArrayList<Coordinate> nearestCarnivore = EncapsulatingWorld.exhaustiveBFS(Location, '@', MAX_SIGHT_DISTANCE - 1, true);
        ArrayList<Coordinate> nearestPlant = EncapsulatingWorld.exhaustiveBFS(Location, '*', MAX_SIGHT_DISTANCE, true);

        double[] possibleMoves = new double[5];

        // Consider no move
        if (nearestCarnivore.size()>0){
            possibleMoves[0] += Coordinate.distance(Location, nearestCarnivore.get(0));
            possibleMoves[0] += Coordinate.distance(Location, nearestCarnivore);
        }

        if (this.energy < super.MAX_ENERGY) {
            if (nearestPlant.size()>0){
                possibleMoves[0] -= Coordinate.distance(Location, nearestPlant.get(0));
                possibleMoves[0] -= Coordinate.distance(Location, nearestPlant);
            }

        }
        if (noStay) possibleMoves[0] -= 20;

        // Consider up
        Coordinate upLocation = new Coordinate(Location.x,Location.y-1);
        if (EncapsulatingWorld.isValid(upLocation)) {
            if (nearestCarnivore.size()>0){
                possibleMoves[1] += Coordinate.distance(upLocation, nearestCarnivore.get(0));
                possibleMoves[1] += Coordinate.distance(upLocation, nearestCarnivore);
            }

            if (this.energy < super.MAX_ENERGY) {
                if (nearestPlant.size()>0){
                    possibleMoves[1] -= Coordinate.distance(upLocation, nearestPlant.get(0));
                    possibleMoves[1] -= Coordinate.distance(upLocation, nearestPlant);
                }
            }
            if (!EncapsulatingWorld.isEmpty(upLocation)) {
                if (EncapsulatingWorld.isPlant(upLocation)){
                    if (this.energy < super.MAX_ENERGY) possibleMoves[1] += 15;
                }
                else possibleMoves[1] -= 15;
            }
        }else possibleMoves[1] -= 15;

        // Consider right
        Coordinate rightLocation = new Coordinate(Location.x+1,Location.y);
        if (EncapsulatingWorld.isValid(rightLocation)) {
            if (nearestCarnivore.size()>0){
                possibleMoves[2] += Coordinate.distance(rightLocation, nearestCarnivore.get(0));
                possibleMoves[2] += Coordinate.distance(rightLocation, nearestCarnivore);
            }

            if (this.energy < super.MAX_ENERGY) {
                if (nearestPlant.size()>0){
                    possibleMoves[2] -= Coordinate.distance(rightLocation, nearestPlant.get(0));
                    possibleMoves[2] -= Coordinate.distance(rightLocation, nearestPlant);
                }

            }
            if (!EncapsulatingWorld.isEmpty(rightLocation)) {
                if (EncapsulatingWorld.isPlant(rightLocation)){
                    if (this.energy < super.MAX_ENERGY) possibleMoves[2] += 15;
                }
                else possibleMoves[2] -= 15;
            }
        }else possibleMoves[2] -= 15;

        // Consider down
        Coordinate downLocation = new Coordinate(Location.x,Location.y+1);
        if (EncapsulatingWorld.isValid(downLocation)) {
            if (nearestCarnivore.size()>0){
                possibleMoves[3] += Coordinate.distance(downLocation, nearestCarnivore.get(0));
                possibleMoves[3] += Coordinate.distance(downLocation, nearestCarnivore);
            }

            if (this.energy < super.MAX_ENERGY) {
                if (nearestPlant.size()>0){
                    possibleMoves[3] -= Coordinate.distance(downLocation, nearestPlant.get(0));
                    possibleMoves[3] -= Coordinate.distance(downLocation, nearestPlant);
                }
            }
            if (!EncapsulatingWorld.isEmpty(downLocation)) {
                if (EncapsulatingWorld.isPlant(downLocation)){
                    if (this.energy < super.MAX_ENERGY) possibleMoves[3] += 15;
                }
                else possibleMoves[3] -= 15;
            }
        }else possibleMoves[3] -= 15;

        // Consider left
        Coordinate leftLocation = new Coordinate(Location.x-1,Location.y);
        if (EncapsulatingWorld.isValid(leftLocation)) {
            if (nearestCarnivore.size()>0){
                possibleMoves[4] += Coordinate.distance(leftLocation, nearestCarnivore.get(0));
                possibleMoves[4] += Coordinate.distance(leftLocation, nearestCarnivore);
            }

            if (this.energy < super.MAX_ENERGY) {
                if (nearestPlant.size()>0){
                    possibleMoves[4] -= Coordinate.distance(leftLocation, nearestPlant.get(0));
                    possibleMoves[4] -= Coordinate.distance(leftLocation, nearestPlant);
                }
            }
            if (!EncapsulatingWorld.isEmpty(leftLocation)) {
                if (EncapsulatingWorld.isPlant(leftLocation)){
                    if (this.energy < super.MAX_ENERGY) possibleMoves[4] += 15;
                }
                else possibleMoves[4] -= 15;
            }
        } else possibleMoves[4] -= 15;

        /*
        System.out.print(this);
        System.out.print(Location);
        System.out.printf(" [%f,%f,%f,%f,%f]%n",possibleMoves[0],possibleMoves[1],possibleMoves[2],possibleMoves[3],possibleMoves[4]);
        */

        int maxIndex = 0;
        for (int i = 1 ; i < 5; i++){
            if (possibleMoves[i] > possibleMoves[maxIndex]) maxIndex = i;
        }

        return maxIndex;
    }

    @Override
    public void giveBirth(){
        //Choose the best spot to give birth to new baby
        Coordinate birthPlace = super.getBestBirthPlace(this.weighOptions(true));
        //Not in the best position to give birth;
        if (birthPlace == null) {
            if (EncapsulatingWorld.showAll) {
                System.out.print(this);
                System.out.println("\t tried to gve birth but did not find optimal spot");
            }
            return;
        }
        EncapsulatingWorld.addHerbivore(birthPlace);
        this.energy -= START_ENERGY;
        if (EncapsulatingWorld.showAll) {
            System.out.print(this);
            System.out.print("\tgave birth at " + birthPlace);
            System.out.println(String.format("\tNew Energy: %d", energy));
        }
    }


    @Override
    public char getChar(){
        return '&';
    }
}
