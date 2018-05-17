package com.rulaibao.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.lzy.imagepicker.view.SystemBarTintManager;
import com.rulaibao.R;
import com.rulaibao.common.MyApplication;
import com.rulaibao.uitls.encrypt.DESUtil;
import com.rulaibao.uitls.OSUtils;
import com.rulaibao.uitls.PreferenceUtil;
import com.rulaibao.uitls.StatusBarUtil;
import com.rulaibao.widget.CustomProgressDialog;

import butterknife.ButterKnife;

import static com.rulaibao.uitls.StatusBarUtil.ANDROID_M;
import static com.rulaibao.uitls.StatusBarUtil.FLYME;
import static com.rulaibao.uitls.StatusBarUtil.MIUI;


/**
 * BaseActivity
 */

public class BaseActivity extends FragmentActivity implements MyApplication.NetListener {
    public BaseActivity mContext;   //Activity 上下文
    public String userId = null;
    public String token = null;
    public String phone = null;
    public CustomProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* //设置状态栏为透明
        initSystemBarTint();
        //设置状态栏为字体黑色
        OSUtils.ROM_TYPE romType =  OSUtils.getRomType();
        if (romType== OSUtils.ROM_TYPE.MIUI){
            StatusBarUtil.statusBarDarkMode(this,MIUI);
        }else if (romType== OSUtils.ROM_TYPE.FLYME){
            StatusBarUtil.statusBarDarkMode(this,FLYME);
        }else if (romType== OSUtils.ROM_TYPE.ANDROID_M){
            StatusBarUtil.statusBarDarkMode(this,ANDROID_M);
        }*/
        setContentView(R.layout.base);

        MyApplication apl = (MyApplication) getApplicationContext();

        mContext = this;


        try {
            userId = DESUtil.decrypt(PreferenceUtil.getUserId());
//            token = DESUtil.decrypt(PreferenceUtil.getToken());
            phone = DESUtil.decrypt(PreferenceUtil.getPhone());
        } catch (Exception e) {
            e.printStackTrace();
        }
        apl.registReceiver();
    }

    public void baseSetContentView(int layoutResId) {
        LinearLayout llContent = (LinearLayout) findViewById(R.id.content);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialog = new CustomProgressDialog(this, "", R.drawable.frame_loading);
        View v = inflater.inflate(layoutResId, null);
        llContent.addView(v);
        ButterKnife.bind(mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication apl = (MyApplication) getApplicationContext();
        apl.addNetListener(this);
        onNetWorkChange(apl.netType);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication apl = (MyApplication) getApplication();
        apl.removeNetListener(this);
    }

    @Override
    public void onNetWorkChange(String netType) {
        View netHint = findViewById(R.id.netfail_hint);
        netHint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
            }
        });
        if (netHint != null) {
            boolean netFail = TextUtils.isEmpty(netType);
            netHint.setVisibility(netFail ? View.VISIBLE : View.GONE);
        }
    }

    public void startLoading() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    public void stopLoading() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog = null;
        }
        MyApplication apl = (MyApplication) getApplicationContext();
        apl.unRegisterNetListener();
    }

    /** 子类可以重写改变状态栏颜色 GApp*/
    protected int setStatusBarColor() {
        return getColorPrimary();
    }

    /** 子类可以重写决定是否使用透明状态栏 */
    protected boolean translucentStatusBar() {
        return false;
    }

    /** 设置状态栏颜色 */
    protected void initSystemBarTint() {
        Window window = getWindow();
        if (translucentStatusBar()) {
            // 设置状态栏全透明
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            return;
        }
        // 沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0以上使用原生方法
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(setStatusBarColor());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4-5.0使用三方工具类，有些4.4的手机有问题，这里为演示方便，不使用沉浸式
    //        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(setStatusBarColor());
        }
    }

    /** 获取主题色 */
    public int getColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    /** 获取深主题色 */
    public int getDarkColorPrimary() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.data;
    }

}
