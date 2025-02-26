package jec.ac.jp.incense;

import android.animation.ValueAnimator;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
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
    private Button btnStart;
    private Button btnStop;
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
    private final Handler uiHandler = new Handler(Looper.getMainLooper());
    private final int inhaleTime = 4000;  // 吸气 4 秒
    private final int exhaleTime = 4000;  // 呼气 4 秒
    private final Handler breathingHandler = new Handler();
    private static final int FEEDBACK_REQUEST_CODE = 1;
    private Runnable breathingRunnable;
    private int initialCircleSize; // 记录圆圈的初始大小

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        // 启用 Edge-to-Edge 显示效果
        EdgeToEdge.enable(this);
        // **🔍 读取上次冥想的杂念情况**
        SharedPreferences sharedPreferences = getSharedPreferences("MeditationRecords", Context.MODE_PRIVATE);
        boolean lastMeditationDiscarded = sharedPreferences.getBoolean("lastMeditationDiscarded", false);
        String lastDistractionLevel = sharedPreferences.getString("lastDistractionLevel", ""); // **获取存储的值**
        // ✅ 添加日志，确保数据正确
        Log.d("DEBUG", "📌 读取数据: 雑念: " + lastDistractionLevel + " | 废弃状态: " + lastMeditationDiscarded);
        // ✅ **如果上次冥想被废弃，优先弹出废弃提示**
        if (lastMeditationDiscarded) {
            showMeditationSuggestionDialog("上回の瞑想記録は破棄されました。次回も頑張りましょう！");

            // ✅ **清除废弃状态，防止每次启动都弹窗**
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("lastMeditationDiscarded", false);
            editor.apply();
        }
