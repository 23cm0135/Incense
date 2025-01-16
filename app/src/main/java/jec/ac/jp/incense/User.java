package jec.ac.jp.incense;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class User extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.user), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 确保首次启动时加载历史数据
        IncenseDetailActivity.loadBrowsingHistory(this);

        LinearLayout historyLayout = findViewById(R.id.layout_history);

        historyLayout.setOnClickListener(v -> {
            // 启动历史记录界面
            Intent intent = new Intent(User.this, BrowsingHistoryActivity.class);
            startActivity(intent);

           // overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        // 获取收藏功能模块的布局
        LinearLayout favoritesLayout = findViewById(R.id.layout_favorites);

        // 设置点击事件
        favoritesLayout.setOnClickListener(v -> {
            // 跳转到收藏列表界面
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
    }
}