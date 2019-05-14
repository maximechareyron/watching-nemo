package mainwindow;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;

import static java.lang.Thread.sleep;

public class ClientController implements Initializable {
    static final String CONFIG_FILE = "display.cfg";

    private Properties config = new Properties();
    private SocketHandler s;

    private ArrayList<Fish> fishArrayList = new ArrayList<>();

    @FXML public Circle ping_status;

    @FXML private TextArea console;


    public ClientController() {
        s = new SocketHandler();
        try {
            loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ClientController(String id) {
        s = new SocketHandler();
        try {
            loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.print(log(id));
    }


    private void loadProperties() throws IOException {
        FileInputStream in = new FileInputStream(CONFIG_FILE);
        config.load(in);
        in.close();
    }


    private boolean connect(){
        try {
            s.startConnection(getControllerAddress(), getControllerPort());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String getControllerAddress(){ return config.get("controller-address").toString(); }

    private int getControllerPort(){ return Integer.valueOf(config.get("controller-port").toString()); }

    private String log() {
      if (!connect()) {
        return null;
      }
        try {
            s.sendMessage("hello");
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
        System.out.println("Not connected to the controller");
        return null;
      }
      System.out.print("Connected as ");
      return rec[1];
    }

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
        System.out.println("Not connected to the controller");
        return null;
      }
      System.out.print("Connected as ");
      return rec[1];
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(log());
        ping_status.setFill(Color.RED);
        console.setText("$ ");
    }
}
