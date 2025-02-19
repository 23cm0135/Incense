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

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int[] buttonIds = {
            R.id.imgbutton1, R.id.imgbutton2, R.id.imgbutton3,
            R.id.imgbutton4, R.id.imgbutton5, R.id.imgbutton6,
            R.id.imgbutton7, R.id.imgbutton8, R.id.imgbutton9
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 启用 Edge-to-Edge 显示效果
        EdgeToEdge.enable(this);

        // 设置主布局
        setContentView(R.layout.activity_main);

        // 处理系统栏的内边距
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 用户按钮点击事件
        ImageButton userButton = findViewById(R.id.btn_user);
        userButton.setOnClickListener(v -> {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            if (firebaseAuth.getCurrentUser() == null) {
                // 未登录，跳转到登录页面
                Intent intent = new Intent(MainActivity.this, Account.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "ログインしてください", Toast.LENGTH_SHORT).show();
            } else {
                // 已登录，跳转到用户页面
                Intent intent = new Intent(MainActivity.this, User.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        // 问题按钮点击事件
        ImageButton question = findViewById(R.id.btn_app);
        question.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Question.class);
            startActivity(intent);
        });

        // **初始化推荐商品**
        updateRecommendedProducts();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // **每次返回 `MainActivity` 时刷新推荐商品**
        updateRecommendedProducts();
    }

    /**
     * 随机获取 9 个推荐商品并更新 UI
     */
    private void updateRecommendedProducts() {
        List<ButtonEnum> randomButtons = ButtonEnum.getRandomButtons();

        for (int i = 0; i < randomButtons.size(); i++) {
            ImageButton button = findViewById(buttonIds[i]);
            final ButtonEnum buttonEnum = randomButtons.get(i);

            // **设定图片**
            button.setImageResource(buttonEnum.getImageResId());

            // **设置点击事件**
            button.setOnClickListener(v -> navigateToMinute(buttonEnum));
        }
    }

    /**
     * 跳转到 MinuteActivity
     */
    private void navigateToMinute(ButtonEnum buttonEnum) {
        Log.d("ButtonEnum", "Navigating with: " + buttonEnum.getText() +
                ", ImageResId: " + buttonEnum.getImageResId() +
                ", URL: " + buttonEnum.getUrl() +
                ", INCENSE_ID: " + buttonEnum.getId() +
                ", INCENSE_NAME: " + buttonEnum.getIncenseName());

        Intent intent = new Intent(MainActivity.this, MinuteActivity.class);
        intent.putExtra("EXTRA_TEXT", buttonEnum.getText());
        intent.putExtra("EXTRA_IMAGE", buttonEnum.getImageResId());
        intent.putExtra("EXTRA_URL", buttonEnum.getUrl());

        // **确保 `INCENSE_ID` 和 `INCENSE_NAME` 正确**
        intent.putExtra("INCENSE_ID", buttonEnum.name());
        intent.putExtra("INCENSE_NAME", buttonEnum.getIncenseName());

        startActivity(intent);
    }
}
