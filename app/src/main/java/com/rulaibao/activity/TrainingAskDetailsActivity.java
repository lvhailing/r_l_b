package com.rulaibao.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rulaibao.R;
import com.rulaibao.adapter.RecyclerBaseAapter;
import com.rulaibao.adapter.TrainingAskDetailsListAdapter;
import com.rulaibao.adapter.TrainingAskListAdapter;
import com.rulaibao.adapter.TrainingHotAskListAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.ResultAskDetailsAnswerBean;
import com.rulaibao.bean.ResultAskDetailsAnswerItemBean;
import com.rulaibao.bean.ResultAskDetailsBean;
import com.rulaibao.bean.ResultAskIndexBean;
import com.rulaibao.bean.TestBean;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.test.TabMenu;
import com.rulaibao.uitls.RlbActivityManager;
import com.rulaibao.widget.CircularImage;
import com.rulaibao.widget.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 问题详情
 */

public class TrainingAskDetailsActivity extends BaseActivity {


    @BindView(R.id.lv_ask_details)
    RecyclerView lvAskDetails;
    @BindView(R.id.tv_ask_details_answer)
    TextView tvAskDetailsAnswer;


    private TextView tv_ask_detais_sort;        //
    private ImageView iv__ask_detais_sort;        //
    private TextView tv_askdetails_title;       //  标题
    private CircularImage iv_ask_detatils_manager;       //  头像
    private TextView tv_ask_details_manager_name;       //  姓名
    private TextView tv_ask_details_time;       //  时间
    private TextView tv_ask_details_content;       //  内容
    private TextView tv_ask_details_ask_count;       //  回答数

    private ArrayList<TestBean> arrayList = new ArrayList<TestBean>();
    private String string = "";
    private TrainingAskDetailsListAdapter adapter;
    private TabMenu tabMenu;
    private LinearLayout ll_ask_details_sort;
    private PopupWindow popupWindow;
    private String questionId = "";
    private int page = 1;
    private MouldList<ResultAskDetailsAnswerItemBean> list;
    private ResultAskDetailsBean detailsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_training_ask_details);
        initTopTitle();

        initView();

    }

    public void initData() {
        list.clear();
        request();
        requestAnswerList();
    }

    public void initView() {

        questionId = getIntent().getStringExtra("questionId");
        detailsBean = new ResultAskDetailsBean();
        list = new MouldList<ResultAskDetailsAnswerItemBean>();
        test();
        initRecyclerView();

    }

    public void initRecyclerView() {

        lvAskDetails.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TrainingAskDetailsListAdapter(this, list, questionId);
//        adapter = new TrainingClassListAdapter(getActivity(),arrayList);
        lvAskDetails.setAdapter(adapter);


        //为RecyclerView添加HeaderView和FooterView
        setHeaderView(lvAskDetails);
//        setFooterView(lvAskDetails);


        lvAskDetails.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {

                    adapter.changeMoreStatus(TrainingHotAskListAdapter.LOADING_MORE);

                    page++;
                    requestAnswerList();

                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //最后一个可见的ITEM
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();


            }
        });

    }

    //获取数据
    public void request() {


//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("questionId", questionId);


        HtmlRequest.getTrainingAskDetails(this, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if (params.result != null) {

                    detailsBean = (ResultAskDetailsBean) params.result;
//                    indexItemBeans = b.getList();
                    setView(detailsBean);
                } else {

                }

            }
        });
    }

    //回答列表
    public void requestAnswerList() {

//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("questionId", questionId);
        map.put("page", page + "");
        map.put("userId", userId);

        HtmlRequest.getTrainingAskDetailsAnswer(this, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if (params.result != null) {

                    ResultAskDetailsAnswerBean b = (ResultAskDetailsAnswerBean) params.result;

                    if (b.getList().size() == 0 && page != 1) {     //  非首次的无数据情况

                        page--;
                        adapter.changeMoreStatus(RecyclerBaseAapter.NO_LOAD_MORE);

                    } else {

                        tv_ask_details_ask_count.setText(b.getTotal() + "回答");
                        adapter.changeMoreStatus(RecyclerBaseAapter.LOADING_MORE);
                        adapter.changeMoreStatus(RecyclerBaseAapter.PULLUP_LOAD_MORE);

                        list.addAll(b.getList());
                    }

                    adapter.notifyDataSetChanged();

                } else {

                }

            }
        });
    }

    public void setView(ResultAskDetailsBean bean) {

        tv_askdetails_title.setText(bean.getAppQuestion().getTitle());
        ImageLoader.getInstance().displayImage(bean.getAppQuestion().getUserPhoto(), iv_ask_detatils_manager);
        tv_ask_details_manager_name.setText(bean.getAppQuestion().getUserName());
        tv_ask_details_time.setText(bean.getAppQuestion().getTime());
        tv_ask_details_content.setText(bean.getAppQuestion().getDescript());
//        tv_ask_details_ask_count.setText(bean.getAppQuestion().getTitle());

    }


    private void setHeaderView(RecyclerView view) {
        View header = LayoutInflater.from(this).inflate(R.layout.activity_training_ask_details_top, view, false);

        ll_ask_details_sort = (LinearLayout) header.findViewById(R.id.ll_ask_details_sort);

        tv_ask_detais_sort = (TextView) header.findViewById(R.id.tv_ask_detais_sort);
        iv__ask_detais_sort = (ImageView) header.findViewById(R.id.iv__ask_detais_sort);
        tv_askdetails_title = (TextView) header.findViewById(R.id.tv_askdetails_title);
        iv_ask_detatils_manager = (CircularImage) header.findViewById(R.id.iv_ask_detatils_manager);
        tv_ask_details_manager_name = (TextView) header.findViewById(R.id.tv_ask_details_manager_name);
        tv_ask_details_time = (TextView) header.findViewById(R.id.tv_ask_details_time);
        tv_ask_details_content = (TextView) header.findViewById(R.id.tv_ask_details_content);
        tv_ask_details_ask_count = (TextView) header.findViewById(R.id.tv_ask_details_ask_count);

        ll_ask_details_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showPopupMenu(v);
                showPopupWindow(v);
//                initTabMenu(v);
//                initDialog(v);
            }
        });


        adapter.setmHeaderView(header);
    }