// ✅ **如果上次冥想未被废弃，检查杂念情况**
        else if (!lastDistractionLevel.isEmpty()) {
            if (lastDistractionLevel.equals("多い")) {
                showMeditationSuggestionDialog("前回の瞑想では雑念が多かったですね。\n今回は短めの瞑想を試してみましょう！");
            } else if (lastDistractionLevel.equals("なし") || lastDistractionLevel.equals("少し")) {
                showMeditationSuggestionDialog("前回の瞑想では雑念が少なかったですね。\n今回はもう少し長めの瞑想に挑戦してみませんか？");
            }
        }
        if (!lastDistractionLevel.isEmpty()) {
            showMeditationSuggestionDialog(lastDistractionLevel); // **调用弹窗**
        }
        etTime = findViewById(R.id.etTime);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        Button btnViewRecords = findViewById(R.id.btnViewRecords);
        spinnerMusic = findViewById(R.id.spinnerMusic);
        tvCountdown = findViewById(R.id.tvCountdown);
        breathingCircle = findViewById(R.id.breathingCircle);
        // 记录圆圈的初始大小
        initialCircleSize = breathingCircle.getLayoutParams().width;
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

        final int[] musicResIds = {R.raw.music1, R.raw.relax, R.raw.forest_lullaby};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, musicOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMusic.setAdapter(adapter);
        spinnerMusic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMusicResId = musicResIds[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
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
        if (!isCounting) return;

        stopBreathingAnimation(); // 停止呼吸动画
        if (countDownTimer != null) {
            countDownTimer.cancel(); // 取消倒计时
        }

        isCounting = false;
        stopMusicService(); // 停止音乐

        long elapsedTime = (totalTimeInMillis / 1000) - progressBar.getProgress();
        if (elapsedTime < 1) {
            Toast.makeText(this, "冥想時間が短すぎます", Toast.LENGTH_SHORT).show();
            resetUI(); // 确保调用 resetUI()
            return;
        }

        resetUI();
        restoreScreenBrightness();
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

        startMusicService();

        btnStop.setVisibility(View.VISIBLE);
        btnStart.setVisibility(View.GONE);
        etTime.setEnabled(false);
        progressBar.setMax((int) (totalTimeInMillis / 1000));
        progressBar.setProgress((int) (totalTimeInMillis / 1000));

        startBreathingAnimation();

        new CountDownTimer(10000, 1000) { // 延迟10秒
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                dimScreen();
            }
        }.start();

        // 开始倒计时
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
                    stopMusicService();
                    openFeedbackScreen(totalTimeInMillis / 1000);

                    restoreScreenBrightness();
                }
            }
        }.start();
    }


    private void dimScreen() {
        if (!isCounting) {
            Log.d("DEBUG", "冥想未开始，屏幕不会变暗");
            return;
        }

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = 0.1f; // 设置为最暗，0表示最暗，1表示最亮
        getWindow().setAttributes(layoutParams);
        Log.d("DEBUG", "屏幕已变暗");
    }


    private void restoreScreenBrightness() {
        try {
            ContentResolver contentResolver = getContentResolver();
            int currentBrightness = Settings.System.getInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS);

            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.screenBrightness = currentBrightness / 255.0f;
            getWindow().setAttributes(layoutParams);

        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }




    private void startMusicService() {
        Intent serviceIntent = new Intent(this, CountdownTimerService.class);
        serviceIntent.putExtra("MUSIC_RES_ID", selectedMusicResId);
        startService(serviceIntent);
    }


    private void stopMusicService() {
        Intent serviceIntent = new Intent(this, CountdownTimerService.class);
        stopService(serviceIntent);
    }


    private void startBreathingAnimation() {
        if (breathingCircle == null) {
            Toast.makeText(this, "エラー: breathingCircle が見つかりません", Toast.LENGTH_SHORT).show();
            return;
        }

        breathingCircle.getLayoutParams().width = initialCircleSize;
        breathingCircle.getLayoutParams().height = initialCircleSize;
        breathingCircle.requestLayout();

        final int expandedSize = (int) (initialCircleSize * 1.4); // 放大后的尺寸

        breathingRunnable = new Runnable() {
            boolean isInhale = true;

            @Override
            public void run() {
                if (!isCounting) return;

                ValueAnimator animator = ValueAnimator.ofInt(
                        isInhale ? initialCircleSize : expandedSize,
                        isInhale ? expandedSize : initialCircleSize
                );
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
    private void stopBreathingAnimation() {
        if (breathingRunnable != null) {
            breathingHandler.removeCallbacks(breathingRunnable);
        }

        if (breathingCircle != null) {
            breathingCircle.getLayoutParams().width = initialCircleSize;
            breathingCircle.getLayoutParams().height = initialCircleSize;
            breathingCircle.requestLayout(); // 确保调用 requestLayout()
            breathingCircle.invalidate();   // 可选：强制重绘
        }

        breathingCircle.setVisibility(View.VISIBLE);
        breathingText.setVisibility(View.VISIBLE);
        breathingGuideText.setVisibility(View.VISIBLE);
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
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }

        if (breathingHandler != null && breathingRunnable != null) {
            breathingHandler.removeCallbacks(breathingRunnable);
        }

        stopGuidedMeditation();

        tvCountdown.setText("00:00");
        progressBar.setProgress(0);
        btnStart.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.GONE);
        etTime.setEnabled(true);
        etTime.setText("");

        // 恢复圆圈大小到初始大小
        if (breathingCircle != null) {
            breathingCircle.getLayoutParams().width = initialCircleSize;
            breathingCircle.getLayoutParams().height = initialCircleSize;
            breathingCircle.requestLayout();
            breathingCircle.invalidate();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetUI();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FEEDBACK_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            long meditationDuration = data.getLongExtra("meditationDuration", 0);
            String usedIncense = data.getStringExtra("usedIncense");

            if (meditationDuration > 0) {
                saveMeditationRecord(meditationDuration, usedIncense);
            } else {
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
        String record = "時間: " + (duration / 60) + "分 " + (duration % 60) + "秒 | 使用した香: " + (incense == null || incense.isEmpty() ? "未入力" : incense);

        editor.putString(timestamp, record);
        editor.apply();
    }

    private void checkLastMeditationStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("MeditationRecords", Context.MODE_PRIVATE);
        boolean lastMeditationDiscarded = sharedPreferences.getBoolean("lastMeditationDiscarded", false);
        String lastDistractionLevel = sharedPreferences.getString("lastDistractionLevel", "");

        Log.d("DEBUG", "📌 checkLastMeditationStatus() -> 读取数据: 雑念: " + lastDistractionLevel + " | 废弃状态: " + lastMeditationDiscarded);

        if (lastMeditationDiscarded) {
            Log.d("DEBUG", "📌 弹出废弃提示对话框！");
            showMeditationSuggestionDialog("上回の瞑想記録は破棄されました。次回も頑張りましょう！");

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("lastMeditationDiscarded", false);
            editor.apply();
        }
        else if (!lastDistractionLevel.isEmpty()) {
            if (lastDistractionLevel.equals("多い")) {
                showMeditationSuggestionDialog("前回の瞑想では雑念が多かったですね。\n今回は短めの瞑想を試してみましょう！");
            } else if (lastDistractionLevel.equals("なし") || lastDistractionLevel.equals("少し")) {
                showMeditationSuggestionDialog("前回の瞑想では雑念が少なかったですね。\n今回はもう少し長めの瞑想に挑戦してみませんか？");
            }
        }
    }

    private void showMeditationSuggestionDialog(String lastDistractionLevel) {
        if (lastDistractionLevel == null || lastDistractionLevel.isEmpty()) {
            Log.d("DEBUG", "📌 showMeditationSuggestionDialog() -> lastDistractionLevel 为空，不弹窗");
            return;
        }


        final String[] messageHolder = new String[1];

        if (lastDistractionLevel.equals("多い")) {
            messageHolder[0] = "前回の冥想では雑念が多かったです。今回は短めの冥想を試してみませんか？";
        } else if (lastDistractionLevel.equals("少し") || lastDistractionLevel.equals("なし")) {
            messageHolder[0] = "前回の冥想では雑念が少なかったです。今回はもっと長めの冥想に挑戦してみませんか？";
        } else {
            return;
        }

        Log.d("DEBUG", "📌 显示弹窗: " + messageHolder[0]);

        runOnUiThread(() -> {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                new AlertDialog.Builder(TimerActivity.this)
                        .setTitle("冥想ヒント")
                        .setMessage(messageHolder[0]) // ✅ 这里使用 `final` 数组
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
            }, 500);
        });
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
