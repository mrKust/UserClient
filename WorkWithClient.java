import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Date;

public class WorkWithClient extends Thread {

    String clientName = null;
    DataOutputStream outputStream = null;
    DataInputStream inputStream = null;
    ClientsArray clientsArray;
    int numberOfPort;
    private Date dateInfo;
    private InetAddress ipInfo;
    private NetworkInterface HardwareInfo;
    JFrame frame;
    JTextArea textField;

    public WorkWithClient(String nameOfClient, DataOutputStream out, DataInputStream in,
                          ClientsArray clientsArray, int portNumber, InetAddress ip,
                          JTextArea textField, JFrame frame) {
        clientName = nameOfClient;
        outputStream = out;
        inputStream = in;
        this.clientsArray = clientsArray;
        this.numberOfPort = portNumber;
        this.dateInfo = new Date();
        this.ipInfo = ip;
        this.frame = frame;
        this.textField = textField;
        try {
            this.HardwareInfo = NetworkInterface.getByInetAddress(this.ipInfo);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public String getClientName() {
        return clientName;
    }

    public void update (ClientsArray clientsArray){
        this.clientsArray = clientsArray;
    }

    public void sendMessage(String message) {
        try {
            outputStream.writeUTF(message);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (inputStream.available() > -1) {
                    String message = inputStream.readUTF();
                    String tmpMessage = clientName + " write in chat > " +
                            "<" + this.dateInfo.toString() + ">" +
                            "<Source IP " + ipInfo.toString() + ">" +
                            "<" + "Port " + this.numberOfPort + ">" +
                            "<MAC " + this.HardwareInfo + ">"
                            + message + "\n";
                    textField.append(tmpMessage);
                    /*System.out.println(clientName + " write in chat > " +
                              "<" + this.dateInfo.toString() + ">" +
                            "<Source IP " + ipInfo.toString() + ">" +
                            "<" + "Port " + this.numberOfPort + ">" +
                            "<MAC " + this.HardwareInfo.getHardwareAddress() + ">"
                                    + message);*/
                    if (message.contains("@senduser")){
                        int tmp = message.indexOf(" ");
                        int tmp2 = message.lastIndexOf(" ");
                        String nameForTmp = message.substring(tmp, tmp2);
                        clientsArray.sendUser(nameForTmp, message.substring(20,message.length()));
                        continue;
                    }
                    clientsArray.sendAll(message);
                    Thread.sleep(1000);
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("No connected users");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}