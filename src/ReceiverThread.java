import java.net.DatagramSocket;

/**
 * Created with IntelliJ IDEA.
 * User: kali
 * Date: 6/3/17
 * Time: 2:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReceiverThread implements Runnable {
    private DatagramSocket socket;

    public ReceiverThread(DatagramSocket socket) {
        this.socket = socket;
    }

    public void run() {

    }
}
