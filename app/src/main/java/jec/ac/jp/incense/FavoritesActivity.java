package jec.ac.jp.incense;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvEmptyMessage;
    private ArrayList<FavoriteItem> favoriteItems = new ArrayList<>();
    private FavoriteAdapter adapter;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        EdgeToEdge.enable(this);

        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);
        recyclerView = findViewById(R.id.favorites_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = auth.getCurrentUser();

        adapter = new FavoriteAdapter(this, favoriteItems, item -> removeFavorite(item));
        recyclerView.setAdapter(adapter);

        loadFavoritesFromFirestore();
    }

    private void loadFavoritesFromFirestore() {
        if (currentUser == null) {
            Toast.makeText(this, "ログインしてください", Toast.LENGTH_SHORT).show();
            return;
        }
        db.collection("users")
                .document(currentUser.getUid())
                .collection("favorites")
                .orderBy("timestamp", Query.Direction.ASCENDING) // timestamp フィールドで昇順にソート
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    favoriteItems.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        FavoriteItem item = doc.toObject(FavoriteItem.class);
                        favoriteItems.add(item);
                    }
                    if (favoriteItems.isEmpty()) {
                        // 資料為空，顯示「お気に入りがありません」
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
        if (currentUser == null) return;

        db.collection("users")
                .document(currentUser.getUid())
                .collection("favorites")
                .document(item.getName())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    favoriteItems.remove(item);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "削除しました: " + item.getName(), Toast.LENGTH_SHORT).show();
                    // 如果刪除後列表變空，也顯示提示訊息
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
