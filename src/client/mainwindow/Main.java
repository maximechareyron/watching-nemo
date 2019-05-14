package mainwindow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import static java.lang.Thread.sleep;

public class Main extends Application {

    //ClientController cc;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("aquarium.fxml"));

        Pane aquarium = (Pane)root.lookup("#aquarium");
        Rectangle rectangle1 = new Rectangle(100, 50, Color.LIGHTGRAY);
        Rectangle rectangle2 = new Rectangle(120, 20, 100, 50);
        rectangle2.setFill(Color.WHITE);
        rectangle2.setStroke(Color.BLACK);

        aquarium.getChildren().addAll(rectangle1, rectangle2);

        primaryStage.setTitle("Watching Nemo");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinHeight(630);
        primaryStage.setMinWidth(800);
        primaryStage.show();

        //startController();
    }

    /*
    private void startController(){
        cc = new ClientController();
    }
    */




    public static void main(String[] args) {
        launch(args);
    }
}
