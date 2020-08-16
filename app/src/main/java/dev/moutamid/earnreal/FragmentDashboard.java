package dev.moutamid.earnreal;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import java.util.List;

public class FragmentDashboard extends Fragment {
    private static final String TAG = "FragmentDashboard";
    private static final String PREMIUM_ADS_QUANTITY = "premium_ads_quantity";
    private static final String DAILY_ADS_QUANTITY = "premium_ads_quantity";
    private static final String PAID_STATUS = "paidStatus";
    private static final String FIRST_TIME_PREMIUM_ADS_QUANTITY = "first_time_premium_ads_quantity";
    private static final String NEXT_DATE = "nextDate";

    private ArrayList<refUser> refUsersList = new ArrayList<>();
    private ArrayList<String> paid_membersList = new ArrayList<>();
    private ArrayList<String> adsShownEmailList = new ArrayList<>();

    private TextView totalBalance_tv, totalWithdraw_tv, currentBalance_tv, paidMembers_tv;
    private TextView paidStatus_tv, paidExpireDate_tv, teamMembers_tv, premiumAds_tv, dailyAds_tv;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private Utils utils = new Utils();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_layout, container, false);

        mAuth = FirebaseAuth.getInstance();

        initViews(view);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);

        // GETTING TOTAL BALANCE, TOTAL WITHDRAW, CURRENT BALANCE, PAID EXPIRY DATE
        getDetailsFromDatabase();

        // GETTING PAID STATUS
        getPaidStatus();

        // GETTING TEAM MEMBERS, PAID MEMBERS
        getTeamFromDatabase();

        // GETTING PREMIUM ADS 
        getPremiumAdsQuantity();

        // GETTING DAILY ADS
        getDailyAdsQuantity();

        // SETTING INFORMATION DIALOGS ON ALL THE LAYOUTS
        setDialogsOnAllLayouts(view);

        return view;
    }

    private void setDialogsOnAllLayouts(View v) {
        LinearLayout totalbalancelayout = v.findViewById(R.id.total_balance_layout_dashboard);
        LinearLayout totalwithdrawlayout = v.findViewById(R.id.total_withdraw_layout_dashboard);
        LinearLayout currentbalancelayout = v.findViewById(R.id.current_balance_layout_dashboard);
        LinearLayout accountstatuslayout = v.findViewById(R.id.account_status_layout_dashboard);
        LinearLayout teamlayout = v.findViewById(R.id.team_members_layout_dashboard);
        LinearLayout paidmemberlayout = v.findViewById(R.id.paid_members_layout_dashboard);
        LinearLayout premiumlayout = v.findViewById(R.id.premium_ads_layout_dashboard);
        LinearLayout dailylayout = v.findViewById(R.id.daily_ads_layout_dashboard);

        totalbalancelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utils.showDialog(getActivity(), "", "This field will show you all the money you have earned so far.", "Ok", "", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
            }
        });
        totalwithdrawlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utils.showDialog(getActivity(), "", "This field will show you all the money you have received so far.", "Ok", "", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
            }
        });
        currentbalancelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utils.showDialog(getActivity(), "", "This field will show you the money you have right now in your account. \nYou can withdraw this money at any time.", "Ok", "", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
            }
        });
        accountstatuslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utils.showDialog(getActivity(), "", "This field will show you your account premium status. \nUpgrade now to get daily ads and earn even more.", "Ok", "", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
            }
        });
        teamlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utils.showDialog(getActivity(), "", "This field will show you all the members you have invited using your referral code.", "Ok", "", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
            }
        });
        paidmemberlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utils.showDialog(getActivity(), "", "This field will show you all the members you have invited and who have paid and upgraded their accounts.", "Ok", "", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
            }
        });
        premiumlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                utils.showDialog(getActivity(), "", "This field will show you the amount of premium ads you can watch. \nYou will get 15 ads on every paid member of your team. \nEvery single ad will give you Rs: 5.00 \nThe more you invite people to get a paid account, the more you earn.", "Ok", "", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
            }
        });
        dailylayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (utils.getStoredBoolean(getActivity(), PAID_STATUS)) {
                    utils.showDialog(getActivity(), "", "This field will show you the amount of daily ads you get. \nOn everyday you will get 20 ads. \nYou will get Rs: 0.20 (20 paisa) on every single ad you watch.", "Ok", "", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    return;
                }

                utils.showDialog(getActivity(), "", "This field will show you the amount of daily ads you will get. \nYou have to be a paid member to unlock daily ads. \nYou can earn a lot of money monthly through this.", "Ok", "", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
            }
        });

    }

    private void getDailyAdsQuantity() {

        // USER IS NOT PAID
        if (!utils.getStoredBoolean(getActivity(), PAID_STATUS)) {
            return;
        }

        // USER IS PAID AND SHOWING ADD FOR THE FIRST TIME
        if (!utils.getStoredBoolean(getActivity(), "firstTime")) {

            utils.storeInteger(getActivity(), DAILY_ADS_QUANTITY, 20);
            dailyAds_tv.setText("20");

            utils.storeString(getActivity(), NEXT_DATE, utils.getNextDate(getActivity()));

            utils.storeBoolean(getActivity(), "firstTime", true);
            return;
        }

        // IF TODAY'S DATE MATCHES THE NEXT DATE SAVED IN PREFERENCES
        if (utils.getDate(getActivity()).equals(utils.getStoredString(getActivity(), NEXT_DATE))) {

            utils.storeInteger(getActivity(), DAILY_ADS_QUANTITY, 20);
            dailyAds_tv.setText("20");

            utils.storeString(getActivity(), NEXT_DATE, utils.getNextDate(getActivity()));

            return;
        }

        // IF DATE IS'NT CHANGING THEN SHOW THE REAL QUANTITY OF ADS
        dailyAds_tv.setText(utils.getStoredInteger(getActivity(), DAILY_ADS_QUANTITY));
    }

    private void getPaidStatus() {
        databaseReference.child("users").child(mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.hasChild("paid")) {

                            boolean is_paid = snapshot.child("paid").getValue(Boolean.class);

                            utils.storeBoolean(getActivity(), PAID_STATUS, is_paid);

                            if (is_paid) paidStatus_tv.setText("PAID" + " UNTIL ");

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.i(TAG, "onCancelled: " + error.toException());

                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void getPremiumAdsQuantity() {
        databaseReference.child("teams").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!utils.getStoredBoolean(getActivity(), PAID_STATUS)) {
                    premiumAds_tv.setText(
                            String.valueOf(utils.getStoredInteger(getActivity(), FIRST_TIME_PREMIUM_ADS_QUANTITY)));
                    return;
                }

                // IF USER NAME IS IN THE TEAMS CHILD
                if (snapshot.hasChild(mAuth.getCurrentUser().getUid())) {

                    // IF USER HAS SOME PEOPLE WHOM ADS ARE SHOWN THEN THEY WILL DISPLAY HERE
                    if (snapshot.child(mAuth.getCurrentUser().getUid()).hasChild("AdsShown")) {

                        for (DataSnapshot dataSnapshot : snapshot.child(mAuth.getCurrentUser().getUid())
                                .child("AdsShown").getChildren()) {

                            // PEOPLE WHOSE PREMIUM ADS ARE ALREADY SHOWN TO USER
                            adsShownEmailList.add(dataSnapshot.getValue(String.class));

                        }

                        // EXTRACTION OUT PEOPLE WHOSE ADS ARE ALREADY SHOWN
                        List<String> union = new ArrayList<>(paid_membersList);
                        union.addAll(adsShownEmailList);
                        List<String> intersection = new ArrayList<>(paid_membersList);
                        union.retainAll(adsShownEmailList);
                        union.removeAll(intersection);

                        // THIS FIELD WILL UPDATE EVERY SINGLE TIME FRAGMENT OPEN SO DO SOMETING ABOUT THIS

                        // RESETTING THE VALUES OF PREMIUM ADS IN THE PREFERENCES
                        utils.storeInteger(getActivity(), PREMIUM_ADS_QUANTITY, 0);
                        utils.storeInteger(getActivity(), PREMIUM_ADS_QUANTITY, union.size() * 12);

                        int first_time = utils.getStoredInteger(getActivity(), FIRST_TIME_PREMIUM_ADS_QUANTITY);

                        // COUNTING AND SETTING THE NUMBER OF PREMIUM ADS WHICH SHOULD BE SHOWN
                        // AND MERGING THE FIRST TIME GIVEN PREMIUM ADS
                        premiumAds_tv.setText(String.valueOf(union.size() * 12 + first_time));

                    } else {
                        // IF NO PEOPLE EXISTS WHOSE PREMIUM ADS ARE SHOWN

                        utils.storeInteger(getActivity(), PREMIUM_ADS_QUANTITY, 0);
                        utils.storeInteger(getActivity(), PREMIUM_ADS_QUANTITY, paid_membersList.size() * 12);

                        int first_time = utils.getStoredInteger(getActivity(), FIRST_TIME_PREMIUM_ADS_QUANTITY);
                        premiumAds_tv.setText(String.valueOf(paid_membersList.size() * 12 + first_time));

                        for (int i = 1; i <= paid_membersList.size(); i++) {

                            snapshot.child(mAuth.getCurrentUser().getUid()).child("AdsShown")


                        }
                    }

                } else {
                    Log.i(TAG, "onDataChange: no child exists");

                    premiumAds_tv.setText(String.valueOf(utils.getStoredInteger(getActivity(), FIRST_TIME_PREMIUM_ADS_QUANTITY)));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i(TAG, "onCancelled: " + error.toException());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDetailsFromDatabase() {
        databaseReference.child("users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.hasChild("details")) {

                    Details details = snapshot.child("details").getValue(Details.class);
                    setValuesToTextViews(details.getTotalBlnc(), details.gettWithdrw(), details.getCvBlnce(), details.getPaidExpireDate());

                } else
                    setValuesToTextViews("0.00", "0.00", "0.00", "");

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
                    teamMembers_tv.setText(String.valueOf(refUsersList.size()));

                    // LOOPING THROUGH THE TEAM LIST AND EXTRACTING OUT PAID MEMBERS
                    for (int i = 0; i <= refUsersList.size() - 1; i++) {
                        if (refUsersList.get(i).isPaid()) {
                            paid_membersList.add(refUsersList.get(i).getEmail());
                        }
                    }// COUNTING THE PAID MEMBERS LIST AND SETTING THE SIZE TO TEXT VIEW
                    paidMembers_tv.setText(String.valueOf(paid_membersList.size()));

                } else {
                    Log.i(TAG, "onDataChange: No child exists");

                    teamMembers_tv.setText("0");
                    paidMembers_tv.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "onCancelled: " + error.toException());

                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setValuesToTextViews(String total_balance, String total_withdraw, String current_balance, String paid_expireDate) {
        Log.i(TAG, "setValuesToTextViews: ");

        totalBalance_tv.setText(total_balance);
        totalWithdraw_tv.setText(total_withdraw);
        currentBalance_tv.setText(current_balance);

        if (!TextUtils.isEmpty(paid_expireDate)) {
            paidExpireDate_tv.setVisibility(View.VISIBLE);
            paidExpireDate_tv.setText(paid_expireDate);
        }
    }

    private void initViews(View v) {
        Log.i(TAG, "initViews: ");

        totalBalance_tv = v.findViewById(R.id.total_balance_tv_dashboard);
        totalWithdraw_tv = v.findViewById(R.id.total_withdraw_tv_dashboard);
        currentBalance_tv = v.findViewById(R.id.current_balance_tv_dashboard);
        paidStatus_tv = v.findViewById(R.id.paid_status_tv_dashboard);
        paidExpireDate_tv = v.findViewById(R.id.paid_status_date_tv_dashboard);
        teamMembers_tv = v.findViewById(R.id.team_members_tv_dashboard);
        paidMembers_tv = v.findViewById(R.id.paid_members_tv_dashboard);
        premiumAds_tv = v.findViewById(R.id.premium_ads_tv_dashboard);
        dailyAds_tv = v.findViewById(R.id.daily_ads_tv_dashboard);

    }

    private static class Details {

        private String paidExpireDate;
        private String totalBlnc, tWithdrw, cvBlnce;

        public Details(String paidExpireDate, String totalBlnc, String tWithdrw, String cvBlnce) {
            this.paidExpireDate = paidExpireDate;
            this.totalBlnc = totalBlnc;
            this.tWithdrw = tWithdrw;
            this.cvBlnce = cvBlnce;
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
