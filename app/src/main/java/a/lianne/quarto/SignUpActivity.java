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

        // הפניות לשדות ולכפתור
        EditText firstNameInput = findViewById(R.id.firstNameInput);
        EditText lastNameInput = findViewById(R.id.lastNameInput);
        EditText emailInput = findViewById(R.id.emailInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        Button signUpButton = findViewById(R.id.signUpButton);

        // לחיצה על כפתור SING UP
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameInput.getText().toString().trim();
                String lastName = lastNameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))
                    Toast.makeText(SignUpActivity.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpActivity.this, task -> {
                                if (task.isSuccessful()) {

                                    String userId = mAuth.getCurrentUser().getUid();

                                    HashMap<String, Object> user = new HashMap<>();
                                    user.put("firstName", firstName);
                                    user.put("lastName", lastName);
                                    user.put("email", email);

                                    db.collection("users").document(userId).set(user).addOnCompleteListener(SignUpActivity.this, task1 -> {
                                        if (task1.isSuccessful()) {
                                            Log.d("TAG", "User added to Firestore");
                                            Intent intent = new Intent(SignUpActivity.this, GameActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(SignUpActivity.this, "Sign-up failed!", Toast.LENGTH_SHORT).show();
                                            Log.w("TAG", "Error adding user to Firestore", task1.getException());
                                        }
                                    });
                                } else {
                                    Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUpActivity.this, "Sign-up failed.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}