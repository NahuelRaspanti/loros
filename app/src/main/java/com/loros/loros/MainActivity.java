package com.loros.loros;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;


public class MainActivity extends AppCompatActivity{

    public FloatingActionButton addButton;
    FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = findViewById(R.id.my_toolbar);
        addButton = findViewById(R.id.fab_add);
        LorosDatabase.getDatabase(getApplicationContext());
        initFCM();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
        setSupportActionBar(mToolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("MIS TAREAS"));
        tabLayout.addTab(tabLayout.newTab().setText("CLASES"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        adapter.addFragment(new MainTaskViewFragment(), "Frag1");
        adapter.addFragment(new ClassroomFragment(), "Frag2");
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.log_out_main:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                return true;
            case R.id.lema:
                startActivity(new Intent(MainActivity.this, LemaActivity.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void sendRegistrationToServer(final String token) {
        Log.d(TAG, "sendRegistrationToServer: sending token to server: " + token);
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {
                    Log.i("Token","Entre a hacer el token, usuario no nulo");
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    reference
                            .child("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("token")
                            .setValue(token);
                }
            }
        };

    }


    private void initFCM(){
        String token = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(token);

    }


}
