package com.w2w.Other;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.w2w.BaseDatos.MyDataSource;
import com.w2w.Logica.Lista;
import com.w2w.Logica.Pelicula;
import com.w2w.R;

public class NewListDialog {

    private Pelicula pelicula = null;

    public NewListDialog() {
    }

    public NewListDialog(Pelicula pelicula) {
        this.pelicula = pelicula;
    }

    public void showDialog(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
        Context context = view.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setPadding(16, 16, 16, 16);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText nameBox = new EditText(context);
        nameBox.setHint(R.string.name);
        layout.addView(nameBox);

        final EditText descriptionBox = new EditText(context);
        descriptionBox.setHint(R.string.description);
        layout.addView(descriptionBox);

        dialog.setTitle(R.string.new_list).setView(layout)//.setIcon(R.drawable...)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (!nameBox.getText().toString().isEmpty()) {
                            Lista lista = new Lista(nameBox.getText().toString(), descriptionBox.getText().toString());
                            MyDataSource db = MyDataSource.getInstance();
                            db.guardarLista(lista);

                            if (pelicula != null) {
                                lista.addPelicula(pelicula);
                                db.addPeliculaLista(pelicula, lista);
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // No hacer nada
                    }
                });

        dialog.show();
    }
}
