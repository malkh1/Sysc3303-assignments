import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Class implementation for Server
 * @author Mohammad Alkhaledi 101162465
 *
 */
public class Server {
    private DatagramSocket socket;

    /**
     * Default constructor for Server
     *
     */
    public Server(int port) {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    /**
     * sends a response packet to a valid read/write request
     * @param requestType specifies read/write request
     */
    private void sendResponsePacket(boolean requestType) {
        if(requestType) {
            byte[] responseData = new byte[4];
            responseData[0] = 0;
            responseData[1] = 4;
            responseData[2] = 0;
            responseData[3] = 0;
            for(byte b : responseData)
                System.out.print(b + "  ");

            System.out.println();
            try {
                DatagramSocket sendToClientSocket = new DatagramSocket();
                DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length,
                        InetAddress.getByName("localhost"), 9999);
                sendToClientSocket.send(responsePacket);
                sendToClientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            byte[] responseData = new byte[4];
            responseData[0] = 0;
            responseData[1] = 3;
            responseData[2] = 0;
            responseData[3] = 1;

            for(byte b : responseData)
                System.out.print(b + "  ");

            System.out.println();
            try {
                DatagramSocket sendToClientSocket = new DatagramSocket();
                DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, InetAddress.getByName("localhost"), 9999);
                sendToClientSocket.send(responsePacket);
                sendToClientSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * finds end of string given a packets data
     * @param data data to check
     * @param position position in to begin
     * @return end of string
     */
    private int findEndOfString(byte[] data, int position) {
        for(int i = position; i < data.length; ++i) {
            //checks to see if byte is NUL character; finds end of string
            if(data[i] == 0) {
                ++position;
                System.out.println("position is " + position);
                break;
            }
            position = ++i;
        }
        return position;
    }


    /**
     * parses packet data to check if request is valid
     * @param data packet data to be parsed
     * @throws Exception invalid request type
     */
    private void parsePacketData(byte[] data) throws Exception {
        byte firstByte = data[0];
        byte secondByte = data[1];
        boolean requestType; // false for readRequest, true for writeRequest

        if(firstByte != 0) {
            throw new Exception("first byte not 0");
        }

        if(secondByte == 1) {
            requestType = false;
        }
        else if(secondByte == 2) {
            requestType = true;
        }
        else {
            throw new Exception("second byte not 1 or 2");
        }

        int position = 2;
        position = findEndOfString(data, position);

        if(data[position] != 0) {
            throw new Exception("byte after first string not 0");
        }

        ++position;
        position = findEndOfString(data, position);

        if(data[position] != 0) {
            throw new Exception("final byte not 0");

        }

        System.out.println("(SERVER)Sending response to client...");
        sendResponsePacket(requestType);

    }

    /**
     * implements the server's operation
     */
    private void serverAlgorithm() {
        byte[] buffer = new byte[20];
        DatagramPacket clientPacket = new DatagramPacket(buffer, buffer.length);

        try {
            System.out.println("(SERVER)Waiting for packet from client...");
            socket.receive(clientPacket);
            Client.printData(clientPacket.getData());
            parsePacketData(clientPacket.getData());

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * start the servers operation
     */
    public void start() {
        while(true)
            serverAlgorithm();
    }

    public static void main(String[] args) {
        Server server = new Server(25565);
        server.start();
    }

}
