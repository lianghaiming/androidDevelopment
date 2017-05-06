package com.example.androiddevelopment.callback;

import android.widget.TextView;

/**
 * Created by Administrator on 2017/5/6.
 */
public interface IMoodView {

    void getMood(String data);

    TextView getShowView();

}
