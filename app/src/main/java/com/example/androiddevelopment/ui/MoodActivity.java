package com.example.androiddevelopment.ui;

import android.widget.TextView;

import com.example.androiddevelopment.R;
import com.example.androiddevelopment.app.MyApp;
import com.example.androiddevelopment.callback.IMoodView;
import com.example.androiddevelopment.greendao.UserDao;
import com.example.androiddevelopment.greendao.bean.User;
import com.example.androiddevelopment.presenter.MoodPresenter;
import com.example.androiddevelopment.ui.base.BaseActivity;
import com.example.androiddevelopment.util.LogUtil;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/5/6.
 */
public class MoodActivity extends BaseActivity<IMoodView, MoodPresenter> implements IMoodView{


    @Bind(R.id.data)
    TextView show;




    @Override
    protected MoodPresenter createPresenter() {
        return new MoodPresenter(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_mood;
    }


    @Override
    public void initData() {
        mPresenter.getData();
        UserDao userDao = MyApp.getGreenDaoHelper().getSession().getUserDao();
        User user = new User("hai", 11);
        long ret = userDao.insert(user);
        LogUtil.print("insert ret " + ret);

        User user1 = new User("hai", 11);
        long ret1 = userDao.insert(user1);
        LogUtil.print("insert ret " + ret1);

        List<User> list = userDao.queryBuilder().list();
        for(User u : list) {
            LogUtil.print(u.toString());

        }

    }

    @Override
    public void getMood(String data) {

        LogUtil.print("activity callback " + data);
    }

    @Override
    public TextView getShowView() {
        return show;
    }
}
