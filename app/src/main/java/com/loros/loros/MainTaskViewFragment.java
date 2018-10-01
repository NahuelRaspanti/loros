package com.loros.loros;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainTaskViewFragment extends Fragment {
    private GridLayout gridLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tasks_main_fragment, null);
        gridLayout = view.findViewById(R.id.gridTaskLayout);
        int childCount = gridLayout.getChildCount();

        for (int i= 0; i < childCount; i++) {
            CardView card = (CardView) gridLayout.getChildAt(i);
            final int pos = i;
            card.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    FragmentInitializer(pos);
                }
            });
        }


        return view;
    }

    private void FragmentInitializer(int pos) {
        TaskListFragment fragment = new TaskListFragment();
        Bundle args = new Bundle();
        switch (pos) {
            case 0:
                args.putInt("Type", TaskType.HIGIENE.getCode());
                break;
            case 1:
                args.putInt("Type", TaskType.SOCIAL.getCode());
                break;
            case 2:
                args.putInt("Type", TaskType.TRABAJO.getCode());
                break;
            case 3:
                args.putInt("Type", TaskType.HOGAR.getCode());
                break;
            default:
                Toast.makeText(getContext().getApplicationContext(), "Gil", Toast.LENGTH_SHORT).show();
        }
        fragment.setArguments(args);
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }
}
