package jec.ac.jp.incense;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class IncenseDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incense_detail);

        // 获取传递的数据
        String name = getIntent().getStringExtra("name");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String description = getIntent().getStringExtra("description");
        String url = getIntent().getStringExtra("url"); // 获取 URL

        // 初始化视图
        TextView nameTextView = findViewById(R.id.incense_detail_name);
        ImageView imageView = findViewById(R.id.incense_detail_image);
        TextView descriptionTextView = findViewById(R.id.incense_detail_description);
        Button openUrlButton = findViewById(R.id.open_url_button);

        // 设置数据到视图
        nameTextView.setText(name);
        Glide.with(this).load(imageUrl).into(imageView);
        descriptionTextView.setText(description);

        // 设置按钮点击事件，打开浏览器
        openUrlButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        });
    }
}
