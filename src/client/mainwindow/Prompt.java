package mainwindow;

import java.io.*;


public class Prompt {

  public Command theCommands;
  private InputStreamReader reader = new InputStreamReader(System.in);
  private BufferedReader in = new BufferedReader(reader);

  public Prompt(){
  }

  public Command parse(String line){
    return null;
  }

  public String read() throws IOException {
    System.out.print("$ ");
    String cmd;
    while((cmd = in.readLine()) != null) {
      System.out.println(cmd);
      //exécuter la commande ici
      System.out.print("$ ");
    }
    return cmd;
  }

}
