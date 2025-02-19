package jec.ac.jp.incense;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnSendReset;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        firebaseAuth = FirebaseAuth.getInstance();


        etEmail = findViewById(R.id.etEmail);
        btnSendReset = findViewById(R.id.btnSendReset);

        btnSendReset.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (!email.isEmpty()) {
                sendResetEmail(email);
            } else {
                Toast.makeText(this, "メールアドレスを入力してください", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendResetEmail(String email) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(
                                ResetPasswordActivity.this,
                                "リセットメールを送信しました: " + email,
                                Toast.LENGTH_SHORT
                        ).show();
                        finish();
                    } else {
                        Toast.makeText(
                                ResetPasswordActivity.this,
                                "送信に失敗しました。もう一度お試しください。",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                });
    }
}
