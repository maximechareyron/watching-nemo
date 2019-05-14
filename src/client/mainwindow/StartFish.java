package mainwindow;

public class StartFish extends Command{

  private String fishName;

  public StartFish(String fn) {
    fishName = fn;
    op = new Operations();
  }

  public void execute(SocketHandler s){
    op.startFish(fishName, s);
  }
}
