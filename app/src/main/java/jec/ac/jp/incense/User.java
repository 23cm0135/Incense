package jec.ac.jp.incense;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);

        // 初始化 FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        // **檢查用戶是否已登入**
        if (user == null) {
            // **如果未登入，則跳轉到登入頁面**
            Intent intent = new Intent(User.this, Account.class);
            startActivity(intent);
            finish(); // 結束當前頁面，防止返回
            return;
        }

        // **如果已登入，顯示用戶名稱**
        TextView userNameTextView = findViewById(R.id.user_name);
        String userName = user.getDisplayName();
        String email = user.getEmail();

        if (userName != null && !userName.isEmpty()) {
            userNameTextView.setText(userName + " さん");
        } else {
            userNameTextView.setText(email + " さん");
        }

        // **設置 UI 事件**
        setupUI();
    }

    private void setupUI() {
        LinearLayout historyLayout = findViewById(R.id.layout_history);
        historyLayout.setOnClickListener(v -> {
            Intent intent = new Intent(User.this, BrowsingHistoryActivity.class);
            startActivity(intent);
        });

        LinearLayout favoritesLayout = findViewById(R.id.layout_favorites);
        favoritesLayout.setOnClickListener(v -> {
            Intent intent = new Intent(User.this, FavoritesActivity.class);
            startActivity(intent);
        });

//        // 跳轉到鬧鐘界面
//        LinearLayout alarm = findViewById(R.id.layout_alarm);
//        alarm.setOnClickListener(v -> {
//            Intent intent = new Intent(User.this, TimerActivity.class);
//            startActivity(intent);
//        });

        // 返回主頁
        ImageButton homeButton = findViewById(R.id.btn_home);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(User.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        // 跳轉到問題頁面
        ImageButton question = findViewById(R.id.btn_app);
        question.setOnClickListener(v -> {
            Intent intent = new Intent(User.this, Question.class);
            startActivity(intent);
        });

        ImageButton alarm = findViewById(R.id.btn_alarm);
        alarm.setOnClickListener(v -> {
            Intent intent = new Intent(User.this, TimerActivity.class);
            startActivity(intent);
        });

        // **登出按鈕**
        LinearLayout logoutLayout = findViewById(R.id.layout_logout);
        logoutLayout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(User.this, Account.class));
            finish();
        });

    }
}
