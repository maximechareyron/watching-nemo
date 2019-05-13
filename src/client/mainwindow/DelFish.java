package mainwindow;

public class DelFish extends Command{

  private String fishName;

  public DelFish(String fn) {
    fishName = fn;
  }

  public void execute(){
    op.delFish(fishName);
  }
}
