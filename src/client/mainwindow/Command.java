package mainwindow;

public abstract class Command {

  public Operations op;

  public abstract void execute(SocketHandler s) throws Exception;
}
