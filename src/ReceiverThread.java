import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created with IntelliJ IDEA.
 * User: kali
 * Date: 6/3/17
 * Time: 2:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReceiverThread implements Runnable {
    private DatagramSocket socket;
    private MessageParser parser;

    public ReceiverThread(DatagramSocket socket, ChatGroup chatGroup) {
        this.socket = socket;
        this.parser = new MessageParser(chatGroup);
    }

    public void run() {
        byte[] buffer = new byte[65536];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            socket.setSoTimeout(ProtocolConstants.RECEIVE_TIMEOUT);
        } catch (SocketException e) {
            System.err.println("[WARN] Cannot set socket timeout. The receiving thread might be stuck before exiting.");
        }

        while (!Thread.interrupted()) {
            try {
                socket.receive(packet);
            } catch (IOException e) {
                continue;
            }

            try {
                String output = parser.parse(packet);
                if (output != null) {
                    System.out.println(output);
                }
            } catch (InvalidMessageException e) {
                System.err.println("[WARN] Got invalid message: " + e.getMessage());
            }
        }
    }
}
