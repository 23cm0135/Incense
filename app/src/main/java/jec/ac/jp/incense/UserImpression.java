package jec.ac.jp.incense;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class UserImpression extends AppCompatActivity {

    private EditText edtUserImpression;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_impression);

        edtUserImpression = findViewById(R.id.edtUserImpression);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> {
            String impression = edtUserImpression.getText().toString();
            if (!impression.isEmpty()) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("IMPRESSION_RESULT", impression);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
