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
import com.google.firebase.firestore.DocumentSnapshot;

public class MinuteActivity extends AppCompatActivity {

    private String url;
    private String incenseId, incenseName;
    private String text;
    private String imageUrl;
    private Button btnFavorite;

    private FirebaseUser currentUser;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minute);
        EdgeToEdge.enable(this);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // 取得 Intent 数据
        text = getIntent().getStringExtra("EXTRA_TEXT");
        imageUrl = getIntent().getStringExtra("EXTRA_IMAGE_URL");
        url = getIntent().getStringExtra("EXTRA_URL");
        if (url == null || url.isEmpty()) {
            url = "https://www.google.com";
        }
        incenseId = getIntent().getStringExtra("INCENSE_ID");
        incenseName = getIntent().getStringExtra("INCENSE_NAME");
        if (incenseId == null) incenseId = "unknown_id";
        if (incenseName == null) incenseName = "不明な香";

        // 设置 UI
        TextView nameTextView = findViewById(R.id.incense_detail_name);
        TextView descriptionTextView = findViewById(R.id.incense_detail_description);
        ImageView imageView = findViewById(R.id.incense_detail_image);

        nameTextView.setText(incenseName);
        descriptionTextView.setText(text);

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(imageView);

        // 「購入」按钮 (始终可用)
        Button btnPurchase = findViewById(R.id.btnPurchase);
        btnPurchase.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });

        // 「投稿する」按钮
        Button btnSubmitImpression = findViewById(R.id.btnSubmitImpression);
        // 「お気に入りに追加」按钮
        btnFavorite = findViewById(R.id.btnFavorite);
        // 「他のユーザーの投稿」按钮 (此處保持不變)
        Button btnViewPosts = findViewById(R.id.btnViewPosts);
        btnViewPosts.setOnClickListener(v -> {
            Intent intent = new Intent(MinuteActivity.this, UserImpressionListActivity.class);
            // 若需要也可傳送 incenseName，但這裡主要傳送 incenseId
            intent.putExtra("INCENSE_ID", incenseId);
            intent.putExtra("INCENSE_NAME", incenseName);
            startActivity(intent);
        });

        // 未登入狀態下，禁用「投稿する」與「お気に入りに追加」按鈕
        if (currentUser == null) {
            btnSubmitImpression.setOnClickListener(v ->
                    Toast.makeText(MinuteActivity.this, "ログインしてください", Toast.LENGTH_SHORT).show());
            btnFavorite.setOnClickListener(v ->
                    Toast.makeText(MinuteActivity.this, "ログインしてください", Toast.LENGTH_SHORT).show());
        } else {
            // 登入狀態：設置正常行為
            btnSubmitImpression.setOnClickListener(v -> {
                Intent intent = new Intent(MinuteActivity.this, UserImpression.class);
                intent.putExtra("INCENSE_ID", incenseId);
                intent.putExtra("INCENSE_NAME", incenseName);
                startActivity(intent);
            });
            checkIfFavorited();
            btnFavorite.setOnClickListener(v -> addToFavorites());
        }

        addToBrowsingHistory();
    }

    private void addToBrowsingHistory() {
        if (currentUser == null) return;

        FavoriteItem item = new FavoriteItem(incenseName, "", imageUrl, text, url);

        db.collection("users")
                .document(currentUser.getUid())
                .collection("history")
                .document(incenseName)
                .set(item)
                .addOnSuccessListener(aVoid ->
                        Log.d("Firestore", "浏览历史添加成功: " + incenseName)
                )
                .addOnFailureListener(e ->
                        Log.e("Firestore", "浏览历史添加失败", e)
                );
    }

    private void addToFavorites() {
        if (currentUser == null) {
            Toast.makeText(this, "ログインしてください", Toast.LENGTH_SHORT).show();
            return;
        }
        FavoriteItem item = new FavoriteItem(incenseName, "", imageUrl, text, url);

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

    private void checkIfFavorited() {
        if (currentUser == null) return;

        db.collection("users")
                .document(currentUser.getUid())
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

    private void setButtonAsFavorited() {
        btnFavorite.setEnabled(false);
        btnFavorite.setText("お気に入り済み");
        btnFavorite.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.darker_gray));
    }
}
