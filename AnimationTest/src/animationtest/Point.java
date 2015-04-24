
package animationtest;

import static animationtest.FTCauto.fieldSize;

public class Point {
    private double x;
    private double y;
    private int speed = 50;
    public int size = 100;
    public int sizeSpeed = 1;
    public int transparency = 1;
    
    public Point(double x, double y)
    {
        this.x = (x-100)  /( fieldSize/1000);
        this.y = (y-10) /( fieldSize/1000);
    }

    public void movePoint(int newX, int newY){
        this.x = newX;
        this.y = newY;
    }
    
    public double getX() {
        return (x * (fieldSize/1000));
    }
    
    public double getY() {
        return (y *( fieldSize/1000));
    }
    
    public int getSpeed() {
        return speed;
    }
    
    public void setX(int x) {
        this.x = (x-100)  /( fieldSize/1000);
    }
    
    public void setY(int y) {
        this.y = (y-10) /( fieldSize/1000);
    }
    
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
}
