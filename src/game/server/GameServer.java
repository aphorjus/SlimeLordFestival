package game.server;

import game.api.GameApi;
import game.api.GameApiRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer extends Thread {
    ServerSocket server;
    ClientHandler[] clients;
    int playerCount;
    int port;

    public GameServer(int port, int playerCount) throws Exception {
        this.clients = new ClientHandler[playerCount];
        this.server = new ServerSocket(port);
        this.port = port;
        this.playerCount = playerCount;
    }

    void handleRequest(ClientHandler client, GameApiRequest req) {
        sendToAllClients(client.id, req);
    }

    void sendToAllClients(int senderId, GameApiRequest req) {
        req.body.put("senderId", senderId);

        for (int i = 0; i < clients.length; i++) {
            clients[i].write(req.toJson());
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Server started");

            for (int i = 0; i < playerCount; i++) {
                System.out.println("Waiting for " + (playerCount - i) + " clients...");

                Socket clientSocket = server.accept();
                DataInputStream input = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
                clients[i] = new ClientHandler(i, clientSocket, input, output);
                clients[i].start();

                String[] clientNames = new String[i + 1];
                for (int j = 0; j < clientNames.length; j++) {
                    clientNames[j] = clients[j].id + "";
                }

                JSONObject body = new JSONObject();
                body.put("clientNames", new JSONArray(clientNames));
                body.put("playerCount", playerCount);

                for (int j = 0; j < clientNames.length; j++) {
                    clients[j].write(new GameApiRequest(GameApi.LobbyClientListUpdate, body).toJson());
                }

                System.out.println("Client accepted");
            }


            String[] clientNames = new String[clients.length];
            for (int j = 0; j < clients.length; j++) {
                clientNames[j] = clients[j].id + "";
            }

            JSONObject body = new JSONObject();
            body.put("clientNames", new JSONArray(clientNames));
            body.put("playerCount", playerCount);
            sendToAllClients(-1, new GameApiRequest(GameApi.LobbyClientListUpdate, body));

            while (true) {
                for (int i = 0; i < playerCount; i++) {
                    JSONObject res;
                    while ((res = clients[i].getActions().poll()) != null) {
                        handleRequest(clients[i], new GameApiRequest(res));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new Exception("Please provide a valid port number and player count");
        }

        int portNumber = Integer.parseInt(args[0]);
        int expectedPlayerCount = Integer.parseInt(args[1]);
        GameServer gs = new GameServer(portNumber, expectedPlayerCount);
        gs.run();
    }
}
