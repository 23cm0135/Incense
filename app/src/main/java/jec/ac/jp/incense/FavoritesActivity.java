package jec.ac.jp.incense;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvEmptyMessage;
    private ArrayList<FavoriteItem> favoriteItems = new ArrayList<>();
    private FavoriteAdapter adapter;
    private FirebaseFirestore db;
    private String userId; // 用戶 UID（用於加載收藏）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);
        recyclerView = findViewById(R.id.favorites_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        // 優先從 Intent 中取得 USER_ID（例如：當從其他用戶的帖子點擊後傳入），若未提供則使用當前用戶 UID
        userId = getIntent().getStringExtra("USER_ID");
        if (userId == null || userId.isEmpty()) {
            if (com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser() != null) {
                userId = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser().getUid();
            } else {
                Toast.makeText(this, "ログインしてください", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }

        adapter = new FavoriteAdapter(this, favoriteItems, item -> {
            // 若需要刪除功能，在這裡處理刪除邏輯；如果不需要可以留空或不設定
            removeFavorite(item);
        });
        recyclerView.setAdapter(adapter);

        loadFavoritesFromFirestore();
    }

    private void loadFavoritesFromFirestore() {
        db.collection("users")
                .document(userId)
                .collection("favorites")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    favoriteItems.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        FavoriteItem item = doc.toObject(FavoriteItem.class);
                        favoriteItems.add(item);
                    }
                    if (favoriteItems.isEmpty()) {
                        tvEmptyMessage.setVisibility(TextView.VISIBLE);
                        recyclerView.setVisibility(RecyclerView.GONE);
                    } else {
                        tvEmptyMessage.setVisibility(TextView.GONE);
                        recyclerView.setVisibility(RecyclerView.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "お気に入りの読み込みに失敗しました", Toast.LENGTH_SHORT).show()
                );
    }

    private void removeFavorite(FavoriteItem item) {
        db.collection("users")
                .document(userId)
                .collection("favorites")
                .document(item.getName())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    favoriteItems.remove(item);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "削除しました：" + item.getName(), Toast.LENGTH_SHORT).show();
                    if (favoriteItems.isEmpty()) {
                        tvEmptyMessage.setVisibility(TextView.VISIBLE);
                        recyclerView.setVisibility(RecyclerView.GONE);
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "削除に失敗しました", Toast.LENGTH_SHORT).show()
                );
    }
}
