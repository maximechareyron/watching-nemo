package mainwindow;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.*;

public class ClientController implements Initializable {
    static final String CONFIG_FILE = "display.cfg";

    private Properties config = new Properties();
    private SocketHandler sh;

    private ArrayList<Fish> fishArrayList = new ArrayList<>();

    private Prompt p;

    private int levelOfLog;

    @FXML public Circle ping_status;
    @FXML private ConsoleView console;

    public ClientController(int log) {
        levelOfLog = log;
        sh = new SocketHandler(levelOfLog);
        try {
            loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClientController(int log, String id) {
        levelOfLog = log;
        sh = new SocketHandler(levelOfLog);
        try {
            loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        sh.sendMessage("hello");
        String[] rec = new String[0];
        try {
            rec = sh.receiveMessage().split(" ");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (rec[0].equals("no")) {
        System.out.println("Not connected to the controller\n");
        return false;
      }
      System.out.println("Connected as " + rec[1]);
      return true;
    }

    /*
    public String log(String id) {
      if (!connect()) {
        return null;
      }
        try {
            s.sendMessage("hello in as " + id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] rec = new String[0];
        try {
            rec = s.receiveMessage().split(" ");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (rec[0].equals("no")) {
        System.out.println("Not connected to the controller\n");
        return null;
      }
      System.out.print("Connected as ");
      return rec[1];
    }
     */



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ping_status.setFill(Color.DARKGRAY);
        if(log())
            sh.startPing(ping_status);
            sh.sendMessage("ls");

        System.setOut(console.getOut());
        System.setIn(console.getIn());
        System.setErr(console.getOut());

    }
}
