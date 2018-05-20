package com.loros.loros;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ClassroomFragment extends Fragment implements ClassroomAdapter.onClasroomClick, ClassroomDialog.NoticeDialogListener{

    private ArrayList<Classroom> mClassroomList;
    private ClassroomAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    final private ArrayList<String> classroomKeys = new ArrayList<>();
    final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    final String currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private User user;

    public ClassroomFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trabalenguas_fragment, null);

        progressBar = view.findViewById(R.id.progressBar);
        mClassroomList = new ArrayList<>();
        initializeClassroomList();
        mRecyclerView = view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // specify an adapter (see also next example)
        mAdapter = new ClassroomAdapter(getActivity(), mClassroomList);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        DatabaseReference ref = database.child("users/" + currentUserUID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if(!user.role.equals("teacher")) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.addButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    public void openDialog() {
        ClassroomDialog diag = new ClassroomDialog();
        diag.setTargetFragment(this, 300);
        diag.show(getFragmentManager(), "Classroom");
    }


    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(getActivity(), ClassroomActivity.class);
        Classroom clickedItem = mClassroomList.get(position);
        detailIntent.putExtra("ClassName", clickedItem.getClass_name());
        detailIntent.putExtra("Key", classroomKeys.get(position));
        startActivity(detailIntent);
    }

    @Override
    public void onDialogPositiveClick(String title) {
        DatabaseReference ref = database.child("classroom");
        DatabaseReference ref_user = database.child("users/"+currentUserUID);
        DatabaseReference refKey = ref.push();
        String key = refKey.getKey();
        Classroom newClass = new Classroom(currentUserUID, title, null, null);
        refKey.setValue(newClass);
        ref_user.child("classroom").child(key).setValue(true);
        mClassroomList.add(newClass);
        classroomKeys.add(key);
        mAdapter.notifyItemInserted(mClassroomList.size() - 1);
    }

    public void initializeClassroomList() {
        final LinkedHashMap<String, Boolean> classroomIds = new LinkedHashMap<>();
        DatabaseReference refUsers = database.child("users/" + currentUserUID);
        final DatabaseReference refClassrooms = database.child("classroom");
        refUsers.child("classroom").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.VISIBLE);
                for (DataSnapshot classroomSnapshot: dataSnapshot.getChildren()) {
                    String key = classroomSnapshot.getKey();
                    Boolean val = (Boolean) classroomSnapshot.getValue();
                    if (val) {
                        classroomIds.put(key, val);
                        classroomKeys.add(key);
                    }
                }
                    for(LinkedHashMap.Entry<String, Boolean> entry : classroomIds.entrySet()) {
                        final int progressBarCount = classroomIds.size();
                        refClassrooms.child(entry.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Classroom classroom = dataSnapshot.getValue(Classroom.class);
                                mClassroomList.add(classroom);
                                mAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
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
