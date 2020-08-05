package dev.moutamid.earnreal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentUpgrade extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upgrade_layout, container, false);

        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.payment_methods_radioGroup_upgrade_layout);

        final int radioButtonId = radioGroup.getCheckedRadioButtonId();

        final ScrollView easypaisaLayout = (ScrollView) view.findViewById(R.id.easypaisa_instructions_layout_upgrade);
        final ScrollView jazzcashLayout = (ScrollView) view.findViewById(R.id.jazzcash_instructions_layout_upgrade);
        final LinearLayout methodSelectionLayout = (LinearLayout) view.findViewById(R.id.method_selection_layout_upgrade);

        view.findViewById(R.id.nextBtn_upgrade_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (radioButtonId == R.id.easypaisa_radioBtn_upgrade_layout) {
                    methodSelectionLayout.setVisibility(View.GONE);
                    easypaisaLayout.setVisibility(View.VISIBLE);

                } else if (radioButtonId == R.id.jazzcash_radioBtn_upgrade_layout) {
                    methodSelectionLayout.setVisibility(View.GONE);
                    jazzcashLayout.setVisibility(View.VISIBLE);

                } else Toast.makeText(getActivity(), "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
