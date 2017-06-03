import java.net.DatagramPacket;

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

    public String parse(DatagramPacket message) {
       return null;
    }
}
