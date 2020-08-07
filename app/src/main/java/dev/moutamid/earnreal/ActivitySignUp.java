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

    private static final String USER_EMAIL = "gender";
    private static final String USER_PASSWORD = "userPassword";
    private static final String USER_GENDER = "userGender";
    private static final String USER_NUMBER = "userGender";
    private static final String PACKAGE_NAME = "dev.moutamid.earnreal";

    private LinearLayout maleBtnLayout, femaleBtnLayout;
    private EditText emailAddressEditText, phoneNmbrEditText, passwordEditText, confirmPasswordEditText, referralCodeEditText;
    private Button signUpBtn;
    private TextView goToLoginActivityBtn;
    private Utils utils = new Utils();
    private String userGenderStr = "male";
    private ProgressDialog mDialog;
    private FirebaseAuth mAuth;
    private Boolean isOnline = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // CHECKING ONLINE STATUS
        checkOnlineStatus();

//        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child(USERS);
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
        final String email = emailAddressEditText.getText().toString().trim().toLowerCase();
        final String phone = phoneNmbrEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim().toLowerCase();
        final String confirmedPassword = confirmPasswordEditText.getText().toString().trim().toLowerCase();

        // USER IS OFFLINE
        if (!isOnline) {
            mDialog.dismiss();
            utils.showOfflineDialog(ActivitySignUp.this);
            return;
        }
        // Email is Empty
        if (TextUtils.isEmpty(email)) {
            mDialog.dismiss();
            emailAddressEditText.setError("Please provide an email!");
            emailAddressEditText.requestFocus();
            return;

        }
        // Password is Empty
        if (TextUtils.isEmpty(password)) {

            mDialog.dismiss();
            passwordEditText.setError("Please provide a password!");
            passwordEditText.requestFocus();
            return;
        }
        // Confirm Password is Empty
        if (TextUtils.isEmpty(confirmedPassword)) {

            mDialog.dismiss();
            confirmPasswordEditText.setError("Please confirm your password!");
            confirmPasswordEditText.requestFocus();
            return;
        }
        // PHONE NUMBER IS EMPTY
        if (TextUtils.isEmpty(phone)) {

            mDialog.dismiss();
            phoneNmbrEditText.setError("Please provide your number!");
            phoneNmbrEditText.requestFocus();
            return;
        }
        // EMAIL LENGTH IS LESS THAN 5
        if (email.length() < 5) {
            mDialog.dismiss();
            emailAddressEditText.setError("Minimum length of email must be 5");
            emailAddressEditText.requestFocus();
            return;
        }
        // PHONE NUMBER LENGTH IS LESS THAN 11
        if (phone.length() < 11) {
            mDialog.dismiss();
            phoneNmbrEditText.setError("Please enter a valid number!");
            phoneNmbrEditText.requestFocus();
            return;
        }
        // PASSWORD LENGTH LESS THAN 5
        if (password.length() < 5) {
            mDialog.dismiss();
            passwordEditText.setError("Minimum length of password must be 5");
            passwordEditText.requestFocus();
            return;
        }
        // PASSWORD AND CONFIRM PASSWORD IS NOT MATCHING
        if (!password.equals(confirmedPassword)) {
            mDialog.dismiss();
            confirmPasswordEditText.setError("Password does not match!");
            confirmPasswordEditText.requestFocus();
            return;
        }
        // EMAIL IS INCORRECT OR INVALID
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mDialog.dismiss();
            emailAddressEditText.setError("Please enter a valid username with no spaces and special characters included!");
            emailAddressEditText.requestFocus();
            return;
        }
        /**
         *CHECKING IF USER IS ALREADY CREATED OR ACCOUNT EXISTS
         */

        createUserWithEmailAndPassword(email, password);

        //String referralCode = referralCodeEditText.getText().toString().trim();


//
//        mDatabaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                // Checking if user exist
//                if (dataSnapshot.hasChild(email)) {
//
//                    mDialog.dismiss();
//                    userNameEditText.setError("Username already exist");
//                    userNameEditText.requestFocus();
//
//
//                } else {
//
//                    // Checking if Fields are empty or not
//        if (!TextUtils.isEmpty(email) &&
//                !TextUtils.isEmpty(password) &&
//                !TextUtils.isEmpty(confirmedPassword) && !TextUtils.isEmpty(phone)
//
//        ) {

        // Signing up user
        signUpUserWithNameAndPassword(email, password);


        //}
//
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//        });
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
                    // ADDING USER INFORMATION LIKE EMAIL, PHONE NUMBER TO DATABASE
                    // ADDING EMAIL AND PHONE NUMBER TO THE REFERRAL NODE OF THE OTHER USER IN THE DATABASE
                } else {
                    // SIGN IN FAILS
                    Log.w(TAG, "createUserWithEmailAndPassword onCompleteFailed: " + task.getException());
                    Toast.makeText(ActivitySignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signUpUserWithNameAndPassword(final String username, String password) {

        if (isOnline) {

            String email = username;


//                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//
//                        if (task.isSuccessful()) {
//
//                            addUserDetailsToDatabase(username);
//
//                        } else {
//
//                            mDialog.dismiss();
//                            Toast.makeText(ActivitySignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
        }


    }

    private void addUserDetailsToDatabase(final String username) {

//        mDatabaseUsers.child(username).child(PUBLIC).child(GENDER).setValue(userGender)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//
//                        // Sign Up Successful
//                        if (task.isSuccessful()) {
//
//                            if (userGender.equals("male"))
//
//                                setImageUrlBoy();
//
//                            else setImageUrlGirl();
//
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
//                                                storeString(USER_NAME, username);
//                                                storeString(USERS_GENDER, userGender);
//                                                storeString(LOG_STATUS, "true");
//                                                storeString(PROFILE_IMAGE, profileImageLink);
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
//
//                                            } else {
//                                                mDialog.dismiss();
//                                                Toast.makeText(ActivitySignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//                                    });
//
//
//                        } else {
//
//                            mDialog.dismiss();
//                            Toast.makeText(ActivitySignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                });
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
