import java.net.MulticastSocket;

/**
 * Created with IntelliJ IDEA.
 * User: ognjen
 * Date: 6/3/17
 * Time: 1:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class SyncThread implements Runnable {
    private MulticastSocket socket;

    public SyncThread(MulticastSocket socket) {
        this.socket = socket;
    }

    public void run() {

    }
}