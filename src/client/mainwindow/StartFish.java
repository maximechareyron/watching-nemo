package mainwindow;

import java.io.*;
import java.util.Scanner;

public class StartFish extends Command{

  private String fishName;

  public StartFish(String fn) {
    fishName = fn;
    op = new Operations();
  }

  public void execute(SocketHandler s, PrintStream p){
    op.startFish(fishName, s, p);
  }
}
