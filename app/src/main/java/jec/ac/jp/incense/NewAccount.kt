package jec.ac.jp.incense

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class NewAccount : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText

    companion object {
        private const val TAG = "NewAccount"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_account)

        // 初始化 FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // 初始化 UI 元素
        edtEmail = findViewById(R.id.etEmail)
        edtPassword = findViewById(R.id.etPassword)
        val btnRegister: Button = findViewById(R.id.btnRegister)

        // 设置注册按钮的点击事件
        btnRegister.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "メールとパスワードを入力してください", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(email, password)
            }
        }
    }

    /**
     * 注册用户
     */
    private fun registerUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = firebaseAuth.currentUser
                    Toast.makeText(this, "登録成功: ${user?.email}", Toast.LENGTH_SHORT).show()
                    navigateToUserScreen()
                } else {
                    Log.e(TAG, "Registration failed", task.exception)
                    Toast.makeText(this, "登録に失敗しました。再試行してください", Toast.LENGTH_SHORT).show()
                }
            }
    }

    /**
     * 跳转到用户界面
     */
    private fun navigateToUserScreen() {
        // 注册成功后关闭当前界面
        finish()
    }
}
