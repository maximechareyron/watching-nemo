package mainwindow;

import java.io.*;
import java.util.Scanner;

public class DelFish extends Command{

  private String fishName;

  public DelFish(String fn) {
    fishName = fn;
    op = new Operations();
  }

  public void execute(SocketHandler s, PrintStream p){
    op.delFish(fishName, s, p);
  }
}
