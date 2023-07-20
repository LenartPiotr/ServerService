package programy.piotr.lenart.com.serverclient.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import programy.piotr.lenart.com.serverclient.basic.ICallback0;

public class Client {
    private final Socket socket;

    private Thread listeningThread;
    public final BufferedReader reader;
    public final PrintWriter writer;

    private ICallback0 disconnectCallback;
    private boolean active;

    public Client(Socket socket) throws IOException {
        this.socket = socket;

        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream());

        active = true;
    }

    public boolean isActive() {
        return active;
    }

    public void onDisconnect(ICallback0 callback) {
        this.disconnectCallback = callback;
    }

    public void stop() {
        if (!active) return;
        if (disconnectCallback != null)
            disconnectCallback.run();
        listeningThread.interrupt();
        try {
            socket.close();
        } catch (IOException ignored) { }
        active = false;
    }

    public void listen() {
        this.listeningThread.start();
    }
}