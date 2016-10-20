package be.rubenvermeulen.android.bluebikert;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchableActivity extends AppCompatActivity {

    @BindView(R.id.empty) TextView tvEmpty;
    @BindView(R.id.list) ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            // Back button action bar

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // Title action bar
            getSupportActionBar().setTitle(getResources().getString(R.string.results));
        }

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            showResults(query);
        }
    }

    private void showResults(String query) {
        final Map<String, String> keyValues = Locations.filtered(query);

        if (keyValues.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
        }
        else {
            ListAdapter adapter = new ArrayAdapter<>(this, R.layout.main_list_item, R.id.value, new ArrayList<>(keyValues.keySet()));

            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView tv = (TextView) view.findViewById(R.id.value);
                    String name = tv.getText().toString();

                    switchActivity(name, keyValues.get(name));
                }
            });
        }
    }

    private void switchActivity(String name, String url) {
        Intent intent = new Intent(SearchableActivity.this, DetailsActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("url", url);

        startActivity(intent);
    }
}
