package a.lianne.quarto;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

public class GameActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private GameFragment gameFragment;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        String destination = getIntent().getStringExtra("destination");

        MaterialToolbar topBar = findViewById(R.id.topBar);
        setSupportActionBar(topBar);


        drawer = findViewById(R.id.main);
        toggle = new ActionBarDrawerToggle(this, drawer, topBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.pink));



        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        String userEmail = mAuth.getCurrentUser().getEmail();

        db.collection("users")
            .document(mAuth.getCurrentUser().getUid())
            .get().addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    Log.d("TAG", "User name: " + task.getResult().getString("firstName") + " " + task.getResult().getString("lastName"));
                    fullName = task.getResult().getString("firstName") + " " + task.getResult().getString("lastName");
                    NavigationView nav = findViewById(R.id.sideNav);
                    TextView email = nav.getHeaderView(0).findViewById(R.id.headerEmail);
                    email.setText("Hello " + fullName + "!");
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                    fullName = "User";
                }
        });


        // hamburger menu
        NavigationView nav = findViewById(R.id.sideNav);
        nav.setNavigationItemSelectedListener(item -> {
            drawer.closeDrawer(GravityCompat.START);
            if (item.getItemId() == R.id.menu_game) {
                switchToGames();
                return true;
            } else if (item.getItemId() == R.id.menu_rules) {
                switchToRules();
                return true;
            }
            else if (item.getItemId() == R.id.menu_reminder) {
                switchToReminder();
                return true;
            }

            else if (item.getItemId() == R.id.menu_leaderboard) {
                switchToLeaderboard();
                return true;
            }
            else if (item.getItemId() == R.id.menu_logout) {
                mAuth.signOut();
                finish();
            }
            return false;
        });

        Menu menu = nav.getMenu();
        MenuItem logoutItem = menu.findItem(R.id.menu_logout);
        SpannableString s = new SpannableString(logoutItem.getTitle());
        s.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.bright_pink)), 0, s.length(), 0);
        // bold
        s.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, s.length(), 0);
        logoutItem.setTitle(s);

        // If there is no savedInstanceState, open the games fragment
        if(savedInstanceState == null) {
            switchToGames();
        }

        // Take the destination from the intent and switch to the corresponding fragment
        if (destination != null && destination.equals("rules")) {
            switchToRules();
        }
        else if (destination != null && destination.equals("game")) {
            switchToGames();
        }
        else if (destination != null && destination.equals("leaderboard")) {
            switchToLeaderboard();
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    finish();
                }

            }
        });
    }

    public void switchToGames() {
        if (gameFragment == null) gameFragment = GameFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, gameFragment)
                .addToBackStack(null)
                .commit();
    }


    public void switchToRules() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, RulesFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    public void switchToReminder() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, ReminderFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    public void switchToLeaderboard() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, LeaderboardFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    public void showWinnerDialog(String winner) {
        // Show dialog of game over
        View dialog = getLayoutInflater().inflate(R.layout.dialog_winner, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(dialog)
                .setCancelable(false)
                .create();
        TextView winnerTv = dialog.findViewById(R.id.winnerTv);

        if (winner.equals("tie")) {
            winnerTv.setText("It's a tie!");
        } else if (winner.equals("player 1")) {
            winnerTv.setText("Player 1 wins!");
            // Add 1 score to logged in user in firestore scores collection
            db.collection("scores").document(mAuth.getCurrentUser().getUid()).update("score", FieldValue.increment(1));
        } else if (winner.equals("player 2")) {
            winnerTv.setText("Player 2 wins!");
        }
//        builder.setPositiveButton("Close", (dialog, which) -> {
//            dialog.dismiss();
//            finish();
//        });
//        builder.setNegativeButton("Start a new Game", (dialog, which) -> {
//            dialog.dismiss();
//            gameFragment.restartGame();
//        });
        dialog.findViewById(R.id.closeButton).setOnClickListener(v -> {
            alertDialog.dismiss();
            finish();
        });
        dialog.findViewById(R.id.newGameButton).setOnClickListener(v -> {
            alertDialog.dismiss();
            gameFragment.restartGame();
        });

        if (!winner.isEmpty())
            alertDialog.show();
    }

}