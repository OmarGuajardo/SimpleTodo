package com.example.simpletodo;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Dialog extends AppCompatDialogFragment {

    private EditText etHandleToDo;
    private DialogListener listener;


    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {

        //Render the PopUp Dialog Box
        AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =  getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog,null);

        builder.setView(view)
                //Defining that nothing will happen if 'Cancel' is clicked
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                //When 'Save' is clicked it will call 'applyText' a method that takes
                //a newItem as a parameter and will be executed in context of MainActivity
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String newItem = etHandleToDo.getText().toString();
                        listener.applyText(newItem);

                    }
                });
        etHandleToDo = view.findViewById(R.id.etHandleToDo);
        return builder.create();

    }

    @Override
    public void onAttach(Context context) {
        //The 'context' that is being passed is making a reference to the parent
        //in this case it's MainActivity
        super.onAttach(context);
        try {
            //Using the context parameter, 'listener' is being assigned the method
            //applyText which is the  method defined in MainActivity
            listener = (DialogListener)context;
        } catch (ClassCastException e) {
           throw new ClassCastException(context.toString() + "must implement DialogListener");
        }
    }

    public interface DialogListener{
        void applyText(String newItem);
    }
}
