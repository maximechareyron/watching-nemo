package mainWindow;

import java.util.Date;
import java.util.TimerTask;

public class PingTask extends TimerTask {
    @Override
    public void run() {
        System.out.println("Ping @" + new Date());
    }
}
