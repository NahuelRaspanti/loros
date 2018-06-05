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

public class SearchUserDialog extends DialogFragment {

    private EditText email;
    NoticeDialogListener mListener;

    public SearchUserDialog() {}

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.member_dialog, null);
        email = view.findViewById(R.id.member_dialog_email);
        builder.setView(view)
                .setTitle("AÑADIR ESTUDIANTE POR CORREO ELECTRÓNICO")
                .setPositiveButton("AÑADIR", new DialogInterface.OnClickListener() {
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
                    String inputEmail = email.getText().toString();
                    if(inputEmail.isEmpty()) {
                        email.setError("INGRESÁ UN EMAIL");
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
        mListener.onDialogPositiveClick(email.getText().toString());
    }

}
