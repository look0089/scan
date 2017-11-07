package com.jidu.scan.order;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.jidu.scan.Api;
import com.jidu.scan.AppConfig;
import com.jidu.scan.databinding.ActivityOrderBinding;
import com.jidu.scan.utils.MyTextWatcher;
import com.jidu.scan.utils.PermissionDialog;
import com.jidu.scan.R;
import com.jidu.scan.TestScanActivity;
import com.jidu.scan.databinding.ActivityMainBinding;
import com.jidu.scan.retorift.RequestCallBack;
import com.jidu.scan.retorift.RetrofitManager;
import com.jidu.scan.utils.MyDialog;
import com.jidu.scan.utils.SharedPreferencesUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.byteam.superadapter.SuperViewHolder;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private OrderAdapter mAdapter;
    private List<OrderEntity.DataEntity.ListEntity> mList = new ArrayList<>();
    private int mPage = 1;
    private RxPermissions rxPermissions;
    private ActivityOrderBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_order);
        rxPermissions = new RxPermissions(this);
        initView();
        order("", mPage + "");
    }

    private void initView() {

        mBinding.setCleanText(v -> {
            mBinding.etOrder.setText("");
            hideSoftInput(v.getWindowToken());
        });

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

        mBinding.setFabOnClick(v -> {
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
                            Toast.makeText(OrderActivity.this, "地址有误，请检查", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

                @Override
                public void cancelHandle() {

                }
            });


        });

        //设置edittext清空按钮
        mBinding.etOrder.addTextChangedListener(new MyTextWatcher() {
            public void afterTextChanged(Editable editable) {
                if (mBinding.etOrder.getText().toString().trim().length() > 0) {
                    mBinding.ivClean.setVisibility(View.VISIBLE);
                } else {
                    mBinding.ivClean.setVisibility(View.GONE);
                }
            }
        });

        //设置edittext搜索按钮
        mBinding.etOrder.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                String order = mBinding.etOrder.getText().toString().trim();
                if (!TextUtils.isEmpty(order)) {
                    mPage = 1;
                    mList.clear();
                    order(order, mPage + "");
                    hideSoftInput(textView.getWindowToken());
                } else {
                    Toast.makeText(this, "请输入订单号", Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        });

        mBinding.refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPage = 1;
                mList.clear();
                order(mBinding.etOrder.getText().toString().toString(), mPage + "");
                refreshlayout.finishRefresh();
            }
        });

        mAdapter = new OrderAdapter(this, mList, R.layout.item_order) {

            @Override
            public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, OrderEntity.DataEntity.ListEntity item) {
                holder.setText(R.id.tv_order_no, "订单号：" + item.order_no);
                holder.setText(R.id.tv_count, "部件数：" + item.count);
                holder.setText(R.id.tv_order_time, "订单时间：" + item.order_time);
                holder.setText(R.id.tv_scan_count, "已扫次数：" + item.scan_count);
                holder.setText(R.id.tv_status, "状态：" + item.status_cn);
            }
        };
        mBinding.rvMain.setAdapter(mAdapter);
    }

    //隐藏键盘
    public void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void order(String order_on, String page) {
        Api.getInstance()
                .order(order_on, page)
                .callBack(new RequestCallBack() {
                    @Override
                    public void onSuccess(int code, String msg, Object object) {
                        OrderEntity.DataEntity entity = ((OrderEntity) object).data;
                        mList.addAll(entity.list);
                        if (mList.size() > 0) {
                            mBinding.tvNodata.setVisibility(View.GONE);
                        } else {
                            mBinding.tvNodata.setVisibility(View.VISIBLE);
                        }
                        if (entity.list.size() < 10) {
                            mBinding.refreshLayout.setEnableLoadmore(false);
                            mBinding.refreshLayout.finishLoadmore();
                        }
                        mAdapter.notifySetChange(mList);
                    }

                    @Override
                    public void onFailure(int code, String msg, Object object) {

                    }
                }).post();
    }
}
