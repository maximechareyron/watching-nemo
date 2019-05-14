package mainwindow;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class ClientController {
    public static final String CONFIG_FILE = "display.cfg";

    private Properties config = new Properties();
    private SocketHandler s;

    private ArrayList<Fish> fishArrayList = new ArrayList<Fish>();


    public ClientController(){
        s = new SocketHandler();
        try {
            loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(log());
    }

    public ClientController(String id) throws Exception {
        s = new SocketHandler();
        try {
            loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(log(id));
    }


    private void loadProperties() throws IOException {
        FileInputStream in = new FileInputStream(CONFIG_FILE);
        config.load(in);
        in.close();
    }


    public boolean connect(){
        try {
            s.startConnection(getControllerAddress(), getControllerPort());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getControllerAddress(){
        return config.get("controller-address").toString();
    }

    public int getControllerPort(){
        return Integer.valueOf(config.get("controller-port").toString());
    }

    public String log() {
      if (connect() == false) {
        return null;
      }
        try {
            s.sendMessage("hello");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String rec[] = new String[0];
        try {
            rec = s.receiveMessage().split("[]+");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (rec[0] == "no") {
        System.out.print("Not connected to the controller");
        return null;
      }
      System.out.print("Connected as ");
      return rec[1];
    }

    public String log(String id) {
      if (connect() == false) {
        return null;
      }
        try {
            s.sendMessage("hello in as " + id);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String rec[] = new String[0];
        try {
            rec = s.receiveMessage().split("[]+");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (rec[0] == "no") {
        System.out.print("Not connected to the controller");
        return null;
      }
      System.out.print("Connected as ");
      return rec[1];
    }


}
