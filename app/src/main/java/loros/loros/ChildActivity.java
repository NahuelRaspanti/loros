package loros.loros;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;

public class ChildActivity extends AppCompatActivity {

    private TextToSpeech mTTS;
    private MenuItem mButtonSpeak;
    String descripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        Intent intent = getIntent();

        String titulo = intent.getStringExtra("Titulo");
        descripcion = intent.getStringExtra("Descripcion");

        TextView textViewTitulo = findViewById(R.id.my_title);
        TextView textViewDesc = findViewById(R.id.my_trabalengua);

        textViewTitulo.setText(titulo);
        textViewDesc.setText(descripcion.toUpperCase());

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
}
