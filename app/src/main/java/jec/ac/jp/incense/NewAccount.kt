package jec.ac.jp.incense

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class NewAccount : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtDisplayName: EditText

    companion object {
        private const val TAG = "NewAccount"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_account)

        // 初始化 FirebaseAuth & Firestore
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // 初始化 UI 元素
        edtEmail = findViewById(R.id.etEmail)
        edtPassword = findViewById(R.id.etPassword)
        edtDisplayName = findViewById(R.id.etDisplayName) // 获取显示名输入框
        val btnRegister: Button = findViewById(R.id.btnRegister)

        // 设置注册按钮的点击事件
        btnRegister.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            val displayName = edtDisplayName.text.toString()

            if (email.isEmpty() || password.isEmpty() || displayName.isEmpty()) {
                Toast.makeText(this, "全ての項目を入力してください", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(email, password, displayName)
            }
        }
    }

    /**
     * 注册用户
     */
    private fun registerUser(email: String, password: String, displayName: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = firebaseAuth.currentUser
                    if (user != null) {
                        // **更新 Firebase 用户的 `displayName`**
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(displayName)
                            .build()
                        user.updateProfile(profileUpdates).addOnCompleteListener {
                            if (it.isSuccessful) {
                                // **存入 Firestore**
                                saveUserToFirestore(user, displayName)
                            } else {
                                Toast.makeText(this, "表示名の設定に失敗しました", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    // **检查错误类型**
                    if (task.exception is FirebaseAuthUserCollisionException) {
                        Toast.makeText(this, "このメールアドレスはすでに登録されています", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e(TAG, "登録に失敗しました", task.exception)
                        Toast.makeText(this, "登録に失敗しました。再試行してください", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    /**
     * 存储用户数据到 Firestore
     */
    private fun saveUserToFirestore(user: FirebaseUser, displayName: String) {
        val userData = hashMapOf(
            "uid" to user.uid,
            "email" to user.email,
            "displayName" to displayName
        )

        firestore.collection("users").document(user.uid)
            .set(userData)
            .addOnSuccessListener {
                Toast.makeText(this, "登録成功", Toast.LENGTH_SHORT).show()
                navigateToUserScreen()
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Firestore error", e)
            }
    }

    private fun navigateToUserScreen() {
        val user = firebaseAuth.currentUser
        if (user != null) {
            val intent = Intent(this, User::class.java)
            intent.putExtra("USER_NAME", user.displayName)
            intent.putExtra("USER_EMAIL", user.email)
            startActivity(intent) // **跳转到 User 画面**
            finish() // **关闭当前注册界面**
        }
    }
}
