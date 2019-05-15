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
import java.util.Scanner;

public class ClientController implements Initializable {
    static final String CONFIG_FILE = "display.cfg";

    private Properties config = new Properties();
    private SocketHandler sh;
    private Thread t;

    private String id;

    private ArrayList<Fish> fishArrayList = new ArrayList<>();

    private Prompt p;


    @FXML public Circle ping_status;
    @FXML private ConsoleView console;

    public ClientController() {
        sh = new SocketHandler();
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

    private void sendMessage(String m){
        try {
            sh.sendMessage(m);
        } catch (Exception e) {
            console.getOut().println("Err : Client is not connected to the controller.");
        }
    }

    private boolean log() {
        if (!connect()) {
            return false;
        }
        sendMessage("hello");
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ping_status.setFill(Color.DARKGRAY);
        if(log()) {
            sh.startPing(ping_status);
            sendMessage("ls");
        }
        t = new Thread(new Prompt(sh, new Scanner(console.getIn()), console.getOut()));
        t.start();
    }
}
