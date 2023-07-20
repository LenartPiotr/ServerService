package lenart.piotr.server.manager;

import lenart.piotr.server.net.Client;

import java.io.IOException;

public class MainReceiver {
    public MainReceiver(Client client) throws IOException {
        while (true) {
            System.out.println(client.reader.readLine());
        }
    }
}
