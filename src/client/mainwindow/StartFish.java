package mainwindow;

public class StartFish extends Command{

  private String fishName;

  StartFish(String fn) {
    fishName = fn;
    op = new Operations();
  }

  public void execute(SocketHandler s) throws Exception {
    op.startFish(fishName, s);
  }
}
