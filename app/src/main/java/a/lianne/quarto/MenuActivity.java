package a.lianne.quarto;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);

        Button playButton = findViewById(R.id.playButton);
        Button rulesButton = findViewById(R.id.rulesButton);
        Button leaderboardButton = findViewById(R.id.leaderboardButton);

        playButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("destination", "game");
            startActivity(intent);
        });

        rulesButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("destination", "rules");
            startActivity(intent);
        });

        leaderboardButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("destination", "leaderboard");
            startActivity(intent);
        });

    }
}