package jec.ac.jp.incense;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class UserImpression extends AppCompatActivity {

    private EditText edtUserImpression;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String incenseId, incenseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_impression);

        edtUserImpression = findViewById(R.id.edtUserImpression);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();  // 初始化 FirebaseAuth

        btnSubmit.setOnClickListener(v -> {
            String impression = edtUserImpression.getText().toString().trim();
            if (!impression.isEmpty()) {
                saveImpressionToFirebase(impression);
            } else {
                Toast.makeText(this, "内容を入力してください", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveImpressionToFirebase(String impression) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "登録してください", Toast.LENGTH_SHORT).show();
            return;
        }

        String username = currentUser.getDisplayName();  // 获取用户名
        if (username == null || username.isEmpty()) {
            username = currentUser.getEmail();  // 若用户名为空，则使用邮箱
        }

        Map<String, Object> post = new HashMap<>();
        post.put("username", username);  // 用 Firebase 登录名
        post.put("content", impression);
        post.put("incenseId", incenseId);
        post.put("incenseName", incenseName);
        post.put("timestamp", System.currentTimeMillis());

        db.collection("posts")
                .add(post)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(UserImpression.this, "投稿成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UserImpression.this, UserImpressionListActivity.class));
                })
                .addOnFailureListener(e ->
                        Toast.makeText(UserImpression.this, "投稿失敗", Toast.LENGTH_SHORT).show()
                );
    }
}
