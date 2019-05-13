package mainwindow;

public class DelFish extends Command{

  private String fishName;

  public void execute(){
    op.delFish(fishName);
  }
}
