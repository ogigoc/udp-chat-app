/**
 * Created with IntelliJ IDEA.
 * User: kali
 * Date: 6/3/17
 * Time: 3:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProtocolConstants {
    public static final int PORT = 12555;
    public static final byte TYPE_PING = 0x00;
    public static final byte TYPE_PUBLIC_MESSAGE = 0x01;
    public static final byte TYPE_PRIVATE_MESSAGE = 0x02;
    public static final byte[] HEADER = { 'P', 'C', 'R' };
    public static final int PING_INTERVAL = 3 * 1000;
    public static final int EXPIRATION_TIME = 10 * 1000;
    public static final int RECEIVE_TIMEOUT = 10 * 1000;
    public static final String USERNAME_REGEX = "[-a-zA-Z0-9_]+";
}
