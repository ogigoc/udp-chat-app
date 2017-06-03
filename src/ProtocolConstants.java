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
}
