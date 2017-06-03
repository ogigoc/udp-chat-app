import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.DatagramPacket;
import java.net.InetAddress;
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
        TestConstants.addMockUsernames(group);
        uut = new MessageBuilder(InetAddress.getByName(TestConstants.GROUP_ADDRESS), group);
    }

    @Test
    public void testMakePublicMessage() throws Exception {
        for (String text : TestConstants.MESSAGE_TEXTS) {
            byte[] textBytes = text.getBytes("UTF-8");

            DatagramPacket packet = uut.makePublicMessage(text);
            checkMessage(packet, TestConstants.GROUP_ADDRESS, ProtocolConstants.TYPE_PUBLIC_MESSAGE, textBytes);
        }
    }

    @Test
    public void testMakePrivateMessage() throws Exception {
        for (String text : TestConstants.MESSAGE_TEXTS) {
            byte[] textBytes = text.getBytes("UTF-8");

            for (int i = 0; i < TestConstants.NICKNAMES.length; i++) {
                DatagramPacket packet = uut.makePrivateMessage(TestConstants.NICKNAMES[i], text);
                checkMessage(packet, TestConstants.NICKNAME_ADDRESSES[i], ProtocolConstants.TYPE_PRIVATE_MESSAGE, textBytes);
            }
        }
    }

    @Test
    public void testMakePing() throws Exception {
        for (String nickname : TestConstants.NICKNAMES) {
            byte[] nicknameBytes = nickname.getBytes("UTF-8");

            DatagramPacket packet = uut.makePing(nickname);
            checkMessage(packet, TestConstants.GROUP_ADDRESS, ProtocolConstants.TYPE_PING, nicknameBytes);
        }
    }
}
