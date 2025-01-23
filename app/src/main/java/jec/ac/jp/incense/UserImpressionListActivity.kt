package jec.ac.jp.incense

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class UserImpressionListActivity : AppCompatActivity() {
    private var txtImpressionList: TextView? = null
    private var db: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_impression_list)
        txtImpressionList = findViewById(R.id.txtImpressionList)
        db = FirebaseFirestore.getInstance()
        loadImpressions()
    }

    private fun loadImpressions() {
        db!!.collection("user_impressions")
            .orderBy("timestamp")
            .get()
            .addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                val impressions = StringBuilder()
                for (document in queryDocumentSnapshots) {
                    val userName = document.getString("userName")
                    val impression = document.getString("impression")
                    impressions.append(userName).append("：").append(impression).append("\n\n")
                }
                txtImpressionList!!.text = impressions.toString()
            }
            .addOnFailureListener { e: Exception? ->
                Toast.makeText(
                    this,
                    "データの読み込みに失敗しました",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}
