package programy.piotr.lenart.com.serverclient;

import androidx.appcompat.app.AppCompatActivity;
import programy.piotr.lenart.com.serverclient.basic.ICallback1;
import network.Client;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private EditText inputAddress;
    private EditText inputPort;
    private Button buttonConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputAddress = findViewById(R.id.inputAddress);
        inputPort = findViewById(R.id.inputPort);
        buttonConnect = findViewById(R.id.buttonConnect);

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String address = inputAddress.getText().toString();
                final String port = inputPort.getText().toString();

                Log.d("xxx", "click");

                new Connection(address, Integer.parseInt(port)).execute();
            }
        });
    }

    class Connection extends AsyncTask<String, Object, Void> {
        String address;
        int port;
        public Connection(String address, int port){
            super();
            this.address = address;
            this.port = port;
        }

        @Override
        protected Void doInBackground(String[] args) {
            Log.d("xxx", "start");
            try {
                Socket socket = new Socket(address, port);
                Client client = new Client(socket);
                client.listen();
                client.on("connection", new ICallback1<Object>() {
                    @Override
                    public void run(Object ignored) {
                        Log.d("xxx", "New connection");
                    }
                });
                for (int i=0; i<5; i++) {
                    client.invoke("test", i);
                    Thread.sleep(1000);
                    Log.d("xxx", "send " + i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
