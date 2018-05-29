package com.rulaibao.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.rulaibao.R;
import com.rulaibao.adapter.TagAdapter;
import com.rulaibao.adapter.TrainingToAskListAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.ResultAskIndexBean;
import com.rulaibao.bean.ResultAskTypeItemBean;
import com.rulaibao.bean.ResultInfoBean;
import com.rulaibao.bean.TestBean;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.uitls.FlowLayout;
import com.rulaibao.uitls.FullyLinearLayoutManager;
import com.rulaibao.uitls.TagFlowLayout;
import com.rulaibao.widget.MyGridView;
import com.rulaibao.widget.TitleBar;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我要提问
 */

public class TrainingToAskActivity extends BaseActivity {

//    @BindView(R.id.gv_training_toask)
//    GridView gvTrainingToask;
    @BindView(R.id.et_toask_title)
    EditText etToaskTitle;
    @BindView(R.id.et_toask_content)
    EditText etToaskContent;
    @BindView(R.id.btn_training_toask)
    Button btnTrainingToask;
    @BindView(R.id.gv_training_toask)
    TagFlowLayout gv_training_toask;
    private TagAdapter<String> tagAdapter;

    private String string = "";
    private TrainingToAskListAdapter adapter;
    private String questionTitle = "";
    private String questionDesc = "";
    private int position = 0;

    private ArrayList<ResultAskTypeItemBean> typeList;
    private ArrayList<String> nameList=new ArrayList<>();
    private ArrayList<String> codeList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_training_toask);
        initTopTitle();
        initView();

    }

    public void initView() {

        typeList = (ArrayList<ResultAskTypeItemBean>) getIntent().getSerializableExtra("type");
        for(int i=0;i<typeList.size();i++){
            nameList.add(typeList.get(i).getTypeName());
            codeList.add(typeList.get(i).getTypeCode());
        }
        flowLayoutAsk();
        /*if (typeList.size() > 0) {
            typeList.get(0).setFlag(true);
        }*/

       /* adapter = new TrainingToAskListAdapter(gvTrainingToask, this, typeList);

//        FullyLinearLayoutManager mLayoutManager = new FullyLinearLayoutManager(this);
//        gvTrainingToask.setLa.setLayoutManager(mLayoutManager);


        gvTrainingToask.setAdapter(adapter);
        setListViewHeightBasedOnChildren(gvTrainingToask);
        gvTrainingToask.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(TrainingToAskActivity.this, "---" + position, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        gvTrainingToask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setPosition(position);
                notifyData(position);
                adapter.notifyDataSetChanged();

//                Toast.makeText(TrainingToAskActivity.this,"-setOnItemClickListener--"+position,Toast.LENGTH_SHORT).show();


            }
        });
*/
    }
    //
    private void flowLayoutAsk() {
        gv_training_toask.setAdapter(tagAdapter=new TagAdapter<String>(nameList){
            @Override
            public View getView(FlowLayout parent, int position, String s){
                TextView tv = (TextView) LayoutInflater.from(TrainingToAskActivity.this).inflate(R.layout.ask_flow_item_tv,
                        gv_training_toask, false);
                tv.setText(s);
                return tv;}
        });
        tagAdapter.setSelectedList(1);

        gv_training_toask.setOnTagClickListener(new TagFlowLayout.OnTagClickListener(){
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent){
                setPosition(position);
           //     requestListData(name);
                return true;
            }
        });
        gv_training_toask.setOnSelectListener(new TagFlowLayout.OnSelectListener(){
            @Override
            public void onSelected(Set<Integer> selectPosSet){
                setTitle("choose:" + selectPosSet.toString());}
        });
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    //发布提问
    public void requestAsk() {
//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("userId", userId);
        map.put("questionTitle", questionTitle);
        map.put("questionDesc", questionDesc);
        map.put("typeCode", codeList.get(getPosition()));

        btnTrainingToask.setClickable(false);

        HtmlRequest.getTrainingToAsk(this, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if (params.result != null) {

                    ResultInfoBean b = (ResultInfoBean) params.result;
                    if (b.getFlag().equals("true")) {
                        Toast.makeText(TrainingToAskActivity.this, b.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(TrainingToAskActivity.this, b.getMessage(), Toast.LENGTH_SHORT).show();

                    }

//                    indexItemBeans = b.getList();
//                    initRecyclerView();

                } else {

                }
                btnTrainingToask.setClickable(true);
            }
        });

    }

    public void notifyData(int position) {

        for (int i = 0; i < typeList.size(); i++) {
            if (i == position) {
                typeList.get(i).setFlag(true);
            } else {
                typeList.get(i).setFlag(false);
            }

        }

    }

    private void initTopTitle() {
        TitleBar title = (TitleBar) findViewById(R.id.rl_title);
        title.setTitle(getResources().getString(R.string.title_null)).setLogo(R.drawable.icons, false)
                .setIndicator(R.mipmap.icon_back).setCenterText(getResources().getString(R.string.training_toask))
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

    @OnClick(R.id.btn_training_toask)
    public void onClick() {
        questionTitle = etToaskTitle.getText().toString();
        questionDesc = etToaskContent.getText().toString();
        if (questionTitle.length() < 10) {
            Toast.makeText(TrainingToAskActivity.this, "标题至少十个字", Toast.LENGTH_SHORT).show();
        } else {
            if (TextUtils.isEmpty(questionDesc.trim())) {
                Toast.makeText(TrainingToAskActivity.this, "请输入问题", Toast.LENGTH_SHORT).show();
            } else {
                requestAsk();
            }

        }

    }

    public static void setListViewHeightBasedOnChildren(GridView listView) {
        // 获取listview的adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int col = 3;// listView.getNumColumns();
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < listAdapter.getCount(); i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
        }

        // 获取listview的布局参数
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // 设置高度
        params.height = totalHeight;
        // 设置margin
        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        // 设置参数
        listView.setLayoutParams(params);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
