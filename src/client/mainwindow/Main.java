package mainwindow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    ClientController cc;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("aquarium.fxml"));
        primaryStage.setTitle("Watching Nemo");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinHeight(330);
        primaryStage.setMinWidth(600);
        primaryStage.show();

        startController();
    }

    private void startController(){
        cc = new ClientController();
    }




    public static void main(String[] args) {
        launch(args);
    }
}
