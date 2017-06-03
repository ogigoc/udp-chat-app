import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * Created with IntelliJ IDEA.
 * User: kali
 * Date: 6/3/17
 * Time: 4:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageParserTest {
    private ChatGroup group;
    private MessageParser uut;
    private MessageBuilder builder;

    @Before
    public void setUp() throws Exception {
        group = new ChatGroup();
        TestConstants.addMockUsernames(group);
        uut = new MessageParser(group);
        builder = new MessageBuilder(InetAddress.getByName(TestConstants.GROUP_ADDRESS), group);
    }

    @Test
    public void testParsePublicMessage() throws Exception {
        for (int i = 0; i < TestConstants.NICKNAMES.length; i++) {
            for (String text : TestConstants.MESSAGE_TEXTS) {
                DatagramPacket packet = builder.makePublicMessage(text);
                packet.setAddress(InetAddress.getByName(TestConstants.NICKNAME_ADDRESSES[i]));

                String output = uut.parse(packet);
                Assert.assertEquals("<" + TestConstants.NICKNAMES[i] + "> " + text + "\n", output);
            }
        }
    }

    @Test
    public void testParsePrivateMessage() throws Exception {
        for (int i = 0; i < TestConstants.NICKNAMES.length; i++) {
            for (String text : TestConstants.MESSAGE_TEXTS) {
                DatagramPacket packet = builder.makePrivateMessage("ogi", text);
                packet.setAddress(InetAddress.getByName(TestConstants.NICKNAME_ADDRESSES[i]));

                String output = uut.parse(packet);
                Assert.assertEquals("[Private] <" + TestConstants.NICKNAMES[i] + "> " + text + "\n", output);
            }
        }
    }

    @Test
    public void testParseUnknownUsername() throws Exception {
        for (int i = 0; i < TestConstants.NICKNAMES.length; i++) {
            for (String text : TestConstants.MESSAGE_TEXTS) {
                DatagramPacket packet = builder.makePrivateMessage("ogi", text);
                packet.setAddress(InetAddress.getByName("10.0.0.2"));

                String output = uut.parse(packet);
                Assert.assertEquals("[Private] <[UNKNOWN]> " + text + "\n", output);

                packet = builder.makePublicMessage(text);
                packet.setAddress(InetAddress.getByName("10.0.0.2"));
                output = uut.parse(packet);
                Assert.assertEquals("<[UNKNOWN]> " + text + "\n", output);
            }
        }
    }

    @Test
    public void testParsePing() throws Exception {
        String[] newNicknames = { "davis1", "hello", "world", "itsme" };
        String[] newAddrs = { "192.168.0.101", "192.168.0.102", "192.168.0.103" };

        for (int i = 0; i < newNicknames.length; i++) {
            DatagramPacket packet = builder.makePing(newNicknames[i]);
            packet.setAddress(InetAddress.getByName(newAddrs[i]));

            String output = uut.parse(packet);
            Assert.assertEquals(null, output);
            Assert.assertEquals(InetAddress.getByName(newAddrs[i]), group.getNicknameToAddress().get(newNicknames[i]));
        }
    }

    @Test(expected=InvalidMessageException.class)
    public void testParseInvalidMagic() throws Exception {
        byte[] data = { 'P', 'C', 'x', 0x01, 0x00, 0x00, 0x00, 0x00 };
        uut.parse(new DatagramPacket(data, data.length, InetAddress.getByName(TestConstants.NICKNAME_ADDRESSES[0]), ProtocolConstants.PORT));
    }

    @Test(expected=InvalidMessageException.class)
    public void testParseShortMessage() throws Exception {
        byte[] data = { 'P', 'C' };
        uut.parse(new DatagramPacket(data, data.length, InetAddress.getByName(TestConstants.NICKNAME_ADDRESSES[0]), ProtocolConstants.PORT));
    }

    @Test(expected=InvalidMessageException.class)
    public void testParseInvalidMessageType() throws Exception {
        byte[] data = { 'P', 'C', 'R', 0x0f, 0x00, 0x00, 0x00, 0x00 };
        uut.parse(new DatagramPacket(data, data.length, InetAddress.getByName(TestConstants.NICKNAME_ADDRESSES[0]), ProtocolConstants.PORT));
    }

    @Test(expected=InvalidMessageException.class)
    public void testParseTooShortMessageForGivenSize() throws Exception {
        byte[] data = { 'P', 'C', 'R', 0x0f, 0x00, 0x00, 0x00, 0x01 };
        uut.parse(new DatagramPacket(data, data.length, InetAddress.getByName(TestConstants.NICKNAME_ADDRESSES[0]), ProtocolConstants.PORT));
    }
}
