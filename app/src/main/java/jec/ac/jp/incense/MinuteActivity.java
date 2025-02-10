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
    private String incenseId, incenseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_minute);

        // 系統視窗邊距設定 (可選)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 取得從前一個 Activity 傳遞的資料
        String text = getIntent().getStringExtra("EXTRA_TEXT");
        int imageResId = getIntent().getIntExtra("EXTRA_IMAGE", R.drawable.default_image);

        // 初始化 URL (若沒傳遞，使用預設)
        url = getIntent().getStringExtra("EXTRA_URL");
        if (url == null || url.isEmpty()) {
            url = "https://www.google.com";  // 預設值
        }

        // 取得 incenseId & incenseName
        incenseId = getIntent().getStringExtra("EXTRA_INCENSE_ID");
        incenseName = getIntent().getStringExtra("EXTRA_INCENSE_NAME");
        if (incenseId == null)  incenseId = "unknown_id";
        if (incenseName == null)  incenseName = "不明な香";

        // 設置文本與圖片
        textView = findViewById(R.id.textView);
        textView.setText(text);

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageResource(imageResId);

        // 「投稿」按鈕
        Button btnSubmitImpression = findViewById(R.id.btnSubmitImpression);
        btnSubmitImpression.setOnClickListener(v -> {
            // 跳轉至 UserImpression
            Intent intent = new Intent(MinuteActivity.this, UserImpression.class);
            // 傳遞 incenseId / incenseName
            intent.putExtra("INCENSE_ID", incenseId);
            intent.putExtra("INCENSE_NAME", incenseName);

            // 若你想要在返回後處理回傳值，可用 startActivityForResult
            startActivityForResult(intent, 1);
        });

        // 「購買」按鈕 -> 跳轉至該 URL
        Button btnPurchase = findViewById(R.id.btnPurchase);
        btnPurchase.setOnClickListener(v -> {
            Intent purchaseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(purchaseIntent);
        });

        // 「查看其他用戶投稿」按鈕
        Button btnViewPosts = findViewById(R.id.btnViewPosts);
        btnViewPosts.setOnClickListener(v -> {
            Intent intent = new Intent(MinuteActivity.this, UserImpressionListActivity.class);
            startActivity(intent);
        });
    }

    // 若要處理從 UserImpression 返回的資料
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
