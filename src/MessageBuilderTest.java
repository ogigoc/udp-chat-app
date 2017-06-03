import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created with IntelliJ IDEA.
 * User: kali
 * Date: 6/3/17
 * Time: 3:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageBuilderTest {
    private ChatGroup group;
    private MessageBuilder uut;

    private final String[] MESSAGE_TEXTS = { "girice da", "girice ne", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "ћирилица бато" };
    private final String[] NICKNAMES = { "davis", "ogi", "midza" };
    private final String[] NICKNAME_ADDRESSES = { "192.168.0.1", "192.168.0.2", "192.168.0.3" };
    private final String GROUP_ADDRESS = "239.1.1.1";

    private void addMockUsernames() throws UnknownHostException {
        for (int i = 0; i <  NICKNAMES.length; i++) {
            group.registerPing(InetAddress.getByName(NICKNAME_ADDRESSES[i]), NICKNAMES[i]);
        }
    }

    private void checkMessage(DatagramPacket packet, String address, byte type, byte[] textBytes) throws Exception {
        Assert.assertEquals(packet.getAddress(), InetAddress.getByName(address));
        Assert.assertEquals(packet.getLength(), ProtocolConstants.HEADER.length + 1 + 4 + textBytes.length);

        ByteBuffer buf = ByteBuffer.wrap(packet.getData());
        buf.order(ByteOrder.BIG_ENDIAN);

        byte[] header = new byte[ProtocolConstants.HEADER.length];
        buf.get(header);
        Assert.assertArrayEquals(ProtocolConstants.HEADER, header);
        Assert.assertEquals(type, buf.get());
        Assert.assertEquals(textBytes.length, buf.getInt());

        byte[] msg = new byte[textBytes.length];
        buf.get(msg);
        Assert.assertArrayEquals(textBytes, msg);
    }

    @Before
    public void setUp() throws Exception {
        group = new ChatGroup();
        uut = new MessageBuilder(InetAddress.getByName(GROUP_ADDRESS), group);
        addMockUsernames();
    }

    @Test
    public void testMakePublicMessage() throws Exception {
        for (String text : MESSAGE_TEXTS) {
            byte[] textBytes = text.getBytes("UTF-8");

            DatagramPacket packet = uut.makePublicMessage(text);
            checkMessage(packet, GROUP_ADDRESS, ProtocolConstants.TYPE_PUBLIC_MESSAGE, textBytes);
        }
    }

    @Test
    public void testMakePrivateMessage() throws Exception {
        for (String text : MESSAGE_TEXTS) {
            byte[] textBytes = text.getBytes("UTF-8");

            for (int i = 0; i < NICKNAMES.length; i++) {
                DatagramPacket packet = uut.makePrivateMessage(NICKNAMES[i], text);
                checkMessage(packet, NICKNAME_ADDRESSES[i], ProtocolConstants.TYPE_PRIVATE_MESSAGE, textBytes);
            }
        }
    }

    @Test
    public void testMakePing() throws Exception {
        for (String nickname : NICKNAMES) {
            byte[] nicknameBytes = nickname.getBytes("UTF-8");

            DatagramPacket packet = uut.makePing(nickname);
            checkMessage(packet, GROUP_ADDRESS, ProtocolConstants.TYPE_PING, nicknameBytes);
        }
    }
}
