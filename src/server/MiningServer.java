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
    private List<ClientHandler> clientList = new CopyOnWriteArrayList<>();

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port + ". Waiting for miners...");

            while (true) {
                // accept incoming connections from miners
                Socket clientSocket = serverSocket.accept();

                // create a separate thread for each client and start it
                ClientHandler client = new ClientHandler(clientSocket, this);
                clientList.add(client);
                client.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // sends a message to all connected clientList simultaneously
    public void broadcast(String message) {
        for (ClientHandler client : clientList) {
            client.sendMessage(message);
        }
    }
        // Genera un bloque de transacciones aleatorias y lo envía a los mineros

    public void sendNewBlock(int difficulty) {
        
        // 1. Generamos por ejemplo 3 transacciones aleatorias unidas por comas

        StringBuilder blockData = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            blockData.append(utils.Transaction.generateRandom().toString());
            if (i < 2) blockData.append(","); // separador de transacciones
        }
        
        String blockString = blockData.toString();
        System.out.println("Broadcasting new block (Difficulty " + difficulty + "): " + blockString);
        
        // 2. Enviamos el mensaje a todos con el formato del plan:
        // new_request|<dificultad>|<datos_transacciones>
        broadcast("new_request|" + difficulty + "|" + blockString);
    }


    // removes a client from the active list when they disconnect
    public void removeClient(ClientHandler client) {
        clientList.remove(client);
        System.out.println("A miner disconnected. Active connections: " + clientList.size());
    }

    public static void main(String[] args) {
        MiningServer server = new MiningServer();
        server.startServer();
    }
}