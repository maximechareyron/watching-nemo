package mainwindow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import static java.lang.Thread.sleep;

public class Main extends Application {

    //ClientController cc;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("aquarium.fxml"));
        primaryStage.setTitle("Watching Nemo");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinHeight(625);
        primaryStage.setMinWidth(800);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
