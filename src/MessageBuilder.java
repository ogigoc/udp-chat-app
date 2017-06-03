import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

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
        try {
            byte[] textBytes = messageText.getBytes("UTF-8");
            ByteBuffer buffer = ByteBuffer.allocate(ProtocolConstants.HEADER.length + 1 + 4 + textBytes.length);
            buffer.put(ProtocolConstants.HEADER)
                    .put(ProtocolConstants.TYPE_PUBLIC_MESSAGE)
                    .putInt(textBytes.length)
                    .put(textBytes);

            byte[] bytes = buffer.array();
            return new DatagramPacket(bytes, bytes.length, groupAddress, ProtocolConstants.PORT);
        } catch (Exception e) {
            return null;
        }
    }

    public DatagramPacket makePing(String nickname) {
        return null;
    }

    public DatagramPacket makePrivateMessage(String destinationUsername, String messageText) {
        return null;
    }
}
