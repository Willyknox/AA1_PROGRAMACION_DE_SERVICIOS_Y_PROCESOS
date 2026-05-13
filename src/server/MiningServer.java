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

    private String currentBlockData = "";

    private int currentDifficulty = 2;

    private boolean blockSolved = false;


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
        this.currentDifficulty = difficulty;
        this.blockSolved = false;

        // 1. Generamos por ejemplo 3 transacciones aleatorias unidas por comas
        StringBuilder blockData = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            blockData.append(utils.Transaction.generateRandom().toString());
            if (i < 2) blockData.append(","); // separador de transacciones
        }

        this.currentBlockData = blockData.toString();
        System.out.println("Broadcasting new block (Difficulty " + difficulty + "): " + currentBlockData);

        // 2. Enviamos el mensaje a todos con el formato del plan:
        // new_request|<dificultad>|<datos_transacciones>
        broadcast("new_request|" + difficulty + "|" + currentBlockData);
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

    // Valida la solución enviada por un minero de forma sincronizada
    public synchronized void verifySolution(ClientHandler miner, int salt) {
        if (blockSolved) {
            // Ya fue resuelto por otro minero más rápido
            miner.sendMessage("sol_result|invalid");
            return;
        }

        String hash = utils.HashUtils.sha256(currentBlockData + salt);
        StringBuilder targetBuilder = new StringBuilder();
        for (int i = 0; i < currentDifficulty; i++) {
            targetBuilder.append("0");
        }
        String targetPrefix = targetBuilder.toString();

        if (hash.startsWith(targetPrefix)) {
            blockSolved = true;
            System.out.println("\n🎉 WINNER! Miner " + miner.getMinerName() + " successfully solved the block!");
            System.out.println("Winning Hash: " + hash);

            // Avisamos a toda la red de que el minado ha terminado
            broadcast("end|" + miner.getMinerName());
        } else {
            // Hizo trampas o el hash está mal calculado
            miner.sendMessage("sol_result|invalid");
        }
    }
}
