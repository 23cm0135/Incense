package jec.ac.jp.incense;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MinuteActivity extends AppCompatActivity {

    private static final String TAG = "MinuteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minute);

        Log.d(TAG, "Activity layout loaded");

        // 获取传递的数据
        String text = getIntent().getStringExtra("EXTRA_TEXT");
        int imageResId = getIntent().getIntExtra("EXTRA_IMAGE", R.drawable.default_image);
        String url = getIntent().getStringExtra("EXTRA_URL");

        // 设置默认文本
        TextView textView = findViewById(R.id.textView);
        if (text == null || text.isEmpty()) {
            text = "Default Text";
        }
        textView.setText(text);

        // 设置默认图片
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageResource(imageResId);

        // 按钮逻辑
        Button openLinkButton = findViewById(R.id.openProductLinkButton);
        if (openLinkButton == null) {
            Log.e(TAG, "Button not found in layout");
            return;
        }

        if (url == null || url.isEmpty() || !url.startsWith("http")) {
            Log.w(TAG, "Invalid URL, disabling button");
            openLinkButton.setEnabled(false); // 禁用按钮
        } else {
            openLinkButton.setVisibility(View.VISIBLE);
            openLinkButton.setOnClickListener(v -> {
                Log.d(TAG, "Opening URL: " + url);
                Intent intentUrl = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (intentUrl.resolveActivity(getPackageManager()) != null) {
                    startActivity(intentUrl);
                } else {
                    Log.e(TAG, "No application can handle this URL");
                }
            });
        }
    }
}
