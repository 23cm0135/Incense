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

        // UI 元件綁定
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

        // 「購入」按钮始终可用
        openUrlButton.setOnClickListener(v -> {
            if (url == null || url.isEmpty()) {
                Toast.makeText(this, "無効なURLです", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        });

        // 添加到浏览历史
        addToBrowsingHistory();

        // 檢查是否已收藏（僅對登錄用戶有效）
        if (currentUser != null) {
            checkIfFavorite();
        }

        // 如果未登錄，投稿與お気に入り按鈕僅彈提示
        if (currentUser == null) {
            userImpressionButton.setOnClickListener(v ->
                    Toast.makeText(IncenseDetailActivity.this, "ログインしてください", Toast.LENGTH_SHORT).show());
            favoriteButton.setOnClickListener(v ->
                    Toast.makeText(IncenseDetailActivity.this, "ログインしてください", Toast.LENGTH_SHORT).show());
        } else {
            // 登錄狀態：正常進行投稿與お気に入り操作
            userImpressionButton.setOnClickListener(v -> {
                Intent intent = new Intent(IncenseDetailActivity.this, UserImpression.class);
                intent.putExtra("INCENSE_NAME", incenseName);
                startActivity(intent);
            });
            favoriteButton.setOnClickListener(v -> addToFavorites());
        }

        // 「他のユーザーの投稿」按鈕（不受登錄限制）
        btnViewPosts.setOnClickListener(v -> {
            Intent intent = new Intent(IncenseDetailActivity.this, UserImpressionListActivity.class);
            // 這裡傳送香的名稱，如果需要用 incenseId 請自行傳送對應的參數
            intent.putExtra("INCENSE_NAME", incenseName);
            startActivity(intent);
        });
    }

    private void addToBrowsingHistory() {
        if (currentUser == null) return;

        FavoriteItem item = new FavoriteItem(incenseName, effect, imageUrl, description, url);

        db.collection("users")
                .document(currentUser.getUid())
                .collection("history")
                .document(incenseName)
                .set(item)
                .addOnSuccessListener(aVoid ->
                        // 日誌輸出
                        Log.d("Firestore", "浏览历史添加成功: " + incenseName)
                )
                .addOnFailureListener(e ->
                        Log.e("Firestore", "浏览历史添加失败", e)
                );
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
                .addOnFailureListener(e ->
                        Log.e("Firestore", "お気に入りチェック失败", e)
                );
    }

    private void addToFavorites() {
        if (currentUser == null) {
            Toast.makeText(this, "ログインしてください", Toast.LENGTH_SHORT).show();
            return;
        }
        FavoriteItem item = new FavoriteItem(incenseName, effect, imageUrl, description, url);

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
                        Log.e("Firestore", "お気に入り保存失败", e)
                );
    }

    private void setButtonAsFavorited() {
        favoriteButton.setEnabled(false);
        favoriteButton.setText("お気に入り済み");
        // 將按鈕背景設置為灰色
        favoriteButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
    }
}
