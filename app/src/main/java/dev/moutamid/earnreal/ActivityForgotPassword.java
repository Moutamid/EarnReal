package dev.moutamid.earnreal;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityForgotPassword extends AppCompatActivity {
    private static final String TAG = "ActivityForgotPassword";

    private Button sendBtn;
    private EditText emailEt;

    private boolean isEmailSent=false;
    private String recentEmailUsed = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Log.d(TAG, "onCreate: started");

        sendBtn = findViewById(R.id.send_btn_forgot_password);
        emailEt = findViewById(R.id.email_editText_forgot_password);

        sendBtn.setOnClickListener(sendBtnClickListener());

    }

    private View.OnClickListener sendBtnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: clicked");

                final String email = (String) emailEt.getText().toString().trim().toLowerCase();

                // IF USER TRY TO SEND RESET EMAIL WITH SAME EMAIL
                if (isEmailSent && recentEmailUsed.equals(email)) {
                    new Utils().showWorkDoneDialog(ActivityForgotPassword.this, "", "");
                    return;
                }

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Log.i(TAG, "onComplete: email sent");

                            new Utils().showWorkDoneDialog(ActivityForgotPassword.this, "", "");
                            recentEmailUsed = email;
                            isEmailSent = true;

                        } else {
                            Log.i(TAG, "onComplete: task failed");
                            Toast.makeText(ActivityForgotPassword.this,
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        };

    }
}
