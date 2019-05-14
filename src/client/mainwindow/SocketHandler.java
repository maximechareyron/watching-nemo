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
        private int levelOfLog;
        protected static Logger logger = Logger.getLogger("mainwindow.SocketHandler");

        public SocketHandler(int log) {
            levelOfLog = log;
            logs();
        }

        public void logs() {
          Handler fh = null;
          try {
            fh = new FileHandler("ClientLog.log", true);
          } catch (IOException e) {
              e.printStackTrace();
          }
          logger.addHandler(fh);
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

        void sendMessage(String msg) {
            out.println(msg);
            if (levelOfLog >= 2) {
              logger.log(Level.INFO, "Sent to the server in port " + clientSocket.getPort() + ".");
            }
        }

        String receiveMessage() throws IOException {
            String resp = in.readLine();
            if(resp == null)
                throw new IOException("No response from server");
            if (levelOfLog >= 2) {
              logger.log(Level.INFO, "Reveived from the server in port " + clientSocket.getPort() + ".");
            }
            return resp;
        }

        void stopConnection() throws IOException {
            in.close();
            out.close();
            clientSocket.close();
            tim.cancel();
            if (levelOfLog >= 1) {
              logger.log(Level.INFO, "Connection to the server");
            }
        }

        void startPing(Circle ping_status){
            tim = new Timer();
            tim.schedule(new PingTask(this, ping_status), 1000, 5000);
            if (levelOfLog >= 2) {
              logger.log(Level.INFO, "Ping sent to the server in port " + clientSocket.getPort() + ".");
            }
        }
}
