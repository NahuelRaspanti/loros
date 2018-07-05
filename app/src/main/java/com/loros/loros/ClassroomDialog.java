package com.loros.loros;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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

    @Override
    public void onResume()
    {
        super.onResume();
        final AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Boolean wantToCloseDialog = false;
                    String trabTitle = title.getText().toString().trim();
                    if(trabTitle.isEmpty()) {
                        title.setError("EL NOMBRE NO PUEDE ESTAR VACÍO");
                    }
                    else {
                        wantToCloseDialog = true;
                    }
                    if(wantToCloseDialog) {
                        sendBackData();
                        d.dismiss();
                    }
                }
            });
        }
    }

    public interface NoticeDialogListener {
        void onDialogPositiveClick(String title);
    }

    public void sendBackData() {
        mListener = (NoticeDialogListener) getTargetFragment();
        mListener.onDialogPositiveClick(title.getText().toString());
    }

}
