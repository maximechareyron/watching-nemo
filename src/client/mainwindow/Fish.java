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
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Fish {

    public final static String PATH_TO_FISHES = "file:fishes/";

    private String name;
    private Timeline t;
    private ImageView i;
    private boolean displayed = false;
    private boolean started = false;
    private Position size;
    private Position start;
    private Position dim = new Position(0,0);

    private Queue<KeyFrame> queue = new ConcurrentLinkedQueue<>();

    public Fish(String name, Position size, Position start) {
        this.name = name;
        this.size = size;
        this.start = start;

        t = new Timeline();
        //t.setOnFinished(e -> loadWaitingKeyFrames());

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

    public void display(Pane p){
        dim.x = p.getWidth();
        dim.y = p.getHeight();
        if(!displayed){
            displayed = true;
            i.setTranslateX(start.x);
            i.setTranslateY(start.y);
            p.getChildren().add(i);
        }
    }

    public void updatePath(Position dest, Position size, int time){
        Position tmp = calculateCoordinatesFromPercentages(dest);
        queue.add(new KeyFrame(Duration.seconds(time), new KeyValue(i.translateXProperty(), tmp.x)));
        queue.add(new KeyFrame(Duration.seconds(time), new KeyValue(i.translateYProperty(), tmp.y)));
    }

    private void loadWaitingKeyFrames(){
        t = new Timeline();
        t.setOnFinished(e -> loadWaitingKeyFrames());
        System.out.println(t.getKeyFrames());
        System.out.println("coucou");
        for (KeyFrame k : queue){
            t.getKeyFrames().add(queue.poll());
        }
        t.play();
    }

    private Image getImageFromName() {
        String imagePath = PATH_TO_FISHES + name + ".png";
        return new Image(imagePath);
    }


    public void move(Position start, Position end, int time){
        t.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, new KeyValue(i.translateXProperty(), 10)),
                new KeyFrame(Duration.millis(4000), new KeyValue(i.translateXProperty(), 500)),
                new KeyFrame(Duration.millis(4000), new KeyValue(i.translateYProperty(), 200)),
                new KeyFrame(Duration.millis(6000), new KeyValue(i.translateXProperty(), 10))
        );
        started = true;
        t.play();

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

    private Position calculateCoordinatesFromPercentages(Position pos){
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
