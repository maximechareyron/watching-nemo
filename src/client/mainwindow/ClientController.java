package mainwindow;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ClientController implements Initializable {
    static final String CONFIG_FILE = "display.cfg";

    private Properties config = new Properties();
    private SocketHandler sh;
    private Thread t;

    private int levelOfLog = 3;

    private String id;

    private ArrayList<Fish> fishArrayList = new ArrayList<>();

    private Prompt p;
    // private FishDisplayer mDrawFishes;


    @FXML private Button drawButton;
    @FXML private Circle ping_status;
    @FXML private ConsoleView console;
    @FXML private Canvas aquarium;

    public ClientController() {
        sh = new SocketHandler(levelOfLog);
        try {
            loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setLogs(int log) {
      this.levelOfLog = log;
    }

    private void loadProperties() throws IOException {
        FileInputStream in = new FileInputStream(CONFIG_FILE);
        config.load(in);
        in.close();
    }


    private boolean connect(){
        try {
            sh.startConnection(getControllerAddress(), getControllerPort());
        } catch (IOException e) {
            System.err.println("Cannot create connection to server " + getControllerAddress() + ":" + getControllerPort() + ".");
            return false;
        }
        ping_status.setFill(Color.GREENYELLOW);
        return true;
    }

    private String getControllerAddress(){ return config.get("controller-address").toString(); }

    private int getControllerPort(){ return Integer.valueOf(config.get("controller-port").toString()); }

    private boolean log() {
      if (!connect()) {
        return false;
      }
      try {
        if (getId() == null) {
          sh.sendMessage("hello");
        }
        else {
          sh.sendMessage("hello in as " + getId());
        }
      } catch (Exception e) {
          e.printStackTrace();
      }
        String[] rec = new String[0];
        try {
            rec = sh.receiveMessage().split(" ");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (rec.length == 0) {
          System.out.println("Not connected to the controller\n");
          return false;
        }
        if (rec[0].equals("no")) {
        System.out.println("Not connected to the controller\n");
        return false;
      }
      System.out.println("Connected as " + rec[1]);
      return true;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ping_status.setFill(Color.DARKGRAY);
        if(log()) {
            sh.startPing(ping_status);
            try {
                sh.sendMessage("ls");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        t = new Thread(new Prompt(sh, new Scanner(console.getIn()), console.getOut()));
        t.start();
        draw_fishes();
    }

    public void draw_fishes(){
        GraphicsContext gc = aquarium.getGraphicsContext2D();

        String imagePath = "file:fishes/wes.png";

        Image image = new Image(imagePath);
        gc.setLineWidth(1.0);
        gc.setFill(Color.ORANGE);

        gc.fillText("BITE", 10, 100 );
        gc.drawImage(image, 10, 10);

    }

    /*
    public void draw_fishes() {
        drawButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent ev) {
                try {
                    FishDisplayer.draw("list [Waf at 90x90, 10x2, 0] [Maximus at 50x50, 20x4, 0]");
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
     */
}
