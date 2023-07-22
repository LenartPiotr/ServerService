package lenart.piotr.server.settings;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Settings {

    private boolean correct;
    private Data data;

    public Settings() {
        correct = true;
        try {
            InputStream inputStream = new FileInputStream("./settings.yml");
            Yaml yaml = new Yaml();
            Data data = yaml.loadAs(inputStream, Data.class);
            this.data = data;
        } catch (FileNotFoundException e) {
            System.out.println("No settings file found");
            correct = false;
        } catch (Exception e) {
            e.printStackTrace();
            correct = false;
        }
    }

    public boolean isCorrect() {
        return correct;
    }

    public Data getData() {
        return data;
    }

    public static class Data {
        private int port;
        private String directory_receipt;

        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }
        public String getDirectory_receipt() { return directory_receipt; }
        public void setDirectory_receipt(String directory_receipt) { this.directory_receipt = directory_receipt; }
    }
}
