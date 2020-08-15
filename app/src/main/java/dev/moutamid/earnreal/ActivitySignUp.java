package dev.moutamid.earnreal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.google.firebase.database.ValueEventListener;


public class ActivitySignUp extends AppCompatActivity {
    private static final String TAG = "ActivitySignUp";

    private static final String USER_EMAIL = "userEmail";
    private static final String USER_ID = "userReferralCode";
    private static final String USER_GENDER = "userGender";
    private static final String USER_NUMBER = "userNumber";
    private static final String REFERRED_BY = "referredBy";
    private static final String PAID_STATUS = "paidStatus";
    private static final String FIRST_TIME_PREMIUM_ADS_QUANTITY = "first_time_premium_ads_quantity";

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
    private boolean isNmbrValid = true;

    private String emailStr, numberStr, passwordStr, confirmPasswordStr, referralCodeStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Log.i(TAG, "onCreate: started");

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
    }

    private void checkOnlineStatus() {
        Log.i(TAG, "checkOnlineStatus: ");

        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.i(TAG, "onDataChange: online status");
                isOnline = snapshot.getValue(Boolean.class);

//                if (isOnline != null)
//                    signUpBtn.setText(isOnline.toString());
//                else signUpBtn.setText("NULL");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "onCancelled: " + error.getMessage());
            }
        });
    }

    private void setListenersToWidgets() {
        Log.i(TAG, "setListenersToWidgets: ");

        femaleBtnLayout.setOnClickListener(femaleBtnLayoutListener());

        maleBtnLayout.setOnClickListener(maleBtnLayoutLsitener());

        goToLoginActivityBtn.setOnClickListener(goToLoginActivityBtnListener());

        signUpBtn.setOnClickListener(signUpBtnListener());

        // IF PHONE NUMBER IS INVALID
        phoneNmbrEditText.addTextChangedListener(phoneNmbrEditTextWatcherListener());

    }

    private TextWatcher phoneNmbrEditTextWatcherListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String nmbr = charSequence.toString();

                if (nmbr.length() == 0)
                    return;

                // FIRST CHARACTER OF THE NUMBER IS NOT 0
                if (nmbr.length() == 1)
                    if (!nmbr.substring(0, 1).equals("0")) {
                        phoneNmbrEditText.setError("Number should start from 0!");
                        isNmbrValid = false;
                        return;
                    }
                // SECOND CHARACTER OF THE NUMBER IS NOT 3
                if (nmbr.length() == 2)
                    if (!nmbr.substring(0, 2).equals("03")) {
                        phoneNmbrEditText.setError("Number should start like 03...!");
                        isNmbrValid = false;
                        return;
                    }

                // THIRD CHARACTER OF THE NUMBER IS 5, 6, 7, 8 OR 9 WHICH ARE INVALID
                if(nmbr.length() >= 3)
                if (nmbr.substring(0, 3).equals("035")
                        || nmbr.substring(0, 3).equals("036")
                        || nmbr.substring(0, 3).equals("037")
                        || nmbr.substring(0, 3).equals("038")
                        || nmbr.substring(0, 3).equals("039")

                ) {
                    phoneNmbrEditText.setError("Number is invalid!");
                    isNmbrValid = false;
                    return;
                }

                isNmbrValid = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    private View.OnClickListener maleBtnLayoutLsitener() {
        Log.i(TAG, "maleBtnLayoutLsitener: ");

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
        Log.i(TAG, "femaleBtnLayoutListener: ");
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
        Log.i(TAG, "goToLoginActivityBtnListener: ");
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivitySignUp.this, ActivityLogin.class));
            }
        };
    }

    private View.OnClickListener signUpBtnListener() {
        Log.i(TAG, "signUpBtnListener: ");
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        Log.i(TAG, "checkStatusOfEditTexts: ");

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

        // EMAIL LENGTH IS LESS THAN 5
        if (emailStr.length() < 5 || emailStr.length() > 30) {
            mDialog.dismiss();
            emailAddressEditText.setError("Email should be between 5 to 30 characters!");
            emailAddressEditText.requestFocus();
            return;
        }

        // EMAIL IS INCORRECT OR INVALID
        if (!Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            mDialog.dismiss();
            emailAddressEditText.setError("Please enter a valid email!");
            emailAddressEditText.requestFocus();
            return;
        }

        // PHONE NUMBER IS EMPTY
        if (TextUtils.isEmpty(numberStr)) {

            mDialog.dismiss();
            phoneNmbrEditText.setError("Please provide your number!");
            phoneNmbrEditText.requestFocus();
            return;
        }

        // PHONE NUMBER IS INVALID
        if (!isNmbrValid) {
            mDialog.dismiss();
            phoneNmbrEditText.setError("Please enter a valid number!");
            phoneNmbrEditText.requestFocus();
            return;
        }

        // PHONE NUMBER LENGTH IS LESS THAN 11
        if (numberStr.length() != 11) {
            mDialog.dismiss();
            phoneNmbrEditText.setError("Please enter a valid number!");
            phoneNmbrEditText.requestFocus();
            return;
        }

        // Password is Empty
        if (TextUtils.isEmpty(passwordStr)) {

            mDialog.dismiss();
            passwordEditText.setError("Please provide a password!");
            passwordEditText.requestFocus();
            return;
        }

        // PASSWORD LENGTH LESS THAN 6 OR GREATER THAN 25
        if (passwordStr.length() < 6 || passwordStr.length() > 25) {
            mDialog.dismiss();
            passwordEditText.setError("Password should be between 6 to 25 characters!");
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

        // PASSWORD AND CONFIRM PASSWORD IS NOT MATCHING
        if (!passwordStr.equals(confirmPasswordStr)) {
            mDialog.dismiss();
            confirmPasswordEditText.setError("Password does not match!");
            confirmPasswordEditText.requestFocus();
            return;
        }

        // USER IS TRYING AGAIN TO ENTER REFERRAL CODE AFTER SIGNING IN
        if (mAuth.getCurrentUser() != null && !TextUtils.isEmpty(referralCodeStr)) {
            checkReferral(true);
            return;
        }

        // USER IS SIMPLY SIGNED IN
        if (mAuth.getCurrentUser() != null) {
            mDialog.dismiss();
            Toast.makeText(ActivitySignUp.this, "You are already signed in", Toast.LENGTH_SHORT).show();

            // ADDING PREMIUM ADS QUANTITY TO SHARED PREFERENCES
            utils.storeInteger(ActivitySignUp.this, FIRST_TIME_PREMIUM_ADS_QUANTITY, 12);

            // REMOVING ALL ACTIVITIES AND STARTING MAIN ACTIVITY
            Intent intent = new Intent(ActivitySignUp.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
            return;
        }


        // IF REFERRAL CODE IS EMPTY
        if (TextUtils.isEmpty(referralCodeStr))

            createUserWithEmailAndPassword(emailStr, passwordStr);

            // IF REFERRAL CODE IS NOT EMPTY
        else {

            createAccountAndCheckReferral();

        }

    }

    private void checkReferral(final boolean isTryingAgain) {

        // IF REFERRAL CODE MATCHES WITH CURRENT USER CODE ID
        if (referralCodeStr.equals(mAuth.getCurrentUser().getUid())) {
            mDialog.dismiss();
            referralCodeEditText.setError("Please enter the correct referral ID!");
            referralCodeEditText.requestFocus();
            return;
        }

        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild(referralCodeStr)) {
                    Log.i(TAG, "onDataChange: has child");

                    // ADDING USER INFORMATION TO THE REFERRED PERSON TEAM
                    refUser refUser = new refUser(emailStr, false);
                    databaseReference.child("teams").child(referralCodeStr).child("users").push().setValue(refUser);

                    // ADDING REFERRAL CODE TO THE USER DATABASE
                    databaseReference.child("users").child(mAuth.getCurrentUser().getUid())
                            .child("refBy").setValue(referralCodeStr);

                    // BOOLEAN VALUE IS TO CHECK THAT IF USER TRIES AGAIN TO ENTER REFERRAL CODE
                    // THEN ADD VALUE TO DATABASE AND UPDATE THE UI OF USER
                    if (isTryingAgain) {
                        mDialog.dismiss();
                        Toast.makeText(ActivitySignUp.this, "Sign up successful", Toast.LENGTH_SHORT).show();

                        // ADDING PREMIUM ADS QUANTITY TO SHARED PREFERENCES
                        utils.storeInteger(ActivitySignUp.this, FIRST_TIME_PREMIUM_ADS_QUANTITY, 12);

                        // REMOVING ALL ACTIVITIES AND STARTING MAIN ACTIVITY
                        Intent intent = new Intent(ActivitySignUp.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        finish();
                        startActivity(intent);
                    }

                } else {
                    Log.i(TAG, "onDataChange: no child");

                    mDialog.dismiss();
                    referralCodeEditText.setError("Please enter the correct referral ID!");
                    referralCodeEditText.requestFocus();

                    Log.i(TAG, "onDataChange: no referral child exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "onCancelled: " + error.toException().getMessage());
            }
        });
    }

    private void createAccountAndCheckReferral() {
        Log.i(TAG, "checkReferralAndCreateAccount: ");

        mAuth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "onComplete: user created");
                    // CREATING USER SUCCESS

                    checkReferral(false);
                    storeUserInformationOffline();
                    addUserDetailsToDatabase();

                    mDialog.dismiss();
                    Toast.makeText(ActivitySignUp.this, "Sign up successful", Toast.LENGTH_SHORT).show();

                    // ADDING PREMIUM ADS QUANTITY TO SHARED PREFERENCES
                    utils.storeInteger(ActivitySignUp.this, FIRST_TIME_PREMIUM_ADS_QUANTITY, 12);

                    // REMOVING ALL ACTIVITIES AND STARTING MAIN ACTIVITY
                    Intent intent = new Intent(ActivitySignUp.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(intent);

                } else {
                    // SIGN IN FAILS
                    Log.w(TAG, "createUserWithEmailAndPassword onCompleteFailed: " + task.getException());
                    mDialog.dismiss();
                    Toast.makeText(ActivitySignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void createUserWithEmailAndPassword(String email, String password) {
        Log.i(TAG, "createUserWithEmailAndPassword: ");

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "onComplete: user created");
                    // CREATING USER SUCCESS

                    storeUserInformationOffline();
                    addUserDetailsToDatabase();

                    mDialog.dismiss();
                    Toast.makeText(ActivitySignUp.this, "Sign up successful", Toast.LENGTH_SHORT).show();

                    // ADDING PREMIUM ADS QUANTITY TO SHARED PREFERENCES
                    utils.storeInteger(ActivitySignUp.this, FIRST_TIME_PREMIUM_ADS_QUANTITY, 12);

                    // REMOVING ALL ACTIVITIES AND STARTING MAIN ACTIVITY
                    Intent intent = new Intent(ActivitySignUp.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(intent);


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
        Log.i(TAG, "storeUserInformationOffline: ");

        utils.storeString(ActivitySignUp.this, USER_GENDER, userGenderStr);
        utils.storeString(ActivitySignUp.this, USER_EMAIL, emailStr);
        utils.storeString(ActivitySignUp.this, USER_NUMBER, numberStr);
        utils.storeString(ActivitySignUp.this, USER_ID, mAuth.getCurrentUser().getUid());

        if (!TextUtils.isEmpty(referralCodeStr))
            utils.storeString(ActivitySignUp.this, REFERRED_BY, referralCodeStr);

    }

    private void addUserDetailsToDatabase() {
        Log.i(TAG, "addUserDetailsToDatabase: ");

        // UPLOADING USER DETAILS TO THE DATABASE
//        databaseReference.child("users").child(mAuth.getCurrentUser().getUid())
//                .child("email").setValue(emailStr);
//
//        databaseReference.child("users").child(mAuth.getCurrentUser().getUid())
//                .child("nmbr").setValue(numberStr);
//
//        databaseReference.child("users").child(mAuth.getCurrentUser().getUid())
//                .child("gender").setValue(userGenderStr);

        User user = new User(emailStr, userGenderStr, numberStr, false);
        databaseReference.child("users").child(mAuth.getCurrentUser().getUid()).setValue(user);

    }

    private void initViews() {
        Log.i(TAG, "initViews: ");

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

        private String email, gender, nmbr;
        private boolean paid;

        public User(String email, String gender, String nmbr, boolean paid) {
            this.email = email;
            this.gender = gender;
            this.nmbr = nmbr;
            this.paid = paid;
        }

        User() {
        }

        public boolean isPaid() {
            return paid;
        }

        public void setPaid(boolean paid) {
            this.paid = paid;
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

    private static class refUser {

        private String email;
        private boolean paid;

        refUser() {

        }

        public refUser(String email, boolean paid) {
            this.email = email;
            this.paid = paid;
        }

        public boolean isPaid() {
            return paid;
        }

        public void setPaid(boolean paid) {
            this.paid = paid;
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
