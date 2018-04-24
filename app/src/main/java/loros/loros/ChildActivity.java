package loros.loros;

import android.content.Intent;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Locale;

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

                textViewTitle.setText(editedTitle);
                textViewDesc.setText(editedDesc);

                mEditableTextTitle.setVisibility(View.GONE);
                textViewTitle.setVisibility(View.VISIBLE);

                mEditableTextDesc.setVisibility(View.GONE);
                textViewDesc.setVisibility(View.VISIBLE);

                ACTIVE = false;
            }
        });

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
                }
                else {
                    Log.e("TTS", "Inizialization failed");
                }
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

    @Override
    protected void onDestroy() {
        if(mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }

        super.onDestroy();
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
