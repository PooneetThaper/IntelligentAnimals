import java.util.ArrayList;

/**
 * Created by Pthaper on 3/31/17.
 */
public class Coordinate {
    public int x;
    public int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate(Coordinate c) {
        this.x = c.x;
        this.y = c.y;
    }

    @Override
    public String toString(){
        return String.format("(%d,%d)",x,y);
    }

    public static double distance(Coordinate a,Coordinate b){
        return Math.sqrt(Math.pow((double)(b.x) - (double)(a.x),2) + Math.pow((double)(b.y)-(double)(a.y),2));
    }

    public static double distance(int x,int y,Coordinate a){
        return Math.sqrt(Math.pow((double)(x) - (double)(a.x),2) + Math.pow((double)(y)-(double)(a.y),2));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coordinate) {
            return ((((Coordinate) obj).x == this.x) && (((Coordinate) obj).y == this.y));
        } else return false;
    }

    public static double distance(Coordinate current, ArrayList<Coordinate> list){
        double sum = 0;
        for (int i =0;i < list.size();i++){
            sum += distance(list.get(i),current);
        }
        return sum/list.size();
    }

    public void yUp(){y = y-1;}
    public void yDown(){y = y+1;}
    public void xRight(){x = x+1;}
    public void xLeft(){x = x-1;}

}
