package big2.game;

public class SystemOutMessenger implements Messenger {
    @Override
    public void talk(String name, String msg) {
        System.out.println(name + ": " + msg);
    }
}
