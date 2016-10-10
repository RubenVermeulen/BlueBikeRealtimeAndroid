package be.rubenvermeulen.android.bluebikert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Map<String, String> keyValues = new TreeMap<>();

        keyValues.put("Gent-Sint-Pieters", "https://datatank.stad.gent/4/mobiliteit/bluebikedeelfietsensintpieters.json");
        keyValues.put("Gent-Dampoort", "https://datatank.stad.gent/4/mobiliteit/bluebikedeelfietsendampoort.json");

        ListView listView = (ListView) findViewById(R.id.list);
        ListAdapter adapter = new ArrayAdapter<>(this, R.layout.main_list_item, R.id.value, new ArrayList<>(keyValues.keySet()));

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.value);
                String name = tv.getText().toString();

                startActivityDetails(
                        name,
                        keyValues.get(name)
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
