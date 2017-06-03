import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created with IntelliJ IDEA.
 * User: kali
 * Date: 6/3/17
 * Time: 4:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestConstants {
    public final static String[] MESSAGE_TEXTS = { "girice da", "girice ne", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "ћирилица бато" };
    public final static String[] NICKNAMES = { "davis", "ogi", "midza" };
    public final static String[] NICKNAME_ADDRESSES = { "192.168.0.1", "192.168.0.2", "192.168.0.3" };
    public final static String GROUP_ADDRESS = "239.1.1.1";

    public static void addMockUsernames(ChatGroup group) throws UnknownHostException {
        for (int i = 0; i <  TestConstants.NICKNAMES.length; i++) {
            group.registerPing(InetAddress.getByName(TestConstants.NICKNAME_ADDRESSES[i]), TestConstants.NICKNAMES[i]);
        }
    }

}
