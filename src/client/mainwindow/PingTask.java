package mainwindow;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.Date;
import java.util.TimerTask;

public class PingTask extends TimerTask {

    private SocketHandler s;
    private Circle ping_status;

    PingTask(SocketHandler s, Circle c) {
        this.s = s;
        ping_status = c;
    }

    @Override
    public void run() {
        System.out.println("Ping @" + new Date());
        try {
            s.sendMessage("ping 12345");
        } catch (Exception e) {
            System.err.println("Could not ping remote host.");
            ping_status.setFill(Color.RED);
        }
        try {
            System.out.println(s.receiveMessage());
            ping_status.setFill(Color.GREENYELLOW);
        } catch (Exception e) {
            ping_status.setFill(Color.RED);
            System.err.println("No response from server.");
        }
    }
}
