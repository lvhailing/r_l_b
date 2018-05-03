package com.rulaibao.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rulaibao.R;
import com.rulaibao.activity.MyAskActivity;
import com.rulaibao.activity.MyCollectionActivity;
import com.rulaibao.activity.MyInfoActivity;
import com.rulaibao.activity.MyPartakeActivity;
import com.rulaibao.activity.MyTopicActivity;
import com.rulaibao.activity.NewsActivity;
import com.rulaibao.activity.PolicyBookingListActivity;
import com.rulaibao.activity.PolicyRecordListActivity;
import com.rulaibao.activity.RenewalReminderActivity;
import com.rulaibao.activity.SettingActivity;
import com.rulaibao.activity.TransactionRecordActivity;
import com.rulaibao.bean.MineData2B;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.uitls.ImageUtils;
import com.rulaibao.uitls.PreferenceUtil;
import com.rulaibao.uitls.encrypt.DESUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * “我的”模块
 * Created by junde on 2018/3/26.
 */

public class MineFragment extends Fragment implements View.OnClickListener {

    private View mView;
    private ImageView iv_settings; // icon_mine_setting
    private ImageView iv_news; // icon_mine_news
    private ImageView iv_user_photo; // 用户头像
    private ImageView iv_status; // 认证状态
    private ImageView iv_eye; //

    private RelativeLayout rl_total_commission; // 累计佣金布局
    private RelativeLayout rl_my_policy; // 我的保单
    private RelativeLayout rl_renewal_reminder; // 续保提醒
    private RelativeLayout rl_my_bookings; // 我的预约
    private RelativeLayout rl_my_ask; // 我的提问
    private RelativeLayout rl_my_topic; // 我的话题
    private RelativeLayout rl_my_participation; // 我的参与
    private RelativeLayout rl_my_collection; // 我的收藏

    private TextView tv_message_total; // 消息总数
    private TextView tv_user_name; // 用户姓名
    private TextView tv_user_phone; // 用户手机
    private TextView tv_mine_login; // 登录
    private TextView tv_total_commission; // 累计佣金
    private TextView tv_check_pending; // 待审核
    private TextView tv_underwriting; // 已承保
    private TextView tv_problem_parts; // 问题件
    private TextView tv_return_receipt; // 回执签收
    private TextView tv_renewal_numbers; // 续保提醒 数量
    private Intent intent;
    private Context context;
    private String userId = "18032709463185347077"; // 测试userId
    private MineData2B data;
    private int messageTotal;

