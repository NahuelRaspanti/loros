package loros.loros;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = findViewById(R.id.trabalenguas_list_view);
// 1
        final ArrayList<Trabalengua> trabalenguaList = Trabalengua.getTrabalenguasFromFile("trabalenguas.json", this);
// 2
        String[] listItems = new String[trabalenguaList.size()];
// 3
        for(int i = 0; i < trabalenguaList.size(); i++){
            Trabalengua trabalengua = trabalenguaList.get(i);
            listItems[i] = trabalengua.title;
        }
// 4
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        mListView.setAdapter(adapter);
    }

}
