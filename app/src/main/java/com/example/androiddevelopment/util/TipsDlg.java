package com.example.androiddevelopment.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiaoke.live555.R;

/**
 * Created by Administrator on 2017/4/17.
 */
public class TipsDlg extends Dialog {
    private static TipsDlg tipsDlg;
    private Handler mHandle ;
    private static final int DISMISS_DLG =100;
    private TextView showText;


    class MyHandle extends Handler{
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch(msg.what){
                case DISMISS_DLG:
                    if(tipsDlg.isShowing())
                        tipsDlg.dismiss();
                    break;

            }
        }
    }
    private TipsDlg(Context context) {
        super(context, R.style.style_my_dialog);
        setContentView(R.layout.dialog_alarm1);
        showText = (TextView) findViewById(R.id.tv_dialog);
        mHandle =new MyHandle();
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = (int) (Utils.getDisplayHeight(context) * 0.5);
        lp.height = (int) (Utils.getDisplayHeight(context) * 0.5);   //高度设置为屏幕的0.3
        lp.width = (Utils.getDisplayWidth(context));    //宽度设置为屏幕的0.5
        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);

        getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
    }


    public static TipsDlg get(Context context) {
        if (tipsDlg == null) {
            tipsDlg = new TipsDlg(context);
        }
        return tipsDlg;
    }
    public void dlgDismiss(){
        if(isShowing())
            dismiss();
    }

    public void showTips(String msg) {
        if(showText != null) {
            showText.setText(msg);
        }
        super.show();
    }
    public void showTips(int resId) {
        showTips(getContext().getString(resId));
    }

}
