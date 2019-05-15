package mainwindow;

import java.io.*;
import java.util.Scanner;

public class Status extends Command{

  public Status() {
    op = new Operations();
  }

  public void execute(SocketHandler s, PrintStream p){
    op.status(s, p);
  }
}
