package programy.piotr.lenart.com.serverclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class ReceiptsMainActivity extends AppCompatActivity {

    private LinearLayout newReceiptButton;
    private LinearLayout listReceiptsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipts_main);

        newReceiptButton = findViewById(R.id.buttonNewReceipt);
        listReceiptsButton = findViewById(R.id.buttonListReceipts);

        listReceiptsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceiptsMainActivity.this, ReceiptsListActivity.class);
                startActivity(intent);
            }
        });
    }
}
