package com.loros.loros;

import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChildActivity extends AppCompatActivity {


    @BindView (R.id.my_toolbar) Toolbar mToolbar;
    @BindView (R.id.edit_button) Button mEdit;
    @BindView (R.id.save_button) Button mSave;
    @BindView (R.id.my_title) TextView textViewTitle;
    @BindView (R.id.my_trabalengua) TextView textViewDesc;
    @BindView (R.id.my_title_edit) EditText mEditableTextTitle;
    @BindView (R.id.my_trabalengua_edit) EditText mEditableTextDesc;
    @BindView (R.id.trab_speed) SeekBar mSpeed;
    @BindView (R.id.trab_pitch) SeekBar mPitch;

    private TextToSpeech mTTS;
    private String trabalenguas;
    private int position;
    private Intent resultIntent;
    private static boolean ACTIVE = false;
    private boolean isOnline;
    private String key;
    private  String trabKey;
    private ArrayList<Trabalengua> trabalenguaList;
    final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    final String currentUserUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private User user;
    String descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        ButterKnife.bind(this);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        getUser();
        final String titulo = intent.getStringExtra("Titulo");
        descripcion = intent.getStringExtra("Descripcion");

        isOnline = intent.getBooleanExtra("Online", false);

        if(!isOnline) {
            trabalenguas = getIntent().getStringExtra("List");
            trabalenguaList = new Gson().fromJson(trabalenguas, new TypeToken<ArrayList<Trabalengua>>() {
            }.getType());
            position = getIntent().getIntExtra("Position", 0);
            mEdit.setVisibility(View.VISIBLE);
            mSave.setVisibility(View.VISIBLE);
        }
        else {
            key = intent.getStringExtra("Key");
            trabKey = intent.getStringExtra("TrabKey");
        }

        mEdit.setText("EDITAR");
        textViewTitle.setText(titulo.toUpperCase());
        textViewDesc.setText(descripcion.toUpperCase());

        textViewTitle.setSelected(true);

        //Clickable words
        SpannableString ss = new SpannableString(descripcion.toUpperCase());
        String[] words = descripcion.split(" ");
        int i = 0;
        for (final String word : words) {

                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                        speak(word);
                        Random rnd = new Random();
                        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                        textViewDesc.setHighlightColor(color);
                    }
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setColor(Color.BLACK);
                        ds.setUnderlineText(false);

                    }

                };
                ss.setSpan(clickableSpan, i, i + word.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                i = i + word.length() + 1;

        }
        textViewDesc.setText(ss);
        textViewDesc.setMovementMethod(LinkMovementMethod.getInstance());

        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pasa de CANCELAR a EDITAR
                if(ACTIVE) {
                    textViewTitle.setText(titulo.toUpperCase());
                    textViewDesc.setText(descripcion.toUpperCase());

                    mEdit.setText("EDITAR");

                    mSave.setVisibility(View.GONE);

                    mEditableTextTitle.setVisibility(View.GONE);
                    textViewTitle.setVisibility(View.VISIBLE);

                    mEditableTextDesc.setVisibility(View.GONE);
                    textViewDesc.setVisibility(View.VISIBLE);

                    mEditableTextTitle.setText(titulo.toUpperCase());
                    mEditableTextDesc.setText(descripcion.toUpperCase());
                    ACTIVE = false;
                }
                //Pasa de EDITAR a CANCELAR
                else {
                    mEdit.setText("CANCELAR");
                    mSave.setVisibility(View.VISIBLE);

                    mEditableTextTitle.setVisibility(View.VISIBLE);
                    mEditableTextDesc.setVisibility(View.VISIBLE);

                    textViewTitle.setVisibility(View.GONE);
                    textViewDesc.setVisibility(View.GONE);

                    mEditableTextTitle.setText(titulo.toUpperCase());
                    mEditableTextDesc.setText(descripcion.toUpperCase());
                    ACTIVE = true;
                }
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEdit.setText("EDITAR");
                mSave.setVisibility(View.GONE);

                String editedDesc = mEditableTextDesc.getText().toString();
                String editedTitle = mEditableTextTitle.getText().toString();

                if(!isOnline) {
                    trabalenguaList.set(position, new Trabalengua(editedTitle, editedDesc));
                    saveTrabalenguas(trabalenguaList);
                }
                else {
                    saveTrabalenguasOnline(new Trabalengua(editedTitle, editedDesc));
                    resultIntent = new Intent();
                    resultIntent.putExtra("result", true);
                }

                textViewTitle.setText(editedTitle.toUpperCase());
                textViewDesc.setText(editedDesc.toUpperCase());

                mEditableTextTitle.setVisibility(View.GONE);
                textViewTitle.setVisibility(View.VISIBLE);

                mEditableTextDesc.setVisibility(View.GONE);
                textViewDesc.setVisibility(View.VISIBLE);

                descripcion = editedDesc;

                Toast.makeText(ChildActivity.this, "TRABALENGUAS GUARDADO!", Toast.LENGTH_SHORT).show();
                ACTIVE = false;
            }
        });


    }

    @Override
    public void onBackPressed() {
        if(isOnline) {
            if (resultIntent == null) {
                resultIntent = new Intent();
                resultIntent.putExtra("result", false);
            }
            setResult(RESULT_OK, resultIntent);
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.child_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.play:
                speak();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void speak() {
        float speed = (float) mSpeed.getProgress() / 50;
        if(speed < 0.1) speed = 0.1f;
        float pitch = (float) mPitch.getProgress() / 50;
        if(pitch < 0.1) pitch = 0.1f;
        mTTS.setSpeechRate(speed);
        mTTS.setPitch(pitch);
        mTTS.speak(descripcion, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    private void speak(String word) {
        float speed = (float) mSpeed.getProgress() / 50;
        if(speed < 0.1) speed = 0.1f;
        float pitch = (float) mPitch.getProgress() / 50;
        if(pitch < 0.1) pitch = 0.1f;
        mTTS.setSpeechRate(speed);
        mTTS.setPitch(pitch);
        mTTS.speak(word, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    protected void onDestroy() {
        if(mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }

        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if(mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
            mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(status == TextToSpeech.SUCCESS) {
                        Locale locSpanish = new Locale("spa", "ARG");
                        int result = mTTS.setLanguage(locSpanish);

                        if(result == TextToSpeech.LANG_MISSING_DATA
                                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Log.e("TTS", "Language not supported");
                        }
                        if(getApplicationInfo().targetSdkVersion >= 21) {
                            mTTS.getVoices();
                            Voice voice = new Voice("es-es-x-ana#female_3-local", Locale.getDefault(), 1, 1, false, null);
                            mTTS.setVoice(voice);
                        }
                    }
                    else {
                        Log.e("TTS", "Inizialization failed");
                    }
                }
            });
        super.onResume();
    }



    private void saveTrabalenguas(ArrayList<Trabalengua> trab) {
        try {
            Writer output = null;
            String outDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/trabalenguas.json";
            File file = new File(outDir);
            output = new BufferedWriter(new FileWriter(file));

            String json = new Gson().toJson(trab);
            output.write(json);
            output.close();
        }
        catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void saveTrabalenguasOnline(Trabalengua trab) {
        DatabaseReference ref = database.child("classroom").child(key).child("trabalenguas");
        ref.child(trabKey).setValue(trab);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
