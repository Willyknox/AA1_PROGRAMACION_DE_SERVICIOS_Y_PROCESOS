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
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port + ". Waiting for miners...");
            
            while (true) {
                // accept incoming connections from miners
                Socket clientSocket = serverSocket.accept();
                
                // create a separate thread for each client and start it
                ClientHandler client = new ClientHandler(clientSocket, this);
                clients.add(client);
                client.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // sends a message to all connected clients simultaneously
    public void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    // removes a client from the active list when they disconnect
    public void removeClient(ClientHandler client) {
        clients.remove(client);
        System.out.println("A miner disconnected. Active connections: " + clients.size());
    }

    public static void main(String[] args) {
        MiningServer server = new MiningServer();
        server.startServer();
    }
}