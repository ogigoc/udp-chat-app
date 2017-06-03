import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class Main {
    private static final int PORT = 12555;

    private static boolean mainLoop() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return true;
    }

    public static void main(String[] args) {
        String nick;
        String addr;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            do {
                System.out.print("Choose a nickname:                   ");
                nick = br.readLine();
            } while (!nick.matches("[-a-zA-Z0-9_]+"));

            System.out.print("Multicast group to join [239.1.1.1]: ");
            addr = br.readLine();
            if (addr.isEmpty()) {
                addr = "239.1.1.1";
            }
        } catch (IOException ex) {
            System.out.println("IO failed.");
            return;
        }

        MulticastSocket socket;
        try {
            socket = new MulticastSocket(PORT);
            socket.joinGroup(InetAddress.getByName(addr));
        } catch (UnknownHostException e) {
            System.out.println("Unknown host.");
            return;
        } catch (IOException e) {
            System.out.println("Could not join multicast group.");
            return;
        }

        SyncThread sync = new SyncThread(socket);
        Thread syncThread = new Thread(sync);
        syncThread.start();

        ReceiverThread receiver = new ReceiverThread(socket);
        Thread receiverThread = new Thread(receiver);
        receiverThread.start();

        while (mainLoop());
    }
}
