package jec.ac.jp.incense;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BrowsingHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BrowsingHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing_history);

        recyclerView = findViewById(R.id.browsing_history_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BrowsingHistoryAdapter(IncenseDetailActivity.browsingHistory, this);
        recyclerView.setAdapter(adapter);
    }
}
