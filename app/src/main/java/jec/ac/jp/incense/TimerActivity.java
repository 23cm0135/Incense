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
    private int inhaleTime = 4000;  // 吸气 4 秒
    private int exhaleTime = 4000;  // 呼气 4 秒
    private Handler breathingHandler = new Handler();
    private static final int FEEDBACK_REQUEST_CODE = 1;
    private Runnable breathingRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        // 启用 Edge-to-Edge 显示效果
        EdgeToEdge.enable(this);

        // 读取上次反馈信息
        // **🔍 读取上次冥想的杂念情况**
        SharedPreferences sharedPreferences = getSharedPreferences("MeditationRecords", Context.MODE_PRIVATE);
        String lastDistractionLevel = sharedPreferences.getString("lastDistractionLevel", ""); // **获取存储的值**
//        SharedPreferences sharedPreferences = getSharedPreferences("MeditationRecords", Context.MODE_PRIVATE);
//        String lastFeedback = sharedPreferences.getString("lastMeditationFeedback", "");

        if (!lastDistractionLevel.isEmpty()) {
            showMeditationSuggestionDialog(lastDistractionLevel); // **调用弹窗**
        }

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
        // **恢复音乐选择功能**
        setupMusicSpinner();
        // **检查上次冥想状态**
        checkLastMeditationStatus();

        btnStart.setOnClickListener(v -> startMeditationWithCountdown());
        btnStop.setOnClickListener(v -> stopCountdown());
        btnViewRecords.setOnClickListener(v -> startActivity(new Intent(TimerActivity.this, RecordActivity.class)));
    }

    private void setupMusicSpinner() {
        String[] musicOptions = {"雨", "Relax", "Forest Lullaby"};


        final int[] musicResIds = {R.raw.music1,R.raw.relax,R.raw.forest_lullaby};


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, musicOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMusic.setAdapter(adapter);

        spinnerMusic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMusicResId = musicResIds[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void startMeditationWithCountdown() {
        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                btnStart.setText("開始まで " + millisUntilFinished / 1000 + " 秒");
            }
            @Override
            public void onFinish() {
                btnStart.setText("冥想開始");
                startCountdown();
            }
        }.start();
    }
    private void stopCountdown() {
        if (!isCounting) return; // 确保当前正在倒计时才执行

        if (countDownTimer != null) {
            countDownTimer.cancel(); // 取消倒计时
        }

        isCounting = false;
        stopMusicService(); // **确保音乐停止**

        long elapsedTime = (totalTimeInMillis / 1000) - progressBar.getProgress();
        if (elapsedTime < 1) {
            Toast.makeText(this, "冥想時間が短すぎます", Toast.LENGTH_SHORT).show();
            resetUI();
            return;
        }

        openFeedbackScreen(elapsedTime);
    }


    private void startCountdown() {
        if (isCounting) return;

        String inputTime = etTime.getText().toString().trim();
        if (inputTime.isEmpty()) {
            Toast.makeText(this, "時間を入力してください！", Toast.LENGTH_SHORT).show();
            return;
        }

        long inputMinutes = Long.parseLong(inputTime);
        if (inputMinutes <= 0) {
            Toast.makeText(this, "冥想時間は1分以上にしてください", Toast.LENGTH_SHORT).show();
            return;
        }

        totalTimeInMillis = inputMinutes * 60 * 1000;
        isCounting = true;

        // **启动倒计时音乐服务**
        startMusicService();

        btnStop.setVisibility(View.VISIBLE);
        btnStart.setVisibility(View.GONE);
        etTime.setEnabled(false);
        progressBar.setMax((int) (totalTimeInMillis / 1000));
        progressBar.setProgress((int) (totalTimeInMillis / 1000));

        startBreathingAnimation();

        countDownTimer = new CountDownTimer(totalTimeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress((int) (millisUntilFinished / 1000));
                tvCountdown.setText(formatTime(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                if (isCounting) {
                    isCounting = false;
                    stopMusicService(); // **冥想结束，停止音乐**
                    openFeedbackScreen(totalTimeInMillis / 1000);
                }
            }
        }.start();
    }

    /**
     * 启动音乐播放服务
     */
    private void startMusicService() {
        Intent serviceIntent = new Intent(this, CountdownTimerService.class);
        serviceIntent.putExtra("MUSIC_RES_ID", selectedMusicResId);
        startService(serviceIntent);
    }

    /**
     * 停止音乐播放服务
     */
    private void stopMusicService() {
        Intent serviceIntent = new Intent(this, CountdownTimerService.class);
        stopService(serviceIntent);
    }


    private void startBreathingAnimation() {
        breathingCircle = findViewById(R.id.breathingCircle);
        if (breathingCircle == null) {
            Toast.makeText(this, "エラー: breathingCircle が見つかりません", Toast.LENGTH_SHORT).show();
            return;
        }

        final int initialSize = breathingCircle.getLayoutParams().width;
        final int expandedSize = (int) (initialSize * 1.4);

        breathingRunnable = new Runnable() {
            boolean isInhale = true;
            @Override
            public void run() {
                if (!isCounting) return;

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
    private void openFeedbackScreen(long meditationDuration) {
        if (meditationDuration < 1) {
            Toast.makeText(this, "冥想時間が短すぎます", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, MeditationFeedbackActivity.class);
        intent.putExtra("meditationDuration", meditationDuration);
        startActivityForResult(intent, FEEDBACK_REQUEST_CODE);
    }

    private void resetUI() {
        // **确保倒计时完全停止**
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }

        // **确保呼吸动画完全停止**
        if (breathingHandler != null && breathingRunnable != null) {
            breathingHandler.removeCallbacks(breathingRunnable);
        }

        // **停止背景音乐**
        stopGuidedMeditation();

        // **重置 UI**
        tvCountdown.setText("00:00");
        progressBar.setProgress(0);
        btnStart.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.GONE);
        etTime.setEnabled(true);
        etTime.setText(""); // **清除用户输入的时间**
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FEEDBACK_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // **获取冥想时间**
            long meditationDuration = data.getLongExtra("meditationDuration", 0);
            String usedIncense = data.getStringExtra("usedIncense");

            if (meditationDuration > 0) {
                saveMeditationRecord(meditationDuration, usedIncense);
            } else {
               // Toast.makeText(this, "エラー: 冥想時間が正しく取得されませんでした。", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == RESULT_CANCELED && data != null && data.getBooleanExtra("meditationDiscarded", false)) {
            showMeditationSuggestionDialog("冥想記録が保存されませんでした。次回も頑張りましょう！");
        }

        resetUI();
    }


    private void saveMeditationRecord(long duration, String incense) {
        SharedPreferences sharedPreferences = getSharedPreferences("MeditationRecords", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        String record = "時間: " + (duration / 60) + "分 " + (duration % 60) + "秒 | 使用した香: " + (incense == null || incense.isEmpty() ? "不明" : incense);

        editor.putString(timestamp, record);
        editor.apply();
    }
    private void checkLastMeditationStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("MeditationRecords", Context.MODE_PRIVATE);
        String lastStatus = sharedPreferences.getString("lastMeditationStatus", "");

        if (!lastStatus.isEmpty()) {
            if (lastStatus.equals("saved")) {
                showMeditationSuggestionDialog("前回の冥想が正常に記録されました。");
            } else if (lastStatus.equals("discarded")) {
                showMeditationSuggestionDialog("前回の冥想記録は保存されませんでした。");
            }

            // **弹窗只显示一次，清除记录**
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("lastMeditationStatus");
            editor.apply();
        }
    }
    private void showMeditationSuggestionDialog(String lastDistractionLevel) {
        String message = "";

        if (lastDistractionLevel.equals("多い")) {
            message = "前回の冥想では雑念が多かったです。次回は短めの冥想を試してみませんか？";
        } else if (lastDistractionLevel.equals("少し") || lastDistractionLevel.equals("なし")) {
            message = "前回の冥想では雑念が少なかったです。次回はもっと長めの冥想に挑戦してみませんか？";
        } else {
            return; // **如果数据无效，不弹窗**
        }

        new AlertDialog.Builder(this)
                .setTitle("冥想ヒント")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }


    private String formatTime(long seconds) {
        long min = seconds / 60;
        long sec = seconds % 60;
        return String.format("%02d:%02d", min, sec);
    }
    private void stopGuidedMeditation() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
