package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MiningServer {
    
    // port where the server will listen for incoming miner connections
    private int port = 5000;
    
    // thread-safe list to store all currently connected client threads
    private List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public void startServer() {
        // TODO 1: Create a ServerSocket listening on 'port' inside a try-catch block
        
        // TODO 2: Inside an infinite loop while(true), accept incoming connections using serverSocket.accept()
        
        // TODO 3: For each accepted Socket, create a new ClientHandler object, add it to the 'clients' list, and start the thread (.start())
        System.out.println("Server started on port " + port + ". Waiting for miners...");
    }

    // sends a message to all connected clients simultaneously
    public void broadcast(String message) {
        // TODO 4: Iterate through the 'clients' list and call client.sendMessage(message) on each one
    }

    // removes a client from the active list when they disconnect
    public void removeClient(ClientHandler client) {
        // TODO 5: Remove the client object from the 'clients' list
        System.out.println("A miner disconnected. Active connections: " + clients.size());
    }

    public static void main(String[] args) {
        MiningServer server = new MiningServer();
        server.startServer();
    }
}
