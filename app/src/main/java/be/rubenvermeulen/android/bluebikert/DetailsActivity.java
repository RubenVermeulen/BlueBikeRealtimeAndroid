package be.rubenvermeulen.android.bluebikert;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.swipeRefresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.title_details) TextView tvTitle;
    @BindView(R.id.description) TextView tvDescription;
    @BindView(R.id.lastUpdated) TextView tvLastUpdated;
    @BindView(R.id.activity_details) LinearLayout linearLayout;
    @BindView(R.id.refreshDetails) Button btnRefresh;

    private String name;
    private String url;
    private Uri gmmIntentUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        name = intent.getStringExtra("name");
        url = intent.getStringExtra("url");

        if (getSupportActionBar() != null) {
            // Back button action bar

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // Title action bar
            getSupportActionBar().setTitle(name);
        }

        initializeRefreshOnSwipe();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionRefresh:
                // User chose the "Refresh" item.
                swipeRefreshLayout.setRefreshing(true);
                reloadData();

                return true;

            case R.id.actionLocation:
                if (gmmIntentUri != null) {
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    startActivity(mapIntent);

                    return true;
                }

                return super.onOptionsItemSelected(item);

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void refresh(View view) {
        reloadData();
    }

    private void reloadData() {
        initializeData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initializeData();
    }

    public void initializeData() {
        new JSONTask(this).execute(url);
    }

    private void initializeRefreshOnSwipe() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                reloadData();
            }
        });
    }

    public String getName() {
        return name;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public boolean hasInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void failedToConnectToTheInternet() {
        Toast toast = Toast.makeText(this, getResources().getString(R.string.failed_to_connect_to_the_internet), Toast.LENGTH_LONG);
        toast.show();
    }

    public void somethingWentWrong() {
        Toast toast = Toast.makeText(this, getResources().getString(R.string.could_not_load_data), Toast.LENGTH_LONG);
        toast.show();
    }

    public void setGmmIntentUri(Uri gmmIntentUri) {
        this.gmmIntentUri = gmmIntentUri;
    }
}
