package com.loros.loros;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class ClassroomDialog extends DialogFragment {

    private EditText title;
    NoticeDialogListener mListener;

    public ClassroomDialog() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.classroom_dialog, null);
        builder.setView(view)
                .setTitle("CREAR NUEVA CLASE")
                .setPositiveButton("CREAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String trabTitle = title.getText().toString();
                        sendBackData();
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        title = view.findViewById(R.id.classroom_diag_title);
        return builder.create();
    }

    public interface NoticeDialogListener {
        void onDialogPositiveClick(String title);
    }

    public void sendBackData() {
        mListener = (NoticeDialogListener) getTargetFragment();
        mListener.onDialogPositiveClick(title.getText().toString());
    }

}
