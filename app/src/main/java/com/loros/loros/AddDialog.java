package com.loros.loros;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddDialog extends DialogFragment {

    private EditText title;
    private EditText desc;
    NoticeDialogListener mListener;

    public AddDialog () {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_dialog, null);
        title = view.findViewById(R.id.diag_title);
        desc = view.findViewById(R.id.diag_desc);
        builder.setView(view)
                .setTitle("AGREGAR TRABALENGUAS")
                .setPositiveButton("GUARDAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
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
                    String trabTitle = title.getText().toString();
                    String trabDesc = desc.getText().toString();
                    if(trabTitle.isEmpty()) {
                        title.setError("EL TÍTULO NO PUEDE ESTAR VACÍO");
                    }
                    else if (trabDesc.isEmpty()) {
                        desc.setError("EL TRABALENGUAS NO PUEDE ESTAR VACÍO");
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
        void onDialogPositiveClick(String title, String desc);
    }

    public void sendBackData() {
        mListener = (NoticeDialogListener) getTargetFragment();
        mListener.onDialogPositiveClick(title.getText().toString(), desc.getText().toString());
    }

}
