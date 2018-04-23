package loros.loros;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListener, AddDialog.AddDialogListener {

    private RecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<Trabalengua> mTrabalengua;
    private FloatingActionButton addButton;
    private ActionModeCallback actionModeCallback = new ActionModeCallback();
    private ActionMode actionMode;
    private int MY_STORAGE_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askForPermissions();

        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/trabalenguas.json");
        if(!f.isFile()) {
            copyAssets();
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTrabalengua = new ArrayList<>();
        // specify an adapter (see also next example)

        final ArrayList<Trabalengua> trabalenguaList = Trabalengua.getTrabalenguasFromFile("trabalenguas.json", this);

        for(int i = 0; i < trabalenguaList.size(); i++){
            Trabalengua trabalengua = trabalenguaList.get(i);
            String title = trabalengua.title.toUpperCase();
            String desc = trabalengua.description;
            mTrabalengua.add(new Trabalengua(title, desc));
        }
        mAdapter = new RecyclerViewAdapter(this, mTrabalengua);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(MainActivity.this);


        addButton = findViewById(R.id.fab_add);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


    }

    private class ActionModeCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.action_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_remove:
                    mAdapter.removeItems(mAdapter.getSelectedItems());
                    saveTrabalenguas();
                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mAdapter.clearSelection();
            actionMode = null;
        }
    }

    public void toggleSelection(int position) {
        mAdapter.toggleSelection(position);
        int count = mAdapter.getSelectedItemCount();
        if(count == 0) {
            actionMode.finish();
        }
        else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    @Override
    public void onItemClick(int position) {
        if(actionMode!= null) {
            toggleSelection(position);
        }
        else {
            Intent detailIntent = new Intent(this, ChildActivity.class);
            Trabalengua clickedItem = mTrabalengua.get(position);
            detailIntent.putExtra("Titulo", clickedItem.getTitle());
            detailIntent.putExtra("Descripcion", clickedItem.getDescription());

            startActivity(detailIntent);
        }
    }

    @Override
    public boolean onLongItemClick(int position) {
        if(actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
        return true;
    }

    public void openDialog() {
        AddDialog diag = new AddDialog();
        diag.show(getFragmentManager(), "TRABALENGUAS");
    }

    @Override
    public void onDialogPositiveClick(String title, String desc) {
        mTrabalengua.add(new Trabalengua(title, desc));
        mAdapter.notifyItemInserted(mTrabalengua.size() - 1);

        try {
            Writer output = null;
            String outDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/trabalenguas.json";
            File file = new File(outDir);
            output = new BufferedWriter(new FileWriter(file));

            String json = new Gson().toJson(mTrabalengua);
            output.write(json);
            output.close();
            Toast.makeText(getApplicationContext(), "TRABALENGUAS GUARDADO!", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    private void saveTrabalenguas() {
        try {
            Writer output = null;
            String outDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/trabalenguas.json";
            File file = new File(outDir);
            output = new BufferedWriter(new FileWriter(file));

            String json = new Gson().toJson(mTrabalengua);
            output.write(json);
            output.close();
        }
        catch (Exception e) {
            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private void copyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        for(String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);

                String outDir = Environment.getExternalStorageDirectory().getAbsolutePath();

                File outFile = new File(outDir, filename);

                boolean exists =      outFile.exists();      // Check if the file exists
                boolean isDirectory = outFile.isDirectory(); // Check if it's a directory
                boolean isFile =      outFile.isFile();

                out = new FileOutputStream(outFile);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
        }
    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    private void askForPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_STORAGE_PERMISSION);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode == MY_STORAGE_PERMISSION) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "YA EXISTEN LOS PERMISOS", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "NECESITAS ACEPTAR LOS PERMISOS", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
