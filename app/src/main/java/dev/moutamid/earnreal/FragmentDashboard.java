package dev.moutamid.earnreal;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentDashboard extends Fragment {
    private static final String TAG = "FragmentDashboard";

    private ArrayList<refUser> refUsersList = new ArrayList<>();
    private ArrayList<Integer> paid_membersList = new ArrayList<>();

    private TextView totalBalance, totalWithdraw, currentBalance, paidStatus, paidExpireDate, teamMembers, paidMembers, premiumAds, dailyAds;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_layout, container, false);

        mAuth = FirebaseAuth.getInstance();

        initViews(view);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);

        // GETTING totalBalance, totalWithdraw, currentBalance, paidStatus, paidExpireDate
        getDetailsFromDatabase();

        // GETTING teamMembers, paidMembers
        getTeamFromDatabase();

        // GETTING PREMIUM ADS 
        getPremiumAdsQuantity();

//        premiumAds.setText("15");

        // GETTING DAILY ADS
//        dailyAds.setText("20");

        // CONFIGURE THE PREMIUM ADS STRUCTURE
        // CONFIGURE THE DAILY ADS STRUCTURE

        return view;
    }

    private void getPremiumAdsQuantity() {
        databaseReference.child("teams").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getDetailsFromDatabase() {
        databaseReference.child("users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild("details")) {

                    Details details = snapshot.child("details").getValue(Details.class);
                    setValuesToTextViews(details.getTotalBlnc(), details.gettWithdrw(), details.getCvBlnce(), details.getPaidExpireDate(), details.isPaid());

                } else
                    setValuesToTextViews("0.00", "0.00", "0.00", "", false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "onCancelled: " + error.toException());

                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getTeamFromDatabase() {
        databaseReference.child("teams").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild(mAuth.getCurrentUser().getUid())) {

                    // CLEARING ALL THE ITEMS
                    refUsersList.clear();
                    paid_membersList.clear();


                    // LOOPING THROUGH ALL THE CHILDREN OF TEAM
                    for (DataSnapshot dataSnapshot : snapshot.child(mAuth.getCurrentUser().getUid()).child("users").getChildren()) {

                        refUsersList.add(dataSnapshot.getValue(refUser.class));

                    }// COUNTING AMOUNT OF TEAM MEMBERS AND SETTING TO TEXT VIEW
                    teamMembers.setText(refUsersList.size());

                    // LOOPING THROUGH THE TEAM LIST AND EXTRACTING OUT PAID MEMBERS
                    for (int i = 1; i <= refUsersList.size(); i++) {
                        if (refUsersList.get(i).isPaid()) {
                            paid_membersList.add(1);
                        }
                    }// COUNTING THE PAID MEMBERS LIST AND SETTING THE SIZE TO TEXT VIEW
                    paidMembers.setText(paid_membersList.size());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "onCancelled: " + error.toException());

                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setValuesToTextViews(String total_balance, String total_withdraw, String current_balance, String paid_expireDate, boolean is_paid) {
        Log.i(TAG, "setValuesToTextViews: ");

        totalBalance.setText(total_balance);
        totalWithdraw.setText(total_withdraw);
        currentBalance.setText(current_balance);
        if (is_paid) paidStatus.setText("PAID" + " UNTIL ");

        if (!TextUtils.isEmpty(paid_expireDate)) {
            paidExpireDate.setVisibility(View.VISIBLE);
            paidExpireDate.setText(paid_expireDate);
        }
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

    private static class Details {

        private String paidExpireDate;
        private String totalBlnc, tWithdrw, cvBlnce;
        private boolean paid;

        public Details(String paidExpireDate, String totalBlnc, String tWithdrw, String cvBlnce, boolean paid) {
            this.paidExpireDate = paidExpireDate;
            this.totalBlnc = totalBlnc;
            this.tWithdrw = tWithdrw;
            this.cvBlnce = cvBlnce;
            this.paid = paid;
        }

        Details() {
        }

        public String getPaidExpireDate() {
            return paidExpireDate;
        }

        public void setPaidExpireDate(String paidExpireDate) {
            this.paidExpireDate = paidExpireDate;
        }

        public String getTotalBlnc() {
            return totalBlnc;
        }

        public void setTotalBlnc(String totalBlnc) {
            this.totalBlnc = totalBlnc;
        }

        public String gettWithdrw() {
            return tWithdrw;
        }

        public void settWithdrw(String tWithdrw) {
            this.tWithdrw = tWithdrw;
        }

        public String getCvBlnce() {
            return cvBlnce;
        }

        public void setCvBlnce(String cvBlnce) {
            this.cvBlnce = cvBlnce;
        }

        public boolean isPaid() {
            return paid;
        }

        public void setPaid(boolean paid) {
            this.paid = paid;
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
}
