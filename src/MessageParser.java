import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: kali
 * Date: 6/3/17
 * Time: 3:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageParser {
    private ChatGroup chatGroup;

    public MessageParser(ChatGroup chatGroup) {
        this.chatGroup = chatGroup;
    }

    public String parse(DatagramPacket message) throws InvalidMessageException {
        byte[] data = message.getData();
        if (data.length < ProtocolConstants.HEADER.length + 1 + 4) {
            throw new InvalidMessageException("Message too short");
        }

        ByteBuffer buffer = ByteBuffer.wrap(data);
        byte[] header = new byte[3];
        buffer.get(header);
        for (int i = 0; i < ProtocolConstants.HEADER.length; i++) {
            if (header[i] != ProtocolConstants.HEADER[i]) {
                throw new InvalidMessageException("Invalid message header");
            }
        }

        byte type = buffer.get();
        switch (type) {
            case ProtocolConstants.TYPE_PUBLIC_MESSAGE: return parsePublicMessage(message.getAddress(), buffer);
            case ProtocolConstants.TYPE_PRIVATE_MESSAGE: return parsePrivateMessage(message.getAddress(), buffer);
            case ProtocolConstants.TYPE_PING: parsePing(message.getAddress(), buffer); return null;
            default: throw new InvalidMessageException("Unrecognized message type");
        }
    }

    public void parsePing(InetAddress addr, ByteBuffer buffer) throws InvalidMessageException {
        String username = extractData(buffer);
        if (!username.matches(ProtocolConstants.USERNAME_REGEX)) {
            throw new InvalidMessageException("Invalid username");
        }
        chatGroup.registerPing(addr, username);
    }

    private String getUsername(InetAddress addr) {
        String username = chatGroup.getUsername(addr);
        if (username == null) {
            username = "[UNKNOWN]";
        }

        return username;
    }

    public String parsePrivateMessage(InetAddress addr, ByteBuffer buffer) throws InvalidMessageException {
        String message = extractData(buffer);

        StringBuilder sb = new StringBuilder();
        sb.append("[Private] <");
        sb.append(getUsername(addr));
        sb.append("> ");
        sb.append(message);
        sb.append('\n');

       return sb.toString();
    }

    public String parsePublicMessage(InetAddress addr, ByteBuffer buffer) throws InvalidMessageException {
        String message = extractData(buffer);

        StringBuilder sb = new StringBuilder();
        sb.append('<');
        sb.append(getUsername(addr));
        sb.append("> ");
        sb.append(message);
        sb.append('\n');

       return sb.toString();
    }

    private String extractData(ByteBuffer buffer) throws InvalidMessageException {
        int length = buffer.getInt();
        if (buffer.limit() - buffer.position() < length) {
            throw new InvalidMessageException("Message too short for given length");
        }

        byte[] data = new byte[length];
        buffer.get(data);

        try {
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new InvalidMessageException("UTF-8 not supported");
        }
    }
}
