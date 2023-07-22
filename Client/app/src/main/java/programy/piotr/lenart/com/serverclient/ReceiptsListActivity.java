package programy.piotr.lenart.com.serverclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import programy.piotr.lenart.com.serverclient.basic.ICallback1;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class ReceiptsListActivity extends AppCompatActivity {

    private EditText input;
    private LinearLayout scrollView;
    private List<ListElement> elements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipts_list);

        input = findViewById(R.id.editTextSearchForReceipts);
        scrollView = findViewById(R.id.scrollViewReceipts);

        elements = new ArrayList<>();

        Connection.getInstance().client.on("receipts_listAll", new ICallback1<Object>() {
            @Override
            public void run(Object arg) {
                HashMap<String, Object>[] data = (HashMap<String, Object>[])arg;
                for (HashMap<String, Object> fileData : data) {
                    elements.add(new ListElement(fileData));
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        redraw();
                    }
                });
            }
        });

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                redraw();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        Connection.getInstance().client.invoke("receipts_listAll", 1);
    }

    void redraw() {
        scrollView.removeAllViews();
        Pattern pattern = Pattern.compile(".*" + input.getText().toString() + ".*", Pattern.CASE_INSENSITIVE);
        for (ListElement e : elements) {
            e.draw(pattern);
        }
    }

    private static abstract class MyRunnable implements Runnable {
        protected Object args;
        public MyRunnable(Object params) {
            args = params;
        }
    }

    private class ListElement {
        private HashMap<String, Object> data;

        public ListElement(HashMap<String, Object> data) {
            this.data = data;
        }

        private String getName() {
            String text = (String)data.get("name");
            int lastDotIndex = text.lastIndexOf(".");
            if (lastDotIndex != -1) {
                return text.substring(0, lastDotIndex);
            }
            return text;
        }

        public void draw(Pattern input) {

            if (!input.matcher(getName()).matches()) {
                Log.d("xxx", "no matches");
                return;
            }

            Context context = ReceiptsListActivity.this;
            LinearLayout content = new LinearLayout(context);
            content.setLayoutParams(new LinearLayout.LayoutParams(800, 150));
            content.setBackgroundResource(R.drawable.border);

            content.setBackgroundTintList(getResources().getColorStateList(R.color.color_dynamic_list_state));
            content.setGravity(Gravity.CENTER);
            content.setOrientation(LinearLayout.HORIZONTAL);

            TextView tw = new TextView(context);
            tw.setTextSize(24);
            tw.setText(getName());
            content.addView(tw);
            scrollView.addView(content);

            LinearLayout space = new LinearLayout(context);
            space.setLayoutParams(new LinearLayout.LayoutParams(600, 100));
            scrollView.addView(space);
        }
    }
}
