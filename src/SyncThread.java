import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
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
    private String nickname;
    private ChatGroup chatGroup;
    private MessageBuilder mb;

    public SyncThread(MulticastSocket socket, InetAddress address, String nickname, ChatGroup chatGroup) {
        this.socket = socket;
        this.nickname = nickname;
        this.chatGroup = chatGroup;
        this.mb = new MessageBuilder(address, null);
    }

    public void run() {
        while(!Thread.interrupted()) {
            try {
                DatagramPacket packet = mb.makePing(nickname);
                socket.send(packet);
            } catch (IOException e) {
                System.err.println("OgiError: Could not send ping packet.");
                e.printStackTrace();
            }

            try {
                Thread.sleep(ProtocolConstants.PING_INTERVAL);
            } catch (InterruptedException e) {
                return;
            }

            chatGroup.removeDeadClients();
        }
    }
}
