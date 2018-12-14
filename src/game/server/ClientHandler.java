package game.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.LinkedList;

import org.json.*;

public class ClientHandler extends Thread {
    int id;
    DataInputStream input;
    DataOutputStream output;
    Socket socket;
    LinkedList<JSONObject> gameActions;

    public ClientHandler(int id, Socket socket, DataInputStream input, DataOutputStream output) {
        this.id = id;
        this.socket = socket;
        this.input = input;
        this.output = output;
        this.gameActions = new LinkedList<>();
    }

    public boolean hasActions() {
        return gameActions.size() > 0;
    }

    public LinkedList<JSONObject> getActions() {
        return gameActions;
    }

    public void write(JSONObject json) {
        try {
            output.writeUTF(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                while (input.available() > 0) {
                    gameActions.push(new JSONObject(input.readUTF()));
                }
                sleep(15);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }

        }

        try {
            input.close();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
