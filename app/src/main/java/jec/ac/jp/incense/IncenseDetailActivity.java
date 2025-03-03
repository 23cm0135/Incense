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
    private boolean isFavorited = false; // 添加一个标志来跟踪收藏状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incense_detail);
        EdgeToEdge.enable(this);

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // 从 Intent 中获取数据
        incenseName = getIntent().getStringExtra("name");
        effect = getIntent().getStringExtra("effect");
        imageUrl = getIntent().getStringExtra("imageUrl");
        description = getIntent().getStringExtra("description");
        url = getIntent().getStringExtra("url");

        // 如果缺少香名或 URL 则给予默认值
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

        // 点击“详细链接”按钮，打开网页
        openUrlButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });

        // 将当前浏览记录存入 Firestore
        addToBrowsingHistory();

        // 检查用户是否登录
        if (currentUser == null) {
            // 若未登录，禁用收藏和用户评论按钮
            userImpressionButton.setEnabled(false);
            userImpressionButton.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.darker_gray));
            userImpressionButton.setOnClickListener(v ->
                    Toast.makeText(IncenseDetailActivity.this, "ログインしてください", Toast.LENGTH_SHORT).show());
            favoriteButton.setEnabled(false);
            favoriteButton.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.darker_gray));
            favoriteButton.setOnClickListener(v ->
                    Toast.makeText(IncenseDetailActivity.this, "ログインしてください", Toast.LENGTH_SHORT).show());
        } else {
            // 登录状态下，点击用户评论按钮跳转到 UserImpression 页面
            userImpressionButton.setOnClickListener(v -> {
                Intent intent = new Intent(IncenseDetailActivity.this, UserImpression.class);
                intent.putExtra("INCENSE_NAME", incenseName);
                startActivity(intent);
            });
            // 检查是否已收藏
            checkIfFavorited();

            // 点击收藏按钮则添加到收藏或取消收藏
            favoriteButton.setOnClickListener(v -> toggleFavorite());
        }

        // 点击“查看评论”按钮跳转到 UserImpressionListActivity 页面
        btnViewPosts.setOnClickListener(v -> {
            Intent intent = new Intent(IncenseDetailActivity.this, UserImpressionListActivity.class);
            intent.putExtra("INCENSE_NAME", incenseName);
            startActivity(intent);
        });
    }

    /**
     * 检查当前香品是否已在收藏列表中
     */
    private void checkIfFavorited() {
        if (currentUser == null) return;

        db.collection("users")
                .document(currentUser.getUid())
                .collection("favorites")
                .document(incenseName)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        isFavorited = true;
                        setButtonAsFavorited();
                    } else {
                        isFavorited = false;
                        setButtonAsNotFavorited();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Error checking favorite status", e);
                    setButtonAsNotFavorited();
                });
    }

    /**
     * 切换收藏状态：添加到收藏或取消收藏
     */
    private void toggleFavorite() {
        if (currentUser == null) {
            Toast.makeText(this, "ログインしてください", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isFavorited) {
            // 取消收藏
            db.collection("users")
                    .document(currentUser.getUid())
                    .collection("favorites")
                    .document(incenseName)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "お気に入りから削除しました: " + incenseName, Toast.LENGTH_SHORT).show();
                        isFavorited = false;
                        setButtonAsNotFavorited();
                    })
                    .addOnFailureListener(e ->
                            Log.e("Firestore", "Error deleting favorite", e)
                    );
        } else {
            // 添加到收藏
            addToFavorites();
        }
    }

    /**
     * 将当前浏览的香记录存入 Firestore 的 "history" 子集合中
     */
    private void addToBrowsingHistory() {
        if (currentUser == null) return;

        // 建立一个新的 FavoriteItem 对象用于记录浏览历史（此处依然使用 FavoriteItem 类）
        FavoriteItem item = new FavoriteItem(incenseName, effect, imageUrl, description, url);
        // 设定当前用户的 UID 至 userId 栏位
        item.setUserId(currentUser.getUid());
        // 设定 Firestore 服务器时间为 timestamp
        item.setTimestamp(Timestamp.now());

        // 储存到用户的 "history" 子集合中，文档 ID 使用 incenseName
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
     * 将当前香品添加到收藏中
     */
    private void addToFavorites() {
        if (currentUser == null) {
            Toast.makeText(this, "ログインしてください", Toast.LENGTH_SHORT).show();
            return;
        }
        // 建立 FavoriteItem 并设定相应栏位
        FavoriteItem item = new FavoriteItem(incenseName, effect, imageUrl, description, url);
        // 设定收藏该香的用户 UID
        item.setUserId(currentUser.getUid());
        // 设定收藏时间戳记
        item.setTimestamp(Timestamp.now());

        db.collection("users")
                .document(currentUser.getUid())
                .collection("favorites")
                .document(incenseName)
                .set(item)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "お気に入りに追加しました: " + incenseName, Toast.LENGTH_SHORT).show();
                    isFavorited = true;
                    setButtonAsFavorited();
                })
                .addOnFailureListener(e ->
                        Log.e("Firestore", "Error saving favorite", e)
                );
    }

    /**
     * 收藏成功后，禁用收藏按钮并更新按钮显示为“已收藏”状态
     */
    private void setButtonAsFavorited() {
        favoriteButton.setEnabled(true); // 启用按钮，以便可以取消收藏
        favoriteButton.setText("お気に入り済み");
        favoriteButton.setBackgroundTintList(ContextCompat.getColorStateList(this, android.R.color.darker_gray));
    }

    /**
     * 取消收藏后，启用收藏按钮并更新按钮显示为“收藏”状态
     */
    private void setButtonAsNotFavorited() {
        favoriteButton.setEnabled(true); // 启用按钮，以便可以添加到收藏
        favoriteButton.setText("お気に入り");
        favoriteButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black)); // 或者其他适合的颜色
    }
}