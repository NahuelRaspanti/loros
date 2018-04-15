package loros.loros;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class AddDialog extends AppCompatDialogFragment {

    private EditText title;
    private EditText desc;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_dialog, null);
        builder.setView(view)
                .setTitle("TRABALENGUAS")
                .setPositiveButton("GUARDAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String trabTitle = title.getText().toString();
                        String trabDesc = desc.getText().toString();

                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        title = view.findViewById(R.id.diag_title);
        desc = view.findViewById(R.id.diag_desc);

        return builder.create();
    }
}
