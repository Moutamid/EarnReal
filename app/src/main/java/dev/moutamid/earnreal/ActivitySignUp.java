package dev.moutamid.earnreal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;

//import Utils.Utils;

public class ActivitySignUp extends AppCompatActivity {

    private LinearLayout maleBtnLayout, femaleBtnLayout;
    private EditText userNameEditText, passwordEditText, confirmPasswordEditText;
    private Button signUpBtn;
    private TextView goToLoginActivityBtn;

    private Utils utils = new Utils();
    private String userGender = "male";
    private ProgressDialog mDialog;

    private static final String PUBLIC = "Public";
    private static final String PRIVATE = "Private";
    private static final String GENDER = "gender";
    private static final String PROFILE_IMAGE = "profile_image";
    private static final String MOUTAMID = "moutamid";
    private static final String PASSWORD = "password";
    private static final String USERS = "users";
    private static final String STATUS = "status";
    private static final String USER_NAME = "userName";
    private static final String LAST_MESSAGE = "last_message";
    private static final String LAST_MESSAGE_TIME = "last_message_time";
    private static final String CHAT_STATUS = "chats_status";
    private static final String USER_PASSWORD = "userPassword";
    private static final String USERS_GENDER = "userGender";
    private static final String LOG_STATUS = "logStatus";
    private static final String CHATS = "chats";
    private static final String PACKAGE_NAME = "dev.moutamid.strangers";
    private static final String DOMAIN = "@strangers.com";

    private static final String MESSAGE_1 = "Hey there! Welcome to my app :)";
    private static final String MESSAGE_2 = "I am the developer of this app. You can send me a message and i will try to respond immediately";
    private static final String MESSAGE_3 = "You can give me any suggestion and complaints about this app and i will certainly react to them just mention a BIG COMPLAINT TITLE before sending any complaints ;)";
    private static final String MESSAGE_4 = "Hope you'll enjoy using my app Bye bye.. ;)";

//    FirebaseAuth mAuth;

    private static final String BOY0 = "https://firebasestorage.googleapis.com/v0/b/strangers-43e21.appspot.com/o/boy0.png?alt=media&token=17a0d80e-3935-4c35-8e45-46084c240a33";
    private static final String BOY1 = "https://firebasestorage.googleapis.com/v0/b/strangers-43e21.appspot.com/o/boy1.png?alt=media&token=38825ff3-e597-49ec-95ed-5d420edebc71";
    private static final String BOY2 = "https://firebasestorage.googleapis.com/v0/b/strangers-43e21.appspot.com/o/boy2.png?alt=media&token=f5b42b0b-49f4-4f8c-a047-db32f99cb4b9";
    private static final String BOY3 = "https://firebasestorage.googleapis.com/v0/b/strangers-43e21.appspot.com/o/boy3.png?alt=media&token=1752f72d-1df4-4763-a6a7-baf4368c3c5f";
    private static final String BOY4 = "https://firebasestorage.googleapis.com/v0/b/strangers-43e21.appspot.com/o/boy4.png?alt=media&token=124e58d9-6435-400b-b1f9-a40ee51cce33";
    private static final String BOY5 = "https://firebasestorage.googleapis.com/v0/b/strangers-43e21.appspot.com/o/boy5.png?alt=media&token=b8ca30c9-e564-4620-9773-79429ab1270f";
    private static final String BOY6 = "https://firebasestorage.googleapis.com/v0/b/strangers-43e21.appspot.com/o/boy6.png?alt=media&token=ca7b39a6-ade7-4579-a504-c01862bce4cd";
    private static final String GIRL0 = "https://firebasestorage.googleapis.com/v0/b/strangers-43e21.appspot.com/o/girl0.png?alt=media&token=375a42cb-9daa-40e9-a02f-ecf0dab28c0d";
    private static final String GIRL1 = "https://firebasestorage.googleapis.com/v0/b/strangers-43e21.appspot.com/o/girl1.png?alt=media&token=d8f13191-8497-4cb9-a496-1bee51ac82f9";
    private static final String GIRL2 = "https://firebasestorage.googleapis.com/v0/b/strangers-43e21.appspot.com/o/girl2.png?alt=media&token=d7cbf74a-f6c3-41c0-9999-e09c6282d032";

//    private DatabaseReference mDatabaseUsers;
//    private DatabaseReference mDatabaseChats;
    private Boolean isOnline = false;
    private SharedPreferences sharedPreferences;

    String profileImageLink;
    private int randomNmbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //new OnlineStatus().execute();

//        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child(USERS);
//        mDatabaseUsers.keepSynced(true);

//        mDatabaseChats = FirebaseDatabase.getInstance().getReference().child(CHATS);
//        mDatabaseChats.keepSynced(true);

//        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = this.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);

        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(false);
        mDialog.setMessage("Signing you in...");

        initViews();

        setListenersToWidgets();
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
                userGender = "male";
            }
        };
    }

    private View.OnClickListener femaleBtnLayoutListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maleBtnLayout.setBackgroundResource(0);
                femaleBtnLayout.setBackground(getResources().getDrawable(R.drawable.background_main));
                userGender = "female";
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
                startActivity(new Intent(ActivitySignUp.this, ActivityVerifyNmbr.class));
