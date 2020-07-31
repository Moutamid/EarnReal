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

//    private FirebaseAuth mAuth;

    private static final String PACKAGE_NAME = "dev.moutamid.strangers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        loginBtn = findViewById(R.id.welcome_loginBtn);
        goToSignUpBtn = findViewById(R.id.goTo_signUp_activity_welcome);
  //      mAuth = FirebaseAuth.getInstance();

        this.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE).edit().clear().apply();
//        if (mAuth.getCurrentUser() != null)
//            mAuth.signOut();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ActivityWelcome.this, ActivityLogin.class));
                finish();
            }
        });

        goToSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ActivityWelcome.this, ActivitySignUp.class));
                finish();
            }
        });
    }

}
