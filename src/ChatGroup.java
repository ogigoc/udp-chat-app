import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: kali
 * Date: 6/3/17
 * Time: 2:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChatGroup {
    private HashMap<InetAddress, Date> lastPingTime = new HashMap<>();
    private HashMap<InetAddress, String> addressToNickname = new HashMap<>();
    private HashMap<String, InetAddress> nicknameToAddress = new HashMap<>();

    public synchronized void registerPing(InetAddress address, String nickname) {
    }

    // getUsername() ako ima vise od jedan sa istim usernameom onda
    // vrati username[ip]
    public synchronized String getUsername(InetAddress address) {
        return "mock";
    }

    // getAddress() ako ima vise od jedan sa istim usernameom onda
    // prihvata username[ip], u suprotnom username
    public synchronized InetAddress getAddress(String username) {
        return null;
    }

    public synchronized void removeDeadClients() {

    }
}
