public class DelFish extends Command{

  private String fishName;

  public void execute(){
    operation.delFish(fishName);
  }
}
