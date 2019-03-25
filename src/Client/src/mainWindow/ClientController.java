package mainWindow;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class ClientController {
    public static final String CONFIG_FILE = "./path/to/config.cfg";

    private Properties config = new Properties();
    private SocketHandler s;

    private ArrayList<Fish> fishArrayList = new ArrayList<Fish>();



    public ClientController() throws Exception {
        loadProperties();
    }


    private void loadProperties() throws IOException {
        FileInputStream in = new FileInputStream(CONFIG_FILE);
        config.load(in);
        in.close();
    }


    public boolean connect(){
        try {
            s.startConnection(config.get("controller-address").toString(), (int) config.get("controller-port"));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }



}
