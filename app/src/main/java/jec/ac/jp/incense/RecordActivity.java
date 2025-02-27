package jec.ac.jp.incense;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    private String spName = "MeditationRecords";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        EdgeToEdge.enable(this);

        // 檢查是否已登錄
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "ログインしてください", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RecordActivity.this, Account.class);
            startActivity(intent);
            finish();
            return;
        } else {
            spName = "MeditationRecords_" + currentUser.getUid();
        }

        Button homeButton = findViewById(R.id.btn_come);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecordActivity.this, TimerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        listViewRecords = findViewById(R.id.listViewRecords);
        listViewRecords.setOnItemLongClickListener((parent, view, position, id) -> {
            String selectedRecord = (String) parent.getItemAtPosition(position);
            String timestamp = selectedRecord.split(" - ")[0];
            new android.app.AlertDialog.Builder(RecordActivity.this)
                    .setTitle("記録の削除")
                    .setMessage("本当にこの記録を削除しますか？")
                    .setPositiveButton("削除", (dialog, which) -> {
                        deleteMeditationRecord(timestamp);
                        loadMeditationRecords();
                    })
                    .setNegativeButton("キャンセル", (dialog, which) -> dialog.dismiss())
                    .show();
            return true;
        });

        loadMeditationRecords();
    }

    private void deleteMeditationRecord(String timestamp) {
        SharedPreferences sharedPreferences = getSharedPreferences(spName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.contains(timestamp)) {
            editor.remove(timestamp);
            editor.apply();
            Toast.makeText(this, "記録が削除されました: " + timestamp, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "削除する記録が見つかりません", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMeditationRecords() {
        SharedPreferences sharedPreferences = getSharedPreferences(spName, Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPreferences.getAll();

        ArrayList<String> recordList = new ArrayList<>();
        ArrayList<RecordEntry> sortedRecords = new ArrayList<>();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            try {
                Date date = dateFormat.parse(entry.getKey());
                if (date != null) {
                    sortedRecords.add(new RecordEntry(date, entry.getKey() + " - " + entry.getValue().toString()));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(sortedRecords, new Comparator<RecordEntry>() {
            @Override
            public int compare(RecordEntry o1, RecordEntry o2) {
                return o2.date.compareTo(o1.date);
            }
        });

        for (RecordEntry entry : sortedRecords) {
            recordList.add(entry.recordText);
        }

        if (recordList.isEmpty()) {
            recordList.add("記録がありません");
            Toast.makeText(this, "記録が見つかりません", Toast.LENGTH_SHORT).show();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recordList);
        listViewRecords.setAdapter(adapter);
    }

    private static class RecordEntry {
        Date date;
        String recordText;
        public RecordEntry(Date date, String recordText) {
            this.date = date;
            this.recordText = recordText;
        }
    }
}
