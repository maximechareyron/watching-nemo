package mainwindow;

public class AddFish extends Command{

  private String fishName;
  private String coordinate;
  private String size;
  private String mobility;

  public AddFish(String fn, String cd, String s, String m) {
    fishName = fn;
    coordinate = cd;
    size = s;
    mobility = m;
  }

  public void execute(){
    op.addFish(fishName, size, coordinate, mobility);
  }
}
