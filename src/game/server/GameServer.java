package game.server;

import game.api.GameApiRequest;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    ServerSocket server;
    ClientHandler[] clients;
    int turnId;

    public GameServer(int port, int playerCount) throws Exception {
        clients = new ClientHandler[playerCount];
        server = new ServerSocket(port);

        System.out.println("Server started");

        for (int i = 0; i < playerCount; i++) {
            System.out.println("Waiting for " + (playerCount - i) + " clients...");

            Socket clientSocket = server.accept();
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());

            clients[i] = new ClientHandler(i, clientSocket, input, output);

            System.out.println("Client accepted");
        }

        turnId = 0;

        for (int i = 0; i < playerCount; i++) {
            clients[i].start();
        }

        while (true) {
            for (int i = 0; i < playerCount; i++) {
                JSONObject res;
                while ((res = clients[i].getActions().poll()) != null) {
                    handleRequest(clients[i], new GameApiRequest(res));
                }
            }
        }
    }

    void handleRequest(ClientHandler client, GameApiRequest req) {
        sendToAll(req);
    }

    void sendToAll(GameApiRequest req) {
        for (int i = 0; i < clients.length; i++) {
            clients[i].write(req.toJson());
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new Exception("Please provide a valid port number and player count");
        }

        int portNumber = Integer.parseInt(args[0]);
        int expectedPlayerCount = Integer.parseInt(args[1]);
        new GameServer(portNumber, expectedPlayerCount);
    }
}
