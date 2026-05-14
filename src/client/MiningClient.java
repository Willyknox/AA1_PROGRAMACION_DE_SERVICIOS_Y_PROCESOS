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
            // assign to the class attribute instead of creating a local variable
            socket = new Socket(serverAddress, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // send login message
            out.println("connect|" + minerName);
            String response = in.readLine();
            if ("ack".equals(response)) {
                System.out.println("Connected to server successfully as: " + minerName);
                // listen for jobs from the server
                listenToServer();
            }
        } catch (Exception e) {
            System.out.println("Could not connect to server: " + e.getMessage());
        }
    }

    private void listenToServer() {
        try {
            String line;
            // infinite loop listening to the server
            while ((line = in.readLine()) != null) {

                // mining request format: new_request|<difficulty>|<data>|<startSalt>|<endSalt>
                if (line.startsWith("new_request|")) {
                    String[] parts = line.split("\\|");
                    int difficulty = Integer.parseInt(parts[1]);
                    String blockData = parts[2];
                    int startSalt = parts.length > 3 ? Integer.parseInt(parts[3]) : 0;
                    int endSalt = parts.length > 4 ? Integer.parseInt(parts[4]) : Integer.MAX_VALUE;

                    System.out.println("\n--- New Mining Job Received ---");
                    System.out.println("Block Data: " + blockData);
                    System.out.println("Target Difficulty: " + difficulty + " zero(s)");
                    System.out.println("Assigned Search Bounds: [" + startSalt + " - " + (endSalt == Integer.MAX_VALUE ? "∞" : endSalt) + "]");

                    // respond with ack to confirm reception
                    out.println("ack");

                    // build the difficulty zero target string
                    StringBuilder targetPrefixBuilder = new StringBuilder();
                    for (int i = 0; i < difficulty; i++) {
                        targetPrefixBuilder.append("0");
                    }
                    String targetPrefix = targetPrefixBuilder.toString();

                    // --- brute force search loop ---
                    int salt = startSalt;
                    String hash;
                    System.out.println("Mining in progress... Please wait.");

                    long startTime = System.currentTimeMillis();
                    while (salt <= endSalt) {
                        hash = utils.HashUtils.sha256(blockData + salt);

                        if (hash.startsWith(targetPrefix)) {
                            long duration = System.currentTimeMillis() - startTime;
                            System.out.println("SUCCESS! Valid Hash found!");
                            System.out.println("Salt: " + salt);
                            System.out.println("Hash: " + hash);
                            System.out.println("Time taken: " + duration + " ms");

                            // send solution to server: sol|<salt>
                            out.println("sol|" + salt);
                            break;
                        }
                        salt++;
                    }
                } else if (line.startsWith("end|")) {
                    String winner = line.substring(4);
                    System.out.println("\n🛑 ROUND ENDED! Miner [" + winner + "] solved the block!");
                } else if (line.startsWith("sol_result|")) {
                    String result = line.substring(11);
                    System.out.println("Server verification result: " + result.toUpperCase());
                }
            }
        } catch (Exception e) {
            System.out.println("Disconnected from listening loop.");
        }
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
    }

    public static void main(String[] args) {
        MiningClient miner = new MiningClient("Miner-1");
        miner.start();
    }

}
