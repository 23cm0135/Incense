package jec.ac.jp.incense;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class UserImpression extends AppCompatActivity {

    private EditText edtUserImpression;
    private TextView incenseNameTextView;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private String incenseId;
    private String incenseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_impression);
        EdgeToEdge.enable(this);

        edtUserImpression = findViewById(R.id.edtUserImpression);
        incenseNameTextView = findViewById(R.id.incenseNameTextView);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        if (intent != null) {
            incenseId = intent.getStringExtra("INCENSE_ID");
            incenseName = intent.getStringExtra("INCENSE_NAME");

            if (incenseId == null || incenseId.isEmpty()) {
                incenseId = "unknown_id";
            }
            if (incenseName == null || incenseName.isEmpty()) {
                incenseName = "不明な香";
            }
        } else {
            incenseId = "unknown_id";
            incenseName = "不明な香";
        }

        // **顯示香的名稱**
        incenseNameTextView.setText(incenseName);

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
            Toast.makeText(this, "ログインしてください", Toast.LENGTH_SHORT).show();
            return;
        }

        String username = currentUser.getDisplayName();
        if (username == null || username.isEmpty()) {
            username = currentUser.getEmail();
        }

        Map<String, Object> post = new HashMap<>();
        post.put("username", username);
        post.put("content", impression);
        post.put("incenseId", incenseId);
        post.put("incenseName", incenseName);
        post.put("timestamp", System.currentTimeMillis());

        db.collection("posts").add(post)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(UserImpression.this, "投稿成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UserImpression.this, UserImpressionListActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UserImpression.this, "投稿失敗", Toast.LENGTH_SHORT).show();
                });
    }
}
