package jec.ac.jp.incense;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

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
            if (email.isEmpty()) {
                Toast.makeText(this, "メールアドレスを入力してください", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "正しいメールアドレスの形式で入力してください", Toast.LENGTH_SHORT).show();
            } else {
                sendResetEmail(email);
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
                        if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                            Toast.makeText(
                                    ResetPasswordActivity.this,
                                    "このメールアドレスは登録されていません",
                                    Toast.LENGTH_SHORT
                            ).show();
                        } else {
                            Toast.makeText(
                                    ResetPasswordActivity.this,
                                    "送信に失敗しました。もう一度お試しください。",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                });
    }
}
