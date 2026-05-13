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
            // TODO 1: Initialize 'in' using new BufferedReader(new InputStreamReader(socket.getInputStream()))
            
            // TODO 2: Initialize 'out' using new PrintWriter(socket.getOutputStream(), true) // 'true' enables autoflush
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // helper method to send data to this specific client
    public void sendMessage(String msg) {
        // TODO 3: Send the message using out.println(msg)
    }

    @Override
    public void run() {
        try {
            String line;
            // TODO 4: Read lines from 'in' using a while((line = in.readLine()) != null) loop
            
                // TODO 5: If the line starts with "connect|", extract the name using line.substring(...) or line.split("\\|")
                // Save the name in 'minerName', print a welcome log, and send back an "ack" message using sendMessage("ack")
                
                // TODO 6: If the line equals "disconnect", break the loop to end the thread
                
        } catch (Exception e) {
            System.out.println("Connection error with miner: " + minerName);
        } finally {
            try {
                // TODO 7: Close the socket and call server.removeClient(this) to clean up the list
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
