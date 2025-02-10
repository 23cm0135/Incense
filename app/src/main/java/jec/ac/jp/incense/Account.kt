package jec.ac.jp.incense

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class Account : AppCompatActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        // 初始化 Firebase 身份验证
        firebaseAuth = FirebaseAuth.getInstance()

        // 配置 Google Sign-In
        setupGoogleSignIn()

        // 按钮设置
        setupUI()

        // 检查当前用户是否已登录
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            navigateToUserScreen(currentUser)
        }
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // 替换为你的客户端 ID
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun setupUI() {
        findViewById<Button>(R.id.btnGoogleLogin).setOnClickListener {
            startGoogleSignIn()
        }

        findViewById<Button>(R.id.btnEmailLogin).setOnClickListener {
            startActivity(Intent(this, EmailLogin::class.java))
        }

        findViewById<Button>(R.id.btnNewAccount).setOnClickListener {
            startActivity(Intent(this, NewAccount::class.java))
        }

        findViewById<Button>(R.id.btnPassword).setOnClickListener {
            resetPassword("user@example.com") // 替换为用户输入的邮箱
        }

        findViewById<Button>(R.id.btn_back).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun startGoogleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                Log.d(TAG, "Google 登録成功: ${account.email}")
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = firebaseAuth.currentUser
                            Toast.makeText(this, "登録成功", Toast.LENGTH_SHORT).show()
                            navigateToUserScreen(user)
                        } else {
                            Log.e(TAG, "Firebase 登録失敗", task.exception)
                            Toast.makeText(this, "ログインに失敗しました。もう一度お試しください", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        } catch (e: ApiException) {
            Log.e(TAG, "Google 登録失敗", e)
            Toast.makeText(this, "Google 登録失敗，もう一度お試しください", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetPassword(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "パスワードのリセットメールを送信しました。メールをご確認ください。", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, "パスワードのリセットに失敗しました。", task.exception)
                    Toast.makeText(this, "リセットに失敗しました。もう一度お試しください。", Toast.LENGTH_SHORT).show()
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
        private const val TAG = "GoogleSignIn"
    }
}
