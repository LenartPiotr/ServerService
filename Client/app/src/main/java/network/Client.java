package network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import programy.piotr.lenart.com.serverclient.basic.ICallback0;
import programy.piotr.lenart.com.serverclient.basic.ICallback1;

public class Client {
    private final Socket socket;
    private final Map<String, ICallback1<Object>> listeners;

    private Thread listeningThread;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;

    private ICallback0 disconnectCallback;
    private boolean active;

    public Client(final Socket socket) throws IOException {
        this.socket = socket;
        this.listeners = new HashMap<>();

        outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(new Message("connection", true));
        outputStream.flush();
        inputStream = new ObjectInputStream(socket.getInputStream());
        active = true;

        this.listeningThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!listeningThread.isInterrupted()) {
                    try {
                        Object obj = inputStream.readObject();
                        Message message = (Message) obj;
                        // System.out.println("Received (" + message.key + "): " + message.object.toString());
                        if (listeners.containsKey(message.key)) {
                            listeners.get(message.key).run(message.object);
                        }
                    } catch (IOException e) {
                        Log.d("xxx", "is closed " + socket.isClosed());
                        Log.d("xxx", "is connected " + socket.isConnected());
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
                Client.this.stop();
            }
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
        AsyncInvokeTask task = new AsyncInvokeTask(outputStream, message, new ICallback1<IOException>() {
            @Override
            public void run(IOException arg) {
                arg.printStackTrace();
                if (active) stop();
            }
        });
        task.execute();
    }

    class AsyncInvokeTask extends AsyncTask<String, Object, Void> {

        ObjectOutputStream stream;
        Message message;
        ICallback1<IOException> onException;

        public AsyncInvokeTask(ObjectOutputStream stream, Message message, ICallback1<IOException> exceptionCallback){
            super();
            this.stream = stream;
            this.message = message;
            onException = exceptionCallback;
        }

        @Override
        protected Void doInBackground(String[] args) {
            try {
                stream.writeObject(message);
            } catch (IOException e) {
                onException.run(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
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
