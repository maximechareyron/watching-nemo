package mainwindow;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static java.lang.Thread.sleep;

public class MainTest {
    public static void main(String[] args) throws Exception {
        testPing();
    }

    public static void testPing() throws IOException, InterruptedException {
        SocketHandler s = new SocketHandler();
        s.startConnection("127.0.0.1", 12345);
        s.sendMessage("hello");
        s.receiveMessage();


        s.startPing();
        sleep(10000);
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

    public static void testPrompt() throws IOException {
        Prompt p = new Prompt();
        p.read();
    }


}
