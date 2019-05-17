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


    public Fish(String name, double width, double height ) {
        this.name = name;
        t = new Timeline();

        i = new ImageView(getImageFromName());
        if(width > height)
            i.setFitHeight(height);
        else
            i.setFitWidth(width);
        i.setPreserveRatio(true);


    }


    public void display(Canvas c, double x, double y){
        GraphicsContext gc = c.getGraphicsContext2D();
        gc.drawImage(getImageFromName(), x, y);
    }

    public void display(Pane p, double x, double y){
        i.setTranslateX(100);
        p.getChildren().add(i);
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
        timeline.play();

    }


    private static List<String> getAvailableFishNames(){
        File folder = new File("fishes");
        File[] listOfFiles = folder.listFiles();
        List<String> l = new ArrayList<String>();
        for(File f : listOfFiles){
            l.add(f.toString().split("/")[1].split(".png")[0]);
        }
        return l;
    }

    public static String getRandomFishName(){
        Random r = new Random();
        List<String> l = getAvailableFishNames();
        return getAvailableFishNames().get(r.nextInt(l.size()));
    }

    public void hide(Pane p){
        p.getChildren().remove(i);

    }

}
