package jec.ac.jp.incense;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class IncenseDetailActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    private String incenseName, effect, imageUrl, description, url;
    private Button favoriteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incense_detail);
        EdgeToEdge.enable(this);

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // 從 Intent 中取得數據
        incenseName = getIntent().getStringExtra("name");
        effect = getIntent().getStringExtra("effect");
        imageUrl = getIntent().getStringExtra("imageUrl");
        description = getIntent().getStringExtra("description");
        url = getIntent().getStringExtra("url");

        // 如果缺少香名或URL則給予默認值
        if (incenseName == null || incenseName.isEmpty()) {
            incenseName = "不明な香";
        }
        if (url == null || url.isEmpty()) {
            url = "https://www.google.com";
        }

        TextView nameTextView = findViewById(R.id.incense_detail_name);
        TextView descriptionTextView = findViewById(R.id.incense_detail_description);
        ImageView imageView = findViewById(R.id.incense_detail_image);
        favoriteButton = findViewById(R.id.favorite_button);
        Button openUrlButton = findViewById(R.id.open_url_button);
        Button btnViewPosts = findViewById(R.id.btnViewPosts);
        Button userImpressionButton = findViewById(R.id.UserImpression);

        nameTextView.setText(incenseName);
        descriptionTextView.setText(description);

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(imageView);

        // 點擊“詳細連結”按鈕，打開網頁
        openUrlButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });

        // 將當前瀏覽記錄存入 Firestore
        addToBrowsingHistory();

        // 檢查用戶是否登入
        if (currentUser == null) {
            // 若未登入，禁用收藏和用戶評論按鈕
            userImpressionButton.setEnabled(false);
            userImpressionButton.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.darker_gray));
            userImpressionButton.setOnClickListener(v ->
                    Toast.makeText(IncenseDetailActivity.this, "ログインしてください", Toast.LENGTH_SHORT).show());
            favoriteButton.setEnabled(false);
            favoriteButton.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.darker_gray));
            favoriteButton.setOnClickListener(v ->
                    Toast.makeText(IncenseDetailActivity.this, "ログインしてください", Toast.LENGTH_SHORT).show());
        } else {
            // 登入狀態下，點擊用戶評論按鈕跳轉到 UserImpression 頁面
            userImpressionButton.setOnClickListener(v -> {
                Intent intent = new Intent(IncenseDetailActivity.this, UserImpression.class);
                intent.putExtra("INCENSE_NAME", incenseName);
                startActivity(intent);
            });
            // 點擊收藏按鈕則添加到收藏
            favoriteButton.setOnClickListener(v -> addToFavorites());
        }

        // 點擊“查看評論”按鈕跳轉到 UserImpressionListActivity 頁面
        btnViewPosts.setOnClickListener(v -> {
            Intent intent = new Intent(IncenseDetailActivity.this, UserImpressionListActivity.class);
            intent.putExtra("INCENSE_NAME", incenseName);
            startActivity(intent);
        });
    }

    /**
     * 將當前瀏覽的香記錄存入 Firestore 的 "history" 子集合中
     */
    private void addToBrowsingHistory() {
        if (currentUser == null) return;

        // 建立一個新的 FavoriteItem 對象用於記錄瀏覽歷史（此處依然使用 FavoriteItem 類）
        FavoriteItem item = new FavoriteItem(incenseName, effect, imageUrl, description, url);
        // 設定當前用戶的 UID 至 userId 欄位
        item.setUserId(currentUser.getUid());
        // 設定 Firestore 伺服器時間為 timestamp
        item.setTimestamp(Timestamp.now());

        // 儲存到用戶的 "history" 子集合中，文檔ID使用 incenseName
        db.collection("users")
                .document(currentUser.getUid())
                .collection("history")
                .document(incenseName)
                .set(item)
                .addOnSuccessListener(aVoid -> {
                    Log.d("TimestampDebug", "Browsing history saved with timestamp: " + item.getTimestamp());
                })
                .addOnFailureListener(e ->
                        Log.e("Firestore", "Error saving browsing history", e)
                );
    }

    /**
     * 將當前香品添加到收藏中
     */
    private void addToFavorites() {
        if (currentUser == null) {
            Toast.makeText(this, "ログインしてください", Toast.LENGTH_SHORT).show();
            return;
        }
        // 建立 FavoriteItem 並設定相應欄位
        FavoriteItem item = new FavoriteItem(incenseName, effect, imageUrl, description, url);
        // 設定收藏該香的用戶 UID
        item.setUserId(currentUser.getUid());
        // 設定收藏時間戳記
        item.setTimestamp(Timestamp.now());

        db.collection("users")
                .document(currentUser.getUid())
                .collection("favorites")
                .document(incenseName)
                .set(item)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "お気に入りに追加しました: " + incenseName, Toast.LENGTH_SHORT).show();
                    setButtonAsFavorited();
                })
                .addOnFailureListener(e ->
                        Log.e("Firestore", "Error saving favorite", e)
                );
    }

    /**
     * 收藏成功後，禁用收藏按鈕並更新按鈕顯示
     */
    private void setButtonAsFavorited() {
        favoriteButton.setEnabled(false);
        favoriteButton.setText("お気に入り済み");
        favoriteButton.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.darker_gray));
    }
}
