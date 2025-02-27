package jec.ac.jp.incense;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
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

        // 系統欄位 Padding 處理
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 在主畫面上方顯示用戶登錄名稱
        TextView tvUserName = findViewById(R.id.tvUserName);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // 如果有 displayName 就用 displayName，否則用 Email
            String name = (currentUser.getDisplayName() != null && !currentUser.getDisplayName().isEmpty())
                    ? currentUser.getDisplayName() : currentUser.getEmail();
            tvUserName.setText("ようこそ、" + name + "さん！");
        } else {
            tvUserName.setText("ようこそ、ゲストさん！");
        }

        // 用戶圖示按鈕
        ImageButton userButton = findViewById(R.id.btn_user);
        userButton.setOnClickListener(v -> {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            if (firebaseAuth.getCurrentUser() == null) {
                // 未登錄 → 跳轉到 Account 畫面
                startActivity(new Intent(MainActivity.this, Account.class));
                Toast.makeText(MainActivity.this, "ログインしてください", Toast.LENGTH_SHORT).show();
            } else {
                // 登錄狀態 → 跳轉到 User 畫面
                Intent intent = new Intent(MainActivity.this, User.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        // 問題畫面按鈕
        ImageButton question = findViewById(R.id.btn_app);
        question.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Question.class);
            startActivity(intent);
        });

        // 定時器畫面按鈕
        ImageButton alarm = findViewById(R.id.btn_alarm);
        alarm.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TimerActivity.class);
            startActivity(intent);
        });

        // 初始化推薦商品
        updateRecommendedProducts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 返回時刷新推薦商品
        updateRecommendedProducts();
    }

    /**
     * 隨機取得9個 ButtonEnum，並使用 Glide 載入網路圖片，點擊後跳轉到 MinuteActivity
     */
    private void updateRecommendedProducts() {
        List<ButtonEnum> randomButtons = ButtonEnum.getRandomButtons();
        for (int i = 0; i < randomButtons.size(); i++) {
            ImageButton button = findViewById(buttonIds[i]);
            ButtonEnum buttonEnum = randomButtons.get(i);

            Glide.with(this)
                    .load(buttonEnum.getImageUrl())
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .into(button);

            button.setOnClickListener(v -> navigateToMinute(buttonEnum));
        }
    }

    /**
     * 點擊後跳轉到 MinuteActivity，並傳遞必要資訊
     */
    private void navigateToMinute(ButtonEnum buttonEnum) {
        Log.d("ButtonEnum", "Navigating with: " + buttonEnum.getText() +
                " | imageUrl: " + buttonEnum.getImageUrl() +
                " | URL: " + buttonEnum.getUrl() +
                " | INCENSE_ID: " + buttonEnum.getId() +
                " | INCENSE_NAME: " + buttonEnum.getIncenseName());

        Intent intent = new Intent(MainActivity.this, MinuteActivity.class);
        intent.putExtra("EXTRA_TEXT", buttonEnum.getText());
        intent.putExtra("EXTRA_IMAGE_URL", buttonEnum.getImageUrl());
        intent.putExtra("EXTRA_URL", buttonEnum.getUrl());
        intent.putExtra("INCENSE_ID", buttonEnum.getId());
        intent.putExtra("INCENSE_NAME", buttonEnum.getIncenseName());
        startActivity(intent);
    }
}
