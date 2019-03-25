package mainWindow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    ClientController cc;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        primaryStage.setTitle("Watching Nemo");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        // cc = new ClientController();
        //cc.connect();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
