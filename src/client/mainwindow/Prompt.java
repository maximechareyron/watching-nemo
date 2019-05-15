package mainwindow;

import java.io.*;
import java.util.Scanner;


public class Prompt implements Runnable {

  private SocketHandler sh;
  private Command theCommand;
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
    else if (line.contains("help")) {
      print_help();
      return null;
    }
    else {
      out.println("NOK : Invalid command");
      return null;
    }
  }

  private void print_help(){
    out.println("status : displays informations on the state of the application\n" +
            "addFish <name> at 00x00, 00x00, <moving strategy> : adds a fish accordingly to the given parameters\n" +
            "delFish <name> : removes specified fish\n" +
            "startFish <name> : enables given fish to move");
  }

  private void execute(Command c){
    try {
      theCommand.execute(sh, out);
    } catch (Exception e) {
      out.println("Err: Client is not connected to the server.");
    }
  }

  public void run() {
    while(true){
      out.print("$ ");
      String input = in.nextLine();
      out.print("\t-> ");

      if("q".equals(input)){
        out.println("Exit!");
        System.exit(0);
      }

      theCommand = parse(input);
      if(theCommand != null){
        execute(theCommand);
      }
    }
  }

}
