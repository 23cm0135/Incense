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

        btnStart.setOnClickListener(v -> startMeditationWithCountdown());
        btnStop.setOnClickListener(v -> stopCountdown());
        btnViewRecords.setOnClickListener(v -> startActivity(new Intent(TimerActivity.this, RecordActivity.class)));
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
                    openFeedbackScreen(totalTimeInMillis / 1000);
                }
            }
        }.start();
    }
    private void stopCountdown() {
        if (!isCounting) return;

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        isCounting = false;
        stopGuidedMeditation();

        long elapsedTime = (totalTimeInMillis / 1000) - progressBar.getProgress();
        if (elapsedTime < 1) {
            Toast.makeText(this, "冥想時間が短すぎます", Toast.LENGTH_SHORT).show();
            resetUI();
            return;
        }

        openFeedbackScreen(elapsedTime);
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

        if (requestCode == FEEDBACK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) { // **用户选择“保存”**
                String suggestion = data.getStringExtra("meditationSuggestion");
                if (suggestion != null) {
                    if (suggestion.equals("shorter")) {
                        showMeditationSuggestionDialog("次回は短めの冥想を試してみませんか？");
                    } else if (suggestion.equals("longer")) {
                        showMeditationSuggestionDialog("次回はもう少し長めの冥想を試してみませんか？");
                    }
                }
            } else if (resultCode == RESULT_CANCELED && data != null && data.getBooleanExtra("meditationDiscarded", false)) {
                // **用户点击了“废弃”**
                showMeditationSuggestionDialog("冥想記録が保存されませんでした。次回も頑張りましょう！");
            }

            resetUI(); // **确保无论用户选择“保存”还是“废弃”，UI 都会被正确重置**
        }
    }

    private void showMeditationSuggestionDialog(String message) {
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
