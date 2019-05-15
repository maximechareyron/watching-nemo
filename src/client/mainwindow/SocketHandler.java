package mainwindow;

import javafx.scene.shape.Circle;

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
    }

    void sendMessage(String msg) throws Exception {
        if(isConnected)
            out.println(msg);
        else
            throw new Exception("Client is not connected to the server");
    }

    String receiveMessage() throws IOException {
        String resp = in.readLine();
        if(resp == null) {
            isConnected = false;
            throw new IOException("No response from server");
        }
        return resp;
    }



    void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        tim.cancel();
        isConnected = false;
    }

    void startPing(Circle ping_status){
        tim = new Timer();
        tim.schedule(new PingTask(this, ping_status), 1000, 5000);
    }
}
