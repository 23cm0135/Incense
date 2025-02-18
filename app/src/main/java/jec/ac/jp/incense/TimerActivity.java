package jec.ac.jp.incense;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.*;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;


public class TimerActivity extends AppCompatActivity {
    private EditText etTime;
    private Button btnStart, btnStop, btnViewRecords;
    private Spinner spinnerMusic;
    private TextView tvCountdown;
    private ProgressBar progressBar;
    private CountDownTimer countDownTimer;
    private boolean isCounting = false;
    private int selectedMusicResId = R.raw.music1; // 默认音乐
    private long totalTimeInMillis;
    private Handler uiHandler = new Handler(Looper.getMainLooper());
    private View breathingCircle; // 呼吸动画的视图

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        EdgeToEdge.enable(this);

        etTime = findViewById(R.id.etTime);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnViewRecords = findViewById(R.id.btnViewRecords);
        spinnerMusic = findViewById(R.id.spinnerMusic);
        tvCountdown = findViewById(R.id.tvCountdown);
        progressBar = findViewById(R.id.progressBar);
        breathingCircle = findViewById(R.id.breathingCircle); // 获取呼吸引导动画的 View

        // 设置音乐选择
        String[] musicOptions = {"雨", "Relax", "Forest Lullaby"};
        final int[] musicResIds = {R.raw.music1, R.raw.relax, R.raw.forest_lullaby};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, musicOptions);
        spinnerMusic.setAdapter(adapter);
        spinnerMusic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMusicResId = musicResIds[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnStart.setOnClickListener(v -> startCountdown());
        btnStop.setOnClickListener(v -> stopCountdown());
        btnViewRecords.setOnClickListener(v -> {
            Intent intent = new Intent(TimerActivity.this, RecordActivity.class);
            startActivity(intent);
        });
    }

    private void startCountdown() {
        if (isCounting) return;

        String inputTime = etTime.getText().toString().trim();
        if (inputTime.isEmpty()) {
            Toast.makeText(this, "時間を入力してください！", Toast.LENGTH_SHORT).show();
            return;
        }

        totalTimeInMillis = Long.parseLong(inputTime) * 60 * 1000;
        if (totalTimeInMillis <= 0) return;

        isCounting = true;
        btnStop.setVisibility(View.VISIBLE);
        btnStart.setVisibility(View.GONE);
        etTime.setEnabled(false);
        progressBar.setMax((int) (totalTimeInMillis / 1000));
        progressBar.setProgress((int) (totalTimeInMillis / 1000));

        startBreathingAnimation(); // 开始呼吸引导动画

        countDownTimer = new CountDownTimer(totalTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsLeft = millisUntilFinished / 1000;
                uiHandler.post(() -> {
                    tvCountdown.setText(formatTime(secondsLeft));
                    progressBar.setProgress((int) secondsLeft);
                });
            }

            @Override
            public void onFinish() {
                uiHandler.post(() -> {
                    tvCountdown.setText("00:00");
                    progressBar.setProgress(0);
                    isCounting = false;
                    btnStart.setVisibility(View.VISIBLE);
                    btnStop.setVisibility(View.GONE);
                    etTime.setEnabled(true);
                    saveMeditationRecord(inputTime);
                });

                sendNotification();
                stopService(new Intent(TimerActivity.this, CountdownTimerService.class));
            }
        }.start();

        Intent serviceIntent = new Intent(this, CountdownTimerService.class);
        serviceIntent.putExtra("MUSIC_RES_ID", selectedMusicResId);
        startService(serviceIntent);
    }

    private void stopCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isCounting = false;
        tvCountdown.setText("00:00");
        progressBar.setProgress(0);
        btnStart.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.GONE);
        etTime.setEnabled(true);
        stopService(new Intent(this, CountdownTimerService.class));
    }

    private String formatTime(long seconds) {
        long min = seconds / 60;
        long sec = seconds % 60;
        return String.format("%02d:%02d", min, sec);
    }

    private void sendNotification() {
        Toast.makeText(this, "カウントダウンは終わった！", Toast.LENGTH_LONG).show();
    }

    private void startBreathingAnimation() {
        ScaleAnimation animation = new ScaleAnimation(
                1.0f, 1.5f, 1.0f, 1.5f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        animation.setDuration(4000);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setRepeatCount(Animation.INFINITE);
        breathingCircle.startAnimation(animation);
    }

    private void saveMeditationRecord(String minutes) {
        SharedPreferences sharedPreferences = getSharedPreferences("MeditationRecords", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        editor.putString(timestamp, minutes + " 分");
        editor.apply();
    }
}
