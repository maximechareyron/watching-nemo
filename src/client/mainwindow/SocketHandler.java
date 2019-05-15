package mainwindow;

import javafx.scene.shape.Circle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.logging.*;

public class SocketHandler {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Timer tim;
    private int levelOfLog = 2;
    protected static Logger logger = Logger.getLogger("mainwindow.SocketHandler");

        public SocketHandler() {
            logs();
        }

        public void logs() {
          Handler fh = null;
          try {
            fh = new FileHandler("ClientLog.log", true);
          } catch (IOException e) {
              e.printStackTrace();
          }
          fh.setFormatter(new SimpleFormatter());
          logger.addHandler(fh);
        }

    private boolean isConnected = false;

    public boolean isConnected() {
        return isConnected;
    }

    void setConnected(boolean connected) {
        isConnected = connected;
    }


    void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        clientSocket.setSoTimeout(5000);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        if (levelOfLog >= 1) {
            logger.log(Level.INFO, "Connection to the server");
        }
    }

    void sendMessage(String msg) throws Exception {
        if(isConnected){
            out.println(msg);
            if (levelOfLog >= 2) {
              logger.log(Level.INFO, "Sent to the server in port " + clientSocket.getPort() + ".");
            }
        }
        else
            throw new Exception("Client is not connected to the server");
    }

        String receiveMessage() throws IOException {
            String resp = in.readLine();
            if(resp == null) {
                isConnected = false;
                throw new IOException("No response from server");
            }
            if (levelOfLog >= 2 && !resp.startsWith("ping")) {
              logger.log(Level.INFO, "Reveived from the server in port " + clientSocket.getPort() + ".");
            } else if (levelOfLog >= 3) {
              logger.log(Level.INFO, "Reveived pong from the server in port " + clientSocket.getPort() + ".");
            }
            return resp;
        }

        void stopConnection() throws IOException {
            in.close();
            out.close();
            clientSocket.close();
            tim.cancel();
            isConnected = false;
            if (levelOfLog >= 1) {
              logger.log(Level.INFO, "Connection to the server");
            }
        }

        void startPing(Circle ping_status){
            tim = new Timer();
            tim.schedule(new PingTask(this, ping_status), 1000, 5000);
            if (levelOfLog >= 3) {
              logger.log(Level.INFO, "Ping sent to the server in port " + clientSocket.getPort() + ".");
            }
        }
}
