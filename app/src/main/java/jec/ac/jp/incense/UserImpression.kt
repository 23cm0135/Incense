package jec.ac.jp.incense

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

class UserImpressionActivity : AppCompatActivity() {

    private lateinit var edtUserImpression: EditText
    private lateinit var btnSubmitImpression: Button
    private val db = FirebaseFirestore.getInstance()
    private val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_impression)

        edtUserImpression = findViewById(R.id.edtUserImpression)
        btnSubmitImpression = findViewById(R.id.btnSubmitImpression)

        btnSubmitImpression.setOnClickListener {
            val impressionText = edtUserImpression.text.toString()

            if (impressionText.isNotEmpty()) {
                saveImpressionToFirestore(impressionText)
            } else {
                Toast.makeText(this, "感想を入力してください", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveImpressionToFirestore(impression: String) {
        if (user != null) {
            val impressionData = hashMapOf(
                "userId" to user.uid,
                "email" to user.email,
                "impression" to impression,
                "timestamp" to System.currentTimeMillis()
            )

            db.collection("user_impressions")
                .add(impressionData)
                .addOnSuccessListener {
                    Toast.makeText(this, "感想が送信されました！", Toast.LENGTH_SHORT).show()
                    edtUserImpression.text.clear()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "送信に失敗しました。再試行してください", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "ユーザーがログインしていません", Toast.LENGTH_SHORT).show()
        }
    }
}
