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
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Fish extends Thread {

    public final static String PATH_TO_FISHES = "file:fishes/";

    private String name;
    private Timeline t;
    private ImageView i;
    private boolean displayed = false;
    private boolean started = false;
    private Position size;
    private Position start;
    private Position dim = new Position(0,0);

    private boolean isMoving = false;
    private int lastAddedTime=0;

    private Thread th;

    private Queue<KeyFrame> queue = new ConcurrentLinkedQueue<>();

    public Fish(String name, Position size, Position start) {
        this.name = name;
        this.size = size;
        this.start = start;

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
        System.out.println("Pane size = " + dim.x + "x" + dim.y);
        if(!displayed){
            displayed = true;
            i.setTranslateX(start.x);
            i.setTranslateY(start.y);
            p.getChildren().add(i);
        }
    }

    public void updatePath(Position dest, Position size, int time){
        Position tmp = calculateCoordinatesFromPercentages(dest);
        System.out.println("new dest : " + tmp.x + "x" +  tmp.y);
        queue.add(new KeyFrame(Duration.seconds(time+lastAddedTime), new KeyValue(i.translateXProperty(), tmp.x)));
        queue.add(new KeyFrame(Duration.seconds(time+lastAddedTime), new KeyValue(i.translateYProperty(), tmp.y)));
        lastAddedTime = time;
        if(!isMoving){
            run();
        }
    }

    private void loadWaitingKeyFrames(){
        lastAddedTime = 0;
        System.out.println("KFs : " + t.getKeyFrames());
        System.out.println("New KFs : " + queue);
        for (KeyFrame k : queue){
            t.getKeyFrames().add(queue.poll());
        }
    }

    public void nextTimeline(){
        t = null;
        t = new Timeline();
        t.setOnFinished(e -> run());
        loadWaitingKeyFrames();
        t.play();
    }

    public void run(){
        t = null;
        t = new Timeline();
        if(queue.isEmpty()){
            isMoving = false;
            System.out.println("going to sleep");
            return;
        }
        t.setOnFinished(e -> run());
        loadWaitingKeyFrames();
        isMoving = true;
        t.play();
    }


    private Image getImageFromName() {
        String imagePath = PATH_TO_FISHES + name + ".png";
        return new Image(imagePath);
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

    private String getFishState(){
        if(started)
            return "started";
        else
            return "notStarted";
    }

    public String getFishName(){
        return name;
    }

    @Override
    public String toString(){
        return "Fish " + name + " at " + start + "," + size + " " + getFishState();
    }
}
