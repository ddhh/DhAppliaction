package com.dh.dhappliaction.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dh.dhappliaction.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 端辉 on 2016/3/3.
 */
public class ContactsInfoActivity extends AppCompatActivity {


    public static final String INTENT_CONTACTS_NAME = "INTNET_CONTACTS_NAME";

    public static final String INTENT_CONTACTS_NUMBER = "INTENT_CONTACTS_NUMBER";


    @Bind(R.id.info_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @Bind(R.id.info_fab)
    FloatingActionButton mFab;
    @Bind(R.id.tv_contacts_info_type)
    TextView tv_Type;
    @Bind(R.id.tv_contacts_info_number)
    TextView tv_Number;
    @Bind(R.id.ib_contacts_call)
    AppCompatImageButton mIb_Call;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_info);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        String name = getIntent().getStringExtra(INTENT_CONTACTS_NAME);
        mToolbarLayout.setTitle(name);
        String number = getIntent().getStringExtra(INTENT_CONTACTS_NUMBER);

        tv_Type.setText("手机");
        tv_Number.setText(number);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.ib_contacts_call, R.id.info_fab})
    public void onClick(View view) {
        Intent intent;
        Uri uri;
        String number = getIntent().getStringExtra(INTENT_CONTACTS_NUMBER);
        switch (view.getId()) {
            case R.id.ib_contacts_call:
                intent = new Intent(Intent.ACTION_SENDTO);
                uri = Uri.parse("smsto:" + number);
                intent.setData(uri);
                startActivity(intent);
                break;
            case R.id.info_fab:
                intent = new Intent(Intent.ACTION_CALL);
                uri = Uri.parse("tel:" + number);
                intent.setData(uri);
                startActivity(intent);
                break;
        }
    }
}
