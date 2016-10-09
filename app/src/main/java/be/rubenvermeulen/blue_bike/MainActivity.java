package be.rubenvermeulen.blue_bike;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private TextView tvJson;

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // buttons
        Button locationOne = (Button) findViewById(R.id.locationOne);
        Button locationTwo = (Button) findViewById(R.id.locationTwo);

        // event listeners
        locationOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityDetails(
                        getResources().getString(R.string.gent_sint_pieters),
                        "https://datatank.stad.gent/4/mobiliteit/bluebikedeelfietsensintpieters.json"
                );
            }
        });

        locationTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityDetails(
                        getResources().getString(R.string.gent_dampoort),
                        "https://datatank.stad.gent/4/mobiliteit/bluebikedeelfietsendampoort.json"
                );
            }
        });
    }

    private void startActivityDetails(String name, String url) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("url", url);

        startActivity(intent);
    }
}
