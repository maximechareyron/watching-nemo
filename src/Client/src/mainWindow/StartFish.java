package mainWindow;

public class StartFish extends Command{

  private String fishName;

  public void execute(){
    op.startFish(fishName);
  }
}
