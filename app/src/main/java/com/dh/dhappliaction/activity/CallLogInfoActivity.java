package com.dh.dhappliaction.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.dh.dhappliaction.R;
import com.dh.dhappliaction.adapter.CallLogRecyclerViewAdapter;
import com.dh.dhappliaction.bean.CallLogBean;
import com.dh.dhappliaction.db.OperateCallLogUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 端辉 on 2016/3/3.
 */
public class CallLogInfoActivity extends AppCompatActivity {


    public static final String INTENT_CALLLOG_NAME = "INTENT_CALLLOG_NAME";

    public static final String INTENT_CALLLOG_TYPE = "INTENT_CALLLOG_TYPE";

    public static final String INTENT_CALLLOG_DATE = "INTENT_CALLLOG_DATE";

    public static final String INTENT_CALLLOG_TIME = "INTENT_CALLLOG_TIME";

    public static final String INTENT_CALLLOG_NUMBER = "INTENT_CALLLOG_NUMBER";

    @Bind(R.id.info_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @Bind(R.id.info_fab)
    FloatingActionButton mFab;
    @Bind(R.id.tv_calllog_info_type)
    TextView tv_type;
    @Bind(R.id.tv_calllog_info_date)
    TextView tv_date;
    @Bind(R.id.tv_calllog_info_time)
    TextView tv_time;
    @Bind(R.id.recyclerview_calllog_info)
    RecyclerView mRecyclerView;

    private List<CallLogBean> mList = new ArrayList<>();
    private CallLogRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calllog_info);
        ButterKnife.bind(this);
        initView();
        getRecyclerViewData();
        setAdapter();
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        String name = getIntent().getStringExtra(INTENT_CALLLOG_NAME);
        mToolbarLayout.setTitle(name);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration
                        .Builder(this)
                        .marginResId(R.dimen.activity_horizontal_margin, R.dimen.activity_horizontal_margin)
                        .build());
        initTextView();
    }

    private void initTextView() {
        Intent intent = getIntent();
        int type = intent.getIntExtra(CallLogInfoActivity.INTENT_CALLLOG_TYPE, -1);
        String date = intent.getStringExtra(CallLogInfoActivity.INTENT_CALLLOG_DATE);
        String time = intent.getStringExtra(CallLogInfoActivity.INTENT_CALLLOG_TIME);
        tv_type.setText(setTypeStr(type));
        tv_date.setText(date);
        tv_time.setText(time);
    }

    private String setTypeStr(int type) {
        switch (type) {
            case 1:
                return "来电";
            case 2:
                return "去电";
            case 3:
                return "未接";
            default:
                return "未知";
        }
    }

    private void getRecyclerViewData() {
        mList = OperateCallLogUtil.getOnePersonAllCalllogInfo(this, getIntent().getStringExtra(INTENT_CALLLOG_NUMBER));
    }

    private void setAdapter() {
        mAdapter = new CallLogRecyclerViewAdapter(mList);
        mAdapter.setOnItemClickListener(new CallLogRecyclerViewAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri uri = Uri.parse("tel"+mList.get(position).getNumber());
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(int position) {
                return false;
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.info_fab)
    public void onClick() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri uri = Uri.parse("tel:"+getIntent().getStringExtra(INTENT_CALLLOG_NUMBER));
        intent.setData(uri);
        startActivity(intent);
    }
}
