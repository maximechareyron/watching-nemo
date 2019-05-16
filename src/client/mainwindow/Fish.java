package mainwindow;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Fish {
    private String name;
    private int time;

    private Position start;
    private Position destination;

    public Fish(String name, int time, Position start, Position destination) {
        this.name = name;
        this.time = time;
        this.start = start;
        this.destination = destination;
    }

    public void display(Canvas c, double x, double y){
        GraphicsContext gc = c.getGraphicsContext2D();
        gc.drawImage(getImageFromName(), x, y);
    }

    private Image getImageFromName(){
        String imagePath = "file:fishes/" + name + ".png";
        return new Image(imagePath);
    }


}
