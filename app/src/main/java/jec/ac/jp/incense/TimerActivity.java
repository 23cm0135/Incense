package jec.ac.jp.incense;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class TimerActivity extends AppCompatActivity {
    private EditText etTime;
    private Button btnStart, btnStop;
    private Spinner spinnerMusic;
    private TextView tvCountdown;
    private ProgressBar progressBar;
    private CountDownTimer countDownTimer;
    private boolean isCounting = false;
    private int selectedMusicResId = R.raw.music1; // 默认音乐
    private long totalTimeInMillis;  // 总倒计时时间
    private Handler uiHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        etTime = findViewById(R.id.etTime);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        spinnerMusic = findViewById(R.id.spinnerMusic);
        tvCountdown = findViewById(R.id.tvCountdown);
        progressBar = findViewById(R.id.progressBar);

        // 设置音乐选择

        String[] musicOptions = {"雨", "Relax", "Forest Lullaby"};
        final int[] musicResIds = {R.raw.music1,R.raw.relax,R.raw.forest_lullaby};
 7ef077858f4a5a65b42a277829cdbd4a455adf86
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
    }

    private void startCountdown() {
        if (isCounting) return;

        String inputTime = etTime.getText().toString().trim();
        if (inputTime.isEmpty()) {
            Toast.makeText(this, "時間を入力してください！", Toast.LENGTH_SHORT).show(); // 输入提示改为日文
            return;
        }

        // 将用户输入的分钟数转换为毫秒
        totalTimeInMillis = Long.parseLong(inputTime) * 60 * 1000; // 乘以 60 秒和 1000 毫秒
        if (totalTimeInMillis <= 0) return;

        isCounting = true;
        btnStop.setVisibility(View.VISIBLE);
        btnStart.setVisibility(View.GONE);
        etTime.setEnabled(false);
        progressBar.setMax((int) (totalTimeInMillis / 1000));
        progressBar.setProgress((int) (totalTimeInMillis / 1000));

        countDownTimer = new CountDownTimer(totalTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsLeft = millisUntilFinished / 1000;
                uiHandler.post(() -> {
                    tvCountdown.setText(formatTime(secondsLeft)); // 格式化时间显示
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
                });

                sendNotification();
                stopService(new Intent(TimerActivity.this, CountdownTimerService.class)); // 停止音乐播放
            }
        }.start();

        // 立即播放音乐
        Intent serviceIntent = new Intent(this, CountdownTimerService.class);
        serviceIntent.putExtra("MUSIC_RES_ID", selectedMusicResId);
        startService(serviceIntent); // 立即启动音乐服务
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

        // 停止音乐
        stopService(new Intent(this, CountdownTimerService.class));
    }

    private String formatTime(long seconds) {
        long min = seconds / 60; // 分钟
        long sec = seconds % 60; // 秒
        return String.format("%02d:%02d", min, sec); // 格式化为 00:00
    }

    private void sendNotification() {
        Toast.makeText(this, "カウントダウンは終わった！", Toast.LENGTH_LONG).show();
    }
}
