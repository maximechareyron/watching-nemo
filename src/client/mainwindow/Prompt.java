package mainwindow;

import java.io.*;
import java.util.Scanner;


public class Prompt implements Runnable {

  public static final String ANSI_RESET = "\u001B[0m";
  public static final String ANSI_RED = "\u001B[31m";
  private SocketHandler sh;
  public Command theCommands;
  private Scanner in;
  private PrintStream out;

  Prompt(SocketHandler soc, Scanner s, PrintStream p){
    sh = soc;
    in = s;
    out = p;
  }

  private Command parse(String line){
    Command c;
    String[] lineParsed = line.split(" ");
    if (line.contains("status")) {
      c = new Status();
      return c;
    }
    else if (line.contains("startFish")) {
      if (lineParsed.length != 2) {
        out.println("wrong usage : startFish [fishName]");
        return null;
      }
      c = new StartFish(lineParsed[1]);
      return c;
    }
    else if (line.contains("delFish")) {
      if (lineParsed.length != 2) {
        out.println("wrong usage : delFish [fishName]");
        return null;
      }
      c = new DelFish(lineParsed[1]);
      return c;
    }
    else if (line.contains("addFish")) {
      if (lineParsed.length != 6) {
        out.print("wrong usage : addFish [fishName] at [coordinate], [size], [mobility]\n");
        return null;
      }
      for (String s : lineParsed) {
        out.println(s);
      }
      c = new AddFish(lineParsed[1], lineParsed[3].substring(0, lineParsed[3].length() - 1), lineParsed[4].substring(0, lineParsed[4].length() - 1), lineParsed[5]);
      return c;
    }
    else {
      out.println("Invalid command");
      return null;
    }
  }

  /*
  public String read() throws IOException {
    System.out.print("$ ");
    String cmd;
    while((cmd = in.readLine()) != null) {
      //System.out.println(cmd);
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
   */

  public void run() {
    while(true){
      out.print("$ ");
      String input = in.nextLine();

      if("q".equals(input)){
        out.println("Exit!");
        System.exit(0);
      }

      parse(input);
    }
  }

}
