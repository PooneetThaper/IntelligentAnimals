import java.util.ArrayList;

/**
 * Created by Pthaper on 3/30/17.
 */
public class Carnivore extends Animal {

    public Carnivore(World world, Coordinate location, int age){
        EncapsulatingWorld = world;
        Location = location;
        super.age = age;
        energyBoost();
    }

    public Carnivore(World world, Coordinate location){
        EncapsulatingWorld = world;
        Location = location;
        cantMove = ((Math.abs(EncapsulatingWorld.random.nextInt()) % 2) + 1);
        // Cant move for a random period of time after birth (either 1 or 2 clocks)
        energyBoost();
    }

    void energyBoost(){
        //Add a random bonus amount of energy (more possible for herbivore than carnivore)
        energy += (Math.abs(EncapsulatingWorld.random.nextInt()) % 4);
    }

    @Override
    public int weighOptions(){
        return weighOptions(false);
    }

    @Override
    public int weighOptions(boolean noStay){

        //Use full list of all herbivores nearby
        ArrayList<Coordinate> nearestHerbivore = EncapsulatingWorld.exhaustiveBFS(Location,'&',MAX_SIGHT_DISTANCE, true);
        //System.out.println(nearestHerbivore);

        //System.out.println("Current Location: " + Location);
        //System.out.println("Nearest Herbivore: " + nearestHerbivore.get(0));

        double[] possibleMoves = new double[5];

        // Consider no move
        if (this.energy < super.MAX_ENERGY) {
            if (nearestHerbivore.size()>0){
                possibleMoves[0] -= Coordinate.distance(Location, nearestHerbivore.get(0));
                possibleMoves[0] -= Coordinate.distance(Location, nearestHerbivore);
            }

        }
        if (noStay) possibleMoves[0] -= 20;

        // Consider up
        Coordinate upLocation = new Coordinate(Location.x,Location.y-1);
        if (EncapsulatingWorld.isValid(upLocation)) {
            if (this.energy < super.MAX_ENERGY) {
                if (nearestHerbivore.size()>0){
                    possibleMoves[1] -= Coordinate.distance(upLocation, nearestHerbivore.get(0));
                    possibleMoves[1] -= Coordinate.distance(upLocation, nearestHerbivore);
                }

            }
            if (!EncapsulatingWorld.isEmpty(upLocation)) {
                if (EncapsulatingWorld.isHerbivore(upLocation)){
                    if (this.energy < super.MAX_ENERGY) possibleMoves[1] += 15;
                }
                else possibleMoves[1] -= 15;
            }
        }else possibleMoves[1] -= 15;

        // Consider right
        Coordinate rightLocation = new Coordinate(Location.x+1,Location.y);
        if (EncapsulatingWorld.isValid(rightLocation)) {
            if (this.energy < super.MAX_ENERGY) {
                if (nearestHerbivore.size()>0){
                    possibleMoves[2] -= Coordinate.distance(rightLocation, nearestHerbivore.get(0));
                    possibleMoves[2] -= Coordinate.distance(rightLocation, nearestHerbivore);
                }

            }
            if (!EncapsulatingWorld.isEmpty(rightLocation)) {
                if (EncapsulatingWorld.isHerbivore(rightLocation)){
                    if (this.energy < super.MAX_ENERGY) possibleMoves[2] += 15;
                }
                else possibleMoves[2] -= 15;
            }
        }else possibleMoves[2] -= 15;

        // Consider down
        Coordinate downLocation = new Coordinate(Location.x,Location.y+1);
        if (EncapsulatingWorld.isValid(downLocation)) {
            if (this.energy < super.MAX_ENERGY) {
                if (nearestHerbivore.size()>0){
                    possibleMoves[3] -= Coordinate.distance(downLocation, nearestHerbivore.get(0));
                    possibleMoves[3] -= Coordinate.distance(downLocation, nearestHerbivore);
                }

            }
            if (!EncapsulatingWorld.isEmpty(downLocation)) {
                if (EncapsulatingWorld.isHerbivore(downLocation)){
                    if (this.energy < super.MAX_ENERGY) possibleMoves[3] += 15;
                }
                else possibleMoves[3] -= 15;
            }
        }else possibleMoves[3] -= 15;

        // Consider left
        Coordinate leftLocation = new Coordinate(Location.x - 1 ,Location.y);
        if (EncapsulatingWorld.isValid(leftLocation)) {
            if (this.energy < super.MAX_ENERGY) {
                if (nearestHerbivore.size()>0){
                    possibleMoves[4] -= Coordinate.distance(leftLocation, nearestHerbivore.get(0));
                    possibleMoves[4] -= Coordinate.distance(leftLocation, nearestHerbivore);
                }

            }
            if (!EncapsulatingWorld.isEmpty(leftLocation)) {
                if (EncapsulatingWorld.isHerbivore(leftLocation)){
                    if (this.energy < super.MAX_ENERGY) possibleMoves[4] += 15;
                }
                else possibleMoves[4] -= 15;
            }
        }else possibleMoves[4] -= 15;

        /*
        System.out.print(this);
        System.out.print(Location);
        System.out.printf("[%f,%f,%f,%f,%f]%n",possibleMoves[0],possibleMoves[1],possibleMoves[2],possibleMoves[3],possibleMoves[4]);
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
        if (birthPlace == null) return;
        EncapsulatingWorld.addCarnivore(birthPlace);
        this.energy -= START_ENERGY;
        if (EncapsulatingWorld.showAll) {
            System.out.print(this);
            System.out.print("\tgave birth at " + birthPlace);
            System.out.println(String.format("\tNew Energy: %d", energy));
        }
    }

    @Override
    public char getChar(){
        return '@';
    }
}
