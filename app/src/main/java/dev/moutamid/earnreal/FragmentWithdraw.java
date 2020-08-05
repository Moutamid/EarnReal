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

public class FragmentWithdraw extends Fragment {
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

                String details = radioBtn.getText().toString() + "\n" + accountNameEt.getText().toString() + "\n" + accountNmbrEt.getText().toString() + "\n" + amountEt.getText().toString();

                new Utils().showDialog(getActivity(), "Please confirm your details!", details, "Submit", "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();
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
}
