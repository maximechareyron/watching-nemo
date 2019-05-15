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
        System.out.println(new Date() + "-- ping 12345");
        try {
            s.sendMessage("ping 12345");
        } catch (Exception e) {
            System.err.println("Could not ping remote host.");
            s.setConnected(false);
            ping_status.setFill(Color.RED);
        }
        try {
            String resp = s.receiveMessage();
            System.out.println(new Date() + " " + resp);
            if(resp.equals("pong 12345")) {
                s.setConnected(true);
                ping_status.setFill(Color.GREENYELLOW);
            } else {
                ping_status.setFill(Color.ORANGE);
            }
        } catch (Exception e) {
            s.setConnected(false);
            ping_status.setFill(Color.RED);
            System.err.println("No response from server.");
        }
    }
}
