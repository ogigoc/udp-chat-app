import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;

/**
 * Created with IntelliJ IDEA.
 * User: ognjen
 * Date: 6/3/17
 * Time: 3:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChatGroupTest {

    private ChatGroup cg;
    @Before
    public void setUp() throws Exception {
        cg = new ChatGroup();
    }

    @Test
    public void testRegisterPing() throws Exception {
        ChatGroup c = new ChatGroup();
        String ip = "192.168.10.10";
        InetAddress address;
        String nick = "ogi";
        address = InetAddress.getByName(ip);

        c.registerPing(address, nick);

        Assert.assertEquals(c.getNicknameToAddress().get(nick), address);
        Assert.assertEquals(c.getAddressToNickname().get(address), nick);

        Assert.assertEquals(c.getNicknameToAddress().get("kurac"), null);
        Assert.assertEquals(c.getAddressToNickname().get(InetAddress.getByName("192.168.10.11")), null);

        InetAddress address2;
        String nick2 = "david";
        address2 = InetAddress.getByName("192.168.10.11");

        c.registerPing(address2, nick2);

        Assert.assertEquals(c.getNicknameToAddress().get(nick2), address2);
        Assert.assertEquals(c.getAddressToNickname().get(address2), nick2);

        c.registerPing(address, nick);

        Assert.assertEquals(c.getNicknameToAddress().get(nick), address);
        Assert.assertEquals(c.getAddressToNickname().get(address), nick);

        String ip3 = "192.168.10.12";
        InetAddress address3;
        String nick3 = "ogi";
        address3 = InetAddress.getByName(ip3);

        Assert.assertEquals(c.getAddressToNickname().get(address3), nick3 + "[" + ip3 + "]");
        Assert.assertEquals(c.getAddressToNickname().get(address), nick + "[" + ip + "]");
        Assert.assertEquals(c.getNicknameToAddress().get(nick3 + "[" + ip3 + "]"), address3);
        Assert.assertEquals(c.getNicknameToAddress().get(nick + "[" + ip + "]"), address);
    }

    @Test
    public void testGetUsername() throws Exception {

    }

    @Test
    public void testGetAddress() throws Exception {

    }

    @Test
    public void testRemoveDeadClients() throws Exception {

    }
}
