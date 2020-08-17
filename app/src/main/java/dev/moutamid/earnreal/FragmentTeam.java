package dev.moutamid.earnreal;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentTeam extends Fragment {
    private static final String TAG = "FragmentTeam";

    private ArrayList<refUser> refUsersList = new ArrayList<>();
    private ArrayList<String> paid_membersList = new ArrayList<>();

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_team_layout,container,false);

        mAuth = FirebaseAuth.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);

        getTeamFromDatabase();

        initRecyclerView(view);

        return view;
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
                    //teamMembers_tv.setText(String.valueOf(refUsersList.size()));

                    // LOOPING THROUGH THE TEAM LIST AND EXTRACTING OUT PAID MEMBERS
                    for (int i = 0; i <= refUsersList.size() - 1; i++) {
                        if (refUsersList.get(i).isPaid()) {
                            paid_membersList.add(refUsersList.get(i).getEmail());
                        }
                    }// COUNTING THE PAID MEMBERS LIST AND SETTING THE SIZE TO TEXT VIEW
                    //paidMembers_tv.setText(String.valueOf(paid_membersList.size()));

                } else {
                    Log.i(TAG, "onDataChange: No child exists");

//                    teamMembers_tv.setText("0");
//                    paidMembers_tv.setText("0");
                }

//                isDone_getTeamFromDatabase = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "onCancelled: " + error.toException());

                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

//                isDone_getTeamFromDatabase = true;
            }
        });
    }

    private void initRecyclerView(View view) {
        //Log.d(TAG, "initRecyclerView: ");

        RecyclerView conversationRecyclerView = view.findViewById(R.id.team_recyclerView);
        RecyclerViewAdapterTeam adapter = new RecyclerViewAdapterTeam();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        conversationRecyclerView.setLayoutManager(linearLayoutManager);
        conversationRecyclerView.setHasFixedSize(true);
        conversationRecyclerView.setNestedScrollingEnabled(false);

        conversationRecyclerView.setAdapter(adapter);

    }

    private class RecyclerViewAdapterTeam extends RecyclerView.Adapter
            <RecyclerViewAdapterTeam.ViewHolderTeam> {

        @NonNull
        @Override
        public ViewHolderTeam onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            //Log.d(TAG, "onCreateViewHolder: ");
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_team_members, parent, false);
            return new ViewHolderTeam(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderTeam holder, int position) {
            //Log.d(TAG, "onBindViewHolder: " + position);
//
//            if (currentMessagesArrayList.get(position).getMsgUser().equals("me")) {
//
//                holder.rightText.setText(currentMessagesArrayList.get(position).getMsgText());
//
//                holder.leftText.setVisibility(View.GONE);
//
//            } else {
//
//                holder.leftText.setText(currentMessagesArrayList.get(position).getMsgText());
//
//                holder.rightText.setVisibility(View.GONE);
//            }

        }

        @Override
        public int getItemCount() {
//            if (currentMessagesArrayList == null)
                return 0;
//            return currentMessagesArrayList.size();
        }

        public class ViewHolderTeam extends RecyclerView.ViewHolder {

            //TextView leftText, rightText;
            //LinearLayout rightTextLayout;

            public ViewHolderTeam(@NonNull View v) {
                super(v);

//                leftText = v.findViewById(R.id.leftText);
//                rightText = v.findViewById(R.id.rightText);
                //  rightTextLayout = v.findViewById(R.id.rightTextLayout);
            }
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
