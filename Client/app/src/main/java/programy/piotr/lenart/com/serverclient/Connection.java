package programy.piotr.lenart.com.serverclient;

import network.Client;

public class Connection {
    private static Connection instance;
    public static Connection getInstance() {
        if (instance == null) {
            instance = new Connection();
        }
        return instance;
    }

    public Client client;
    public void setUpNewConnection(Client client) {
        this.client = client;
    }
}
