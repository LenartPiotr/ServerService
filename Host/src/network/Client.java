package network;

import lenart.piotr.server.basic.ICallback0;
import lenart.piotr.server.basic.ICallback1;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Client {
    private final Socket socket;

    private final Map<String, ICallback1<Object>> listeners;

    private Thread listeningThread;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;

    private ICallback0 disconnectCallback;
    private boolean active;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        this.listeners = new HashMap<>();

        outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(new Message("connection", true));
        outputStream.flush();
        inputStream = new ObjectInputStream(socket.getInputStream());
        active = true;

        this.listeningThread = new Thread(() -> {
            while (!listeningThread.isInterrupted()) {
                try {
                    Object obj = inputStream.readObject();
                    Message message = (Message) obj;
                    // System.out.println("Received (" + message.key + "): " + message.object.toString());
                    if (listeners.containsKey(message.key)) {
                        listeners.get(message.key).run(message.object);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    break;
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    break;
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
            stop();
        });
    }

    public void on(String key, ICallback1<Object> callback) {
        listeners.put(key, callback);
    }

    public void off(String key) {
        listeners.remove(key);
    }

    public void invoke(String key, Object obj) {
        Message message = new Message(key, obj);
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            if (active) {
                stop();
            }
        }
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

    public void listen() {
        this.listeningThread.start();
    }

    private static class Message implements Serializable {
        public String key;
        public Object object;
        public Message(String key, Object object) {
            this.key = key;
            this.object = object;
        }
    }
}
