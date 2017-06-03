import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class Main {

    private static boolean mainLoop(MessageBuilder builder, MulticastSocket socket, BufferedReader stdin) {
        try {
            String command = stdin.readLine();
            if (command.isEmpty()) {
                return true;
            }

            if (command.equals("/quit") || command.equals("/q")) {
                return false;
            }

            if (command.charAt(0) == '@') {
                int firstWhitespace = command.indexOf(' ');
                if (firstWhitespace == -1) {
                    return true;
                }

                String destinationUsername = command.substring(1, firstWhitespace);
                String message = command.substring(firstWhitespace + 1);
                DatagramPacket packet = builder.makePrivateMessage(destinationUsername, message);
                if (packet == null) {
                    System.err.println("[ERR ] Unknown username `" + destinationUsername + "`");
                    return true;
                }

                socket.send(packet);
            } else {
                DatagramPacket packet = builder.makePublicMessage(command);
                socket.send(packet);
            }
        } catch (IOException e) {
            System.err.println("[ERR ] I/O error on stdin");
        }

        return true;
    }

    public static void main(String[] args) {
        String nick;
        String addr;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            do {
                System.out.print("Choose a nickname:                   ");
                nick = br.readLine();
            } while (!nick.matches(ProtocolConstants.USERNAME_REGEX));

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
        InetAddress address;
        try {
            socket = new MulticastSocket(ProtocolConstants.PORT);
            address = InetAddress.getByName(addr);
            socket.joinGroup(address);
        } catch (UnknownHostException e) {
            System.out.println("Unknown host.");
            return;
        } catch (IOException e) {
            System.out.println("Could not join multicast group.");
            return;
        }

        ChatGroup chatGroup = new ChatGroup();

        SyncThread sync = new SyncThread(socket, address, nick);
        Thread syncThread = new Thread(sync);
        syncThread.start();

        ReceiverThread receiver = new ReceiverThread(socket, chatGroup);
        Thread receiverThread = new Thread(receiver);
        receiverThread.start();

        MessageBuilder builder = new MessageBuilder(address, chatGroup);
        while (mainLoop(builder, socket, br));

        System.err.println("[INFO] Shutting down...");
        receiverThread.interrupt();
        syncThread.interrupt();
        try {
            receiverThread.join();
            syncThread.join();
        } catch (InterruptedException e) {

        }

        System.err.println("[INFO] Bye!");
    }
}
