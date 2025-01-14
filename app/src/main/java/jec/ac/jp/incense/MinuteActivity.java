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
        String url = getIntent().getStringExtra("EXTRA_URL"); // 获取传递的 URL

        // 设置文本和图片
        TextView textView = findViewById(R.id.textView);
        textView.setText(text);

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageResource(imageResId);

        // 处理滚动事件，显示链接按钮
        ScrollView scrollView = findViewById(R.id.scrollView);
        Button openLinkButton = findViewById(R.id.openLinkButton); // 获取链接按钮
        openLinkButton.setVisibility(View.GONE); // 默认不显示链接按钮

        // 滚动监听：滚动到底部时显示链接按钮
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (!scrollView.canScrollVertically(1)) { // 判断是否滚动到最底部
                openLinkButton.setVisibility(View.VISIBLE); // 显示链接按钮
            }
        });
        //TODO： 收藏按钮
//        ImageButton favoriteButton = findViewById(R.id.favoriteButton);
//        favoriteButton.setOnClickListener(v -> {
//
//        });

        // 如果有 URL，设置点击事件打开链接
        if (url != null && !url.isEmpty()) {
            openLinkButton.setOnClickListener(v -> {
                // 创建 Intent 打开链接
                Intent intentUrl = new Intent(Intent.ACTION_VIEW);
                intentUrl.setData(Uri.parse(url)); // 使用传递的 URL
                startActivity(intentUrl);
            });
        }
    }
}
