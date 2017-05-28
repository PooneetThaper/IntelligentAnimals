import java.util.ArrayList;

public class Location {
    private int x;
    private int y;

    public int getX() {return x;}
    public int getY() {return y;}

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Location(Location loc) {
        this.x = loc.getX();
        this.y = loc.getY();
    }

    public void yUp(){y = y - 1;}
    public void yDown(){y = y + 1;}
    public void xRight(){x = x + 1;}
    public void xLeft(){x = x - 1;}

    public static double distance(Location a, Location b) {
        double aX = a.getX();
        double aY = a.getY();
        double bX = b.getX();
        double bY = b.getY();
        return Math.sqrt(Math.pow(bX - aX, 2) + Math.pow(bY - aY, 2));
    }

    public static double distance(int x, int y, Location a) {
        double doubleX = x;
        double doubleY = y;
        double aX = a.getX();
        double aY = a.getY();
        return Math.sqrt(Math.pow(doubleX - aX, 2) + Math.pow(doubleY - aY, 2));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Location) {
            return ((((Location) obj).getX() == this.x) && (((Location) obj).getY() == this.y));
        } else return false;
    }

    public static double distance(Location current, ArrayList<Location> list) {
        double sum = 0;
        for (int i = 0; i < list.size(); i++){
            sum += distance(list.get(i), current);
        }
        return sum / list.size();
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)",x,y);
    }
}