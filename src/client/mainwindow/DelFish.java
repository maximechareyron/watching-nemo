package mainwindow;

public class DelFish extends Command{

  private String fishName;

  DelFish(String fn) {
    fishName = fn;
    op = new Operations();
  }

  public void execute(SocketHandler s) throws Exception {
    op.delFish(fishName, s);
  }
}
