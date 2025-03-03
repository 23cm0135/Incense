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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        updateUserName();

        ImageButton userButton = findViewById(R.id.btn_user);
        userButton.setOnClickListener(v -> {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            if (firebaseAuth.getCurrentUser() == null) {
                startActivity(new Intent(MainActivity.this, Account.class));
                Toast.makeText(MainActivity.this, "ログインしてください", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, User.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        ImageButton question = findViewById(R.id.btn_app);
        question.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Question.class);
            startActivity(intent);
        });

        ImageButton alarm = findViewById(R.id.btn_alarm);
        alarm.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TimerActivity.class);
            startActivity(intent);
        });

        updateRecommendedProducts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUserName();
        updateRecommendedProducts();
    }

    /**
     * 更新欢迎用戶名稱
     */
    private void updateUserName() {
        TextView tvUserName = findViewById(R.id.tvUserName);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String name = currentUser.getDisplayName();
            if (name == null || name.isEmpty()) {
                name = currentUser.getEmail();
            }
            tvUserName.setText("ようこそ、" + name + "さん！");
        } else {
            tvUserName.setText("ようこそ、ゲストさん！");
        }
    }

    private void updateRecommendedProducts() {
        // 隨機獲取一組 ButtonEnum 數據
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
