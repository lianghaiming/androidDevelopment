package com.example.androiddevelopment.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.duzun.player.R;
import com.duzun.player.progressbar.MaterialProgressBar;

/**
 * Created by xiaofei on 2015/12/16 13:33.
 */
public class DialogCreator {

    public static enum Type {
        PROGRESS, ALARM, NORMAL
    }

    public static enum Position {
        BOTTOM, CENTER, TOP
    }

    public DialogCreator() {
    }

    public static Dialog createNormalDialog(Context context, View view, Position position) {
        int style = 0;
        switch (position) {
            case BOTTOM:
                style = R.style.BottomDialog;
                break;
            case CENTER:
                style = R.style.CenterDialog;
                break;
            case TOP:
                style = R.style.TopDialog;
                break;
        }
        Dialog dialog = new Dialog(context, style);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        switch (position) {
            case BOTTOM:
                params.gravity = Gravity.BOTTOM;
                break;
            case CENTER:
                params.gravity = Gravity.CENTER;
                break;
            case TOP:
                params.gravity = Gravity.TOP;
                break;
        }
        params.width = SizeUtils.getScreenWidth(context);
        dialog.getWindow().setAttributes(params);
        return dialog;
    }

    public static Dialog createBottomDialog(Context context, View view) {
        Dialog dialog = new Dialog(context, R.style.BottomDialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);

        Window window = dialog.getWindow();

        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = context.getResources().getDisplayMetrics().widthPixels;
        window.setAttributes(params);

        return dialog;
    }

    public static Dialog createProgressDialog(Context context, String message) {
        int style = 0;
        View view = null;
        style = R.style.CenterDialog;
        view = LayoutInflater.from(context).inflate(R.layout.center_progress_dialog_layout, null, false);
        MaterialProgressBar progressBar = (MaterialProgressBar) view.findViewById(R.id.progress);
        TextView textView = (TextView) view.findViewById(R.id.message);
        textView.setText(message);

        Dialog dialog = new Dialog(context, style);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = Gravity.CENTER;

        params.width = context.getResources().getDisplayMetrics().widthPixels;
        dialog.getWindow().setAttributes(params);
        return dialog;
    }

    public static AlertDialog createDialog(Context context,String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }
}
