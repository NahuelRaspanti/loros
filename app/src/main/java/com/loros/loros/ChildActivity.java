package com.loros.loros;

import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class ChildActivity extends AppCompatActivity {

    private TextToSpeech mTTS;
    private SeekBar mSpeed;
    private SeekBar mPitch;
    private String trabalenguas;
    private int position;
    private Button mEdit;
    private Button mSave;
    private static boolean ACTIVE = false;
    String descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        Intent intent = getIntent();

        final String titulo = intent.getStringExtra("Titulo");
        descripcion = intent.getStringExtra("Descripcion");
        trabalenguas = getIntent().getStringExtra("List");
        final ArrayList<Trabalengua> trabalenguaList = new Gson().fromJson(trabalenguas, new TypeToken<ArrayList<Trabalengua>>(){}.getType());
        position = getIntent().getIntExtra("Position", 0);


        final TextView textViewTitle = findViewById(R.id.my_title);
        final TextView textViewDesc = findViewById(R.id.my_trabalengua);
        final EditText mEditableTextTitle = findViewById(R.id.my_title_edit);
        final EditText mEditableTextDesc = findViewById(R.id.my_trabalengua_edit);

        mSpeed = findViewById(R.id.trab_speed);
        mPitch = findViewById(R.id.trab_pitch);


        mEdit = findViewById(R.id.edit_button);
        mSave = findViewById(R.id.save_button);
        mEdit.setText("EDITAR");
        textViewTitle.setText(titulo);
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

                trabalenguaList.set(position, new Trabalengua(editedTitle, editedDesc));
                saveTrabalenguas(trabalenguaList);

                textViewTitle.setText(editedTitle.toUpperCase());
                textViewDesc.setText(editedDesc.toUpperCase());

                mEditableTextTitle.setVisibility(View.GONE);
                textViewTitle.setVisibility(View.VISIBLE);

                mEditableTextDesc.setVisibility(View.GONE);
                textViewDesc.setVisibility(View.VISIBLE);

                Toast.makeText(ChildActivity.this, "TRABALENGUAS GUARDADO!", Toast.LENGTH_SHORT).show();
                ACTIVE = false;
            }
        });


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
        mTTS.speak(descripcion, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void speak(String word) {
        float speed = (float) mSpeed.getProgress() / 50;
        if(speed < 0.1) speed = 0.1f;
        float pitch = (float) mPitch.getProgress() / 50;
        if(pitch < 0.1) pitch = 0.1f;
        mTTS.setSpeechRate(speed);
        mTTS.setPitch(pitch);
        mTTS.speak(word, TextToSpeech.QUEUE_FLUSH, null);
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


}
