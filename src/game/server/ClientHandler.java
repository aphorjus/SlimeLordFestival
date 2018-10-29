package game.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread {
    final DataInputStream input;
    final DataOutputStream output;
    final Socket socket;

    public ClientHandler(Socket socket, DataInputStream input, DataOutputStream output) {
        this.socket = socket;
        this.input = input;
        this.output = output;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String received = input.readUTF();
                System.out.println(received);

                // parse string into json
                // emit json to game
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
