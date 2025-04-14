package a.lianne.quarto;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        // הפניות לשדות ולכפתורים
        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        Button signInButton = findViewById(R.id.signInButton);

        // לחיצה על כפתור SING IN
        signInButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignInActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            } else if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))
                Toast.makeText(SignInActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
            else if (password.length() < 6)
                Toast.makeText(SignInActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            else {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(SignInActivity.this, task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(SignInActivity.this, GameActivity.class);
                        startActivity(intent);
                        Toast.makeText(SignInActivity.this, "Signed in successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.w("TAG", "signInWithEmail:failure", task.getException());
                        Toast.makeText(SignInActivity.this, "Sign-in failed.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // מציאת הטקסט "Haven’t signed up yet?"
        TextView signUpText = findViewById(R.id.signUpText);

        // הגדרת פעולה ללחיצה
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // מעבר לעמוד SignUpActivity
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


    }}
