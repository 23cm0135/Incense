package jec.ac.jp.incense

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class EmailLogin : AppCompatActivity() {
    private var firebaseAuth: FirebaseAuth? = null
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private var firestore: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 启用沉浸式模式
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )
        window.statusBarColor = android.graphics.Color.TRANSPARENT

        setContentView(R.layout.activity_email_login)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        val btnLogin = findViewById<Button>(R.id.btnRegister)

        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    "メールとパスワード入力してください",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                loginWithEmail(email, password)
            }
        }
    }

    private fun loginWithEmail(email: String, password: String) {
        firebaseAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    val user = firebaseAuth?.currentUser
                    if (user != null) {
                        Toast.makeText(this, "登録成功", Toast.LENGTH_SHORT).show()
                        navigateToUserScreen(user)
                    }
                } else {
                    Toast.makeText(this, "ログインに失敗しました。もう一度お試しください", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Login failed", task.exception)
                }
            }
    }

    private fun saveUserToFirestore(user: FirebaseUser?) {
        if (user != null) {
            val userData = mapOf(
                "name" to user.displayName,
                "email" to user.email,
                "uid" to user.uid
            )
            firestore?.collection("users")
                ?.document(user.uid)
                ?.set(userData)
                ?.addOnSuccessListener {
                    Log.d(TAG, "User data saved to Firestore")
                }
                ?.addOnFailureListener { e ->
                    Log.e(TAG, "Error saving user data", e)
                }
        }
    }

    private fun navigateToUserScreen(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, User::class.java)
            intent.putExtra("USER_NAME", user.displayName)
            intent.putExtra("USER_EMAIL", user.email)
            startActivity(intent)
            finish()
        }
    }

    companion object {
        private const val TAG = "EmailLogin"
    }
}
