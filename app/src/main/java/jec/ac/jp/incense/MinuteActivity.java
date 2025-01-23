package jec.ac.jp.incense;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MinuteActivity extends AppCompatActivity {

    private TextView textView;

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

        // 設置文本和圖片
        textView = findViewById(R.id.textView);
        textView.setText(text);

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageResource(imageResId);

        // 投稿按鈕點擊事件
        Button btnSubmitImpression = findViewById(R.id.btnSubmitImpression);
        btnSubmitImpression.setOnClickListener(v -> {
            Intent intent = new Intent(MinuteActivity.this, UserImpression.class);
            startActivityForResult(intent, 1);
        });

        // 购买按钮点击事件（示例跳转到 Google 搜索）
        Button btnPurchase = findViewById(R.id.btnPurchase);
        btnPurchase.setOnClickListener(v -> {
            Intent purchaseIntent = new Intent(Intent.ACTION_VIEW);
            purchaseIntent.setData(Uri.parse("https://www.google.com/search?q=incense+buy"));
            startActivity(purchaseIntent);
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
