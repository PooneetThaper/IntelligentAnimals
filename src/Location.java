import java.util.ArrayList;

/**
 * Represents a Location in a 2D space using x-coordinates and y-coordinates.
 */
public class Location {
    /**
     * The x-coordinate in a 2D space.
     */
    private int x;

    /**
     * The y-coordinate in a 2D space.
     */
    private int y;

    /**
     * Gets the x-coordinate of this Location.
     * @return this Location's x-coordinate.
     */
    public int getX() {return x;}

    /**
     * Gets the y-coordinate of this Location.
     * @return this Location's y-coordinate.
     */
    public int getY() {return y;}

    /**
     * Creates a Location using the given x-coordinate and y-coordinate.
     * @param x The given x-coordinate.
     * @param y The given y-coordinate.
     */
    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a Location that has the x-coordinate and y-coordinate of an existing Location object.
     * @param loc An existing Location object.
     */
    public Location(Location loc) {
        this.x = loc.getX();
        this.y = loc.getY();
    }

    /**
     * Decreases this Location's y-coordinate by 1.
     */
    public void yUp() {y = y - 1;}

    /**
     * Increases this Location's y-coordinate by 1.
     */
    public void yDown() {y = y + 1;}

    /**
     * Increases this Location's x-coordinate by 1.
     */
    public void xRight() {x = x + 1;}

    /**
     * Decreases this Location's x-coordinate by 1.
     */
    public void xLeft() {x = x - 1;}

    /**
     * Gets the distance between two Locations.
     * @param a One of the given Locations.
     * @param b Another given Location.
     * @return the distance between the two Locations.
     */
    public static double distance(Location a, Location b) {
        double aX = a.getX();
        double aY = a.getY();
        double bX = b.getX();
        double bY = b.getY();
        return Math.sqrt(Math.pow(bX - aX, 2) + Math.pow(bY - aY, 2));
    }

    /**
     * Gets the distance between coordinates and a given Location.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param a The given Location.
     * @return the distance between the coordinates and the given Location.
     */
    public static double distance(int x, int y, Location a) {
        double doubleX = x;
        double doubleY = y;
        double aX = a.getX();
        double aY = a.getY();
        return Math.sqrt(Math.pow(doubleX - aX, 2) + Math.pow(doubleY - aY, 2));
    }

    /**
     * Determines whether or not this Location is the equal to another object.
     * @param obj Anything one would want to compare this Location to.
     * @return a boolean value of whether or not this Location is equal to the given Object. True if both are
     * Locations with the same coordinates. False otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Location) {
            int objX = ((Location)obj).getX();
            int objY = ((Location)obj).getY();
            return (objX == this.x) && (objY == this.y);
        } 
        else {
            return false;
        }
    }

    /**
     * Gets the average distance between this Location and other Locations provided in an ArrayList.
     * @param current
     * @param list
     * @return
     */
    public static double distance(Location current, ArrayList<Location> list) {
        double sum = 0;
        for (int i = 0; i < list.size(); i++){
            sum += distance(list.get(i), current);
        }
        return sum / list.size();
    }

    /**
     * Gets this Location's String representation, formatted (x,y).
     * @return this Location's String representation
     */
    @Override
    public String toString() {
        return String.format("(%d,%d)",x,y);
    }
}