package mainwindow;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;

import static java.lang.Thread.sleep;

public class Main extends Application {

    //ClientController cc;
    public DrawFishes mDrawFishes;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("aquarium.fxml"));
        primaryStage.setTitle("Watching Nemo");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinHeight(675);
        primaryStage.setMinWidth(800);
        primaryStage.show();
        mDrawFishes = new DrawFishes((Pane)root.lookup("#aquarium"));

        Button b = (Button)root.lookup("#drawButton");
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent ev) {
                try {
                    mDrawFishes.draw("list [Waf at 90x90, 10x2, 0] [Maximus at 50x50, 20x4, 0]");
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}
