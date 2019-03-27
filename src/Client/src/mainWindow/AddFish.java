public class AddFish extends Command{

  private String fishName;
  private String coordinate;
  private String size;
  private String mobility;

  public void execute(){
    operation.addFish(fishName, size, coordinate, mobility);
  }
}
