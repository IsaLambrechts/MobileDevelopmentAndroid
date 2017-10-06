package be.ap.edu.mobiledevelopmentexamen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;

public class DetailActivity extends AppCompatActivity {

    private TextView textView;
    private TextView textView1;
    private Button button;
    private Serializable text;
    private Serializable text1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textView = (TextView) findViewById(R.id.editText);
        text = getIntent().getSerializableExtra("longitude");
        textView.setText(text.toString());

        textView1 = (TextView) findViewById(R.id.editText2);
        text1 = getIntent().getSerializableExtra("latitude");
        textView1.setText(text1.toString());

        button = (Button) findViewById(R.id.button);

    }
}
