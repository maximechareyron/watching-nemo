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
    private PingTask pt;

    private Log log;

    public SocketHandler(int logg) {
        log = new Log(logg);
        log.logs();
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
        setConnected(true);
        log.createLogs(Level.INFO, 1, "Connection to the server");
        clientSocket.setKeepAlive(true);
    }

    void sendMessage(String msg) throws Exception {
        if(isConnected){
            out.println(msg);
            log.createLogs(Level.INFO, 2, "Sent to server " + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + ": " + msg);
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
        if (!resp.startsWith("ping")) {
          log.createLogs(Level.INFO, 2, "Received from server in port " + clientSocket.getPort() + ": " + resp);
        }
        else {
          log.createLogs(Level.INFO, 3,  "Received ping from server in port " + clientSocket.getPort() + ": " + resp);
        }
        return resp;
    }


    void listenContinuously() throws IOException
    {
        while (true) {
            String r = null;
            while ((r = in.readLine()) == null);
            if (r.startsWith("pong")) {
                System.out.println("received pong");
                pt.notifyPongResponse();
            } else if (r.startsWith("list")) {
                // Updates fish pos
            } else {
            }
            //out.println(r);
        }
    }

    void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        tim.cancel();
        isConnected = false;
        log.createLogs(Level.INFO, 1, "disconnect from the server");
    }

    void startPing(Circle c){
        tim = new Timer();
        pt = new PingTask(this, c);
        tim.schedule(pt, 1000, 5000);
        log.createLogs(Level.INFO, 3, "Ping sent to the server in port " + clientSocket.getPort() + ".");
    }
}
