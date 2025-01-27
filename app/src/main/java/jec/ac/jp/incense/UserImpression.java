package jec.ac.jp.incense;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class UserImpression extends AppCompatActivity {

    private EditText edtUserImpression;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_impression);

        edtUserImpression = findViewById(R.id.edtUserImpression);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        db = FirebaseFirestore.getInstance();

        btnSubmit.setOnClickListener(v -> {
            String impression = edtUserImpression.getText().toString().trim();
            if (!impression.isEmpty()) {
                saveImpressionToFirebase(impression);
            } else {
                Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveImpressionToFirebase(String impression) {
        Map<String, Object> post = new HashMap<>();
        post.put("username", "用户A");  // 假设的用户名
        post.put("content", impression);
        post.put("timestamp", System.currentTimeMillis());

        db.collection("posts")
                .add(post)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(UserImpression.this, "投稿成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UserImpression.this, UserImpressionListActivity.class));
                })
                .addOnFailureListener(e ->
                        Toast.makeText(UserImpression.this, "投稿失败", Toast.LENGTH_SHORT).show()
                );
    }
}
