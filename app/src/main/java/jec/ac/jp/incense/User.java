package jec.ac.jp.incense;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(User.this, Account.class);
            startActivity(intent);
            finish();
            return;
        }

        TextView userNameTextView = findViewById(R.id.user_name);
        String userName = user.getDisplayName();
        String email = user.getEmail();

        if (userName != null && !userName.isEmpty()) {
            userNameTextView.setText(userName + " さん");
        } else {
            userNameTextView.setText(email + " さん");
        }

        setupUI();
    }

    private void setupUI() {
        LinearLayout historyLayout = findViewById(R.id.layout_history);
        historyLayout.setOnClickListener(v -> {
            Intent intent = new Intent(User.this, BrowsingHistoryActivity.class);
            startActivity(intent);
        });

        LinearLayout favoritesLayout = findViewById(R.id.layout_favorites);
        favoritesLayout.setOnClickListener(v -> {
            Intent intent = new Intent(User.this, FavoritesActivity.class);
            startActivity(intent);
        });

//        LinearLayout alarm = findViewById(R.id.layout_alarm);
//        alarm.setOnClickListener(v -> {
//            Intent intent = new Intent(User.this, TimerActivity.class);
//            startActivity(intent);
//        });

        ImageButton homeButton = findViewById(R.id.btn_home);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(User.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        ImageButton question = findViewById(R.id.btn_app);
        question.setOnClickListener(v -> {
            Intent intent = new Intent(User.this, Question.class);
            startActivity(intent);
        });

        ImageButton alarm = findViewById(R.id.btn_alarm);
        alarm.setOnClickListener(v -> {
            Intent intent = new Intent(User.this, TimerActivity.class);
            startActivity(intent);
        });


        LinearLayout logoutLayout = findViewById(R.id.layout_logout);
        logoutLayout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(User.this, Account.class));
            finish();
        });

    }
}
