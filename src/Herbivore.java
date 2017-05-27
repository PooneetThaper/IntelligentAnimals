import java.util.ArrayList;
import java.util.Random;

public class Herbivore extends Animal {
    private Random rand = new Random();

    public Herbivore(World world, Location location) {
        super(world, location);
    }

    @Override
    public int weighOptions() {
        return weighOptions(false);
    }

    @Override
    public int weighOptions(boolean noStay) {
        //Use full list of all carnivores and plants in a certain area
        ArrayList<Location> nearestCarnivore = world.exhaustiveBFS(location, '@', MAX_SIGHT_DISTANCE - 1, true);
        ArrayList<Location> nearestPlant = world.exhaustiveBFS(location, '*', MAX_SIGHT_DISTANCE, true);
        double[] possibleMoves = new double[5];
        int locationX = location.getX();
        int locationY = location.getY();

        // Consider no move
        if (nearestCarnivore.size() > 0){
            possibleMoves[0] += Location.distance(location, nearestCarnivore.get(0));
            possibleMoves[0] += Location.distance(location, nearestCarnivore);
        }

        if (this.energy < super.MAX_ENERGY) {
            if (nearestPlant.size() > 0){
                possibleMoves[0] -= Location.distance(location, nearestPlant.get(0));
                possibleMoves[0] -= Location.distance(location, nearestPlant);
            }

        }
        if (noStay) possibleMoves[0] -= 20;

        // Consider up
        Location upLocation = new Location(locationX, locationY - 1);
        if (world.isValid(upLocation)) {
            if (nearestCarnivore.size() > 0){
                possibleMoves[1] += Location.distance(upLocation, nearestCarnivore.get(0));
                possibleMoves[1] += Location.distance(upLocation, nearestCarnivore);
            }

            if (this.energy < super.MAX_ENERGY) {
                if (nearestPlant.size() > 0){
                    possibleMoves[1] -= Location.distance(upLocation, nearestPlant.get(0));
                    possibleMoves[1] -= Location.distance(upLocation, nearestPlant);
                }
            }
            if (!world.isEmpty(upLocation)) {
                if (world.isPlant(upLocation)){
                    if (this.energy < super.MAX_ENERGY) possibleMoves[1] += 15;
                }
                else possibleMoves[1] -= 15;
            }
        }else possibleMoves[1] -= 15;

        // Consider right
        Location rightLocation = new Location(locationX + 1,locationY);
        if (world.isValid(rightLocation)) {
            if (nearestCarnivore.size() > 0){
                possibleMoves[2] += Location.distance(rightLocation, nearestCarnivore.get(0));
                possibleMoves[2] += Location.distance(rightLocation, nearestCarnivore);
            }

            if (this.energy < super.MAX_ENERGY) {
                if (nearestPlant.size() > 0){
                    possibleMoves[2] -= location.distance(rightLocation, nearestPlant.get(0));
                    possibleMoves[2] -= location.distance(rightLocation, nearestPlant);
                }

            }
            if (!world.isEmpty(rightLocation)) {
                if (world.isPlant(rightLocation)){
                    if (this.energy < super.MAX_ENERGY) possibleMoves[2] += 15;
                }
                else possibleMoves[2] -= 15;
            }
        }else possibleMoves[2] -= 15;

        // Consider down
        Location downLocation = new Location(locationX,locationY + 1);
        if (world.isValid(downLocation)) {
            if (nearestCarnivore.size() > 0){
                possibleMoves[3] += Location.distance(downLocation, nearestCarnivore.get(0));
                possibleMoves[3] += Location.distance(downLocation, nearestCarnivore);
            }

            if (this.energy < super.MAX_ENERGY) {
                if (nearestPlant.size() > 0){
                    possibleMoves[3] -= Location.distance(downLocation, nearestPlant.get(0));
                    possibleMoves[3] -= Location.distance(downLocation, nearestPlant);
                }
            }
            if (!world.isEmpty(downLocation)) {
                if (world.isPlant(downLocation)){
                    if (this.energy < super.MAX_ENERGY) possibleMoves[3] += 15;
                }
                else possibleMoves[3] -= 15;
            }
        }else possibleMoves[3] -= 15;

        // Consider left
        Location leftLocation = new Location(locationX - 1, locationY);
        if (world.isValid(leftLocation)) {
            if (nearestCarnivore.size() > 0){
                possibleMoves[4] += Location.distance(leftLocation, nearestCarnivore.get(0));
                possibleMoves[4] += Location.distance(leftLocation, nearestCarnivore);
            }

            if (this.energy < super.MAX_ENERGY) {
                if (nearestPlant.size()>0){
                    possibleMoves[4] -= Location.distance(leftLocation, nearestPlant.get(0));
                    possibleMoves[4] -= Location.distance(leftLocation, nearestPlant);
                }
            }
            if (!world.isEmpty(leftLocation)) {
                if (world.isPlant(leftLocation)){
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
        Location birthPlace = super.getBestBirthPlace(this.weighOptions(true));
        //Not in the best position to give birth;
        if (birthPlace == null) {
            if (world.getShowAll()) {
                System.out.print(this);
                System.out.println("\t tried to gve birth but did not find optimal spot");
            }
            return;
        }
        Herbivore h = new Herbivore(world, birthPlace);
        h.cantMove = Math.abs(rand.nextInt() % 2) + 1;
        energy -= h.energy;
        // increase age when carnivores can reproduce so they won't
        // multiply too quickly after every turn
        reproduceAge += rand.nextInt(3) + 3;
        if (world.getShowAll()) {
            System.out.print(this);
            System.out.print("\tgave birth at " + birthPlace);
            System.out.println(String.format("\tNew Energy: %d", energy));
        }
    }

    public void eat(Plant eaten) {
        // Herbivores gain as much energy as the plant's age
        energy += eaten.getAge();
        eaten.removeSelfFromWorld();
        if (world.getShowAll()) {
            System.out.print(this);
            System.out.print("\tate ");
            System.out.print(eaten);
            System.out.print(" at ");
            System.out.print(location);
            System.out.println(String.format("\tNew Energy: %d", energy));
        }
    }

    public void act() {
        Entity[][] map = world.getMap();
        Location start = location;
        int startX = location.getX();
        int startY = location.getY();

        if (age > lifespan || energy < 0) {
            if (world.getShowAll()) {
                System.out.print(this);
                System.out.print("\tdied at ");
                System.out.print(location);
                System.out.println(String.format("\tAge: %d, Energy: %d", age, energy));
            }
            this.removeSelfFromWorld();
        }
        move(weighOptions());
        Location end = location;
        int endX = location.getX();
        int endY = location.getY();

        if (world.getShowAll()) {
            System.out.print(this);
            System.out.println("\t" + start + " -> " + end);
        }

        if (map[endX][endY]!= null && map[endX][endY] != this) {
            Plant eaten = (Plant)map[endX][endY];
            eat(eaten);
        }
        map[endX][endY] = this;

        if (startX != endX || startY != endY) {
            map[startX][startY] = null;
        }

        age++;
        energy--;
    }

    @Override
    public char getChar(){
        return '&';
    }
}
