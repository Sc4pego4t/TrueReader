package ru.scapegoats.truereader.modules;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import ru.scapegoats.truereader.R;
import ru.scapegoats.truereader.utils.ProgressDrawable;

public class ProgressDialog extends Dialog {
    public ProgressDialog(Context context){
        super(context,R.style.Theme_AppCompat_Light_NoActionBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.progress_dialog_layout);
        ProgressBar progressBar=findViewById(R.id.progressBar);
        progressBar.setIndeterminateDrawable(
                new ProgressDrawable(getContext().getResources()
                        .getIntArray(R.array.progress_colors)));
    }
}
