package com.jidu.scan;


import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.Toast;

import com.jidu.scan.databinding.ActivityMainBinding;
import com.jidu.scan.order.OrderActivity;
import com.jidu.scan.retorift.RequestCallBack;
import com.jidu.scan.retorift.RetrofitManager;
import com.jidu.scan.utils.MyDialog;
import com.jidu.scan.utils.PermissionDialog;
import com.jidu.scan.utils.SharedPreferencesUtil;
import com.tbruyelle.rxpermissions.RxPermissions;

public class MainActivity extends AppCompatActivity {
    private RxPermissions rxPermissions;
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        rxPermissions = new RxPermissions(this);

        Toolbar mToolbarTb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbarTb);
        mToolbarTb.setTitle("富友盘查");

        initView();
        apiCheck();
    }

    private void initView() {

        mBinding.setOrderClick(v ->
                rxPermissions
                        .request(Manifest.permission.READ_PHONE_STATE)
                        .subscribe(granted -> {
                            if (granted) { // Always true pre-M
                                startActivity(new Intent(this, OrderActivity.class));
                            } else {
                                new PermissionDialog(this).show();
                            }
                        })
        );

        mBinding.setOpenScan(v ->
                rxPermissions
                        .request(Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.CAMERA)
                        .subscribe(granted -> {
                            if (granted) { // Always true pre-M
                                Intent intent = new Intent(this, TestScanActivity.class);
                                intent.putExtra("type", "1");
                                startActivity(intent);
                            } else {
                                new PermissionDialog(this).show();
                            }
                        })
        );

        mBinding.setSetClick(v -> {
            MyDialog myDialog = new MyDialog();
            myDialog.showDialog(this, "请输入要连接的ip地址,并以 “/” 结束");
            myDialog.showEt();
            myDialog.setCallBack(new MyDialog.DialogCallBack() {
                @Override
                public void handle() {
                    String etText = myDialog.getEtText();
                    if (!TextUtils.isEmpty(etText)) {
                        if (etText.startsWith("https://") || etText.startsWith("http://") && etText.endsWith("/")) {
                            SharedPreferencesUtil.saveString("base_url", etText);
                            AppConfig.BASE_URL = etText;
                            RetrofitManager.baseUrl = etText;
                            final Snackbar snackbar = Snackbar.make(v, "设置成功", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            snackbar.setAction("确定", view -> snackbar.dismiss());
                        } else {
                            Toast.makeText(MainActivity.this, "地址有误，请检查", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                @Override
                public void cancelHandle() {

                }
            });


        });

    }


    private void apiCheck() {
        Api.getInstance()
                .apiCheck()
                .callBack(new RequestCallBack() {
                    @Override
                    public void onSuccess(int code, String msg, Object object) {
                        if (code == 0) {
                            final Snackbar snackbar = Snackbar.make(mBinding.llRoot, "连接成功", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            snackbar.setAction("确定", view -> snackbar.dismiss());
                        } else {
                            final Snackbar snackbar = Snackbar.make(mBinding.llRoot, "连接失败，请检查链接地址", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            snackbar.setAction("确定", view -> snackbar.dismiss());
                        }
                    }

                    @Override
                    public void onFailure(int code, String msg, Object object) {
                        final Snackbar snackbar = Snackbar.make(mBinding.llRoot, "连接失败，请检查链接地址", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        snackbar.setAction("确定", view -> snackbar.dismiss());
                    }
                }).post();
    }
}
