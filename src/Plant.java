/**
 * Created by Pthaper on 3/30/17.
 */
public class Plant extends Entity{
    private int age;

    public Plant(World world, Coordinate location){
        super.EncapsulatingWorld = world;
        super.Location = location;
        age = Math.abs(EncapsulatingWorld.random.nextInt())%3;
    }

    public Plant(World world, Coordinate location, int age){
        super.EncapsulatingWorld = world;
        super.Location = location;
        this.age = age;
    }

    public void update(){
        age++;
    }

    public int getAge(){ return age;}

    @Override
    public char getChar(){
        return '*';
    }
}
