package com.loros.loros;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

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
                    ActivityInitializer(pos);
                }
            });
        }


        return view;
    }

    private void ActivityInitializer(int pos) {
        Intent taskListActivity = new Intent(getActivity(), TaskListActivity.class);
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
        taskListActivity.putExtras(args);
        startActivity(taskListActivity);
    }
}
