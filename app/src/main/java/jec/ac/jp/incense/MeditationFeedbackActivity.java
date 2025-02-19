package jec.ac.jp.incense;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MeditationFeedbackActivity extends AppCompatActivity {
    private long meditationDuration;
    private SharedPreferences sharedPreferences;
    private RadioGroup rgDistraction;
    private RadioButton rbNoDistraction, rbLittleDistraction, rbMuchDistraction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation_feedback);
        // 启用 Edge-to-Edge 显示效果
        EdgeToEdge.enable(this);

        loadMeditationProducts(); // **加载推荐产品**

        sharedPreferences = getSharedPreferences("MeditationRecords", Context.MODE_PRIVATE);
        meditationDuration = getIntent().getLongExtra("meditationDuration", 0);

        rgDistraction = findViewById(R.id.rgDistraction);
        rbNoDistraction = findViewById(R.id.rbNoDistraction);
        rbLittleDistraction = findViewById(R.id.rbLittleDistraction);
        rbMuchDistraction = findViewById(R.id.rbMuchDistraction);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnDiscard = findViewById(R.id.btnDiscard);

        // **用户点击“保存”时**
        btnSave.setOnClickListener(v -> {
            saveMeditationRecord(meditationDuration);

            Intent intent = new Intent();
            if (rbMuchDistraction.isChecked()) {
                intent.putExtra("meditationSuggestion", "shorter"); // **下次尝试更短**
            } else if (rbNoDistraction.isChecked() || rbLittleDistraction.isChecked()) {
                intent.putExtra("meditationSuggestion", "longer"); // **下次尝试更长**
            }
            setResult(RESULT_OK, intent);
            finish();
        });

        // **用户点击“废弃”时**
        btnDiscard.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("meditationDiscarded", true); // **通知 TimerActivity 用户放弃记录**
            setResult(RESULT_CANCELED, intent);
            finish();
        });

    }

    // **保存冥想记录**
    private void saveMeditationRecord(long duration) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        editor.putString(timestamp, duration + " 秒");
        editor.apply();
    }

    private class Product {
        String name;
        String effect;
        String material;
        String imageUrl;
        String description;
        String url;
    }


    private void displayProductRecommendation(Product product) {
        LinearLayout productLayout = findViewById(R.id.productLayout);
        productLayout.setVisibility(View.VISIBLE);

        TextView productName = findViewById(R.id.productName);
        ImageView productImage = findViewById(R.id.productImage);
        TextView productDesc = findViewById(R.id.productDesc);
        Button btnBuyProduct = findViewById(R.id.btnBuyProduct); // **购买按钮**

        productName.setText(product.name);
        productDesc.setText(product.description);
        btnBuyProduct.setVisibility(View.VISIBLE); // **显示按钮**
        btnBuyProduct.setOnClickListener(v -> openProductLink(product.url));

        // **异步加载产品图片**
        new Thread(() -> {
            try {
                InputStream is = new java.net.URL(product.imageUrl).openStream();
                Drawable drawable = Drawable.createFromStream(is, "src");
                runOnUiThread(() -> productImage.setImageDrawable(drawable));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
    private void openProductLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
    private void loadMeditationProducts() {
        try {
            // **读取 assets/products.json**
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("products.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            String json = new String(buffer, StandardCharsets.UTF_8);

            // **解析 JSON**
            ProductResponse productResponse = new Gson().fromJson(json, ProductResponse.class);

            // **确保 `productResponse.items` 不为空**
            if (productResponse == null || productResponse.items == null || productResponse.items.isEmpty()) {
                return;
            }

            // **筛选所有 "effect": "瞑想" 的产品**
            List<Product> meditationProducts = new ArrayList<>();
            for (Product product : productResponse.items) {
                if ("瞑想".equals(product.effect)) {
                    meditationProducts.add(product);
                }
            }

            // **如果找到符合条件的产品，随机选择一个**
            if (!meditationProducts.isEmpty()) {
                Random random = new Random();
                int randomIndex = random.nextInt(meditationProducts.size()); // **随机选一个产品**
                displayProductRecommendation(meditationProducts.get(randomIndex));
            }
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
    }
    private class ProductResponse {
        List<Product> items;
    }

}
