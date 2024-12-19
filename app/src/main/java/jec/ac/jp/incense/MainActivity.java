package jec.ac.jp.incense;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        // 按钮对应的布局 ID
        int[] buttonIds = {
                R.id.imgbutton1, R.id.imgbutton2, R.id.imgbutton3,
                R.id.imgbutton4, R.id.imgbutton5, R.id.imgbutton6,
                R.id.imgbutton7, R.id.imgbutton8, R.id.imgbutton9
        };

        // 为每个按钮设置点击事件
        for (int i = 0; i < buttonIds.length; i++) {
            ImageButton button = findViewById(buttonIds[i]);
            ButtonEnum buttonEnum = ButtonEnum.values()[i]; // 获取相应的枚举值
            button.setOnClickListener(v -> navigateToMinute(buttonEnum.getText(), buttonEnum.getImageResId(), buttonEnum.getUrl()));
        }

        ImageButton userButton = findViewById(R.id.btn_user);
        userButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, User.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        ImageButton question = findViewById(R.id.btn_app);
        question.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Question.class);
            startActivity(intent);
        });
    }

    /**
     * 跳转到 MinuteActivity 的方法
     *
     * @param text      传递的文本内容
     * @param imageResId 图片资源 ID
     * @param url       链接地址
     */
    private void navigateToMinute(String text, int imageResId, String url) {
        Intent intent = new Intent(this, MinuteActivity.class);
        intent.putExtra("EXTRA_TEXT", text);
        intent.putExtra("EXTRA_IMAGE", imageResId);
        intent.putExtra("EXTRA_URL", url); // 传递链接
        startActivity(intent);
    }
}