//    private void setFooterView(RecyclerView view){
//        View footer = LayoutInflater.from(this).inflate(R.layout.footer, view, false);
//        adapter.setmFooterView(footer);
//    }


    public void initDialog(View view) {

        Dialog customizeDialog =
                new Dialog(this, R.style.SortDialog);
        final View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.pw_sort, null);
        customizeDialog.setContentView(dialogView);


        Window window = customizeDialog.getWindow();
        //设置窗口的位置
        //window.setGravity(Gravity.LEFT | Gravity.TOP);

        //设置窗口的属性，以便设设置
        WindowManager.LayoutParams layoutParams = window.getAttributes();

        layoutParams.x = view.getWidth() + 130;//x 位置设置
        layoutParams.y = view.getHeight() - 180;//y 位置设置

             /*  ViewGroup.LayoutParams params=new
             ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
             ViewGroup.LayoutParams.WRAP_CONTENT);*/
        //layoutParams.width = params.width; // 宽度
        //layoutParams.height = params.height; // 高度

        //layoutParams.width = 200; // 宽度
        //layoutParams.height = 200; // 高度

        layoutParams.alpha = 0.6f; // 透明度

        window.setAttributes(layoutParams);

        customizeDialog.show();

    }

    public void showPopupWindow(View view) {

        LayoutInflater inflater = LayoutInflater.from(this);
        View itemView = inflater.inflate(R.layout.pw_sort, null);
        TextView tv_sort_default = (TextView) itemView.findViewById(R.id.tv_sort_default);
        TextView tv_sort_times = (TextView) itemView.findViewById(R.id.tv_sort_times);

        tv_sort_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_ask_detais_sort.setText("默认排序");
                Toast.makeText(getApplicationContext(), "默认排序", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        });

        tv_sort_times.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_ask_detais_sort.setText("最新排序");
                Toast.makeText(getApplicationContext(), "最新排序", Toast.LENGTH_SHORT).show();
                popupWindow.dismiss();
            }
        });


        popupWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(itemView);

        //点击空白区域PopupWindow消失，这里必须先设置setBackgroundDrawable，否则点击无反应
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(true);

        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);

        //设置是否允许PopupWindow的范围超过屏幕范围
        popupWindow.setClippingEnabled(true);

        popupWindow.setAnimationStyle(R.style.Theme_PopupMenu);


        //设置PopupWindow消失监听
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                iv__ask_detais_sort.setImageResource(R.mipmap.img_sort_down);

            }
        });

        iv__ask_detais_sort.setImageResource(R.mipmap.img_sort_up);
        popupWindow.showAsDropDown(view);

    }


    private void showPopupMenu(View view) {

        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);

        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.sort_menu, popupMenu.getMenu());

        Menu menu = popupMenu.getMenu();

        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // PopupMenu关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                Toast.makeText(getApplicationContext(), "关闭PopupMenu", Toast.LENGTH_SHORT).show();
            }
        });
//        popupMenu.
        popupMenu.show();
    }

    public void test() {

        for (int i = 0; i < 10; i++) {

            arrayList.add(new TestBean("渣渣辉" + i));
        }

    }

    public void initTabMenu(View view) {


        LayoutInflater inflater = LayoutInflater.from(this);
        View itemView = inflater.inflate(R.layout.pw_sort, null);

        tabMenu = new TabMenu(this, null, R.mipmap.ic_launcher, "设置",
                10, R.color.blue, R.color.white, R.style.PopupAnimation);


        tabMenu.setContentView(itemView);

        //点击空白区域PopupWindow消失，这里必须先设置setBackgroundDrawable，否则点击无反应
        tabMenu.setBackgroundDrawable(new ColorDrawable(0x00000000));
        tabMenu.setOutsideTouchable(true);

        tabMenu.showAsDropDown(view);


    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false)
                .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.training_ask_details))
                .showMore(false).setOnActionListener(new TitleBar.OnActionListener() {
            @Override
            public void onMenu(int id) {
            }

            @Override
            public void onBack() {
                finish();
            }

            @Override
            public void onAction(int id) {

            }
        });
    }

    @OnClick(R.id.tv_ask_details_answer)
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tv_ask_details_answer:
                HashMap<String, Object> map = new HashMap<>();
                map.put("questionId", questionId);
                map.put("title", detailsBean.getAppQuestion().getTitle());
                RlbActivityManager.toTrainingAnswerActivity(this, map, false);
                break;

            default:

                break;

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
