package com.dh.dhappliaction.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.dh.dhappliaction.R;
import com.dh.dhappliaction.util.SharedPreferencesUtil;

/**
 * Created by 端辉 on 2016/5/25.
 */
public class InfoActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_exit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        initView();
    }

    private void initView(){
        getSupportActionBar().setTitle("个人中心");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        btn_exit = (Button) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_exit:
                SharedPreferencesUtil.editor(this,"","");
                setResult(2);
                finish();
                break;
        }
    }
}
