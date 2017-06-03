import java.net.InetAddress;
import java.util.ArrayList;
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
        if(this.nicknameToAddress.containsKey(nickname) && !this.nicknameToAddress.get(nickname).equals(address)) {

            InetAddress oldAddress = this.nicknameToAddress.get(nickname);
            String oldNick = nickname + "[" + oldAddress.getHostName() + "]";
            Date oldDate = this.lastPingTime.get(oldAddress);

            InetAddress newAddress = address;
            String newNick = nickname + "[" + newAddress.getHostName() + "]";

            this.lastPingTime.put(oldAddress, oldDate);
            this.addressToNickname.put(oldAddress, oldNick);
            this.nicknameToAddress.remove(nickname);
            this.nicknameToAddress.put(oldNick, oldAddress);

            this.lastPingTime.put(newAddress, new Date());
            this.addressToNickname.put(newAddress, newNick);
            this.nicknameToAddress.put(newNick, newAddress);

            return;
        }

        if(this.addressToNickname.containsKey(address) && !this.addressToNickname.get(address).equals(nickname)) {
            String oldNick = this.addressToNickname.get(address);

            this.nicknameToAddress.remove(oldNick);
        }
        this.lastPingTime.put(address, new Date());
        this.addressToNickname.put(address, nickname);
        this.nicknameToAddress.put(nickname, address);
    }

    // getUsername() ako ima vise od jedan sa istim usernameom onda
    // vrati username[ip]
    public synchronized String getUsername(InetAddress address) {
        return this.addressToNickname.get(address);
    }

    // getAddress() ako ima vise od jedan sa istim usernameom onda
    // prihvata username[ip], u suprotnom username
    public synchronized InetAddress getAddress(String username) {
        return this.nicknameToAddress.get(username);
    }

    public synchronized void removeDeadClients() {
        Date now = new Date();
        ArrayList<InetAddress> expiredAddresses = new ArrayList<>();

        for(InetAddress a : this.lastPingTime.keySet()) {
            if(now.getTime() - this.lastPingTime.get(a).getTime() > ProtocolConstants.EXPIRATION_TIME) {
                expiredAddresses.add(a);
            }
        }

        for(InetAddress a : expiredAddresses) {
            System.out.println("[Offline] " + addressToNickname.get(a));
            this.removeAddress(a);
        }
    }

    private void removeAddress(InetAddress a) {
        String nick = this.addressToNickname.get(a);
        this.lastPingTime.remove(a);
        this.addressToNickname.remove(a);
        this.nicknameToAddress.remove(nick);
    }

    public HashMap<InetAddress, Date> getLastPingTime() {
        return lastPingTime;
    }

    public HashMap<InetAddress, String> getAddressToNickname() {
        return addressToNickname;
    }

    public HashMap<String, InetAddress> getNicknameToAddress() {
        return nicknameToAddress;
    }
}
