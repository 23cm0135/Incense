package jec.ac.jp.incense;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Map;

public class RecordActivity extends AppCompatActivity {
    private ListView listViewRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        // 启用 Edge-to-Edge 显示效果
        EdgeToEdge.enable(this);

        listViewRecords = findViewById(R.id.listViewRecords);
        SharedPreferences sharedPreferences = getSharedPreferences("MeditationRecords", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();

        ArrayList<String> recordList = new ArrayList<>();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            recordList.add(entry.getKey() + " - " + entry.getValue().toString());
        }

        // 如果没有记录，显示 "没有记录"
        if (recordList.isEmpty()) {
            recordList.add("記録がありません");
            Toast.makeText(this, "記録が見つかりません", Toast.LENGTH_SHORT).show(); // 提示用户
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recordList);
        listViewRecords.setAdapter(adapter);
    }
}
