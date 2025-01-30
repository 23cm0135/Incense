package jec.ac.jp.incense;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MinuteActivity extends AppCompatActivity {

    private TextView textView;
    private String url;
    private String incenseId, incenseName; // ✅ 添加变量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_minute);

        // 設置窗口Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 獲取傳遞的數據
        String text = getIntent().getStringExtra("EXTRA_TEXT");
        int imageResId = getIntent().getIntExtra("EXTRA_IMAGE", R.drawable.default_image);

        // 初始化 URL，如果未传递则使用默认链接
        url = getIntent().getStringExtra("EXTRA_URL");
        if (url == null || url.isEmpty()) {
            url = "https://www.google.com";  // 默认值
        }

        // ✅ 获取 `incenseId` 和 `incenseName`
        incenseId = getIntent().getStringExtra("EXTRA_INCENSE_ID");
        incenseName = getIntent().getStringExtra("EXTRA_INCENSE_NAME");

        // ✅ 确保它们不是 null
        if (incenseId == null) incenseId = "unknown_id";
        if (incenseName == null) incenseName = "不明な香";

        // 設置文本和圖片
        textView = findViewById(R.id.textView);
        textView.setText(text);

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageResource(imageResId);

        // 投稿按鈕點擊事件
        Button btnSubmitImpression = findViewById(R.id.btnSubmitImpression);
        btnSubmitImpression.setOnClickListener(v -> {
            Intent intent = new Intent(MinuteActivity.this, UserImpression.class);

            // ✅ 传递 `incenseId` 和 `incenseName`
            intent.putExtra("INCENSE_ID", incenseId);
            intent.putExtra("INCENSE_NAME", incenseName);

            startActivityForResult(intent, 1);
        });

        // 购买按钮点击事件（示例跳转到指定 URL）
        Button btnPurchase = findViewById(R.id.btnPurchase);
        btnPurchase.setOnClickListener(v -> {
            Intent purchaseIntent = new Intent(Intent.ACTION_VIEW);
            purchaseIntent.setData(Uri.parse(url));
            startActivity(purchaseIntent);
        });

        // 查看其他用户投稿的按钮点击事件
        Button btnViewPosts = findViewById(R.id.btnViewPosts);
        btnViewPosts.setOnClickListener(v -> {
            Intent intent = new Intent(MinuteActivity.this, UserImpressionListActivity.class);
            startActivity(intent);
        });
    }

    // 處理從投稿頁面返回的數據
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String userEmail = data.getStringExtra("USER_NAME");
            String submittedText = data.getStringExtra("IMPRESSION_RESULT");
            if (userEmail != null && submittedText != null && !submittedText.isEmpty()) {
                textView.append("\n\n" + userEmail + " の投稿:\n" + submittedText);
            }
        }
    }
}
