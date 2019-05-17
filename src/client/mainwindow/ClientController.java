package mainwindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
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
    private Thread t1;

    private int levelOfLog = 3;

    private String id;

    private ArrayList<Fish> fishArrayList = new ArrayList<>();
    Fish f2;

    private Prompt p;

    @FXML private Circle ping_status;
    @FXML private ConsoleView console;
    @FXML private Pane aquariumPane;

    @FXML private void drawButtonAction(){
        console.getOut().println(f2.getFishName() + " going to 100x100");
        console.getOut().print("$ ");

        f2.updatePath(new Position(80, 80), new Position(60, 60), 5);
    }

    @FXML private void addButtonAction(){
        console.getOut().println(f2.getFishName() + " going to 0x100");
        console.getOut().print("$ ");

        f2.updatePath(new Position(0, 80), new Position(60, 60), 5);
    }

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
    }

    public void postScene(){
        console.changeColors();

        try {
            f2 = new Fish(Fish.getRandomFishName(), new Position(60, 60), new Position(200, 10));
        } catch (Exception e) {
            e.printStackTrace();
        }
        t1 = new Thread(f2);
        t1.start();
        f2.display(aquariumPane);
        // f2.move2(new Position(0,0), new Position(0, 0), 10);
        f2.updatePath(new Position(0, 0), new Position(60, 60), 5);
        f2.run();

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
