package a.lianne.quarto;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // הפניות לשדות ולכפתורים
        EditText usernameInput = findViewById(R.id.usernameInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        Button signInButton = findViewById(R.id.signInButton);

        // לחיצה על כפתור SING IN
        signInButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignInActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            } else {
                // ביצוע פעולה, לדוגמה מעבר למסך בית
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(SignInActivity.this, "Signed up successfully!", Toast.LENGTH_SHORT).show();
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
