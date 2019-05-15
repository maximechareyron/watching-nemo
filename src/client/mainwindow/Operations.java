package mainwindow;

import java.io.IOException;
import java.io.*;
import java.util.Scanner;

class Operations {

  void status(SocketHandler s, PrintStream out) throws Exception {
    s.sendMessage("getFishes");
    String rec = "";
    try {
      do {
        rec = s.receiveMessage();
      } while (rec.startsWith("pong"));
    } catch (IOException e) {
        e.printStackTrace();
    }
    String[] recSplit = rec.split(" \\[");
    if (!recSplit[0].equals("list")) {
      out.print(recSplit[0]);
      out.println("No connected\n");
    }
    else {
      out.println("OK : Connecté au contrôleur, " + (recSplit.length - 1) + " poissons trouvés\n");
      //TODO call drawfish by Amelli
      for (String fish : recSplit) {
        if (!fish.equals("list")) {
          out.println(fish.substring(0, fish.length() - 1) + "\n");
        }
      }
    }
  }

  void addFish(String fishName, String coordinate, String size, String mobility, SocketHandler s, PrintStream out) throws Exception {
    s.sendMessage("addFish " + fishName + " at " + coordinate + "," + size + ", " + mobility);
    outout.println("addFish " + fishName + " at " + coordinate + "," + size + ", " + mobility);
    String rec = "";
    try {
      do {
        rec = s.receiveMessage();
        if (rec.startsWith("list")) {
          //TODO call drawfish by Amelli
        }
      } while (rec.startsWith("list") || rec.startsWith("pong"));
    } catch (IOException e) {
        e.printStackTrace();
    }
    out.println(rec);
    if (rec.equals("OK")) {
      out.println("OK\n");
    }
    else {
      out.println(rec + " : modèle de mobilité non supporté\n");
    }
  }

  void delFish(String fishName, SocketHandler s, PrintStream out) throws Exception {
    s.sendMessage("delFish " + fishName);
    String rec = "";
    try {
      do {
        rec = s.receiveMessage();
        if (rec.startsWith("list")) {
          //TODO call drawfish by Amelli
        }
      } while (rec.startsWith("list") || rec.startsWith("pong"));
    } catch (IOException e) {
        e.printStackTrace();
    }
    if (rec.equals("OK")) {
      out.println("OK\n");
    }
    else {
      out.println(rec + " : Poisson inexistant\n");
    }
  }

  void startFish(String fishName, SocketHandler s, PrintStream out) throws Exception {
    s.sendMessage("startFish " + fishName);
    String rec = "";
    try {
      do {
        rec = s.receiveMessage();
        if (rec.startsWith("list")) {
          //TODO call drawfish by Amelli
        }
      } while (rec.startsWith("list") || rec.startsWith("pong"));
    } catch (IOException e) {
        e.printStackTrace();
    }
    if (rec.equals("OK")) {
      out.println("OK\n");
    }
    else {
      out.println(rec + " : Poisson inexistant\n");
    }
  }
}
