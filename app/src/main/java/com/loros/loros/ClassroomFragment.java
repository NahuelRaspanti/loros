package com.loros.loros;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassroomFragment extends Fragment implements ClassroomAdapter.onClassroomClick, ClassroomDialog.NoticeDialogListener{

    @BindView (R.id.progressBar) ProgressBar progressBar;
    @BindView (R.id.my_recycler_view) RecyclerView mRecyclerView;
    @BindView (R.id.empty_recylcer) TextView mEmptyRecycler;

    private ArrayList<Classroom> mClassroomList;
    private ArrayList<String> mNames;
    private ClassroomAdapter mAdapter;
    final private ArrayList<String> classroomKeys = new ArrayList<>();
    final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    final String currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private User user;

    public ClassroomFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trabalenguas_fragment, null);

        ButterKnife.bind(this, view);
        mClassroomList = new ArrayList<>();
        mEmptyRecycler.setText(R.string.empty_classroom);
        initializeClassroomList();

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
        if(user.role.equals("teacher")) {
            mainActivity.addButton.setVisibility(View.VISIBLE);
        }
        else
        {
            mainActivity.addButton.setVisibility(View.GONE);
        }
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
                if(dataSnapshot.getValue() == null){
                    mRecyclerView.setVisibility(View.GONE);
                    mEmptyRecycler.setVisibility(View.VISIBLE);
                    return;
                }
                mRecyclerView.setVisibility(View.VISIBLE);
                mEmptyRecycler.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                for (DataSnapshot classroomSnapshot: dataSnapshot.getChildren()) {
                    String key = classroomSnapshot.getKey();
                    Boolean val = (Boolean) classroomSnapshot.getValue();
                    if (val) {
                        classroomIds.put(key, val);
                        classroomKeys.add(key);
                    }
                }
                    for(final LinkedHashMap.Entry<String, Boolean> entry : classroomIds.entrySet()) {
                        final int progressBarCount = classroomIds.size();
                        refClassrooms.child(entry.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Classroom classroom = dataSnapshot.getValue(Classroom.class);
                                getStudentNames(entry.getKey(), classroom);
                                mClassroomList.add(classroom);
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

    private void getStudentNames(String key, final Classroom classroom) {
        DatabaseReference ref = database.child("classroom").child(key).child("students");
        final ArrayList<String> names = new ArrayList<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot students: dataSnapshot.getChildren()) {
                    String userKey = students.getKey();
                    DatabaseReference userRef = database.child("users").child(userKey);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            names.add(user.name);
                            classroom.setStudentNames(names);
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
