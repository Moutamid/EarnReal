package dev.moutamid.earnreal;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentDashboard extends Fragment {
    private static final String TAG = "FragmentDashboard";

    private TextView totalBalance, totalWithdraw, currentBalance, paidStatus, paidExpireDate, teamMembers, paidMembers, premiumAds, dailyAds;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_layout,container,false);

        initViews(view);

        setValuesToTextViews();

        return view;
    }

    private void setValuesToTextViews() {
        Log.i(TAG, "setValuesToTextViews: ");

        totalBalance.setText("4000");
        totalWithdraw.setText("2600");
        currentBalance.setText("600");
        paidStatus.setText("PAID");
        paidExpireDate.setText("25/10/2020");
        teamMembers.setText("26");
        paidMembers.setText("17");
        premiumAds.setText("15");
        dailyAds.setText("20");

    }

    private void initViews(View v) {
        Log.i(TAG, "initViews: ");

        totalBalance = v.findViewById(R.id.total_balance_tv_dashboard);
        totalWithdraw = v.findViewById(R.id.total_withdraw_tv_dashboard);
        currentBalance = v.findViewById(R.id.current_balance_tv_dashboard);
        paidStatus = v.findViewById(R.id.paid_status_tv_dashboard);
        paidExpireDate = v.findViewById(R.id.paid_status_date_tv_dashboard);
        teamMembers = v.findViewById(R.id.team_members_tv_dashboard);
        paidMembers = v.findViewById(R.id.paid_members_tv_dashboard);
        premiumAds = v.findViewById(R.id.premium_ads_tv_dashboard);
        dailyAds = v.findViewById(R.id.daily_ads_tv_dashboard);

    }
}
