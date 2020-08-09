package dev.moutamid.earnreal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityLogin extends AppCompatActivity {
    private static final String TAG = "ActivityLogin";

    private static final String USER_EMAIL = "userEmail";
    private static final String USER_ID = "userReferralCode";
    private static final String USER_GENDER = "userGender";
    private static final String USER_NUMBER = "userNumber";
    private static final String REFERRED_BY = "referredBy";

    private EditText emailEditText, passwordEditText;
    private TextView forgotPasswordBtn, goToSignUpActivityBtn;
    private Button loginBtn;

    private Utils utils = new Utils();

    private ProgressDialog mDialog;

    private Boolean isOnline = false;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(TAG, "onCreate: started");

        mAuth = FirebaseAuth.getInstance();

        // CHECKING IF USER IS ALREADY LOGGED IN
        checkLoginStatus();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);

        // CHECKING ONLINE STATUS
        checkOnlineStatus();

        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(false);
        mDialog.setMessage("Signing you in...");

        initViews();

        setListenersToWidgets();

    }

    private void checkLoginStatus() {
        // IF USER IS SIGNED IN
        if (mAuth.getCurrentUser() != null) {
            Toast.makeText(this, "You are signed in!", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkOnlineStatus() {
        Log.i(TAG, "checkOnlineStatus: ");

        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i(TAG, "onDataChange: online status");

                isOnline = snapshot.getValue(Boolean.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "onCancelled: " + error.getMessage());
            }
        });

    }

    private void setListenersToWidgets() {
        Log.i(TAG, "setListenersToWidgets: ");

        forgotPasswordBtn.setOnClickListener(forgotPasswordBtnListener());

        loginBtn.setOnClickListener(loginBtnListener());

        goToSignUpActivityBtn.setOnClickListener(signUpBtnListener());
    }

    private View.OnClickListener signUpBtnListener() {
        Log.i(TAG, "signUpBtnListener: ");
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: sign up button");

                startActivity(new Intent(ActivityLogin.this, ActivitySignUp.class));
            }
        };
    }

    private View.OnClickListener loginBtnListener() {
        Log.i(TAG, "loginBtnListener: ");
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: login button");

                if (isOnline) {
                    mDialog.show();
                    checkStatusOfEditTexts();
                } else {
                    utils.showOfflineDialog(ActivityLogin.this);
                }
            }
        };
    }

    private void checkStatusOfEditTexts() {
        Log.i(TAG, "checkStatusOfEditTexts: ");

        String emailStr = emailEditText.getText().toString().trim().toLowerCase();
        String passwordStr = passwordEditText.getText().toString().trim().toLowerCase();

        if (!isOnline) {
            mDialog.dismiss();
            utils.showOfflineDialog(ActivityLogin.this);
            return;
        }

        if (TextUtils.isEmpty(emailStr)) {
            mDialog.dismiss();
            emailEditText.setError("Please enter an email!");
            emailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            mDialog.dismiss();
            emailEditText.setError("Email is invalid!");
            emailEditText.requestFocus();
        }

        if (TextUtils.isEmpty(passwordStr)) {
            mDialog.dismiss();
            passwordEditText.setError("Please enter a password!");
            passwordEditText.requestFocus();
            return;
        }

        signInUserWithNameAndPassword(emailStr, passwordStr);
    }

    private View.OnClickListener forgotPasswordBtnListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: forgot password btn");

                startActivity(new Intent(ActivityLogin.this, ActivityForgotPassword.class));
            }
        };
    }


    private void getUserDetailsFromDatabaseAndStoreOffline() {
        Log.i(TAG, "getUserDetailsFromDatabaseAndStoreOffline: ");

        // UPLOADING USER DETAILS TO THE DATABASE
//        databaseReference.child("users").child(mAuth.getCurrentUser().getUid())
//                .child("email").setValue(emailStr);
//
//        databaseReference.child("users").child(mAuth.getCurrentUser().getUid())
//                .child("nmbr").setValue(numberStr);
//
//        databaseReference.child("users").child(mAuth.getCurrentUser().getUid())
//                .child("gender").setValue(userGenderStr);

        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(mAuth.getCurrentUser().getUid())) {
                    User user = snapshot.child(mAuth.getCurrentUser().getUid()).getValue(User.class);

                    String email = user.getEmail();
                    String number = user.getNmbr();
                    String gender = user.getGender();
                    String refBy = "";

                    if (snapshot.child(mAuth.getCurrentUser().getUid()).hasChild("refBy"))
                        refBy = snapshot.child(mAuth.getCurrentUser().getUid()).child("refBy").getValue(String.class);

                    storeUserInformationOffline(email, number, gender, refBy);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "onCancelled: " + error.toException());

                mDialog.dismiss();
                Toast.makeText(ActivityLogin.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void storeUserInformationOffline(String email, String number, String gender, String refBy) {
        Log.i(TAG, "storeUserInformationOffline: ");

        utils.storeString(ActivityLogin.this, USER_GENDER, gender);
        utils.storeString(ActivityLogin.this, USER_EMAIL, email);
        utils.storeString(ActivityLogin.this, USER_NUMBER, number);
        utils.storeString(ActivityLogin.this, USER_ID, mAuth.getCurrentUser().getUid());

        if (!TextUtils.isEmpty(refBy)){
            utils.storeString(ActivityLogin.this, REFERRED_BY, refBy);
        }

        mDialog.dismiss();
        Toast.makeText(this, "You are logged in!", Toast.LENGTH_SHORT).show();
    }


    private void signInUserWithNameAndPassword(String email, String password) {
        Log.i(TAG, "signInUserWithNameAndPassword: ");

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "onComplete: user signed in");

                    getUserDetailsFromDatabaseAndStoreOffline();

                } else {
                    mDialog.dismiss();
                    Log.w(TAG, "onComplete: task is not completed " + task.getException());
                    Toast.makeText(ActivityLogin.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }


            }
        });

//                Intent intent = new Intent(ActivityLogin.this, BottomNavigationActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                mProgressDialog.dismiss();
//
//                Toast.makeText(ActivityLogin.this, "You are successfully logged in", Toast.LENGTH_SHORT).show();
//
//                startActivity(intent);
//                finish();
    }


    private void initViews() {
        Log.i(TAG, "initViews: ");

        emailEditText = findViewById(R.id.email_login_editText);
        passwordEditText = findViewById(R.id.password_login_editText);
        forgotPasswordBtn = findViewById(R.id.forgotPassword_login_textView);
        goToSignUpActivityBtn = findViewById(R.id.goTo_signUp_activity_textView);
        loginBtn = findViewById(R.id.login_btn);
    }

    private static class User {

        private String email, gender, nmbr;

        public User(String email, String gender, String nmbr) {
            this.email = email;
            this.gender = gender;
            this.nmbr = nmbr;
        }

        User() {
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getNmbr() {
            return nmbr;
        }

        public void setNmbr(String nmbr) {
            this.nmbr = nmbr;
        }

    }

}
