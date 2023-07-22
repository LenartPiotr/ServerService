package programy.piotr.lenart.com.serverclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class ServicesListActivity extends AppCompatActivity {

    private LinearLayout receiptButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_list);

        receiptButton = findViewById(R.id.buttonReceipt);
        receiptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServicesListActivity.this, ReceiptsMainActivity.class);
                startActivity(intent);
            }
        });
    }
}
