package lenart.piotr.server.manager;

import lenart.piotr.server.receipts.ReceiptsManager;
import network.Client;

import java.util.Timer;
import java.util.TimerTask;

public class MainReceiver {
    public MainReceiver(Client client) {
        client.on("connection", ignored -> {
            new ReceiptsManager(client);
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                client.stop();
            }
        }, 60 * 60 * 1000);
    }
}
