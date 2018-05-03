package com.rulaibao.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.rulaibao.R;
import com.rulaibao.adapter.InsuranceProductAdapter;
import com.rulaibao.adapter.TagAdapter;
import com.rulaibao.base.BaseActivity;
import com.rulaibao.bean.InsuranceProduct1B;
import com.rulaibao.bean.InsuranceProduct2B;
import com.rulaibao.bean.InsuranceProduct3B;
import com.rulaibao.dialog.DeleteHistoryDialog;
import com.rulaibao.network.BaseParams;
import com.rulaibao.network.BaseRequester;
import com.rulaibao.network.HtmlRequest;
import com.rulaibao.network.types.MouldList;
import com.rulaibao.uitls.FlowLayout;
import com.rulaibao.uitls.ListDataSave;
import com.rulaibao.uitls.TagFlowLayout;
import com.rulaibao.widget.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * 保险产品搜索页面
 */
public class SearchActivity extends BaseActivity implements View.OnClickListener,TextWatcher {
    private TitleBar titleBar;
    private EditText et_search;//输入框
    private Button bt_clear;//清除
    private TextView tv_search_cancel;
    private ViewSwitcher vs;
    private ListView listView;
    private InsuranceProductAdapter mAdapter;
    private MouldList<InsuranceProduct2B> totalList = new MouldList<>();
    Context mContext;
   private ArrayList listString=new ArrayList();;//历史搜索list
    private String[] mHot = new String[]
            {"E生保", "抗癌卫士", "平安保险 ", "保险", "人寿保险", "E生保",
                    "平安保险", "抗癌卫士", "人寿保险"};//固定的
    private String[] mInsurance = new String[]
            {"中国人寿", "阳光保险", "中华保险"};//固定的
    private TagFlowLayout mFlowLayoutHot;//热门搜词
    private TagFlowLayout mFlowLayoutInsurance;//保险公司
    private TagFlowLayout mFlowLayoutHistory;//历史搜索
    private LinearLayout ll_delete_history;
    private TextView tv_search_history_lines;
    private ImageView iv_delete_history;
    ListDataSave dataSave;
    boolean isDelete;

