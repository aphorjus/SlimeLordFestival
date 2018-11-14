package game.server;

import game.Game;
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
                    handleClientAction(clients[i], res);
                }
            }
        }
    }

    void handleClientAction(ClientHandler client, JSONObject req) {
        String type = req.getString("type");

        System.out.println("handle action");
        System.out.println(type);

        if (type.equals(Game.Api.POST)) {
            System.out.println("post");
            Post(client, req);
        }
    }

    void Post(ClientHandler client, JSONObject req) {
        if (req.getString("actionType").equals(Game.Action.MESSAGE)) {
            System.out.println("sent");
            sendJson(req);
        }
    }

    void sendJson(JSONObject res) {
        for (int i = 0; i < clients.length; i++) {
            clients[i].write(res);
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
