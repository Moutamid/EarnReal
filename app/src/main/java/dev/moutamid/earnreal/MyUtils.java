package dev.moutamid.earnreal;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AlertDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

//import dev.moutamid.strangers.R;

public class MyUtils {

    private static final String PUBLIC = "Public";
    private static final String PRIVATE = "Private";
    private static final String GENDER = "gender";
    private static final String PROFILE_IMAGE = "profile_image";
    private static final String PASSWORD = "password";
    private static final String USERS = "users";
    private static final String USER_NAME = "userName";
    private static final String USER_PASSWORD = "userPassword";
    private static final String USERS_GENDER = "userGender";
    private static final String LOG_STATUS = "logStatus";
    private static final String PACKAGE_NAME = "dev.moutamid.strangers";
    private static final String STATUS = "status";
    private static final String FULLNAME = "fullname";
    private static final String ONLINE_STATUS = "online_status";
    private static final String LAST_MESSAGE = "last_message";
    private static final String LAST_MESSAGE_TIME = "last_message_time";
    private static final String CHAT_STATUS = "chats_status";
    private static final String READ_STATUS = "read_status";
    private static final String CHATS = "chats";
    private static final String MSG_TEXT = "msgText";
    private static final String MSG_USER = "msgUser";
    private static final String WAITING = "waiting";
    private static final String MSG_TIME = "msgTime";

    private static final String USER_OWN = "userOwn";
    private static final String USER_OTHER = "userOther";
    private static final String USER_ALL = "userAll";
    private static final String STATUS_IMAGE = "statusImage";

//    private DatabaseReference mDatabase;
    private SharedPreferences sharedPreferences;

    public String getStoredString(Context context, String name) {

        sharedPreferences = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(name, "Error");

    }

    public void storeString(Context context, String name, String object) {
        //SharedPreferences sharedPreferences = this.getSharedPreferences("dev.moutamid.strangers", Context.MODE_PRIVATE);

        sharedPreferences = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(name, object).apply();

    }

    public String getRandomNmbr(int length) {

        return String.valueOf(new Random().nextInt(length) + 1);
    }

    public void showOfflineDialog(Context context) {

        TextView closeBtn;
        Button okayBtn;

        final Dialog dialogOffline = new Dialog(context);
        dialogOffline.setContentView(R.layout.dialog_offline);

        closeBtn = dialogOffline.findViewById(R.id.close_btn_offline_dialog);
        okayBtn = dialogOffline.findViewById(R.id.okay_btn_offline_dialog);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogOffline.dismiss();
            }
        });

        okayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogOffline.dismiss();
            }
        });

        dialogOffline.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogOffline.show();

    }

    public String getTime() {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        return sdf.format(date);

    }

    public void showDialog(Context context, String title, String message, String positiveBtnName, String negativeBtnName, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener) {

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveBtnName, positiveListener)
                .setNegativeButton(negativeBtnName, negativeListener)
                .setCancelable(false)
                .show();

    }
}
