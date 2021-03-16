import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver {
    static JFrame frame;
    static JTextArea textField;
    static JScrollPane scrollPane;

    public static void main(String[] args) {
        ServerSocket server = null;
        ClientsArray clientsArray = new ClientsArray();
        frame = new JFrame("Окно получателя");
        frame.setSize(900, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout());
        textField = new JTextArea();
        frame.add(textField);
        scrollPane = new JScrollPane(textField);
        frame.add(scrollPane);
        frame.setVisible(true);
        try {
            server = new ServerSocket(3345);
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            while (!server.isClosed()) {
                Socket client = server.accept();
                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                DataInputStream in = new DataInputStream(client.getInputStream());
                //out.writeUTF("Input your nickname for this chat ");
                //out.flush();
                int sourcePort = client.getPort();
                //String nameOfClient = in.readUTF();
                String nameOfClient = Integer.toString(sourcePort);
                InetAddress ipInfo = client.getInetAddress();
                WorkWithClient tmp =  new WorkWithClient(nameOfClient, out, in, clientsArray,
                        sourcePort, ipInfo, textField, frame);
                clientsArray.addNewClient(nameOfClient,tmp);
                out.writeUTF("Your connection has been accepted");
                out.flush();
                System.out.print("Connection accepted.\n");
                tmp.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}