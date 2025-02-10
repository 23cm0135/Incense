package jec.ac.jp.incense;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * お香の感想を投稿する画面。
 * 「香の名前」を Intentで受け取り、画面上に自動表示し、Firestore に書き込む。
 */
public class UserImpression extends AppCompatActivity {

    private EditText edtUserImpression;
    private TextView incenseNameTextView;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private String incenseId;   // お香のIDがあるなら
    private String incenseName; // お香の名前をここに保持

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_impression);

        // View 初期化
        edtUserImpression = findViewById(R.id.edtUserImpression);
        incenseNameTextView = findViewById(R.id.incenseNameTextView);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        // Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // 前の画面 (例: MinuteActivity) から受け取る
        incenseId = getIntent().getStringExtra("INCENSE_ID");
        incenseName = getIntent().getStringExtra("INCENSE_NAME");

        // もし null だったらデフォルト値を入れる
        if (incenseId == null) {
            incenseId = "unknown_id";
        }
        if (incenseName == null) {
            incenseName = "不明な香";
        }

        // 画面に自動表示
        incenseNameTextView.setText(incenseName);

        // ボタン押下
        btnSubmit.setOnClickListener(v -> {
            String impression = edtUserImpression.getText().toString().trim();
            if (!impression.isEmpty()) {
                saveImpressionToFirebase(impression);
            } else {
                Toast.makeText(this, "内容を入力してください", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Firestore に投稿内容を書き込む
     */
    private void saveImpressionToFirebase(String impression) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "ログインしてください", Toast.LENGTH_SHORT).show();
            return;
        }

        // ログインユーザー名 (null/空ならメールアドレス)
        String username = currentUser.getDisplayName();
        if (username == null || username.isEmpty()) {
            username = currentUser.getEmail();
        }

        Map<String, Object> post = new HashMap<>();
        post.put("username", username);
        post.put("content", impression);
        post.put("incenseId", incenseId);
        post.put("incenseName", incenseName); // ここで enum由来の香の名前を保存
        post.put("timestamp", System.currentTimeMillis());

        db.collection("posts")
                .add(post)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(UserImpression.this, "投稿成功", Toast.LENGTH_SHORT).show();
                    // 投稿完了後、投稿一覧へジャンプ
                    startActivity(new Intent(UserImpression.this, UserImpressionListActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UserImpression.this, "投稿失敗", Toast.LENGTH_SHORT).show();
                });
    }
}
