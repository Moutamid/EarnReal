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
import android.widget.LinearLayout;
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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;


public class ActivitySignUp extends AppCompatActivity {
    private static final String TAG = "ActivitySignUp";

    private static final String USER_EMAIL = "userEmail";
    private static final String USER_REFERRAL_ID = "userReferralCode";
    private static final String USER_GENDER = "userGender";
    private static final String USER_NUMBER = "userNumber";
    private static final String REFERRED_BY = "referredBy";
    //private static final String PACKAGE_NAME = "dev.moutamid.earnreal";

    private LinearLayout maleBtnLayout, femaleBtnLayout;
    private EditText emailAddressEditText, phoneNmbrEditText, passwordEditText, confirmPasswordEditText, referralCodeEditText;
    private Button signUpBtn;
    private TextView goToLoginActivityBtn;
    private Utils utils = new Utils();
    private String userGenderStr = "male";
    private ProgressDialog mDialog;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private Boolean isOnline = false;

    private String emailStr, numberStr, passwordStr, confirmPasswordStr, referralCodeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // CHECKING ONLINE STATUS
        checkOnlineStatus();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);

//        mDatabaseChats = FirebaseDatabase.getInstance().getReference().child(CHATS);
//        mDatabaseChats.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();


        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(false);
        mDialog.setMessage("Signing you in...");

        initViews();

        setListenersToWidgets();

        Toast.makeText(this, String.valueOf(ServerValue.TIMESTAMP), Toast.LENGTH_SHORT).show();
    }

    private void checkOnlineStatus() {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                isOnline = snapshot.getValue(Boolean.class);

                if (isOnline != null)
                    signUpBtn.setText(isOnline.toString());
                else signUpBtn.setText("NULL");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "onCancelled: " + error.getMessage());
            }
        });
    }

    private void setListenersToWidgets() {

        femaleBtnLayout.setOnClickListener(femaleBtnLayoutListener());

        maleBtnLayout.setOnClickListener(maleBtnLayoutLsitener());

        goToLoginActivityBtn.setOnClickListener(goToLoginActivityBtnListener());

        signUpBtn.setOnClickListener(signUpBtnListener());

    }

    private View.OnClickListener maleBtnLayoutLsitener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                femaleBtnLayout.setBackgroundResource(0);
                maleBtnLayout.setBackground(getResources().getDrawable(R.drawable.background_main));
                userGenderStr = "male";
            }
        };
    }

    private View.OnClickListener femaleBtnLayoutListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maleBtnLayout.setBackgroundResource(0);
                femaleBtnLayout.setBackground(getResources().getDrawable(R.drawable.background_main));
                userGenderStr = "female";
            }
        };
    }

    private View.OnClickListener goToLoginActivityBtnListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivitySignUp.this, ActivityLogin.class));
            }
        };
    }

    private View.OnClickListener signUpBtnListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(ActivitySignUp.this, ActivityVerifyNmbr.class));
                if (isOnline) {
                    mDialog.show();
                    checkStatusOfEditTexts();
                } else {

                    utils.showOfflineDialog(ActivitySignUp.this);
                }
            }
        };
    }


    private void checkStatusOfEditTexts() {

        // Getting strings from edit texts
        emailStr = emailAddressEditText.getText().toString().trim().toLowerCase();
        numberStr = phoneNmbrEditText.getText().toString().trim();
        passwordStr = passwordEditText.getText().toString().trim().toLowerCase();
        confirmPasswordStr = confirmPasswordEditText.getText().toString().trim().toLowerCase();
        referralCodeStr = referralCodeEditText.getText().toString().trim();

        // USER IS OFFLINE
        if (!isOnline) {
            mDialog.dismiss();
            utils.showOfflineDialog(ActivitySignUp.this);
            return;
        }

        // Email is Empty
        if (TextUtils.isEmpty(emailStr)) {
            mDialog.dismiss();
            emailAddressEditText.setError("Please provide an email!");
            emailAddressEditText.requestFocus();
            return;

        }

        // Password is Empty
        if (TextUtils.isEmpty(passwordStr)) {

            mDialog.dismiss();
            passwordEditText.setError("Please provide a password!");
            passwordEditText.requestFocus();
            return;
        }

        // Confirm Password is Empty
        if (TextUtils.isEmpty(confirmPasswordStr)) {

            mDialog.dismiss();
            confirmPasswordEditText.setError("Please confirm your password!");
            confirmPasswordEditText.requestFocus();
            return;
        }

        // PHONE NUMBER IS EMPTY
        if (TextUtils.isEmpty(numberStr)) {

            mDialog.dismiss();
            phoneNmbrEditText.setError("Please provide your number!");
            phoneNmbrEditText.requestFocus();
            return;
        }

        // EMAIL LENGTH IS LESS THAN 5
        if (emailStr.length() < 5 || emailStr.length() > 30) {
            mDialog.dismiss();
            emailAddressEditText.setError("Email should be between 5 to 30 characters!");
            emailAddressEditText.requestFocus();
            return;
        }

        // PHONE NUMBER LENGTH IS LESS THAN 11
        if (numberStr.length() != 11) {
            mDialog.dismiss();
            phoneNmbrEditText.setError("Please enter a valid number!");
            phoneNmbrEditText.requestFocus();
            return;
        }

        // PASSWORD LENGTH LESS THAN 5
        if (passwordStr.length() < 5 || passwordStr.length() > 25) {
            mDialog.dismiss();
            passwordEditText.setError("Password should be between 5 to 25 characters!");
            passwordEditText.requestFocus();
            return;
        }

        // PASSWORD AND CONFIRM PASSWORD IS NOT MATCHING
        if (!passwordStr.equals(confirmPasswordStr)) {
            mDialog.dismiss();
            confirmPasswordEditText.setError("Password does not match!");
            confirmPasswordEditText.requestFocus();
            return;
        }

        // EMAIL IS INCORRECT OR INVALID
        if (!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            mDialog.dismiss();
            emailAddressEditText.setError("Please enter a valid email!");
            emailAddressEditText.requestFocus();
            return;
        }

        // IF REFERRAL CODE IS EMPTY
        if (TextUtils.isEmpty(referralCodeStr))

            createUserWithEmailAndPassword(emailStr, passwordStr);

        // IF REFERRAL CODE IS NOT EMPTY
        else checkReferralAndCreateAccount();

    }

    private void checkReferralAndCreateAccount() {
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(referralCodeStr)) {

                    User user = new User(emailStr, false);
                    databaseReference.child("users").child(referralCodeStr).child("team").push().setValue(user);
                    createUserWithEmailAndPassword(emailStr, passwordStr);
                } else {
                    mDialog.dismiss();
                    referralCodeEditText.setError("Please enter the correct referral ID!");
                    referralCodeEditText.requestFocus();

                    Log.i(TAG, "onDataChange: no referral child exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void createUserWithEmailAndPassword(String email, String password) {
        // USER IS OFFLINE
        if (!isOnline) {
            mDialog.dismiss();
            utils.showOfflineDialog(ActivitySignUp.this);
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // CREATING USER SUCCESS

                    storeUserInformationOffline();
                    addUserDetailsToDatabase();

                    mDialog.dismiss();
                    Toast.makeText(ActivitySignUp.this, "You are logged in", Toast.LENGTH_SHORT).show();

                    //Intent intent = new Intent(ActivitySignUp.this, SecurityQuestionActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //mDialog.dismiss();
                    //startActivity(intent);
                    //finish();

                } else {
                    // SIGN IN FAILS
                    Log.w(TAG, "createUserWithEmailAndPassword onCompleteFailed: " + task.getException());
                    mDialog.dismiss();
                    Toast.makeText(ActivitySignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void storeUserInformationOffline() {
        utils.storeString(ActivitySignUp.this, USER_GENDER, userGenderStr);
        utils.storeString(ActivitySignUp.this, USER_EMAIL, emailStr);
        utils.storeString(ActivitySignUp.this, USER_NUMBER, numberStr);
        utils.storeString(ActivitySignUp.this, USER_REFERRAL_ID, mAuth.getCurrentUser().getUid());

        if (!TextUtils.isEmpty(referralCodeStr))
            utils.storeString(ActivitySignUp.this, REFERRED_BY, referralCodeStr);

    }

    private void addUserDetailsToDatabase() {

        // UPLOADING USER DETAILS TO THE DATABASE
        databaseReference.child("users").child(mAuth.getCurrentUser().getUid())
                .child("email").setValue(emailStr);

        databaseReference.child("users").child(mAuth.getCurrentUser().getUid())
                .child("nmbr").setValue(numberStr);

        databaseReference.child("users").child(mAuth.getCurrentUser().getUid())
                .child("gender").setValue(userGenderStr);

        if (!TextUtils.isEmpty(referralCodeStr))
            databaseReference.child("users").child(mAuth.getCurrentUser().getUid())
                    .child("refBy").setValue(referralCodeStr);
    }

    private void initViews() {

        maleBtnLayout = findViewById(R.id.maleBtnLayout);
        femaleBtnLayout = findViewById(R.id.femaleBtnLayout);
        emailAddressEditText = findViewById(R.id.emailAddress_sign_up_editText);
        phoneNmbrEditText = findViewById(R.id.phoneNmbr_sign_up_editText);
        referralCodeEditText = findViewById(R.id.referralCode_sign_up_editText);
        passwordEditText = findViewById(R.id.password_sign_up_editText);
        confirmPasswordEditText = findViewById(R.id.confirmPassword_sign_up_editText);

        signUpBtn = findViewById(R.id.signUp_btn);
        goToLoginActivityBtn = findViewById(R.id.goTo_login_activity_textView);
    }

    private static class User {

        private String email;
        private boolean status;

        User() {

        }

        public User(String email, boolean status) {
            this.email = email;
            this.status = status;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

    }
//    private static class Chat {
//
//        private String msgText, msgUser, msgTime;
//
//        Chat() {
//
//        }
//
//        public Chat(String msgText, String msgUser, String msgTime) {
//            this.msgText = msgText;
//            this.msgUser = msgUser;
//            this.msgTime = msgTime;
//        }
//
//        public String getMsgText() {
//            return msgText;
//        }
//
//        public void setMsgText(String msgText) {
//            this.msgText = msgText;
//        }
//
//        public String getMsgUser() {
//            return msgUser;
//        }
//
//        public void setMsgUser(String msgUser) {
//            this.msgUser = msgUser;
//        }
//
//        public String getMsgTime() {
//            return msgTime;
//        }
//
//        public void setMsgTime(String msgTime) {
//            this.msgTime = msgTime;
//        }
//    }
}
