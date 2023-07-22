package lenart.piotr.server.receipts;

import lenart.piotr.server.Main;
import network.Client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class ReceiptsManager {

    private File[] getFiles() {
        File directory = new File(Main.settings.getDirectory_receipt());
        return directory.listFiles();
    }

    public ReceiptsManager(Client client) {
        client.on("receipts_listAll", object -> {
            Stream<HashMap<String, Object>> stream = Arrays.stream(getFiles()).map(f -> {
                HashMap<String, Object> map = new HashMap<>();
                try {
                    BasicFileAttributes attr = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
                    map.put("date", attr.creationTime().toMillis());
                } catch (IOException ignored) { }
                map.put("name", f.getName());
                return map;
            });
            List<HashMap<String, Object>> list = stream.toList();
            HashMap<String, Object>[] arr = list.toArray(new HashMap[0]);
            list.toArray(arr);
            client.invoke("receipts_listAll", arr);
        });
    }
}
