/**
 * Created by Pthaper on 3/30/17.
 */
public abstract class Entity {
    protected Coordinate Location;
    protected World EncapsulatingWorld;

    public Coordinate getLocation(){return Location;}
    public abstract char getChar();

}
