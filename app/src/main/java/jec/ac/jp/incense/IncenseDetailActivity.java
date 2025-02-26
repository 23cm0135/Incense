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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class IncenseDetailActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    private String incenseName, effect, imageUrl, description, url;
    private Button favoriteButton;
    private Button btnViewPosts;  // 新增這個變數來修正無法點擊問題

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incense_detail);
        EdgeToEdge.enable(this);

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // 取得 Intent 数据
        incenseName = getIntent().getStringExtra("name");
        effect = getIntent().getStringExtra("effect");
        imageUrl = getIntent().getStringExtra("imageUrl");
        description = getIntent().getStringExtra("description");
        url = getIntent().getStringExtra("url");

        // 綁定 UI 元件
        TextView nameTextView = findViewById(R.id.incense_detail_name);
        ImageView imageView = findViewById(R.id.incense_detail_image);
        TextView descriptionTextView = findViewById(R.id.incense_detail_description);
        favoriteButton = findViewById(R.id.favorite_button);
        Button openUrlButton = findViewById(R.id.open_url_button);
        Button userImpressionButton = findViewById(R.id.UserImpression);
        btnViewPosts = findViewById(R.id.btnViewPosts);  // 確保這個按鈕已經正確綁定

        // 設置內容
        nameTextView.setText(incenseName != null ? incenseName : "不明な香");
        descriptionTextView.setText(description != null ? description : "説明なし");

        Glide.with(this)
                .load(imageUrl != null ? imageUrl : R.drawable.default_image)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(imageView);

        // 網址打開按鈕
        openUrlButton.setOnClickListener(v -> {
            if (url == null || url.isEmpty()) {
                Toast.makeText(this, "無効なURLです", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

        // 添加到浏览历史
        addToBrowsingHistory();

        // 检查是否已收藏
        checkIfFavorite();
        favoriteButton.setOnClickListener(v -> addToFavorites());

        // 投稿按鈕
        userImpressionButton.setOnClickListener(v -> {
            Intent intent = new Intent(IncenseDetailActivity.this, UserImpression.class);
            intent.putExtra("INCENSE_NAME", incenseName);
            startActivity(intent);
        });

        // **修正 `btnViewPosts` 無反應的問題**
        btnViewPosts.setOnClickListener(v -> {
            Log.d("IncenseDetailActivity", "Navigating to UserImpressionListActivity");
            Intent intent = new Intent(IncenseDetailActivity.this, UserImpressionListActivity.class);
            startActivity(intent);
        });
    }

    private void addToBrowsingHistory() {
        if (currentUser == null) return;

        FavoriteItem item = new FavoriteItem(
                incenseName != null ? incenseName : "不明な香",
                effect != null ? effect : "効果不明",
                imageUrl != null ? imageUrl : "",
                description != null ? description : "説明なし",
                url != null ? url : "https://www.google.com"
        );

        db.collection("users")
                .document(currentUser.getUid())
                .collection("history")
                .document(incenseName)
                .set(item)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "浏览历史添加成功: " + incenseName))
                .addOnFailureListener(e -> Log.e("Firestore", "浏览历史添加失败", e));
    }

    private void checkIfFavorite() {
        if (currentUser == null) return;

        db.collection("users").document(currentUser.getUid())
                .collection("favorites")
                .document(incenseName)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        setButtonAsFavorited();
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "お気に入り检查失败", e));
    }

    private void addToFavorites() {
        if (currentUser == null) {
            Toast.makeText(this, "ログインしてください", Toast.LENGTH_SHORT).show();
            return;
        }

        FavoriteItem item = new FavoriteItem(
                incenseName != null ? incenseName : "不明な香",
                effect != null ? effect : "効果不明",
                imageUrl != null ? imageUrl : "",
                description != null ? description : "説明なし",
                url != null ? url : "https://www.google.com"
        );

        db.collection("users")
                .document(currentUser.getUid())
                .collection("favorites")
                .document(incenseName)
                .set(item)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "お気に入りに追加: " + incenseName, Toast.LENGTH_SHORT).show();
                    setButtonAsFavorited();
                })
                .addOnFailureListener(e -> Log.e("Firestore", "お気に入り保存失败", e));
    }

    private void setButtonAsFavorited() {
        favoriteButton.setEnabled(false);
        favoriteButton.setText("お気に入り済み");
        favoriteButton.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.darker_gray));
    }
}
