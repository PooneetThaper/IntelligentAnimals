/**
 * Created by Pthaper on 5/28/17.
 */
public class Rock extends Entity implements Obstacle {
    public Rock(World world, Location location) {
        super(world, location);
    }

    public char getChar(){
        return '?';
    }

    public void act(){
        // Its a rock... it doesn't really do anything...
        // (but need this because its an entity)
        return;
    }
}
