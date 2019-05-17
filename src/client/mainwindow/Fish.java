package mainwindow;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Fish {

    public final static String PATH_TO_FISHES = "file:fishes/";

    private String name;
    private Timeline t;
    private ImageView i;
    private boolean displayed = false;
    private boolean started = false;
    private Position size;
    private Position start;


    public Fish(String name, Position size, Position start) {
        this.name = name;
        this.size = size;
        this.start = start;

        t = new Timeline();

        i = new ImageView(getImageFromName());
        if(size.x > size.y)
            i.setFitHeight(size.y);
        else
            i.setFitWidth(size.x);
        i.setPreserveRatio(true);


    }


    public void display(Canvas c, double x, double y){
        GraphicsContext gc = c.getGraphicsContext2D();
        gc.drawImage(getImageFromName(), x, y);
    }

    public void display(Pane p, Position pos){
        if(!displayed){
            displayed = true;
            i.setTranslateX(pos.x);
            i.setTranslateY(pos.y);
            p.getChildren().add(i);
        }
    }

    private Image getImageFromName() {
        String imagePath = PATH_TO_FISHES + name + ".png";
        return new Image(imagePath);
    }


    public void move(Position start, Position end, int time){
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, new KeyValue(i.translateXProperty(), 10)),
                new KeyFrame(Duration.millis(4000), new KeyValue(i.translateXProperty(), 500)),
                new KeyFrame(Duration.millis(4000), new KeyValue(i.translateYProperty(), 200)),
                new KeyFrame(Duration.millis(6000), new KeyValue(i.translateXProperty(), 10))
        );
        started = true;
        timeline.play();

    }


    private static List<String> getAvailableFishNames() throws Exception {
        File folder = new File("fishes");
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles == null){
            throw new Exception("Fishes not found");
        }

        List<String> l = new ArrayList<String>();
        for(File f : listOfFiles){
            l.add(f.toString().split("/")[1].split(".png")[0]);
        }
        return l;
    }

    public static String getRandomFishName() throws Exception {
        Random r = new Random();
        List<String> l = getAvailableFishNames();
        return l.get(r.nextInt(l.size()));
    }

    public void hide(Pane p){
        p.getChildren().remove(i);

    }

    private Position calculateCoordinatesFromPercentages(Position dim, Position pos){
        return new Position( pos.x * dim.x / 100, pos.y * dim.y / 100 );
    }

    private String getState(){
        if(started)
            return "started";
        else
            return "notStarted";
    }

    @Override
    public String toString(){
        return "Fish " + name + " at " + start + "," + size + " " + getState();
    }

}
