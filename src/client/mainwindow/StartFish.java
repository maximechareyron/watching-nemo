package mainwindow;

public class StartFish extends Command{

  private String fishName;

  public StartFish(String fn) {
    fishName = fn;
  }

  public void execute(){
    op.startFish(fishName);
  }
}
