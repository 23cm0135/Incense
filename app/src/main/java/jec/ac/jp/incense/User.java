package jec.ac.jp.incense;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // 初始化 FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // 获取当前登录的用户
        FirebaseUser user = firebaseAuth.getCurrentUser();

        // 更新用户名
        TextView userNameTextView = findViewById(R.id.user_name);
        if (user != null) {
            String userName = user.getDisplayName(); // 获取用户的显示名
            String email = user.getEmail(); // 获取用户的邮箱（备用）

            if (userName != null && !userName.isEmpty()) {
                userNameTextView.setText(userName + " さん");
            } else {
                // 如果显示名不可用，则使用邮箱
                userNameTextView.setText(email + " さん");
            }
        } else {
            userNameTextView.setText("ゲスト さん");
        }

        // 其他逻辑...
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

        ImageButton homeButton = findViewById(R.id.btn_home);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(User.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        ImageButton question = findViewById(R.id.btn_app);
        question.setOnClickListener(v -> {
            Intent intent = new Intent(User.this, Question.class);
            startActivity(intent);
        });

        findViewById(R.id.btn_logout).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(User.this, Account.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }
}
