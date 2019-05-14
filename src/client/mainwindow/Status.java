package mainwindow;

public class Status extends Command{

  public Status() {
    op = new Operations();
  }

  public void execute(SocketHandler s){
    op.status(s);
  }
}
