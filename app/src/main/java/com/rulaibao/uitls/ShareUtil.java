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
import cn.sharesdk.wechat.favorite.WechatFavorite;
import onekeyshare.OnekeyShare;
import onekeyshare.ShareContentCustomizeCallback;

/**
 *  分享时调用的方法
 */
public final class ShareUtil {
    public static void sharedSDK(final Context context, final String title, final String text, final String url) {
        final OnekeyShare oks = new OnekeyShare();

        oks.disableSSOWhenAuthorize();// 关闭sso授权
        oks.addHiddenPlatform(WechatFavorite.NAME); // 隐藏微信收藏；

        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, cn.sharesdk.framework.Platform.ShareParams paramsToShare) {

                //点击微信好友
                if ("Wechat".equals(platform.getName())) {
                    paramsToShare.setText(text+url);
                    paramsToShare.setTitle(title);
                    paramsToShare.setTitleUrl(url);
                    paramsToShare.setUrl(url);
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                    paramsToShare.setImagePath(Environment.getExternalStorageDirectory() + "/haidehui/imgs/haidehui.png");
                    //测试图片地址（网络获取）
//                    paramsToShare.setImageUrl("https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=3208696326,3417130916&fm=173&s=2FE67A221AB13BAB5634185B0100C060&w=343&h=345&img.JPG");
                }

                //点击微信朋友圈
                if ("WechatMoments".equals(platform.getName())) {
                    paramsToShare.setText(text+url);
                    paramsToShare.setTitleUrl(url);
                    paramsToShare.setTitle(title);
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                    paramsToShare.setImagePath(Environment.getExternalStorageDirectory() + "/haidehui/imgs/haidehui.png");
                    paramsToShare.setUrl(url);
                }

                //点击QQ空间
                if ("QZone".equals(platform.getName())){
                    paramsToShare.setTitle( title);
                    paramsToShare.setText(text);
                    paramsToShare.setTitleUrl(url);
                    paramsToShare.setUrl(url);
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                    paramsToShare.setImagePath(Environment.getExternalStorageDirectory() + "/haidehui/imgs/haidehui.png");
                }

                //点击QQ
                if ("QQ".equals(platform.getName())) {
                    paramsToShare.setText(text);
                    paramsToShare.setTitle( title);
                    paramsToShare.setImagePath(Environment.getExternalStorageDirectory() + "/haidehui/imgs/haidehui.png");
                    paramsToShare.setTitleUrl(url);
                    paramsToShare.setUrl(url);
                    paramsToShare.setSite(context.getString(R.string.app_name));
                }
                //点击信息
                if ("ShortMessage".equals(platform.getName())) {
                    paramsToShare.setText(text+url);
                    paramsToShare.setTitleUrl(url);
                    paramsToShare.setUrl(url);
                }
            }
//                if (string.contains("WechatMoments")) {
//                    way = "weixin";            //微信朋友圈
//                    oks.setText(text);
//                    oks.setTitleUrl(url);
//                    oks.setUrl(url);
//                    oks.setTitle(title);
//                    oks.setImagePath(Environment.getExternalStorageDirectory() + "/haidehui/imgs/haidehui.png");
//                } else if (string.contains("Wechat")) {
//                    way = "weixinFr";        //微信好友
//                    oks.setText(text);
//                    oks.setTitle(title);
//                    oks.setTitleUrl(url);
//                    oks.setUrl(url);
////					oks.setImagePath("/sdcard/vjinke/imgs/test.jpg");
//                    oks.setImagePath(Environment.getExternalStorageDirectory() + "/haidehui/imgs/haidehui.png");
//                } else if (string.contains("QZone")) {
//                    way = "Qzone";
//                    oks.setText(text);
//                    oks.setTitle(title);
//                    oks.setTitleUrl(url);
//                    oks.setUrl(url);
//                } else if (string.contains("SinaWeibo")) {
//                    way = "sinablog";
//                    oks.setText(text);
////					oks.setTitleUrl(url);
//                    oks.setUrl(url);
//
//                    oks.setSilent(false);
//                } else if (string.contains("TencentWeibo")) {
//                    way = "tencentblog";
//                    oks.setText(text);
//                    oks.setTitleUrl(url);
//                    oks.setUrl(url);
//                } else if (string.contains("QQ")) {
//                    way = "QQ";
//                    oks.setText(text);
//                    oks.setTitle(title);
//                    oks.setImagePath(Environment.getExternalStorageDirectory() + "/haidehui/imgs/haidehui.png");
//					oks.setTitleUrl(url);
                    // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                    // oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
                    // url仅在微信（包括好友和朋友圈）中使用
//                    oks.setUrl(url);
                    // comment是我对这条分享的评论，仅在人人网和QQ空间使用
                    // oks.setComment("我是测试评论文本");
                    // site是分享此内容的网站名称，仅在QQ空间使用
//                    oks.setSite(context.getString(R.string.app_name));
                    // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//                    oks.setSiteUrl(url);
//                } else if (string.contains("Email")) {
//                    way = "email";
//                    oks.setText(text);
//                    oks.setTitleUrl(url);
//                    oks.setUrl(url);
//                } else if (string.contains("ShortMessage")) {
//                    way = "sms";
//                    oks.setText(text+url);
//                    oks.setTitleUrl(url);
//                    oks.setUrl(url);
//                }
//            }

        });
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(context.getString(R.string.share));

        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        // oks.setTitleUrl("http://www.vjinke.com");

        // 自定义分享按钮
        Bitmap enableLogo = BitmapFactory.decodeResource(context.getResources(), R.drawable.ssdk_logo_lianjie);
        String label = "复制链接";
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                StringBuffer randomNum = new StringBuffer();
                for (int i = 0; i < 6; i++) {
                    int t = (int) (Math.random() * 10);
                    randomNum.append(t);
                }
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(url);
                Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
            }
        };
        oks.setCustomerLogo(enableLogo, label, listener);

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
