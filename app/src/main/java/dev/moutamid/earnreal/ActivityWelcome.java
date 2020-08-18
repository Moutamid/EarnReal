package dev.moutamid.earnreal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class ActivityWelcome extends AppCompatActivity {

    private Button loginBtn;
    private TextView goToSignUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        loginBtn = findViewById(R.id.loginBtn_welcome);
        goToSignUpBtn = findViewById(R.id.goTo_signUp_activity_welcome);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ActivityWelcome.this, ActivityLogin.class));

            }
        });

        goToSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ActivityWelcome.this, ActivitySignUp.class));
            }
        });
    }

}
