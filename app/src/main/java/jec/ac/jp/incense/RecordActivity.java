package jec.ac.jp.incense;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class RecordActivity extends AppCompatActivity {
    private ListView listViewRecords;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        EdgeToEdge.enable(this);

        Button homeButton = findViewById(R.id.btn_come);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecordActivity.this, TimerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        listViewRecords = findViewById(R.id.listViewRecords);
        SharedPreferences sharedPreferences = getSharedPreferences("MeditationRecords", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();
        listViewRecords.setOnItemLongClickListener((parent, view, position, id) -> {
            // **获取用户长按的记录**
            String selectedRecord = (String) parent.getItemAtPosition(position);

            // **从记录中提取时间戳**
            String timestamp = selectedRecord.split(" - ")[0]; // 记录格式: "yyyy-MM-dd HH:mm - 其他信息"

            // **弹出确认对话框**
            new androidx.appcompat.app.AlertDialog.Builder(RecordActivity.this)
                    .setTitle("記録の削除")
                    .setMessage("本当にこの記録を削除しますか？")
                    .setPositiveButton("削除", (dialog, which) -> {
                        deleteMeditationRecord(timestamp); // **调用删除方法**
                        loadMeditationRecords(); // **刷新列表**
                    })
                    .setNegativeButton("キャンセル", (dialog, which) -> dialog.dismiss())
                    .show();

            return true; // **返回 true，表示事件已处理**
        });


        ArrayList<String> recordList = new ArrayList<>();
        ArrayList<RecordEntry> sortedRecords = new ArrayList<>();

        // **解析时间戳并存入临时列表**
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            try {
                Date date = dateFormat.parse(entry.getKey()); // 解析时间戳
                if (date != null) {
                    sortedRecords.add(new RecordEntry(date, entry.getKey() + " - " + entry.getValue().toString()));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // **按时间从最新到最旧排序**
        Collections.sort(sortedRecords, new Comparator<RecordEntry>() {
            @Override
            public int compare(RecordEntry o1, RecordEntry o2) {
                return o2.date.compareTo(o1.date); // 降序排列（最新的排在最上面）
            }
        });

        // **将排序后的记录添加到 `recordList`**
        for (RecordEntry entry : sortedRecords) {
            recordList.add(entry.recordText);
        }

        // **如果没有记录，显示 "記録がありません"**
        if (recordList.isEmpty()) {
            recordList.add("記録がありません");
            Toast.makeText(this, "記録が見つかりません", Toast.LENGTH_SHORT).show();
        }

        // **显示记录**
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recordList);
        listViewRecords.setAdapter(adapter);
    }
    private void deleteMeditationRecord(String timestamp) {
        SharedPreferences sharedPreferences = getSharedPreferences("MeditationRecords", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (sharedPreferences.contains(timestamp)) {
            editor.remove(timestamp); // **删除特定时间戳的冥想记录**
            editor.apply();
            Toast.makeText(this, "記録が削除されました: " + timestamp, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "削除する記録が見つかりません", Toast.LENGTH_SHORT).show();
        }
    }
    private void loadMeditationRecords() {
        SharedPreferences sharedPreferences = getSharedPreferences("MeditationRecords", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();

        ArrayList<String> recordList = new ArrayList<>();
        ArrayList<RecordEntry> sortedRecords = new ArrayList<>();

        // **解析时间戳并存入临时列表**
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            try {
                Date date = dateFormat.parse(entry.getKey()); // 解析时间戳
                if (date != null) {
                    sortedRecords.add(new RecordEntry(date, entry.getKey() + " - " + entry.getValue().toString()));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // **按时间从最新到最旧排序**
        Collections.sort(sortedRecords, (o1, o2) -> o2.date.compareTo(o1.date));

        // **将排序后的记录添加到 `recordList`**
        for (RecordEntry entry : sortedRecords) {
            recordList.add(entry.recordText);
        }

        // **如果没有记录，显示 "記録がありません"**
        if (recordList.isEmpty()) {
            recordList.add("記録がありません");
            Toast.makeText(this, "記録が見つかりません", Toast.LENGTH_SHORT).show();
        }

        // **显示记录**
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recordList);
        listViewRecords.setAdapter(adapter);
    }


    // **记录类**
    private static class RecordEntry {
        Date date;
        String recordText;

        public RecordEntry(Date date, String recordText) {
            this.date = date;
            this.recordText = recordText;
        }
    }
}
