package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// inherits from Thread to handle multiple clients concurrently
public class ClientHandler extends Thread {
    
    private Socket socket;
    private MiningServer server;
    private PrintWriter out;
    private BufferedReader in;
    private String minerName;

    public ClientHandler(Socket socket, MiningServer server) {
        this.socket = socket;
        this.server = server;
        try {
            // initialize input stream reader
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // initialize output print writer with autoflush enabled
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // helper method to send data to this specific client
    public void sendMessage(String msg) {
        if (out != null) {
            out.println(msg);
        }
    }

    @Override
    public void run() {
        try {
            String line;
            // read lines from miner until disconnected
            while ((line = in.readLine()) != null) {
                if (line.startsWith("connect|")) {
                    // extract miner name after "connect|"
                    minerName = line.substring(8);
                    System.out.println("Miner logged in: " + minerName);
                    // confirm login to client
                    sendMessage("ack");
                } else if (line.equals("disconnect")) {
                    System.out.println("Miner requested disconnect: " + minerName);
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Connection error with miner: " + minerName);
        } finally {
            try {
                // close socket and unregister from server
                if (socket != null) {
                    socket.close();
                }
                server.removeClient(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
