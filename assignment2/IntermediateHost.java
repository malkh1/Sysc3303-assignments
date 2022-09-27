import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * class implementation for intermediate host
 * @author Mohammad Alkhaledi 101162465
 *
 */
public class IntermediateHost {
    private DatagramSocket clientSocket;
    private DatagramSocket serverSocket;

    /**
     * Default constructor for IntermediateHost
     * @param clientPort port to communicate with client
     * @param serverPort port to communicate with server
     */
    public IntermediateHost(int clientPort, int serverPort) {
        try {
            clientSocket = new DatagramSocket(clientPort);
            serverSocket = new DatagramSocket(serverPort);
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Intermediate host algorithm to be repeated "forever"
     */
    private void intermediateHostAlgorithm() {
        byte[] buffer = new byte[20];

        try {
            DatagramPacket clientPacket = new DatagramPacket(buffer, buffer.length);
            System.out.println("(IH)Waiting for client to sent packet...");
            clientSocket.receive(clientPacket);
            DatagramPacket packet = new DatagramPacket(clientPacket.getData(), clientPacket.getData().length);
            Client.printData(clientPacket.getData());
            System.out.println("(IH)Sending packet to server...");
            DatagramPacket datagramPacket = new DatagramPacket(clientPacket.getData(), clientPacket.getData().length,
                    InetAddress.getByName("localhost"), 25565);
            serverSocket.send(datagramPacket);
            System.out.println("(IH)Waiting for server to respond...");
            serverSocket.receive(clientPacket);
            Client.printData(clientPacket.getData());
            System.out.println("(IH)Sending response to client...");
            DatagramPacket datagramPacket1 = new DatagramPacket(clientPacket.getData(), clientPacket.getData().length,
                    InetAddress.getByName("localhost"), 7777);
            clientSocket.send(datagramPacket1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * method to start the intermediate host's operation
     */
    public void start() {
        while(true) {
            intermediateHostAlgorithm();
        }
    }

    public static void main(String[] args) {
        IntermediateHost host = new IntermediateHost(65535, 9999);
        host.start();

    }

}
