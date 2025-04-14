package a.lianne.quarto;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        EditText firstNameInput = findViewById(R.id.firstNameInput);
        EditText lastNameInput = findViewById(R.id.lastNameInput);
        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        Button signUpButton = findViewById(R.id.signUpButton);

        signUpButton.setOnClickListener(v -> {
            String firstName = firstNameInput.getText().toString().trim();
            String lastName = lastNameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
                Toast.makeText(SignUpActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                return;
            }

            registerUser(firstName, lastName, email, password);
        });
    }

    private void registerUser(String firstName, String lastName, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        showError("Sign-up failed.", task.getException());
                        return;
                    }

                    String userId = mAuth.getCurrentUser().getUid();
                    saveUserToFirestore(userId, firstName, lastName, email);
                });
    }

    private void saveUserToFirestore(String userId, String firstName, String lastName, String email) {
        HashMap<String, Object> user = new HashMap<>();
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("email", email);

        db.collection("users").document(userId).set(user)
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        showError("Failed to add user to Firestore.", task.getException());
                        return;
                    }

                    saveScoreToFirestore(userId, email);
                });
    }

    private void saveScoreToFirestore(String userId, String email) {
        HashMap<String, Object> scores = new HashMap<>();
        scores.put("score", 0);
        scores.put("email", email);

        db.collection("scores").document(userId).set(scores)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "Sign-up successful, user and score saved.");
                        navigateToGame();
                    } else {
                        showError("Failed to add score to Firestore.", task.getException());
                    }
                });
    }

    private void navigateToGame() {
        Intent intent = new Intent(SignUpActivity.this, GameActivity.class);
        startActivity(intent);
        finish(); // Optional: finish SignUpActivity so user can't go back
    }

    private void showError(String message, Exception e) {
        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
        Log.w("TAG", message, e);
    }
}