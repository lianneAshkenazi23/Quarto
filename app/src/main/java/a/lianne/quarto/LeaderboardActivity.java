package a.lianne.quarto;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

public class LeaderboardActivity extends AppCompatActivity {


    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_leaderboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        db.collection("scores").get().addOnCompleteListener(this, task -> {
           if (task.isSuccessful())  {
               TableLayout table = findViewById(R.id.leaderboardTable);
               for (int i = 0; i < task.getResult().size(); i++) {
                   String email = task.getResult().getDocuments().get(i).getString("email");
//                   String firstName = task.getResult().getDocuments().get(i).getString("firstName");
                   Long score = task.getResult().getDocuments().get(i).getLong("score");
                   TextView row = new TextView(this);
                   row.setText(email + " " + score);
                   table.addView(row);
               }
           }
        });
    }
}