//                if (isOnline) {
//                    mDialog.show();
//                    checkStatusOfEditTexts();
//                } else
//                    utils.showOfflineDialog(ActivitySignUp.this);
            }
        };
    }

    private void setImageUrlBoy() {

        randomNmbr = new Random().nextInt(6);

        switch (randomNmbr) {

            case 0:

                profileImageLink = BOY0;

                break;

            case 1:

                profileImageLink = BOY1;

                break;

            case 2:

                profileImageLink = BOY2;

                break;

            case 3:

                profileImageLink = BOY3;

                break;

            case 4:

                profileImageLink = BOY4;

                break;

            case 5:

                profileImageLink = BOY5;

                break;

            case 6:

                profileImageLink = BOY6;

                break;

        }

    }

    private void setImageUrlGirl() {

        randomNmbr = new Random().nextInt(2);

        switch (randomNmbr) {

            case 0:

                profileImageLink = GIRL0;

                break;

            case 1:

                profileImageLink = GIRL1;

                break;

            case 2:

                profileImageLink = GIRL2;

                break;
        }

    }

    private void checkStatusOfEditTexts() {

        // Getting strings from edit texts
        final String username = userNameEditText.getText().toString().trim().toLowerCase();
        final String password = passwordEditText.getText().toString().trim();
        final String confirmedPassword = confirmPasswordEditText.getText().toString().trim();
//
//        mDatabaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                // Checking if user exist
//                if (dataSnapshot.hasChild(username)) {
//
//                    mDialog.dismiss();
//                    userNameEditText.setError("Username already exist");
//                    userNameEditText.requestFocus();
//
//
//                } else {
//
//                    // Checking if Fields are empty or not
//                    if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmedPassword)) {
//
//                        if (username.length() >= 3) {
//
//                            // Checking Length of password
//                            if (password.length() >= 6) {
//
//                                // Checking if password is equal to confirmed Password
//                                if (password.equals(confirmedPassword)) {
//
//                                    // Signing up user
//                                    signUpUserWithNameAndPassword(username, password);
//
//                                } else if (!password.equals(confirmedPassword)) {
//
//                                    mDialog.dismiss();
//                                    confirmPasswordEditText.setError("Password does not match!");
//                                    confirmPasswordEditText.requestFocus();
//
//                                }
//
//                            } else {
//
//                                mDialog.dismiss();
//                                passwordEditText.setError("Minimum length of password must be 6");
//                                passwordEditText.requestFocus();
//
//                            }
//
//                        } else {
//                            mDialog.dismiss();
//                            userNameEditText.setError("Minimum length of username must be 3");
//                            userNameEditText.requestFocus();
//
//                        }
//
//                        // User Name is Empty
//                    } else if (TextUtils.isEmpty(username)) {
//
//                        mDialog.dismiss();
//                        userNameEditText.setError("Please provide a username");
//                        userNameEditText.requestFocus();
//
//
//                        // Password is Empty
//                    } else if (TextUtils.isEmpty(password)) {
//
//                        mDialog.dismiss();
//                        passwordEditText.setError("Please provide a password");
//                        passwordEditText.requestFocus();
//
//
//                        // Confirm Password is Empty
//                    } else if (TextUtils.isEmpty(confirmedPassword)) {
//
//                        mDialog.dismiss();
//                        confirmPasswordEditText.setError("Please confirm your password");
//                        confirmPasswordEditText.requestFocus();
//
//
//                    }
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

    private void signUpUserWithNameAndPassword(final String username, String password) {

        if (isOnline) {

            String email = username + DOMAIN;

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                //if Email Address is Invalid..

                mDialog.dismiss();
                userNameEditText.setError("Please enter a valid username with no spaces and special characters included");
                userNameEditText.requestFocus();
            } else {

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
        } else {

            mDialog.dismiss();
            utils.showOfflineDialog(ActivitySignUp.this);
        }
    }

    private static class Chat {

        private String msgText, msgUser, msgTime;

        Chat() {

        }

        public Chat(String msgText, String msgUser, String msgTime) {
            this.msgText = msgText;
            this.msgUser = msgUser;
            this.msgTime = msgTime;
        }

        public String getMsgText() {
            return msgText;
        }

        public void setMsgText(String msgText) {
            this.msgText = msgText;
        }

        public String getMsgUser() {
            return msgUser;
        }

        public void setMsgUser(String msgUser) {
            this.msgUser = msgUser;
        }

        public String getMsgTime() {
            return msgTime;
        }

        public void setMsgTime(String msgTime) {
            this.msgTime = msgTime;
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
        userNameEditText = findViewById(R.id.userName_sign_up_editText);
        passwordEditText = findViewById(R.id.password_sign_up_editText);
        confirmPasswordEditText = findViewById(R.id.confirmPassword_sign_up_editText);
//        showPasswordBtn = findViewById(R.id.showPassword_btn_sign_up);
//        showConfirmPasswordBtn = findViewById(R.id.showConfirmPassword_btn_sign_up);
        signUpBtn = findViewById(R.id.signUp_btn);
        goToLoginActivityBtn = findViewById(R.id.goTo_login_activity_textView);
    }

    private void storeString(String name, String object) {
        //SharedPreferences sharedPreferences = this.getSharedPreferences("dev.moutamid.strangers", Context.MODE_PRIVATE);

        sharedPreferences.edit().putString(name, object).apply();

    }

    private class OnlineStatus extends AsyncTask<Boolean, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Boolean... booleans) {

            if (!isCancelled()) {

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//
//                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
//
//                    try {
//
//                        Process p1 = Runtime.getRuntime().exec("ping -c 1 www.google.com");
//                        int returnVal = p1.waitFor();
//                        boolean reachable = (returnVal == 0);
//
//                        return reachable;
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        return false;
//                    }
//
//                } else return false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (!isCancelled()) {
                isOnline = aBoolean;

                new OnlineStatus().execute();
            }

        }


    }

}
