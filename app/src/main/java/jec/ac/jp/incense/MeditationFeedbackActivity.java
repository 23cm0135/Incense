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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
        EdgeToEdge.enable(this);

        meditationDuration = getIntent().getLongExtra("meditationDuration", -1);
        String usedIncense = getIntent().getStringExtra("usedIncense");

        if (meditationDuration == -1) {
            Toast.makeText(this, "エラー: 冥想時間を取得できませんでした。", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadMeditationProducts();
        etUsedIncense = findViewById(R.id.etUsedIncense);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String spName;
        if (currentUser != null) {
            spName = "MeditationRecords_" + currentUser.getUid();
        } else {

            spName = "MeditationRecords";
        }
        sharedPreferences = getSharedPreferences(spName, Context.MODE_PRIVATE);

        rgDistraction = findViewById(R.id.rgDistraction);
        rbNoDistraction = findViewById(R.id.rbNoDistraction);
        rbLittleDistraction = findViewById(R.id.rbLittleDistraction);
        rbMuchDistraction = findViewById(R.id.rbMuchDistraction);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnDiscard = findViewById(R.id.btnDiscard);

        btnSave.setOnClickListener(v -> {
            saveMeditationRecord();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("lastMeditationDiscarded", false);
            editor.apply();
            Log.d("DEBUG", "📌 冥想已保存，废弃状态清除: " + sharedPreferences.getBoolean("lastMeditationDiscarded", false));
        });

        btnDiscard.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("lastMeditationDiscarded", true);
            editor.putString("lastDistractionLevel", "");
            editor.apply();
            Log.d("DEBUG", "📌 冥想被废弃: " + sharedPreferences.getBoolean("lastMeditationDiscarded", false));
            Intent discardIntent = new Intent();
            discardIntent.putExtra("meditationDiscarded", true);
            setResult(RESULT_CANCELED, discardIntent);
            finish();
        });
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

        String usedIncenseInput = etUsedIncense.getText().toString().trim();
        if (usedIncenseInput.isEmpty()) {
            usedIncenseInput = "不明";
        }

        // 由于已经要求登录，所以当前用户一定存在
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String username = currentUser.getDisplayName();
        if (username == null || username.isEmpty()) {
            username = currentUser.getEmail();
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        String record = "ユーザー: " + username +
                " | 時間: " + (meditationDuration / 60) + "分 " + (meditationDuration % 60) +
                "秒 | 使用した香: " + usedIncenseInput +
                " | 雑念: " + distractionLevel;
        editor.putString(timestamp, record);
        editor.putString("lastDistractionLevel", distractionLevel);
        editor.apply();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("meditationSuggestion", distractionLevel);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void loadMeditationProducts() {
        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("products.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, StandardCharsets.UTF_8);
            ProductResponse productResponse = new Gson().fromJson(json, ProductResponse.class);
            if (productResponse == null || productResponse.items == null || productResponse.items.isEmpty()) {
                return;
            }
            List<Product> meditationProducts = new ArrayList<>();
            for (Product product : productResponse.items) {
                if ("瞑想".equals(product.effect)) {
                    meditationProducts.add(product);
                }
            }
            if (!meditationProducts.isEmpty()) {
                Random random = new Random();
                int randomIndex = random.nextInt(meditationProducts.size());
                displayProductRecommendation(meditationProducts.get(randomIndex));
            }
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void displayProductRecommendation(Product product) {
        LinearLayout productLayout = findViewById(R.id.productLayout);
        productLayout.setVisibility(View.VISIBLE);
        TextView productName = findViewById(R.id.productName);
        ImageView productImage = findViewById(R.id.productImage);
        TextView productDesc = findViewById(R.id.productDesc);
        Button btnBuyProduct = findViewById(R.id.btnBuyProduct);
        productName.setText(product.name);
        productDesc.setText(product.description);
        btnBuyProduct.setVisibility(View.VISIBLE);
        btnBuyProduct.setOnClickListener(v -> openProductLink(product.url));
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

    private class Product {
        String name;
        String effect;
        String material;
        String imageUrl;
        String description;
        String url;
    }

    private class ProductResponse {
        List<Product> items;
    }
}
