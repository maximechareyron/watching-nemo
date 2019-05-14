package mainwindow;

import java.io.*;


public class Prompt extends Thread {

  private SocketHandler s;
  public Command theCommands;
  private InputStreamReader reader = new InputStreamReader(System.in);
  private BufferedReader in = new BufferedReader(reader);

  public Prompt(SocketHandler soc){
    // try {
    //   read();
    // } catch (IOException e) {
    //     e.printStackTrace();
    // }
    s = soc;
  }

  public Command parse(String line){
    Command c;
    String[] lineParsed = line.split(" ");
    if (line.contains("status")) {
      c = new Status();
      return c;
    }
    else if (line.contains("startFish")) {
      if (lineParsed.length != 2) {
        System.out.print("wrong usage : startFish [fishName]\n");
        return null;
      }
      c = new StartFish(lineParsed[1]);
      return c;
    }
    else if (line.contains("delFish")) {
      if (lineParsed.length != 2) {
        System.out.print("wrong usage : delFish [fishName]\n");
        return null;
      }
      c = new DelFish(lineParsed[1]);
      return c;
    }
    else if (line.contains("addFish")) {
      if (lineParsed.length != 6) {
        System.out.print("wrong usage : addFish [fishName] at [coordinate], [size], [mobility]\n");
        return null;
      }
      c = new AddFish(lineParsed[1], lineParsed[3].substring(lineParsed[3].length() - 1), lineParsed[4].substring(lineParsed[4].length() - 1), lineParsed[5]);
      return c;
    }
    else {
      return null;
    }
  }

  public String read() throws IOException {
    System.out.print("$ ");
    String cmd;
    while((cmd = in.readLine()) != null) {
      System.out.println(cmd);
      //exécuter la commande ici
      theCommands = parse(cmd);
      if (theCommands == null) {
        System.out.print("Wrong command\n");
      }
      else {
        theCommands.execute(s);
        System.out.print("$ ");
      }
    }
    return cmd;
  }

  public void run() {
    try {
      read();
    } catch (IOException e) {
        e.printStackTrace();
    }
  }

}
