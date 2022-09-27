import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 * class code that implements the client algorithm
 *
 *
 * @author Mohammad Alkhaledi 101162465
 *
 */
public class Client {
    private DatagramSocket socket;

    /**
     * Default constructor for Client
     * @param port port to communicate with server
     */
    public Client(int port) {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * method to build a packet with intention of being sent
     * @param fileName filename of file to be accessed
     * @param address ip address of server
     * @param secondByte indicates whether packet is a read or write request
     * @return a constructed DatagramPacket
     */
    private DatagramPacket createRequestPacket(String fileName, InetAddress address, byte secondByte) {
        byte zeroByte = 0;
        byte[] fileNameBytes = fileName.getBytes();
        String mode = "netascii";
        byte[] modeBytes = mode.getBytes();
        int length = 1 + 1 + fileNameBytes.length + 1 + modeBytes.length + 1;
        byte[] buffer = new byte[length];
        DatagramPacket requestPacket;

        buffer[0] = zeroByte;
        buffer[1] = secondByte;
        int position = 2;
        for(byte b : fileNameBytes) {
            buffer[position] = b;
            ++position;
        }
        buffer[position] = zeroByte;
        ++position;
        for(byte b : modeBytes) {
            buffer[position] = b;
            ++position;
        }
        buffer[position] = zeroByte;



        requestPacket = new DatagramPacket(buffer, length, address, 65535);

        return requestPacket;

    }
    /**
     * prints out packet information as a string and as raw byte data
     * @param data data to be printed as a string and as byte data
     */
    public static void printData(byte[] data) {
        String dataAsString = new String(data);
        System.out.println("Data as a string: " + dataAsString);
        System.out.println("Data in byte representation: ");
        for (byte datum : data) {
            System.out.print(datum + " ");
        }
        System.out.println("\n");
    }

    /**
     * method that performs the client's operation
     * @throws IOException io error
     */
    public void clientAlgorithm() throws IOException {
        int validRequestLimit = 10;
        DatagramPacket packet;
        for(int i = 0; i < validRequestLimit; ++i) {

            if((i % 2) == 0) {
                byte one = 1;
                System.out.println("(CLIENT)Creating read request packet...");
                packet = createRequestPacket("test.txt", InetAddress.getByName("localhost"), one);
            }
            else {
                byte two = 2;
                System.out.println("(CLIENT)Creating write request packet...");
                packet = createRequestPacket("text.txt", InetAddress.getByName("localhost"), two);
            }
            printData(packet.getData());
            System.out.println("(CLIENT)Sending packet to server...");
            socket.send(packet);
            System.out.println("(CLIENT)Waiting for response from server...");
            byte[] buffer = new byte[4];
            packet.setData(buffer);
            socket.receive(packet);
            printData(packet.getData());

        }
        byte invalidByte = 34;
        System.out.println("(CLIENT)Creating invalid request packet...");
        packet = createRequestPacket("test.txt", InetAddress.getByName("localhost"), invalidByte);
        printData(packet.getData());
        System.out.println("(CLIENT)Sending invalid packet to server...");
        socket.send(packet);
        System.out.println("(CLIENT)Waiting for response from server...");
        socket.receive(packet);
        printData(packet.getData());
        socket.close();

    }

    public static void main(String[] args) {
        Client client = new Client(7777);
        try {
            client.clientAlgorithm();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
