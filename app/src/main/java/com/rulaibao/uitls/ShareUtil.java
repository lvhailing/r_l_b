/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

package com.rulaibao.uitls;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.rulaibao.R;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.system.text.ShortMessage;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 *  分享时调用的方法
 */
public final class ShareUtil {
    public static void sharedSDK(final Context context,final int position, final String title, final String text, final String url) {

        final OnekeyShare oks = new OnekeyShare();
        oks.disableSSOWhenAuthorize();// 关闭sso授权
        if (position==0){
            oks.setText(text+url);
            oks.setTitleUrl(url);
            oks.setTitle(title);
            oks.setImagePath(Environment.getExternalStorageDirectory() + "/rulaibao/imgs/rulaibao.png");
            oks.setUrl(url);
            oks.setPlatform(Wechat.NAME);
        }else if(position==1){
            oks.setText(text+url);
            oks.setTitle(title);
            oks.setTitleUrl(url);
            oks.setUrl(url);
            oks.setImagePath(Environment.getExternalStorageDirectory() + "/rulaibao/imgs/rulaibao.png");
            oks.setPlatform(WechatMoments.NAME);
        }
        else if(position==2){
            oks.setText(text+url);
            oks.setTitle( title);
            oks.setImagePath(Environment.getExternalStorageDirectory() + "/rulaibao/imgs/rulaibao.png");
            oks.setTitleUrl(url);
            oks.setUrl(url);
            oks.setSite(context.getString(R.string.app_name));
            oks.setPlatform(QQ.NAME);
        }
        else if(position==3){
            oks.setTitle( title);
            oks.setText(text+url);
            oks.setTitleUrl(url);
            oks.setUrl(url);
            oks.setImagePath(Environment.getExternalStorageDirectory() + "/rulaibao/imgs/rulaibao.png");
            oks.setPlatform(QZone.NAME);
        }
        else if(position==4){
            oks.setText(text+url);
            oks.setTitleUrl(url);
            oks.setUrl(url);
            oks.setPlatform(ShortMessage.NAME);
        }
        else if(position==5){
            StringBuffer randomNum = new StringBuffer();
            for (int i = 0; i < 6; i++) {
                int t = (int) (Math.random() * 10);
                randomNum.append(t);
            }
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(url);
            Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
            return;
        }
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

                if (platform.getName().equals("WechatMoments")) {
                    Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();

                } else if (platform.getName().equals("Wechat")) {
//                    Toast.makeText(context, "weixin share", Toast.LENGTH_SHORT).show();
                    Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
                } else if (platform.getName().equals("QZone")) {

                } else if (platform.getName().equals("SinaWeibo")) {


                } else if (platform.getName().equals("ShortMessage")) {

                } else if (platform.getName().equals("QQ")) {

                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });

        // 启动分享GUI
        oks.show(context);
    }
}
