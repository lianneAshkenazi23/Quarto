package a.lianne.quarto;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, 0, 0, 0);
            return insets;
        });

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

        MaterialToolbar topBar = findViewById(R.id.topBar);
        setSupportActionBar(topBar);

        drawer = findViewById(R.id.main);
        toggle = new ActionBarDrawerToggle(this, drawer, topBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

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

        if(savedInstanceState == null) {
            switchToGames();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // 3 dots menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) return true;
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
        return super.onOptionsItemSelected(item);
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

}