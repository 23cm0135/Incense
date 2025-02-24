package jec.ac.jp.incense;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int[] buttonIds = {
            R.id.imgbutton1, R.id.imgbutton2, R.id.imgbutton3,
            R.id.imgbutton4, R.id.imgbutton5, R.id.imgbutton6,
            R.id.imgbutton7, R.id.imgbutton8, R.id.imgbutton9
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Edge-to-Edge
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // システムバーのパディング処理
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ユーザーアイコン
        ImageButton userButton = findViewById(R.id.btn_user);
        userButton.setOnClickListener(v -> {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            if (firebaseAuth.getCurrentUser() == null) {
                // 未ログイン → Account画面へ
                startActivity(new Intent(MainActivity.this, Account.class));
                Toast.makeText(MainActivity.this, "ログインしてください", Toast.LENGTH_SHORT).show();
            } else {
                // ログイン済み → User画面へ
                Intent intent = new Intent(MainActivity.this, User.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        // Question画面へ
        ImageButton question = findViewById(R.id.btn_app);
        question.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Question.class);
            startActivity(intent);
        });

        // タイマー画面へ
        ImageButton alarm = findViewById(R.id.btn_alarm);
        alarm.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TimerActivity.class);
            startActivity(intent);
        });

        // おすすめ商品を初期化
        updateRecommendedProducts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 戻ってきたタイミングでもリフレッシュ
        updateRecommendedProducts();
    }

    /**
     * ランダム9個の ButtonEnum を取得し、Glideで画像を読み込み、クリックで画面遷移
     */
    private void updateRecommendedProducts() {
        List<ButtonEnum> randomButtons = ButtonEnum.getRandomButtons();
        for (int i = 0; i < randomButtons.size(); i++) {
            ImageButton button = findViewById(buttonIds[i]);
            ButtonEnum buttonEnum = randomButtons.get(i);

            // ★ Glide を使ってネット画像を読み込む
            Glide.with(this)
                    .load(buttonEnum.getImageUrl())
                    .placeholder(R.drawable.default_image) // ローディング中
                    .error(R.drawable.default_image)       // エラー時
                    .into(button);

            // クリック → MinuteActivity に遷移
            button.setOnClickListener(v -> navigateToMinute(buttonEnum));
        }
    }

    /**
     * ボタンを押したら MinuteActivity に飛ぶ
     */
    private void navigateToMinute(ButtonEnum buttonEnum) {
        Log.d("ButtonEnum", "Navigating with: " + buttonEnum.getText() +
                " | imageUrl: " + buttonEnum.getImageUrl() +
                " | URL: " + buttonEnum.getUrl() +
                " | INCENSE_ID: " + buttonEnum.getId() +
                " | INCENSE_NAME: " + buttonEnum.getIncenseName());

        Intent intent = new Intent(MainActivity.this, MinuteActivity.class);
        intent.putExtra("EXTRA_TEXT", buttonEnum.getText());
        // imageResId は使わず、ネット画像 → MinuteActivity 側で読み込むか、あるいは enum 同様に URL で読み込む
        // 例: intent.putExtra("EXTRA_IMAGE_URL", buttonEnum.getImageUrl());
        intent.putExtra("EXTRA_IMAGE_URL", buttonEnum.getImageUrl());

        intent.putExtra("EXTRA_URL", buttonEnum.getUrl());
        intent.putExtra("INCENSE_ID", buttonEnum.getId());
        intent.putExtra("INCENSE_NAME", buttonEnum.getIncenseName());

        startActivity(intent);
    }
}
