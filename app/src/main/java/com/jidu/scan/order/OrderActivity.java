package com.jidu.scan.order;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.jidu.scan.Api;
import com.jidu.scan.R;
import com.jidu.scan.TestScanActivity;
import com.jidu.scan.databinding.ActivityOrderBinding;
import com.jidu.scan.retorift.RequestCallBack;
import com.jidu.scan.utils.MyTextWatcher;
import com.jidu.scan.utils.PermissionDialog;
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
    private int mType = 1;//首次传1 翻页传2
    private RxPermissions rxPermissions;
    private ActivityOrderBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_order);
        rxPermissions = new RxPermissions(this);
        initView();
        order("", mPage + "", mType + "");
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
                                ArrayList<String> list = new ArrayList<String>();
                                for (int i = 0; i < mList.size(); i++) {
                                    list.add(mList.get(i).barcode);
                                }
                                Intent intent = new Intent(this, TestScanActivity.class);
                                intent.putExtra("type", "2");
//                                intent.putStringArrayListExtra("list", list);
                                startActivity(intent);
                            } else {
                                new PermissionDialog(this).show();
                            }
                        })
        );

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
                    mType = 1;
                    mList.clear();
                    order(order, mPage + "", mType + "");
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
                mType = 2;
                mPage++;
                order(mBinding.etOrder.getText().toString().toString(), mPage + "", mType + "");
                refreshlayout.finishLoadmore();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPage = 1;
                mList.clear();
                order(mBinding.etOrder.getText().toString().toString(), mPage + "", mType + "");
                refreshlayout.finishRefresh();
            }
        });

        mAdapter = new OrderAdapter(this, mList, R.layout.item_order) {

            @Override
            public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, OrderEntity.DataEntity.ListEntity item) {
//                holder.setText(R.id.tv_order_no, "订单号：" + item.order_no);
//                holder.setText(R.id.tv_count, "部件数：" + item.count);
//                holder.setText(R.id.tv_order_time, "订单时间：" + item.order_time);
//                holder.setText(R.id.tv_scan_count, "已扫次数：" + item.scan_count);
//                holder.setText(R.id.tv_status, "状态：" + item.status_cn);
                holder.setText(R.id.tv_bar_code, "编号：" + item.barcode);
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

    private void order(String order_on, String page, String type) {
        Api.getInstance()
                .order(order_on, page, type)
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
