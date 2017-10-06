package be.ap.edu.mobiledevelopmentexamen;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.Serializable;

public class DetailActivity extends AppCompatActivity {

    private TextView textView;
    private TextView textView1;
    private TextView omschrijving_text;
    private Button button;
    private Serializable text;
    private Serializable text1;
    private MapSQLiteHelper helper;

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

        helper = new MapSQLiteHelper(this);

        final Context context = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                omschrijving_text = (TextView) findViewById(R.id.omschrijving_text);
                String omschrijving = omschrijving_text.getText().toString();
                Log.d("SQLITE", omschrijving);
                if(!(omschrijving.matches(""))) {
                    helper.addContact(Double.parseDouble(text.toString()), Double.parseDouble(text1.toString()), omschrijving);
                    System.out.println(helper.getTableAsString());

                    Intent i = new Intent(context, MapActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(getApplicationContext(),"Voeg een omschrijving toe", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}
