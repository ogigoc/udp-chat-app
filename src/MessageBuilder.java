import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * Created with IntelliJ IDEA.
 * User: kali
 * Date: 6/3/17
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageBuilder {
    private ChatGroup chatGroup;
    private InetAddress groupAddress;

    public MessageBuilder(InetAddress groupAddress, ChatGroup chatGroup) {
        this.chatGroup = chatGroup;
        this.groupAddress = groupAddress;
    }

    public DatagramPacket makePublicMessage(String messageText) {
        return null;
    }

    public DatagramPacket makePing(String nickname) {
        return null;
    }

    public DatagramPacket makePrivateMessage(String destinationUsername, String messageText) {
        return null;
    }
}
