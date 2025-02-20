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

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        edtEmail = findViewById(R.id.etEmail)
        edtPassword = findViewById(R.id.etPassword)
        edtDisplayName = findViewById(R.id.etDisplayName)
        val btnRegister: Button = findViewById(R.id.btnRegister)


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


    private fun registerUser(email: String, password: String, displayName: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user: FirebaseUser? = firebaseAuth.currentUser
                    if (user != null) {
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(displayName)
                            .build()
                        user.updateProfile(profileUpdates).addOnCompleteListener {
                            if (it.isSuccessful) {

                                saveUserToFirestore(user, displayName)
                            } else {
                                Toast.makeText(this, "表示名の設定に失敗しました", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {

                    if (task.exception is FirebaseAuthUserCollisionException) {
                        Toast.makeText(this, "このメールアドレスはすでに登録されています", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e(TAG, "登録に失敗しました", task.exception)
                        Toast.makeText(this, "登録に失敗しました。再試行してください", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }


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
            startActivity(intent)
            finish()
        }
    }
}
