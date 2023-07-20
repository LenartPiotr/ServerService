package lenart.piotr.server.net;

import lenart.piotr.server.basic.ICallback0;
import lenart.piotr.server.basic.ICallback1;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Client {
    private final Socket socket;
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
        try {
            socket.close();
        } catch (IOException ignored) { }
        active = false;
    }

    private record Message(String key, Object object) implements Serializable { }
}
