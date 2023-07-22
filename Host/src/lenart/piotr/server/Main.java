package lenart.piotr.server;

import lenart.piotr.server.manager.MainReceiver;
import lenart.piotr.server.settings.Settings;
import network.Server;

public class Main {

    public static Settings.Data settings;

    public static void main(String[] args) {
        Settings settings = new Settings();
        if (!settings.isCorrect()) {
            System.out.println("Cannot load settings");
            return;
        }

        Settings.Data d = settings.getData();
        Main.settings = d;
        Server server = new Server(d.getPort(), MainReceiver::new);
        server.run();
    }
}
