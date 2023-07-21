package lenart.piotr.server.manager;

import network.Client;

import java.io.IOException;

public class MainReceiver {
    public MainReceiver(Client client) throws IOException {
        client.on("connection", ignored -> {
            System.out.println("New connection");
        });
        client.on("test", System.out::println);
    }
}
