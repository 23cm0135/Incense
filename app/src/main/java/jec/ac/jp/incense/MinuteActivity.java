package jec.ac.jp.incense;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MinuteActivity extends AppCompatActivity {

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_minute);

        // 设置窗口Insets，处理边距问题
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 获取传递的数据
        String text = getIntent().getStringExtra("EXTRA_TEXT");
        int imageResId = getIntent().getIntExtra("EXTRA_IMAGE", R.drawable.default_image);
        // 初始化 url
        url = getIntent().getStringExtra("EXTRA_URL");
        if (url == null || url.isEmpty()) {
            url = "https://www.bilibili.com"; // 设置默认值
        }

        // 设置文本和图片
        TextView textView = findViewById(R.id.textView);
        textView.setText(text);

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageResource(imageResId);

        // 获取购买按钮
        Button btnPurchase = findViewById(R.id.btnPurchase);

        // 设置按钮点击事件
        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url)); // 可以访问成员变量
                startActivity(intent);

                // 启动浏览器
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    // 如果没有应用能够处理Intent，提示用户
                    // 例如显示Toast
                }
            }
        });
    }
}

