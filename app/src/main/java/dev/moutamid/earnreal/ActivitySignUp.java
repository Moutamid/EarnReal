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

        databaseReference = FirebaseDatabase.getInstance().getReference().child("user");
//        mDatabaseUsers.keepSynced(true);

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
//                if (isOnline) {
//                    mDialog.show();
                checkStatusOfEditTexts();
//                } else
//                    utils.showOfflineDialog(ActivitySignUp.this);
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
            emailAddressEditText.setError("Please provide an emailStr!");
            emailAddressEditText.requestFocus();
            return;

        }
        // Password is Empty
        if (TextUtils.isEmpty(passwordStr)) {

            mDialog.dismiss();
            passwordEditText.setError("Please provide a passwordStr!");
            passwordEditText.requestFocus();
            return;
        }
        // Confirm Password is Empty
        if (TextUtils.isEmpty(confirmPasswordStr)) {

            mDialog.dismiss();
            confirmPasswordEditText.setError("Please confirm your passwordStr!");
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
        if (emailStr.length() < 5) {
            mDialog.dismiss();
            emailAddressEditText.setError("Minimum length of emailStr must be 5");
            emailAddressEditText.requestFocus();
            return;
        }
        // PHONE NUMBER LENGTH IS LESS THAN 11
        if (numberStr.length() < 11) {
            mDialog.dismiss();
            phoneNmbrEditText.setError("Please enter a valid number!");
            phoneNmbrEditText.requestFocus();
            return;
        }
        // PASSWORD LENGTH LESS THAN 5
        if (passwordStr.length() < 5) {
            mDialog.dismiss();
            passwordEditText.setError("Minimum length of passwordStr must be 5");
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
            emailAddressEditText.setError("Please enter a valid username with no spaces and special characters included!");
            emailAddressEditText.requestFocus();
            return;
        }
        /**
         *CHECKING IF USER IS ALREADY CREATED OR ACCOUNT EXISTS
         */

        createUserWithEmailAndPassword(emailStr, passwordStr);


//
//        mDatabaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                // Checking if user exist
//                if (dataSnapshot.hasChild(emailStr)) {
//
//                    mDialog.dismiss();
//                    userNameEditText.setError("Username already exist");
//                    userNameEditText.requestFocus();
//
//
//                }

        // Signing up user
        //signUpUserWithNameAndPassword(emailStr, passwordStr);

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
                    // ADDING EMAIL AND PHONE NUMBER TO THE REFERRAL NODE OF THE OTHER USER IN THE DATABASE

                } else {
                    // SIGN IN FAILS
                    Log.w(TAG, "createUserWithEmailAndPassword onCompleteFailed: " + task.getException());
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

//        mDatabaseUsers.child(username).child(PUBLIC).child(GENDER).setValue(userGender)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//
//                        // Sign Up Successful
//                        if (task.isSuccessful()) {
//                            mDatabaseUsers.child(username).child(PUBLIC).child(PROFILE_IMAGE).setValue(profileImageLink)
//                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                        @Override
//                                        public void onComplete(@NonNull Task<Void> task) {
//
//                                            if (task.isSuccessful()) {
//
//                                                Intent intent = new Intent(ActivitySignUp.this, SecurityQuestionActivity.class);
//                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//

//
//                                                String chatName = username + "_" + MOUTAMID;
//
//                                                mDatabaseChats.child(chatName).push().setValue(new Chat(MESSAGE_1, MOUTAMID, utils.getTime()));
//                                                mDatabaseChats.child(chatName).push().setValue(new Chat(MESSAGE_2, MOUTAMID, utils.getTime()));
//                                                mDatabaseChats.child(chatName).push().setValue(new Chat(MESSAGE_3, MOUTAMID, utils.getTime()));
//                                                mDatabaseChats.child(chatName).push().setValue(new Chat(MESSAGE_4, MOUTAMID, utils.getTime()));
//
//                                                // Uploading the last message
//                                                mDatabaseChats.child(CHAT_STATUS).child(chatName + "_" + STATUS).child(LAST_MESSAGE).setValue(MESSAGE_1);
//
//                                                // Uploading last message time
//                                                mDatabaseChats.child(CHAT_STATUS).child(chatName + "_" + STATUS).child(LAST_MESSAGE_TIME).setValue(utils.getTime());
//
//
//                                                mDialog.dismiss();
//
//                                                startActivity(intent);
//                                                finish();
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

//    private class OnlineStatus extends AsyncTask<Boolean, Void, Boolean> {
//
//        @Override
//        protected Boolean doInBackground(Boolean... booleans) {
//
//            if (!isCancelled()) {
//
//                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
////                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
////
////                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
////
////                    try {
////
////                        Process p1 = Runtime.getRuntime().exec("ping -c 1 www.google.com");
////                        int returnVal = p1.waitFor();
////                        boolean reachable = (returnVal == 0);
////
////                        return reachable;
////
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                        return false;
////                    }
////
////                } else return false;
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
//
//            if (!isCancelled()) {
//                isOnline = aBoolean;
//
//                new OnlineStatus().execute();
//            }
//
//        }
//
//
//    }

}
