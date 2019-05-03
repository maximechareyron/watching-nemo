package mainWindow;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static java.lang.Thread.sleep;

public class MainTest {
    public static void main(String[] args) throws Exception {
    }

    public static void testPing() throws IOException {
        SocketHandler s = new SocketHandler();

        s.startPing();
        System.out.println("bite");
        s.stopConnection();
    }

    public static void testConfig() throws Exception {
        Properties config = new Properties();
        FileInputStream in = new FileInputStream(ClientController.CONFIG_FILE);
        config.load(in);
        in.close();

        System.out.println();
        System.out.println(config.get("controller-port"));
        System.out.println(config.get("display-timeout-value"));
    }


}
