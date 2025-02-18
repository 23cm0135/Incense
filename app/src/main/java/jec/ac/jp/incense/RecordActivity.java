package jec.ac.jp.incense;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Map;

public class RecordActivity extends AppCompatActivity {
    private ListView listViewRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        listViewRecords = findViewById(R.id.listViewRecords);
        SharedPreferences sharedPreferences = getSharedPreferences("MeditationRecords", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();

        ArrayList<String> recordList = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            recordList.add(entry.getKey() + " - " + entry.getValue().toString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recordList);
        listViewRecords.setAdapter(adapter);
    }
}
