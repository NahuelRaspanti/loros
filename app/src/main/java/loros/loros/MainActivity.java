package loros.loros;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListener {

    private RecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<Trabalengua> mTrabalengua;
    private FloatingActionButton addButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            String label = trabalengua.label.toUpperCase();
            mTrabalengua.add(new Trabalengua(title, desc, label));
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

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, ChildActivity.class);
        Trabalengua clickedItem = mTrabalengua.get(position);
        detailIntent.putExtra("Titulo", clickedItem.getTitle());
        detailIntent.putExtra("Descripcion", clickedItem.getDescription());

        startActivity(detailIntent);
    }

    public void openDialog() {
        AddDialog diag = new AddDialog();
        diag.show(getSupportFragmentManager(), "TRABALENGUAS");
    }




}
