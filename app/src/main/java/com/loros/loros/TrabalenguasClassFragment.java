package com.loros.loros;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class TrabalenguasClassFragment extends Fragment implements RecyclerViewAdapter.OnTrabalenguasClickListener, AddDialog.NoticeDialogListener{

    final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    final String currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private ArrayList<Trabalengua> mTrabalenguasList;
    private String key;
    private ArrayList<String> trabKey = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private User user;
    private final boolean FRAGMENT_CLASS = true;

    public TrabalenguasClassFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trabalenguas_fragment, null);
        mTrabalenguasList = new ArrayList<>();
        Bundle bundle = this.getArguments();
        key = bundle.getString("key");
        getTrabalenguasFromDB();
        mRecyclerView = view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // specify an adapter (see also next example)

        mAdapter = new RecyclerViewAdapter(getActivity(), mTrabalenguasList);
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

    public void getTrabalenguasFromDB() {
        DatabaseReference ref = database.child("classroom").child(key);
        ref.child("trabalenguas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot trabalenguasSnapshot: dataSnapshot.getChildren()) {
                    trabKey.add(trabalenguasSnapshot.getKey());
                    mTrabalenguasList.add(trabalenguasSnapshot.getValue(Trabalengua.class));
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void openDialog() {
        AddDialog diag = new AddDialog();
        diag.setTargetFragment(this, 300);
        diag.show(getFragmentManager(), "Trabalenguas");
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(getActivity(), ChildActivity.class);
        Trabalengua clickedItem = mTrabalenguasList.get(position);
        detailIntent.putExtra("Online", FRAGMENT_CLASS);
        detailIntent.putExtra("Key", key);
        detailIntent.putExtra("TrabKey", trabKey.get(position));
        detailIntent.putExtra("Titulo", clickedItem.getTitle());
        detailIntent.putExtra("Descripcion", clickedItem.getDescription());

        startActivity(detailIntent);
    }

    @Override
    public boolean onLongItemClick(int position) {
        return false;
    }

    @Override
    public void onDialogPositiveClick(String title, String desc) {
        DatabaseReference ref = database.child("classroom").child(key).child("trabalenguas");
        DatabaseReference refKey = ref.push();
        String key = refKey.getKey();
        trabKey.add(key);
        Trabalengua newTrabalenguas = new Trabalengua(title, desc);
        refKey.setValue(newTrabalenguas);
        mTrabalenguasList.add(newTrabalenguas);
        mAdapter.notifyItemInserted(mTrabalenguasList.size() - 1);
    }

    private void getUser() {
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
    }

}