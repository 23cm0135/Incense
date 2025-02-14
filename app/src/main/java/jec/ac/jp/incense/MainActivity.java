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

        // **獲取隨機 9 個商品**
        List<ButtonEnum> randomButtons = ButtonEnum.getRandomButtons();

        // **與 UI 按鈕綁定**
        int[] buttonIds = {
                R.id.imgbutton1, R.id.imgbutton2, R.id.imgbutton3,
                R.id.imgbutton4, R.id.imgbutton5, R.id.imgbutton6,
                R.id.imgbutton7, R.id.imgbutton8, R.id.imgbutton9
        };

        for (int i = 0; i < randomButtons.size(); i++) {
            ImageButton button = findViewById(buttonIds[i]);
            final ButtonEnum buttonEnum = randomButtons.get(i);

            // **設置圖片**
            button.setImageResource(buttonEnum.getImageResId());

            // **點擊事件**
            button.setOnClickListener(v -> navigateToMinute(buttonEnum));
        }

        // 问题按钮点击事件
        ImageButton question = findViewById(R.id.btn_app);
        question.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Question.class);
            startActivity(intent);
        });
    }

    /**
     * 跳转到 MinuteActivity 的方法
     *
     * @param buttonEnum 选中的 `ButtonEnum` 对象
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

        // **確認 `INCENSE_ID` 和 `INCENSE_NAME` 是否正確**
        intent.putExtra("INCENSE_ID", buttonEnum.name()); // ✅ 確保傳遞 ButtonEnum 的 ID
        intent.putExtra("INCENSE_NAME", buttonEnum.getIncenseName());

        startActivity(intent);
    }

}
