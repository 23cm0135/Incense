package jec.ac.jp.incense;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    private EditText etUsedIncense;
    private RadioButton rbNoDistraction, rbLittleDistraction, rbMuchDistraction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation_feedback);
        // 启用 Edge-to-Edge 显示效果
        EdgeToEdge.enable(this);

        // ✅ 确保正确获取冥想时间
        Intent intent = getIntent();
        // ✅ 获取一次，不重复定义
        meditationDuration = getIntent().getLongExtra("meditationDuration", -1);
        String usedIncense = getIntent().getStringExtra("usedIncense");

        if (meditationDuration == -1) {
            Toast.makeText(this, "エラー: 冥想時間を取得できませんでした。", Toast.LENGTH_SHORT).show();
            finish(); // **防止错误数据进入**
            return;
        }

        loadMeditationProducts(); // **加载推荐产品**
        etUsedIncense = findViewById(R.id.etUsedIncense); // **获取输入框**

        sharedPreferences = getSharedPreferences("MeditationRecords", Context.MODE_PRIVATE);
        //meditationDuration = getIntent().getLongExtra("meditationDuration", 0);

        rgDistraction = findViewById(R.id.rgDistraction);
        rbNoDistraction = findViewById(R.id.rbNoDistraction);
        rbLittleDistraction = findViewById(R.id.rbLittleDistraction);
        rbMuchDistraction = findViewById(R.id.rbMuchDistraction);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnDiscard = findViewById(R.id.btnDiscard);

        // ✅ 绑定点击事件
        btnSave.setOnClickListener(v -> {
            saveMeditationRecord();

            SharedPreferences sharedPreferences = getSharedPreferences("MeditationRecords", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putBoolean("lastMeditationDiscarded", false); // ✅ 只在保存时清除废弃状态
            editor.apply();

            Log.d("DEBUG", "📌 冥想已保存，废弃状态清除: " + sharedPreferences.getBoolean("lastMeditationDiscarded", false));
        });

//        btnDiscard.setOnClickListener(v -> discardMeditationRecord());
        btnDiscard.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("MeditationRecords", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putBoolean("lastMeditationDiscarded", true); // ✅ 只在废弃时存储废弃状态
            editor.putString("lastDistractionLevel", ""); // ✅ 清除杂念记录，防止错误弹窗
            editor.apply();

            // ✅ 添加日志，确保状态正确存储
            Log.d("DEBUG", "📌 冥想被废弃: " + sharedPreferences.getBoolean("lastMeditationDiscarded", false));

            Intent discardIntent = new Intent();
            discardIntent.putExtra("meditationDiscarded", true);
            setResult(RESULT_CANCELED, discardIntent);
            finish();
        });


//        btnDiscard.setOnClickListener(v -> {
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putBoolean("lastMeditationDiscarded", true);
//            editor.apply();
//
//            Intent discardIntent = new Intent();
//            discardIntent.putExtra("meditationDiscarded", true);
//            editor.putString("lastDistractionLevel", ""); // ✅ 清除上次的杂念信息，防止下次误弹窗
//
//            setResult(RESULT_CANCELED, discardIntent);
//            finish();
//        });
    }
    private void saveMeditationRecord() {
        if (meditationDuration <= 0) {
            Toast.makeText(this, "エラー: 冥想時間が取得できませんでした。", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedId = rgDistraction.getCheckedRadioButtonId();
        String distractionLevel = "なし"; // 默认无杂念
        if (selectedId == -1) {
            Toast.makeText(this, "エラー: 雑念レベルを選択してください。", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedId == R.id.rbMuchDistraction) {
            distractionLevel = "多い";
        } else if (selectedId == R.id.rbLittleDistraction) {
            distractionLevel = "少し";
        }

        // ✅ 获取用户输入的香
        String usedIncense = etUsedIncense.getText().toString().trim();
        if (usedIncense.isEmpty()) {
            usedIncense = "不明";
        }

        // ✅ 存储冥想记录
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        String record = "時間: " + (meditationDuration / 60) + "分 " + (meditationDuration % 60) + "秒 | 使用した香: " + usedIncense + " | 雑念: " + distractionLevel;

        editor.putString(timestamp, record);
        editor.putString("lastDistractionLevel", distractionLevel); // ✅ 记录上次杂念情况
        editor.apply();

        // ✅ 返回 `TimerActivity`
        Intent resultIntent = new Intent();
        resultIntent.putExtra("meditationSuggestion", distractionLevel);
        setResult(RESULT_OK, resultIntent);
        finish();
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
//    private void discardMeditationRecord() {
//        Intent discardIntent = new Intent(); // ✅ 重新创建 Intent
//        discardIntent.putExtra("meditationDiscarded", true);
//        setResult(RESULT_CANCELED, discardIntent);
//        finish();
//    }


}
