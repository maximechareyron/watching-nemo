package mainwindow;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private Thread t2, t3;

    static private int levelOfLog = 0;

    static private String id = "";

    private ArrayList<Fish> fishArrayList = new ArrayList<>();
    private Fish f2, f3;

    private Prompt p;

    @FXML private Circle ping_status;
    @FXML private ConsoleView console;
    @FXML private Pane aquariumPane;

    @FXML private void connectButtonAction(){
        console.getOut().println(f3.getFishName() + " going to 50x50\n$ ");
        f3.updatePath(new Position(50, 50), new Position(60,60), 5);
    }

    @FXML private void listButtonAction(){
        console.getOut().println(f3.getFishName() + " going to 100x0\n$ ");
        f3.updatePath(new Position(100, 0), new Position(60,60), 5);
    }

    @FXML private void drawButtonAction(){
        console.getOut().println(f2.getFishName() + " going to 100x100");
        console.getOut().print("$ ");

        f2.updatePath(new Position(100, 100), new Position(60, 60), 5);
    }

    @FXML private void addButtonAction(){
        console.getOut().println(f2.getFishName() + " going to 0x100");
        console.getOut().print("$ ");

        f2.updatePath(new Position(0, 100), new Position(60, 60), 5);
    }

    @FXML private void clearConsole(){
        console.clear();
    }

    public ClientController() {
        sh = new SocketHandler(levelOfLog);
        try {
            loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static public void setId(String clientId) {
        id = clientId;
    }

    static public String getId() {
        return id;
    }

    static public void setLogs(int log) {
      levelOfLog = log;
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
        if (getId().isEmpty()) {
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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            sh.listenContinuously();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
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
          f2 = new Fish(Fish.getRandomFishName(), new Position(60, 60), new Position(100, 100));
        } catch (Exception e) {
          e.printStackTrace();
        }
        f2.display(aquariumPane);
        t2 = new Thread(f2);
        t2.start();
        //f2.updatePath(new Position(0, 0), new Position(60, 60), 5);

        try {
            f3 = new Fish(Fish.getRandomFishName(), new Position(60, 60), new Position(0, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }
        f3.display(aquariumPane);
        t3 = new Thread(f3);
        t3.start();
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
