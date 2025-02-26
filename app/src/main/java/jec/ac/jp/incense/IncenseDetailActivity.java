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

        TextView nameTextView = findViewById(R.id.incense_detail_name);
        ImageView imageView = findViewById(R.id.incense_detail_image);
        TextView descriptionTextView = findViewById(R.id.incense_detail_description);
        favoriteButton = findViewById(R.id.favorite_button);
        Button openUrlButton = findViewById(R.id.open_url_button);
        Button userImpressionButton = findViewById(R.id.UserImpression);

        nameTextView.setText(incenseName);
        descriptionTextView.setText(description);
        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(imageView);

        openUrlButton.setOnClickListener(v -> {
            if (url == null || url.isEmpty()) {
                Toast.makeText(this, "無効なURLです", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        });

        // 添加到浏览历史
        addToBrowsingHistory();

        // 检查是否已收藏
        checkIfFavorite();
        favoriteButton.setOnClickListener(v -> addToFavorites());

        // 投稿按钮
        userImpressionButton.setOnClickListener(v -> {
            Intent intent = new Intent(IncenseDetailActivity.this, UserImpression.class);
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
                        Log.e("Firestore", "お気に入り检查失败", e)
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
                    Toast.makeText(this, "お気に入りに追加: " + incenseName, Toast.LENGTH_SHORT).show();
                    setButtonAsFavorited();
                })
                .addOnFailureListener(e ->
                        Log.e("Firestore", "お気に入り保存失败", e)
                );
    }

    private void setButtonAsFavorited() {
        favoriteButton.setEnabled(false);
        favoriteButton.setText("お気に入り済み");
        // 将按钮背景设置为灰色
        favoriteButton.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
    }
}
