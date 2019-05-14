package mainwindow;

import java.io.IOException;
import java.util.Date;
import java.util.TimerTask;

public class PingTask extends TimerTask {

    SocketHandler s;

    public PingTask(SocketHandler s) {
        this.s = s;
    }

    @Override
    public void run() {
        System.out.println("Ping @" + new Date());
        try {
            s.sendMessage("ping 12345");
        } catch (IOException e) {
            System.err.println("Could not ping remote host.");
        }
        try {
            System.out.println(s.receiveMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
