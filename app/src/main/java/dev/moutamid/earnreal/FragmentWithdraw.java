package dev.moutamid.earnreal;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FragmentWithdraw extends Fragment {

    private Utils utils = new Utils();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_withdraw_layout,container,false);

        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup_withdraw_layout);

        final EditText accountNameEt = view.findViewById(R.id.accountName_et_withdraw_layout);
        final EditText accountNmbrEt = view.findViewById(R.id.accountNmbr_et_withdraw_layout);
        final EditText amountEt = view.findViewById(R.id.amount_et_withdraw_layout);
        Button submitBtn = view.findViewById(R.id.submit_btn_upgrade_layout);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioButton radioBtn = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());

                String details = "Method: "+radioBtn.getText().toString() + "\n\n" + "Account: " + accountNameEt.getText().toString() + "\n\n" + "Account number: " + accountNmbrEt.getText().toString() + "\n\n" + "Amount: " + amountEt.getText().toString();

                new Utils().showDialog(getActivity(), "Please confirm your details!", details, "Submit", "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        utils.showWorkDoneDialog(getActivity(), "Successful!", "Your request to withdraw money has been submitted successfully. It will be processed in 12 to 24 business hours.");

                        uploadWithdrawDetails();

                        dialogInterface.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                });

            }
        });

        return view;
    }

    private void uploadWithdrawDetails(String method, String name, String number, String amount) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        withdrawRequestDetails details = new withdrawRequestDetails(method, name, number, amount);

        databaseReference.child("withdraw_requests").push().setValue(details)
    }

    private static class withdrawRequestDetails {

        private String method, name, number, amount;


        public withdrawRequestDetails(String method, String name, String number, String amount) {
            this.method = method;
            this.name = name;
            this.number = number;
            this.amount = amount;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        withdrawRequestDetails() {

        }

    }

}
