package com.loros.loros;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClassroomActivity extends AppCompatActivity {

    public ArrayList<Trabalengua> mTrabalenguasList;
    private Fragment trabFrag = new TrabalenguasClassFragment();
    private Fragment memberFrag = new MembersFragment();
    public FloatingActionButton addButton;
    final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    final String currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = findViewById(R.id.my_toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        addButton = findViewById(R.id.fab_add);

        DatabaseReference ref = database.child("users/" + currentUserUID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(!user.role.equals("teacher")) {
                    addButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Intent intent = getIntent();
        String title = intent.getStringExtra("ClassName");
        mToolbar.setTitle(title.toUpperCase());
        Bundle bundle = new Bundle();
        bundle.putString("key", intent.getStringExtra("Key"));

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("TRABALENGUAS"));
        tabLayout.addTab(tabLayout.newTab().setText("MIEMBROS"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        adapter.addFragment(trabFrag, "Class1Frag");
        adapter.addFragment(memberFrag, "Class2Frag");

        trabFrag.setArguments(bundle);
        memberFrag.setArguments(bundle);

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}