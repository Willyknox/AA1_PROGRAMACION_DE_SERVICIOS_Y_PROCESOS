package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MiningClient {

    private String serverAddress = "localhost";
    private int serverPort = 5000;
    private String minerName;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public MiningClient(String minerName) {
        this.minerName = minerName;
    }

    public void start() {
        try {
            // Assign to the class attribute instead of creating a local variable
            socket = new Socket(serverAddress, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // Send login message
            out.println("connect|" + minerName);
            String response = in.readLine();
            if ("ack".equals(response)) {
                System.out.println("Connected to server successfully as: " + minerName);
                // Listen for jobs from the server
                listenToServer();
            }
        } catch (Exception e) {
            System.out.println("Could not connect to server: " + e.getMessage());
        }
    }

    private void listenToServer() {
        // We will implement the job listening loop here in the next step
    }

    public void disconnect() {
        try {
            if (out != null) {
                out.println("disconnect");
            }
            if (socket != null) {
                socket.close();
            }
            System.out.println("Disconnected from server.");
        } catch (Exception e) {
            e.printStackTrace();
    }

    public static void main(String[] args) {
        MiningClient miner = new MiningClient("Miner-1");
        miner.start();
    }

}
