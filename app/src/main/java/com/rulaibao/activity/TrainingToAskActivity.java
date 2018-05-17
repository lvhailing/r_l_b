package com.rulaibao.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.rulaibao.R;
import com.rulaibao.adapter.TrainingToAskListAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.ResultAskIndexBean;
import com.rulaibao.bean.ResultAskTypeItemBean;
import com.rulaibao.bean.ResultInfoBean;
import com.rulaibao.bean.TestBean;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.widget.MyGridView;
import com.rulaibao.widget.TitleBar;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我要提问
 */

public class TrainingToAskActivity extends BaseActivity {

    @BindView(R.id.gv_training_toask)
    MyGridView gvTrainingToask;
    @BindView(R.id.et_toask_title)
    EditText etToaskTitle;
    @BindView(R.id.et_toask_content)
    EditText etToaskContent;
    @BindView(R.id.btn_training_toask)
    Button btnTrainingToask;

    private String string = "";
    private TrainingToAskListAdapter adapter;
    private String questionTitle = "";
    private String questionDesc = "";
    private int position = 0;

    private ArrayList<ResultAskTypeItemBean> typeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_training_toask);
        initTopTitle();
        initView();

    }

    public void initView() {

        typeList = (ArrayList<ResultAskTypeItemBean>)getIntent().getSerializableExtra("type");
        typeList.get(0).setFlag(true);
        adapter = new TrainingToAskListAdapter(this, typeList);
        gvTrainingToask.setAdapter(adapter);

        gvTrainingToask.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(TrainingToAskActivity.this,"---"+position,Toast.LENGTH_SHORT).show();


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

    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    //发布提问
    public void requestAsk(){


//        ArrayMap<String,Object> map = new ArrayMap<String,Object>();
        LinkedHashMap<String,Object> map = new LinkedHashMap<String,Object>();
        map.put("userId",userId);
        map.put("questionTitle",questionTitle);
        map.put("questionDesc",questionDesc);
        map.put("typeCode",typeList.get(position).getTypeCode());

        btnTrainingToask.setClickable(false);

        HtmlRequest.getTrainingToAsk(this, map, new BaseRequester.OnRequestListener() {
            @Override
            public void onRequestFinished(BaseParams params) {

                if(params.result!=null){

                    ResultInfoBean b = (ResultInfoBean)params.result;
                    if(b.getFlag().equals("true")){
                        Toast.makeText(TrainingToAskActivity.this,b.getMessage(),Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(TrainingToAskActivity.this,b.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                    btnTrainingToask.setClickable(true);
//                    indexItemBeans = b.getList();
//                    initRecyclerView();

                }else{

                }

            }
        });



    }


    public void notifyData(int position){

        for(int i=0;i<typeList.size();i++){
            if(i==position){
                typeList.get(i).setFlag(true);
            }else{
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
    public void onClick(){
        questionTitle = etToaskTitle.getText().toString();
        questionDesc = etToaskContent.getText().toString();

        requestAsk();

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
