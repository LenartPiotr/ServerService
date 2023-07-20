package programy.piotr.lenart.com.serverclient;

import androidx.appcompat.app.AppCompatActivity;
import programy.piotr.lenart.com.serverclient.net.Client;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
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

                new Connection().execute(address, port);
            }
        });
    }

    class Connection extends AsyncTask<String, Object, Integer> {
        @Override
        protected Integer doInBackground(String[] args) {
            try {
                Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
                Client client = new Client(socket);
                for (int i=0; i<5; i++) {
                    client.writer.println(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
    }
}