    private String name;//搜索除了保险字段
    private String companyName;//搜索保险字段

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseSetContentView(R.layout.activity_search);
        initTopTitle();
        initView();
        initData();
    }

    private void initTopTitle() {
        titleBar = (TitleBar) findViewById(R.id.rl_title);
        titleBar.setVisibility(GONE);
    }

    private void initView() {
        mContext = getApplicationContext();
        dataSave = new ListDataSave(mContext, "search_pre");//搜索sp
        et_search = (EditText) findViewById(R.id.et_search);
        bt_clear = (Button) findViewById(R.id.bt_clear);
        bt_clear.setVisibility(GONE);
        et_search.addTextChangedListener(this);
        tv_search_cancel= (TextView) findViewById(R.id.tv_search_cancel);
        mFlowLayoutHot = (TagFlowLayout) findViewById(R.id.tagflowLayout_hot);
        mFlowLayoutInsurance = (TagFlowLayout) findViewById(R.id.tagflowLayout_insurance);
        mFlowLayoutHistory = (TagFlowLayout) findViewById(R.id.tagflowLayout_history);
        iv_delete_history= (ImageView) findViewById(R.id.iv_delete_history);
        ll_delete_history= (LinearLayout) findViewById(R.id.ll_delete_history);
        tv_search_history_lines= (TextView) findViewById(R.id.tv_search_history_lines);
        vs= (ViewSwitcher) findViewById(R.id.vs);

        listView= (ListView) findViewById(R.id.listView);
        mAdapter = new InsuranceProductAdapter(mContext, totalList);
        listView.setAdapter(mAdapter);
    }

    private void initData() {
        bt_clear.setOnClickListener(this);
        tv_search_cancel.setOnClickListener(this);
        iv_delete_history.setOnClickListener(this);
        flowLayoutHot();//热门搜词
        flowLayoutInsurance();//保险公司
        flowLayoutHistory();//历史搜索
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //将搜索内容保存到SharedPreferences
                    isDelete=false;
                    listString.add(v.getText().toString());
                    dataSave.setDataList("search_string", listString);

                    name=v.getText().toString();
                    companyName="";
                    //请求数据要搜索的内容
                    requestListData(name,companyName);
                    return true;
                }
                return false;
            }
        });

    }
    //热门搜词
    private void flowLayoutHot() {
        mFlowLayoutHot.setAdapter(new TagAdapter<String>(mHot){
            @Override
            public View getView(FlowLayout parent, int position, String s){
                TextView tv = (TextView) LayoutInflater.from(SearchActivity.this).inflate(R.layout.search_flow_item_tv,
                        mFlowLayoutHot, false);
                tv.setText(s);
                return tv;}
        });

        mFlowLayoutHot.setOnTagClickListener(new TagFlowLayout.OnTagClickListener(){
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent){

                et_search.setText(mHot[position]);
                et_search.setSelection(et_search.getText().toString().length());

                name = mHot[position];
                companyName="";
                //请求数据要搜索的内容
                requestListData(name,companyName);
                return true;
            }
        });
        mFlowLayoutHot.setOnSelectListener(new TagFlowLayout.OnSelectListener(){
            @Override
            public void onSelected(Set<Integer> selectPosSet){
                setTitle("choose:" + selectPosSet.toString());}
        });
    }
    //保险公司
    private void flowLayoutInsurance() {
        mFlowLayoutInsurance.setAdapter(new TagAdapter<String>(mInsurance){
            @Override
            public View getView(FlowLayout parent, int position, String s){
                TextView tv = (TextView) LayoutInflater.from(SearchActivity.this).inflate(R.layout.search_flow_item_tv,
                        mFlowLayoutInsurance, false);
                tv.setText(s);
                return tv;}
        });

        mFlowLayoutInsurance.setOnTagClickListener(new TagFlowLayout.OnTagClickListener(){
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent){

                et_search.setText(mInsurance[position]);
                et_search.setSelection(et_search.getText().toString().length());

                name="";
                companyName = mInsurance[position];
                //请求数据要搜索的内容
                requestListData(name,companyName);
                return true;
            }
        });
        mFlowLayoutInsurance.setOnSelectListener(new TagFlowLayout.OnSelectListener(){
            @Override
            public void onSelected(Set<Integer> selectPosSet){
                setTitle("choose:" + selectPosSet.toString());}
        });
    }
    //历史搜索
    private void flowLayoutHistory() {
        listString=(ArrayList)dataSave.getDataList("search_string".toString(),isDelete);//获取保存在本地的历史搜索数据
        if (listString.size()==0){
            ll_delete_history.setVisibility(GONE);
            tv_search_history_lines.setVisibility(GONE);
        }else{
            ll_delete_history.setVisibility(VISIBLE);
            tv_search_history_lines.setVisibility(VISIBLE);
        }
        mFlowLayoutHistory.setAdapter(new TagAdapter<String>(listString){
            @Override
            public View getView(FlowLayout parent, int position, String s){
                TextView tv = (TextView) LayoutInflater.from(SearchActivity.this).inflate(R.layout.search_flow_item_tv,
                        mFlowLayoutHistory, false);
                tv.setText(s);
                return tv;}
        });

        mFlowLayoutHistory.setOnTagClickListener(new TagFlowLayout.OnTagClickListener(){
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent){

                et_search.setText(listString.get(position).toString());
                et_search.setSelection(et_search.getText().toString().length());

                name = listString.get(position).toString();
                companyName = "";
                //请求数据要搜索的内容
                requestListData(name,companyName);
                return true;
            }
        });
        mFlowLayoutHistory.setOnSelectListener(new TagFlowLayout.OnSelectListener(){
            @Override
            public void onSelected(Set<Integer> selectPosSet){
                setTitle("choose:" + selectPosSet.toString());}
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_clear:
                et_search.setText("");
                flowLayoutHistory();//重新加载搜索内容
                vs.setDisplayedChild(0);
                break;
           case R.id.tv_search_cancel:
               finish();
                break;
            case R.id.iv_delete_history:
                DeleteHistoryDialog dialog = new DeleteHistoryDialog(SearchActivity.this,
                        new DeleteHistoryDialog.OnExitChanged() {

                            @Override
                            public void onConfim() {
                                listString.clear();
                                isDelete=true;
                                flowLayoutHistory();//重新加载搜索内容
                                ll_delete_history.setVisibility(GONE);
                                tv_search_history_lines.setVisibility(GONE);
                            }

                            @Override
                            public void onCancel() {

                            }
                        }, "确定清除历史搜索记录吗？");
                dialog.show();
                break;

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
    /****
     * 对用户输入文字的监听
     *
     * @param editable
     */
    @Override
    public void afterTextChanged(Editable editable) {
        /**获取输入文字**/
        String input = et_search.getText().toString().trim();
        if (input.isEmpty()) {
            bt_clear.setVisibility(GONE);
            vs.setDisplayedChild(0);
        } else {
            bt_clear.setVisibility(VISIBLE);
            vs.setDisplayedChild(1);
        }
    }
    /**
     * 获取保险产品搜索数据
     */
    private void requestListData(String name,String companyName ) {
        LinkedHashMap<String, Object> param = new LinkedHashMap<>();
        param.put("userId", userId);
        param.put("name",name);
        param.put("companyName",companyName);
        try {
            HtmlRequest.getInsuranceProductSearch(mContext, param, new BaseRequester.OnRequestListener() {
                @Override
                public void onRequestFinished(BaseParams params) {
                    if (params==null||params.result == null) {
                        Toast.makeText(mContext, "加载失败，请确认网络通畅", Toast.LENGTH_LONG).show();
                        return;
                    }
                    InsuranceProduct1B data= (InsuranceProduct1B) params.result;
                    MouldList<InsuranceProduct2B> everyList = data.getList();
                    if ((everyList == null || everyList.size() == 0)){
                        Toast.makeText(mContext, "啊哦，没有搜到保险产品！", Toast.LENGTH_SHORT).show();
                    }
                    totalList.clear();
                    totalList.addAll(everyList);
                    //刷新数据
                    mAdapter.notifyDataSetChanged();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}