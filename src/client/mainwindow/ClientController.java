package mainwindow;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class ClientController {
    public static final String CONFIG_FILE = "./display.cfg";

    private Properties config = new Properties();
    private SocketHandler s;

    private ArrayList<Fish> fishArrayList = new ArrayList<Fish>();


    public ClientController() throws Exception {
        s = new SocketHandler();
        loadProperties();
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
        return (int) config.get("controller-port");
    }



}
