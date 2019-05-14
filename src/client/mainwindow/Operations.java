package mainwindow;

import java.io.IOException;

public class Operations {

  public void status(SocketHandler s) {
    s.sendMessage("getFishes");
    String[] rec = new String[0];
    try {
        rec = s.receiveMessage().split(" [");
    } catch (IOException e) {
        e.printStackTrace();
    }
    if (rec[0] != "list") {
      System.out.println("No connected\n");
    }
    else {
      System.out.println("OK : Connecté au contrôleur, " + (rec.length - 1) + " poissons trouvés\n");
      for (String fish : rec) {
        if (fish != "list") {
          System.out.println(fish.substring(fish.length() - 1) + "\n");
        }
      }
    }
  }

  public void addFish(String fishName, String coordinate, String size, String mobility, SocketHandler s) {
    s.sendMessage("addFish " + fishName + "at " + coordinate + ", " + size + ", " + mobility);
    String rec = new String();
    try {
        rec = s.receiveMessage();
    } catch (IOException e) {
        e.printStackTrace();
    }
    if (rec == "OK") {
      System.out.println("OK\n");
    }
    else {
      System.out.println(rec + " : modèle de mobilité non supporté\n");
    }
  }

  public void delFish(String fishName, SocketHandler s) {
    s.sendMessage("delFish " + fishName);
    String rec = new String();
    try {
        rec = s.receiveMessage();
    } catch (IOException e) {
        e.printStackTrace();
    }
    if (rec == "OK") {
      System.out.println("OK\n");
    }
    else {
      System.out.println(rec + " : Poisson inexistant\n");
    }
  }

  public void startFish(String fishName, SocketHandler s) {
    s.sendMessage("startFish " + fishName);
    String rec = new String();
    try {
        rec = s.receiveMessage();
    } catch (IOException e) {
        e.printStackTrace();
    }
    if (rec == "OK") {
      System.out.println("OK\n");
    }
    else {
      System.out.println(rec + " : Poisson inexistant\n");
    }
  }
}
