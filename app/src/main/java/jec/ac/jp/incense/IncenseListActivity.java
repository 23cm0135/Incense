package jec.ac.jp.incense;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IncenseListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private IncenseAdapter adapter;
    private List<Incense> incenseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incense_list);

        // 获取筛选后的数据
        incenseList = getIntent().getParcelableArrayListExtra("incenseList");

        recyclerView = findViewById(R.id.incense_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IncenseAdapter(this, incenseList);
        recyclerView.setAdapter(adapter);
    }
}