    /**
     * 图片保存SD卡位置
     */
    private final static String IMG_PATH = Environment.getExternalStorageDirectory() + "/rlb/imgs/";
    private int insuranceWarning;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_mine, container, false);
            try {
                initView(mView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (mView.getParent() != null) {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
        }

        return mView;
    }

    private void initView(View mView) {
        context = getActivity();

        try {
//            checkStatus = DESUtil.decrypt(PreferenceUtil.getCheckStatus());
            userId = DESUtil.decrypt(PreferenceUtil.getUserId());

        } catch (Exception e) {
            e.printStackTrace();
        }

        iv_settings = (ImageView) mView.findViewById(R.id.iv_settings);
        iv_news = (ImageView) mView.findViewById(R.id.iv_news);
        iv_user_photo = (ImageView) mView.findViewById(R.id.iv_user_photo);
        iv_status = (ImageView) mView.findViewById(R.id.iv_status);
        iv_eye = (ImageView) mView.findViewById(R.id.iv_eye);

        rl_total_commission = (RelativeLayout) mView.findViewById(R.id.rl_total_commission);
        rl_my_policy = (RelativeLayout) mView.findViewById(R.id.rl_my_policy);
        rl_renewal_reminder = (RelativeLayout) mView.findViewById(R.id.rl_renewal_reminder);
        rl_my_bookings = (RelativeLayout) mView.findViewById(R.id.rl_my_bookings);
        rl_my_ask = (RelativeLayout) mView.findViewById(R.id.rl_my_ask);
        rl_my_topic = (RelativeLayout) mView.findViewById(R.id.rl_my_topic);
        rl_my_participation = (RelativeLayout) mView.findViewById(R.id.rl_my_participation);
        rl_my_collection = (RelativeLayout) mView.findViewById(R.id.rl_my_collection);

        tv_message_total = (TextView) mView.findViewById(R.id.tv_message_total);
        tv_user_name = (TextView) mView.findViewById(R.id.tv_user_name);
        tv_user_phone = (TextView) mView.findViewById(R.id.tv_user_phone);
        tv_mine_login = (TextView) mView.findViewById(R.id.tv_mine_login);
        tv_total_commission = (TextView) mView.findViewById(R.id.tv_total_commission);
        tv_renewal_numbers = (TextView) mView.findViewById(R.id.tv_renewal_numbers);
        tv_check_pending = (TextView) mView.findViewById(R.id.tv_check_pending);
        tv_underwriting = (TextView) mView.findViewById(R.id.tv_underwriting);
        tv_problem_parts = (TextView) mView.findViewById(R.id.tv_problem_parts);
        tv_return_receipt = (TextView) mView.findViewById(R.id.tv_return_receipt);


        iv_settings.setOnClickListener(this); // icon_mine_setting
        iv_news.setOnClickListener(this); // icon_mine_news

        tv_mine_login.setOnClickListener(this); // 登录
        iv_user_photo.setOnClickListener(this);  // 用户头像
        rl_total_commission.setOnClickListener(this);  // 累计佣金
        rl_my_policy.setOnClickListener(this); // 我的保单

        tv_check_pending.setOnClickListener(this); // 待审核
        tv_underwriting.setOnClickListener(this); // 已承保
        tv_problem_parts.setOnClickListener(this); // 问题件
        tv_return_receipt.setOnClickListener(this); // 回执签收

        rl_renewal_reminder.setOnClickListener(this); // 续保提醒
        rl_my_bookings.setOnClickListener(this); // 我的预约
        rl_my_ask.setOnClickListener(this); // 我的提问
        rl_my_topic.setOnClickListener(this); // 我的话题
        rl_my_participation.setOnClickListener(this); // 我的参与
        rl_my_collection.setOnClickListener(this); // 我的收藏

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (context != null) {
                requestData();
            }
        }
    }

    @Override
    public void onResume() {
        requestData();
        super.onResume();
//        Log.i("hh", "我的---Fragment----onResume");
    }

    //我的主页面数据
    private void requestData() {

        HashMap<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        HtmlRequest.getMineData(context, param, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {
                if (params.result == null) {
                    Toast.makeText(context, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                    return;
                }
                data = (MineData2B) params.result;
                if (data != null) {
                    setData(data);
                }
            }
        });
    }

    private void setData(MineData2B data) {
        // 获取消息总数
        if (data.getMessageTotal() != null) {
            messageTotal = Integer.parseInt(data.getMessageTotal());
        }
        if (messageTotal > 9) {
            tv_message_total.setVisibility(View.VISIBLE);
            tv_message_total.setText("9+");
            tv_message_total.setTextSize(10f);
        } else {
            if (messageTotal == 0) {
                tv_message_total.setVisibility(View.GONE);
            } else {
                tv_message_total.setVisibility(View.VISIBLE);
                tv_message_total.setText(data.getMessageTotal());
            }
        }
        // 获取续保提醒总数
        if (data.getInsuranceWarning() != null) {
            insuranceWarning = Integer.parseInt(data.getInsuranceWarning());
        }
        if (insuranceWarning > 9) {
            tv_renewal_numbers.setVisibility(View.VISIBLE);
            tv_renewal_numbers.setText("9+");
            tv_renewal_numbers.setTextSize(10f);
        } else {
            if (insuranceWarning == 0) {
                tv_renewal_numbers.setVisibility(View.GONE);
            } else {
                tv_renewal_numbers.setVisibility(View.VISIBLE);
                tv_renewal_numbers.setText(data.getInsuranceWarning());
            }
        }

        // 获取用户姓名、手机号、佣金总额
        tv_user_name.setText(data.getRealName());
        tv_user_phone.setText(data.getMobile());
        tv_total_commission.setText(data.getTotalCommission()+"元");

        String url = data.getHeadPhoto();
        if (!TextUtils.isEmpty(url)) {
            new ImageViewService().execute(url);
        } else {
            iv_user_photo.setImageDrawable(getResources().getDrawable(R.mipmap.icon_user_photo));
        }

        // 判断用户是否认证
        String status = data.getCheckStatus();
        if ("success".equals(status)) {
            iv_status.setImageResource(R.mipmap.icon_certified);
        } else {
            iv_status.setImageResource(R.mipmap.icon_uncertified);
        }
    }


    /**
     * 获取网落图片资源
     *
     * @return
     */
    class ImageViewService extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap data = getImageBitmap(params[0]);
            return data;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (result != null) {
                iv_user_photo.setImageBitmap(result);
                saveBitmap(result);
            } else {
                iv_user_photo.setImageDrawable(getResources().getDrawable(R.mipmap.icon_user_photo));
            }
        }

    }

    private Bitmap getImageBitmap(String url) {
        URL imgUrl = null;
        Bitmap bitmap = null;
        try {
            imgUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imgUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            bitmap = ImageUtils.toRoundBitmap(bitmap); // 把图片处理成圆
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private Uri saveBitmap(Bitmap bm) {
        File tmpDir = new File(IMG_PATH);
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        File img = new File(IMG_PATH + "Test.png");
        try {
            FileOutputStream fos = new FileOutputStream(img);
            bm.compress(Bitmap.CompressFormat.PNG, 70, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_settings:  // 设置
                intent = new Intent(context, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_news:  // 消息
                intent = new Intent(context, NewsActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_user_photo: // 用户头像（跳转到个人信息页）
                intent = new Intent(context, MyInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_total_commission: // 累计佣金
                intent = new Intent(context,TransactionRecordActivity.class);
                intent.putExtra("totalCommission", data.getTotalCommission());
                startActivity(intent);
                break;
            case R.id.tv_mine_login: // 登录
                break;
            case R.id.rl_my_policy: // 我的保单
                intent = new Intent(context,PolicyRecordListActivity.class);
                intent.putExtra("flag", "0");
                intent.putExtra("position",0);
                startActivity(intent);

                break;
            case R.id.tv_check_pending: // 待审核
                intent = new Intent(context,PolicyRecordListActivity.class);
                intent.putExtra("flag", "1000");
                intent.putExtra("position",1);
                startActivity(intent);

                break;
            case R.id.tv_underwriting: // 已承保
                intent = new Intent(context,PolicyRecordListActivity.class);
                intent.putExtra("flag", "1000");
                intent.putExtra("position",2);
                startActivity(intent);
                break;
            case R.id.tv_problem_parts: // 问题件
                intent = new Intent(context,PolicyRecordListActivity.class);
                intent.putExtra("flag", "1000");
                intent.putExtra("position",3);
                startActivity(intent);
                break;
            case R.id.tv_return_receipt: // 回执签收
                intent = new Intent(context,PolicyRecordListActivity.class);
                intent.putExtra("flag", "1000");
                intent.putExtra("position",4);
                startActivity(intent);
                break;
            case R.id.rl_renewal_reminder: // 续保提醒
                intent = new Intent(context,RenewalReminderActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_my_bookings: // 我的预约
                intent = new Intent(context,PolicyBookingListActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_my_ask: // 我的提问
                intent = new Intent(context,MyAskActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_my_topic: // 我的话题
                intent = new Intent(context,MyTopicActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_my_participation: // 我的参与
                intent = new Intent(context,MyPartakeActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_my_collection: // 我的收藏
                intent = new Intent(context,MyCollectionActivity.class);
                startActivity(intent);

                break;
        }   }
}
