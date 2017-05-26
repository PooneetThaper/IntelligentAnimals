/**
 * Created by Pthaper on 3/30/17.
 */
public abstract class Animal extends Entity{
    protected static final int MAX_ENERGY = 20; // Energy at which they stop going after food
    protected static final int BIRTH_ENERGY = 25; // Energy at which they are allowed to give birth
    protected static final int BIRTH_AGE = 5; // Age at which they are allowed to give birth
    protected static final int START_ENERGY = 10;
    protected static final int FOOD_ENERGY = 10;
    protected static final double MAX_SIGHT_DISTANCE = 10.0;
    protected int energy = START_ENERGY;
    protected int age = 0;
    protected int cantMove = 0;

    // Movement         1
    //                4 0 2
    //                  3

    //Decide the best move based on search algorithm
    //public abstract int bestMove();

    //Change state of
    public void move(int direction){
        //System.out.print(this);
        //System.out.println(" wants to move in direction " + direction);
        switch (direction){
            case 1:
                if (Location.y>0) {
                    Location.yUp();
                }
                break;
            case 2:
                if (Location.x<(EncapsulatingWorld.getXSize()-1)) {
                    Location.xRight();
                }
                break;
            case 3:
                if (Location.y<(EncapsulatingWorld.getYSize()-1)) {
                    Location.yDown();
                }
                break;
            case 4:
                if (Location.x>0) {
                    Location.xLeft();
                }
                break;
        }
    }

    public Coordinate getBestBirthPlace(int direction){
        Coordinate birthPlace = null;
        switch (direction){
            case 1:
                if (Location.y>0 && EncapsulatingWorld.isValid(Location.x,Location.y-1)) {
                    birthPlace = new Coordinate(Location.x,Location.y-1);
                }
                break;
            case 2:
                if (Location.x<(EncapsulatingWorld.getXSize()-1) && EncapsulatingWorld.isValid(Location.x+1,Location.y)) {
                    birthPlace = new Coordinate(Location.x+1,Location.y);
                }
                break;
            case 3:
                if (Location.y<(EncapsulatingWorld.getYSize()-1) && EncapsulatingWorld.isValid(Location.x,Location.y+1)) {
                    birthPlace = new Coordinate(Location.x,Location.y+1);
                }
                break;
            case 4:
                if (Location.x>0 && EncapsulatingWorld.isValid(Location.x-1,Location.y)) {
                    birthPlace = new Coordinate(Location.x-1,Location.y);
                }
                break;
        }
        return birthPlace;
    }

    public abstract int weighOptions();
    public abstract int weighOptions(boolean noStay);

    public void update(){
        age++;
        energy--;
        if (energy > BIRTH_ENERGY && age > BIRTH_AGE) this.giveBirth();
    }

    public int getAge(){ return age;}

    public int getEnergy(){ return energy;}

    public void ateFood(){
        energy += FOOD_ENERGY;
        if (energy > BIRTH_ENERGY && age > BIRTH_AGE) this.giveBirth();
        if (this instanceof Herbivore && energy > BIRTH_ENERGY - 5 && age > BIRTH_AGE) this.giveBirth();
    }

    public abstract void giveBirth();

    public boolean canMove() {return age >= cantMove;}


}
