package mainWindow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;

public class SocketHandler {

        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private Timer tim;

        public void startConnection(String ip, int port) throws IOException {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }

        public String sendMessage(String msg) throws IOException {
            out.println(msg);
            String resp = in.readLine();
            return resp;
        }

        public void stopConnection() throws IOException {
            /*
            in.close();
            out.close();
            clientSocket.close();
            */
            tim.cancel();
        }

        public void startPing(){
            tim = new Timer();
            tim.schedule(new PingTask(), 1000, 5000);
        }
}
