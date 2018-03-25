package loros.loros;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<Trabalengua> mTrabalengua;


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
            mTrabalengua.add(new Trabalengua(title));
        }
        mAdapter = new RecyclerViewAdapter(this, mTrabalengua);
        mRecyclerView.setAdapter(mAdapter);
    }

}
