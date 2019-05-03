package mainWindow;

import java.io.*;


public class Prompt {

  public Command theCommands;

    public static void main(String[] args) throws IOException{
    InputStreamReader converter = new InputStreamReader(System.in);
    BufferedReader in = new BufferedReader(converter);
    String cmd = in.readLine();

    while((cmd = in.readLine()) != null) {

    }
  }
}
