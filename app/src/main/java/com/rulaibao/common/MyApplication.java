package com.rulaibao.common;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.rulaibao.R;
import com.rulaibao.network.http.APNManager;
import com.rulaibao.photo_preview.fresco.ImageLoader;
import com.rulaibao.uitls.AuthImageDownloader;
import com.rulaibao.uitls.CityDataHelper;
import com.rulaibao.uitls.ImageLoaderManager;
import com.rulaibao.uitls.NetworkUtils;
import com.rulaibao.uitls.PreferenceUtil;
import com.rulaibao.uitls.SystemInfo;
import com.mob.MobSDK;

import java.io.InputStream;
import java.util.HashSet;

public class MyApplication extends Application {
    private static MyApplication instance;
    public static String mAppId;
    public static String mDownloadPath;
    private static final String TAG = "Init";
    private CityDataHelper dataHelper;
    private BroadcastReceiver mReceiver;
    public String netType;
    IntentFilter mFilter;
    HashSet<NetListener> mListeners = new HashSet<NetListener>();


    public static MyApplication getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        NetworkUtils.setContext(this);
        PreferenceUtil.initialize(this);
        SystemInfo.initialize(this);
        initNetReceiver();

        //imageLoader初始化
        initImageLoader();
        ImageLoaderManager.initImageLoader(this);
        mAppId = getString(R.string.app_id);
        mDownloadPath = "/" + mAppId + "/download";

        //fresco初始化
        ImageLoader.getInstance().initImageLoader(getResources(), 1);
        APNManager.getInstance().checkNetworkType(this);


        //拷贝数据库文件
        dataHelper = CityDataHelper.getInstance(this);
        InputStream in = this.getResources().openRawResource(R.raw.province);
        dataHelper.copyFile(in, CityDataHelper.DATABASE_NAME, CityDataHelper.DATABASES_DIR);

        //ShareSDK 初始化
//        ShareSDK.initSDK(instance);

        //3.X版本以上含3.X版本，ShareSDK 初始化
        // 通过代码注册你的AppKey和AppSecret
        MobSDK.init(instance, "1ea86a798f5d6", "69d1ab82675b878c6061887a6ab49279");
    }

    /****
     * 初始化imageloader
     */
    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCache(new WeakMemoryCache())
                .writeDebugLogs() // Remove for release app
                .imageDownloader(new AuthImageDownloader(this))
                .build();
        // Initialize ImageLoader with configuration.
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);

    }


    public interface NetListener {
        void onNetWorkChange(String netType);
    }

    /**
     * 加入网络监听
     *
     * @param l
     * @return
     */
    public boolean addNetListener(NetListener l) {
        boolean rst = false;
        if (l != null && mListeners != null) {
            rst = mListeners.add(l);
        }
        return rst;
    }

    /**
     * 移除网络监听
     *
     * @param l
     * @return
     */
    public boolean removeNetListener(NetListener l) {
        return mListeners.remove(l);
    }

    /**
     * 初始化网络监听器
     */
    private void initNetReceiver() {
        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo info = manager.getActiveNetworkInfo();
                    if (info != null && info.isConnected()) {
                        netType = info.getTypeName();
                    } else {
                        netType = "";
                    }
                    for (NetListener lis : mListeners) {
                        if (lis != null) {
                            lis.onNetWorkChange(netType);
                        }
                    }
                }
            }
        };
        mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
    }

    /**
     * 注册网络监听器
     */
    public void registReceiver() {
        try {
            registerReceiver(mReceiver, mFilter);
        } catch (Exception e) {
        }
    }

    /**
     * 注销网络监听器
     */
    public void unRegisterNetListener() {
        if (mListeners != null) {
            mListeners.clear();
        }
        try {
            unregisterReceiver(mReceiver);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
