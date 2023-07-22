package programy.piotr.lenart.com.serverclient;

import androidx.appcompat.app.AppCompatActivity;
import programy.piotr.lenart.com.serverclient.basic.ICallback1;
import network.Client;

import android.content.Intent;
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

                ConnectionTask conn = new ConnectionTask(address, Integer.parseInt(port));
                conn.setOnConnectionTask(new ICallback1<Client>() {
                    @Override
                    public void run(Client client) {
                        Connection.getInstance().setUpNewConnection(client);
                        Log.d("xxx", Connection.getInstance().client.toString());
                        Intent intent = new Intent(MainActivity.this, ServicesListActivity.class);
                        startActivity(intent);
                    }
                });
                conn.execute();
            }
        });
    }

    class ConnectionTask extends AsyncTask<String, Object, Void> {
        String address;
        int port;
        Client client;
        ICallback1<Client> onConnectionTask;
        public ConnectionTask(String address, int port){
            super();
            this.address = address;
            this.port = port;
        }

        public void setOnConnectionTask(ICallback1<Client> ConnectionTaskCallback) {
            this.onConnectionTask = ConnectionTaskCallback;
        }

        @Override
        protected Void doInBackground(String[] args) {
            try {
                Socket socket = new Socket(address, port);
                client = new Client(socket);
                client.listen();
                client.on("connection", new ICallback1<Object>() {
                    @Override
                    public void run(Object ignored) {
                        onConnectionTask.run(client);
                    }
                });
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
