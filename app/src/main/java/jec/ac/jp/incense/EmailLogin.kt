package jec.ac.jp.incense

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_email_login)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        val btnLogin = findViewById<Button>(R.id.btnRegister)
        btnLogin.setOnClickListener { v: View? ->
            val email = edtEmail.getText().toString()
            val password = edtPassword.getText().toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this@EmailLogin,
                    "メールとパスワード入力してください",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                loginWithEmail(email, password)
            }
        }
    }

    private fun loginWithEmail(email: String, password: String) {
        firebaseAuth!!.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this
            ) { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    val user = firebaseAuth!!.currentUser
                    if (user != null) {
                        Toast.makeText(this@EmailLogin, "登录成功", Toast.LENGTH_SHORT).show()
                        navigateToUserScreen(user)
                    }
                } else {
                    Toast.makeText(this@EmailLogin, "登录失败，请重试", Toast.LENGTH_SHORT).show()
                    Log.e(
                        TAG,
                        "Login failed",
                        task.exception
                    )
                }
            }
    }

    private fun saveUserToFirestore(user: FirebaseUser?) {
        if (user != null) {
            val userData: MutableMap<String, Any?> = HashMap()
            userData["name"] = user.displayName
            userData["email"] = user.email
            userData["uid"] = user.uid
            firestore!!.collection("users")
                .document(user.uid)
                .set(userData)
                .addOnSuccessListener { aVoid: Void? ->
                    Log.d(
                        TAG,
                        "User data saved to Firestore"
                    )
                }
                .addOnFailureListener { e: Exception? ->
                    Log.e(
                        TAG,
                        "Error saving user data",
                        e
                    )
                }
        }
    }

    private fun navigateToUserScreen(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this@EmailLogin, User::class.java)
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
