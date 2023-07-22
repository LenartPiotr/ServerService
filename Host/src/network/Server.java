package network;

import lenart.piotr.server.basic.ICallback1;
import lenart.piotr.server.manager.MainReceiver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    Thread thread;
    ServerSocket serverSocket;

    ICallback1<Client> onNewConnection;

    List<Socket> sockets;

    public Server(int port, ICallback1<Client> onNewConnection){
        this.onNewConnection = onNewConnection;
        sockets = new ArrayList<>();
        thread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("Server start to listen on port " + port);
                try {
                    while (!thread.isInterrupted()) {
                        Socket socket = serverSocket.accept();
                        Client client = new Client(socket);
                        client.listen();
                        onNewConnection.run(client);
                    }
                } catch (IOException ignored) { }
                System.out.println("Server stopped");
            } catch (IOException ignored) {
                System.out.println("Cannot create server");
            }
        });
    }

    public void run() {
        thread.start();
    }

    public void stop() {
        thread.interrupt();
        try {
            serverSocket.close();
        } catch (IOException ignored) { }
        for (Socket s : sockets) {
            try {
                s.close();
            } catch (Exception ignored) { }
        }
    }
}