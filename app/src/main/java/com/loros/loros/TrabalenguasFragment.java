package com.loros.loros;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class TrabalenguasFragment extends Fragment implements RecyclerViewAdapter.OnTrabalenguasClickListener, AddDialog.NoticeDialogListener {

    private RecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<Trabalengua> mTrabalengua = new ArrayList<>();
    private ActionModeCallback actionModeCallback = new ActionModeCallback();
    private ActionMode actionMode;
    private int MY_PERMISSIONS = 1;
    private boolean FRAG_LOC = false;
    private final String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public TrabalenguasFragment () {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trabalenguas_fragment, container, false);

        if (hasPermissions()) {
            fillTrabalenguasList();
        } else {
            askForPermissions();
        }

        mRecyclerView = view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // specify an adapter (see also next example)

        mAdapter = new RecyclerViewAdapter(getActivity(), mTrabalengua);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (dy > 0)
                    mainActivity.addButton.hide();
                else if (dy < 0)
                    mainActivity.addButton.show();
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
    public void onResume() {
        super.onResume();
        if (hasPermissions()) {
            mTrabalengua.clear();
            fillTrabalenguasList();
            mAdapter.notifyDataSetChanged();
        }
        if (!getUserVisibleHint())
        {
            return;
        }
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.addButton.setVisibility(View.VISIBLE);
        mainActivity.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    private void fillTrabalenguasList() {
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/trabalenguas.json");
        if (!f.isFile()) {
            copyAssets();
        }
        final ArrayList<Trabalengua> trabalenguaList = Trabalengua.getTrabalenguasFromFile("trabalenguas.json", getActivity());

        for (int i = 0; i < trabalenguaList.size(); i++) {
            Trabalengua trabalengua = trabalenguaList.get(i);
            String title = trabalengua.title.toUpperCase();
            String desc = trabalengua.description;
            mTrabalengua.add(new Trabalengua(title, desc));
        }
    }

    private boolean hasPermissions() {
        int res = 0;
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        for (String perms : permissions) {
            res = getActivity().checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }
        return true;
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
                    saveTrabalenguas(mTrabalengua);
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
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    @Override
    public void onItemClick(int position) {
        if (actionMode != null) {
            toggleSelection(position);
        } else {
            Intent detailIntent = new Intent(getActivity(), ChildActivity.class);
            Trabalengua clickedItem = mTrabalengua.get(position);
            String json = new Gson().toJson(mTrabalengua);
            detailIntent.putExtra("Online", FRAG_LOC);
            detailIntent.putExtra("List", json);
            detailIntent.putExtra("Position", position);
            detailIntent.putExtra("Titulo", clickedItem.getTitle());
            detailIntent.putExtra("Descripcion", clickedItem.getDescription());

            startActivity(detailIntent);
        }
    }

    @Override
    public boolean onLongItemClick(int position) {
        if (actionMode == null) {
            actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
        return true;
    }

    public void openDialog() {
        AddDialog diag = new AddDialog();
        diag.setTargetFragment(this, 300);
        diag.show(getFragmentManager(), "Trabalenguas");
    }

    @Override
    public void onDialogPositiveClick(String title, String desc) {
        mTrabalengua.add(new Trabalengua(title.toUpperCase(), desc.toUpperCase()));
        mAdapter.notifyItemInserted(mTrabalengua.size() - 1);

        try {
            Writer output = null;
            String outDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/trabalenguas.json";
            File file = new File(outDir);
            output = new BufferedWriter(new FileWriter(file));

            String json = new Gson().toJson(mTrabalengua);
            output.write(json);
            output.close();
            Toast.makeText(getActivity(), "TRABALENGUAS GUARDADO!", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }


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
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private void copyAssets() {
        String json = null;
        try {
            InputStream inputStream = getActivity().getAssets().open("trabalenguas.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Trabalengua> trabalenguaList = new Gson().fromJson(json, new TypeToken<ArrayList<Trabalengua>>() {
        }.getType());
        saveTrabalenguas(trabalenguaList);
    }

    private void askForPermissions() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                && (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE))) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS);
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                fillTrabalenguasList();
            } else {
                Toast.makeText(getActivity(), "NECESITAS ACEPTAR LOS PERMISOS", Toast.LENGTH_LONG).show();
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.addButton.setClickable(false);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }
}
