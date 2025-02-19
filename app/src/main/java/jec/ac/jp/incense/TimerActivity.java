package jec.ac.jp.incense;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimerActivity extends AppCompatActivity {
    private EditText etTime;
    private Button btnStart, btnStop, btnViewRecords;
    private Spinner spinnerMusic;
    private TextView tvCountdown, breathingText, breathingGuideText;
    private ProgressBar progressBar;
    private View breathingCircle;
    private CountDownTimer countDownTimer;
    private boolean isCounting = false;
    private int selectedMusicResId = R.raw.music1;
    private long totalTimeInMillis;
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private Handler uiHandler = new Handler(Looper.getMainLooper());
    private int inhaleTime = 4000;  // 默认吸气 4 秒
    private int exhaleTime = 4000;  // 默认呼气 4 秒
    private Handler breathingHandler = new Handler();
    private Runnable breathingRunnable; // 让动画的 Runnable 可被停止


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        // 启用 Edge-to-Edge 显示效果
        EdgeToEdge.enable(this);

        etTime = findViewById(R.id.etTime);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnViewRecords = findViewById(R.id.btnViewRecords);
        spinnerMusic = findViewById(R.id.spinnerMusic);
        tvCountdown = findViewById(R.id.tvCountdown);
        breathingCircle = findViewById(R.id.breathingCircle);
        breathingText = findViewById(R.id.breathingText);
        breathingGuideText = findViewById(R.id.breathingGuideText);
        progressBar = findViewById(R.id.progressBar);

        // 音乐选择
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

        btnStart.setOnClickListener(v -> startMeditationWithCountdown());
        btnStop.setOnClickListener(v -> stopCountdown());
        btnViewRecords.setOnClickListener(v -> startActivity(new Intent(TimerActivity.this, RecordActivity.class)));
    }

    private void startMeditationWithCountdown() {
        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                btnStart.setText("開始まで" + millisUntilFinished / 1000 + " 秒");
            }
            public void onFinish() {
                btnStart.setText("开始冥想");
                startCountdown();
            }
        }.start();
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

        startBreathingAnimation();
        startGuidedMeditation();
        dimScreenAfterDelay(10000);

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
                stopGuidedMeditation();

                // 计算冥想时间（分钟）
                long durationInMinutes = totalTimeInMillis / 60000;
                saveMeditationRecord(durationInMinutes + " 分");

                showMeditationSummary(totalTimeInMillis / 1000);
                updateStreak();
            }

        }.start();
    }

    private String formatTime(long seconds) {
        long min = seconds / 60;
        long sec = seconds % 60;
        return String.format("%02d:%02d", min, sec);
    }


    private void stopCountdown() {
        if (countDownTimer != null) countDownTimer.cancel();
        stopGuidedMeditation();
        isCounting = false;

        // 计算已冥想的时间
        long elapsedTime = (totalTimeInMillis / 1000) - (progressBar.getProgress());
        long elapsedMinutes = elapsedTime / 60;
        long elapsedSeconds = elapsedTime % 60;

        // 只有冥想超过 10 秒才保存
        if (elapsedTime >= 3) {
            String timeText = elapsedMinutes + "分" + elapsedSeconds + "秒";
            saveMeditationRecord(timeText);
        }

        tvCountdown.setText("00:00");
        progressBar.setProgress(0);
        btnStart.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.GONE);
        etTime.setEnabled(true);

        if (breathingRunnable != null) {
            breathingHandler.removeCallbacks(breathingRunnable);
        }

        Toast.makeText(this, "冥想を終了しました", Toast.LENGTH_SHORT).show();
    }


    private void startBreathingAnimation() {
        final int initialSize = breathingCircle.getLayoutParams().width;
        final int expandedSize = (int) (initialSize * 1.4);

        breathingRunnable = new Runnable() {
            boolean isInhale = true;
            @Override
            public void run() {
                if (!isCounting) return; // **防止动画在停止后继续执行**

                ValueAnimator animator = ValueAnimator.ofInt(isInhale ? initialSize : expandedSize, isInhale ? expandedSize : initialSize);
                animator.setDuration(isInhale ? inhaleTime : exhaleTime);
                animator.addUpdateListener(animation -> {
                    int animatedValue = (int) animation.getAnimatedValue();
                    breathingCircle.getLayoutParams().width = animatedValue;
                    breathingCircle.getLayoutParams().height = animatedValue;
                    breathingCircle.requestLayout();
                });
                animator.start();

                breathingText.setText(isInhale ? "吸って..." : "吐いて...");
                breathingGuideText.setText(isInhale ? "深く吸って..." : "ゆっくり吐いて...");

                isInhale = !isInhale;
                breathingHandler.postDelayed(this, isInhale ? inhaleTime : exhaleTime);
            }
        };
        breathingHandler.post(breathingRunnable);
    }


    private void startGuidedMeditation() {
        mediaPlayer = MediaPlayer.create(this, R.raw.relax);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    private void stopGuidedMeditation() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    private void dimScreenAfterDelay(int delay) {
        new Handler().postDelayed(() -> {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.screenBrightness = 0.1f;
            getWindow().setAttributes(layoutParams);
        }, delay);
    }

    private void showMeditationSummary(long duration) {
        new AlertDialog.Builder(this)
                .setTitle("冥想完了")
                .setMessage("今回の冥想時間：" + duration / 60 + " 分\nリラックスできましたか？")
                .setPositiveButton("確認", (dialog, which) -> dialog.dismiss())
                .show();
    }


    private void updateStreak() {
        SharedPreferences sharedPreferences = getSharedPreferences("MeditationRecords", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 获取当前日期
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String lastDate = sharedPreferences.getString("lastMeditationDate", "");

        int streak = sharedPreferences.getInt("streak", 0);

        // 如果今天已经记录过，不重复增加
        if (!today.equals(lastDate)) {
            // 如果上次冥想是昨天，增加 streak，否则重置
            String yesterday = getYesterdayDate();
            if (lastDate.equals(yesterday)) {
                streak++;
            } else {
                streak = 1; // 不是连续的，重置 streak
            }
            editor.putString("lastMeditationDate", today);
            editor.putInt("streak", streak);
            editor.apply();
        }

        Toast.makeText(this, "連続冥想 " + streak + " 日達成！", Toast.LENGTH_SHORT).show();
    }

    // 获取昨天的日期
    private String getYesterdayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }


    private void saveMeditationRecord(String duration) {
        SharedPreferences sharedPreferences = getSharedPreferences("MeditationRecords", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 记录时间戳
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        editor.putString(timestamp, duration);
        editor.apply(); // 保存数据
    }


}
