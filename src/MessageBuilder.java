import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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

    private DatagramPacket makeMessage(String text, byte type, InetAddress address) {
        try {
            byte[] textBytes = text.getBytes("UTF-8");
            ByteBuffer buffer = ByteBuffer.allocate(ProtocolConstants.HEADER.length + 1 + 4 + textBytes.length);
            buffer.order(ByteOrder.BIG_ENDIAN);

            buffer.put(ProtocolConstants.HEADER)
                    .put(type)
                    .putInt(textBytes.length)
                    .put(textBytes);

            byte[] bytes = buffer.array();
            return new DatagramPacket(bytes, bytes.length, address, ProtocolConstants.PORT);
        } catch (Exception e) {
            return null;
        }
    }

    public DatagramPacket makePublicMessage(String messageText) {
        return makeMessage(messageText, ProtocolConstants.TYPE_PUBLIC_MESSAGE, groupAddress);
    }

    public DatagramPacket makePing(String nickname) {
        return makeMessage(nickname, ProtocolConstants.TYPE_PING, groupAddress);
    }

    public DatagramPacket makePrivateMessage(String destinationUsername, String messageText) {
        InetAddress address = chatGroup.getAddress(destinationUsername);
        return makeMessage(messageText, ProtocolConstants.TYPE_PRIVATE_MESSAGE, address);
    }
}
