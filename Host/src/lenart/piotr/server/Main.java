package lenart.piotr.server;

import lenart.piotr.server.net.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(30415);
        server.run();
    }
}
