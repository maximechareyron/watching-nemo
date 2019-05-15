package mainwindow;

public class Status extends Command{

  Status() {
    op = new Operations();
  }

  public void execute(SocketHandler s) throws Exception {
    op.status(s);
  }
}
