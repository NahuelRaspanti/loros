package com.loros.loros;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MembersFragment extends Fragment implements MemberAdapter.onMemberClick, SearchUserDialog.NoticeDialogListener{

    private ProgressBar progressBar;
    private ArrayList<User> mMemberList;
    private RecyclerView mRecyclerView;
    private MemberAdapter mAdapter;
    private String key;
    final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


    public MembersFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trabalenguas_fragment, null);

        progressBar = view.findViewById(R.id.progressBar);
        mMemberList = new ArrayList<>();

        Bundle bundle = this.getArguments();
        key = bundle.getString("key");

        getMembers();

        mRecyclerView = view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // specify an adapter (see also next example)
        mAdapter = new MemberAdapter(getActivity(), mMemberList);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            onResume();
        }
    }

    @Override
    public void onResume () {
        super.onResume();
        if (!getUserVisibleHint())
        {
            return;
        }
        ClassroomActivity mainActivity = (ClassroomActivity) getActivity();
        mainActivity.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    private void openDialog() {
        SearchUserDialog diag = new SearchUserDialog();
        diag.setTargetFragment(this, 300);
        diag.show(getFragmentManager(), "Member");
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onDialogPositiveClick(final String email) {
        DatabaseReference ref = database.child("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot users : dataSnapshot.getChildren()) {
                    User user = users.getValue(User.class);
                    if(currentUser.getEmail().equals(email.toLowerCase())) {
                        Toast.makeText(getActivity(), "NO PUEDES AÑADIRTE A TU PROPIA CLASE", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(user.email.equals(email.toLowerCase())) {
                        database.child("users").child(users.getKey()).child("classroom").child(key).setValue(true);
                        database.child("classroom").child(key).child("students").child(users.getKey()).setValue(true);
                        mMemberList.add(user);
                        mAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getMembers() {
        DatabaseReference ref = database.child("classroom").child(key).child("students");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot students: dataSnapshot.getChildren()) {
                    String userKey = students.getKey();
                    DatabaseReference userRef = database.child("users").child(userKey);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mMemberList.add(dataSnapshot.getValue(User.class));
                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
