package com.jidu.scan;


import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.jidu.scan.databinding.ActivityMainBinding;
import com.jidu.scan.order.OrderActivity;
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
        initView();
    }

    private void initView() {

        mBinding.setOrderClick(v -> startActivity(new Intent(this, OrderActivity.class)));

        mBinding.setOpenScan(v ->
                rxPermissions
                        .request(Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.CAMERA)
                        .subscribe(granted -> {
                            if (granted) { // Always true pre-M
                                startActivity(new Intent(this, TestScanActivity.class));
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

}
