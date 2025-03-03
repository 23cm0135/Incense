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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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

        Button btnPurchase = findViewById(R.id.btnPurchase);
        btnPurchase.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });

        Button btnSubmitImpression = findViewById(R.id.btnSubmitImpression);
        btnFavorite = findViewById(R.id.btnFavorite);
        Button btnViewPosts = findViewById(R.id.btnViewPosts);
        btnViewPosts.setOnClickListener(v -> {
            Intent intent = new Intent(MinuteActivity.this, UserImpressionListActivity.class);
            intent.putExtra("INCENSE_ID", incenseId);
            intent.putExtra("INCENSE_NAME", incenseName);
            startActivity(intent);
        });

        if (currentUser == null) {
            btnSubmitImpression.setOnClickListener(v ->
                    Toast.makeText(MinuteActivity.this, "ログインしてください", Toast.LENGTH_SHORT).show());
            btnFavorite.setOnClickListener(v ->
                    Toast.makeText(MinuteActivity.this, "ログインしてください", Toast.LENGTH_SHORT).show());
        } else {
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

//    private void addToBrowsingHistory() {
//        if (currentUser == null) return;
//
//        FavoriteItem item = new FavoriteItem(incenseName, "", imageUrl, text, url);
//
//        db.collection("users")
//                .document(currentUser.getUid())
//                .collection("history")
//                .document(incenseName)
//                .set(item)
//                .addOnSuccessListener(aVoid ->
//                        Log.d("Firestore", "浏览历史添加成功: " + incenseName)
//                )
//                .addOnFailureListener(e ->
//                        Log.e("Firestore", "浏览历史添加失败", e)
//                );
//    }
private void addToBrowsingHistory() {
    if (currentUser == null) return;

    FavoriteItem item = new FavoriteItem(incenseName, "", imageUrl, text, url);
    item.setTimestamp(Timestamp.now());

    db.collection("users")
            .document(currentUser.getUid())
            .collection("history")
            .whereEqualTo("name", incenseName)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                if (!queryDocumentSnapshots.isEmpty()) {
                    Log.d("Firestore", "找到 " + queryDocumentSnapshots.size() + " 条同名记录，准备删除");
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        document.getReference().delete()
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("Firestore", "成功删除记录: " + document.getId());
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firestore", "删除记录失败: " + document.getId(), e);
                                });
                    }
                } else {
                    Log.d("Firestore", "未找到同名记录，直接添加新记录");
                }

                db.collection("users")
                        .document(currentUser.getUid())
                        .collection("history")
                        .document(incenseName)
                        .set(item)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("TimestampDebug", "Setting timestamp for history: " + item.getTimestamp());
                        })
                        .addOnFailureListener(e ->
                                Log.e("Firestore", "浏览历史添加失败", e)
                        );
            })
            .addOnFailureListener(e ->
                    Log.e("Firestore", "浏览历史检查失败", e)
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
