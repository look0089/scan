package com.jidu.scan.order;

import android.content.Context;
import android.support.annotation.LayoutRes;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jzs on 2017/7/18.
 */

public class OrderAdapter extends SuperAdapter<OrderEntity.DataEntity.ListEntity> {

    protected List<OrderEntity.DataEntity.ListEntity> mDatas = new ArrayList<>();

    public OrderAdapter(Context context, List<OrderEntity.DataEntity.ListEntity> items, @LayoutRes int layoutResId) {
        super(context, items, layoutResId);
        this.mDatas.clear();
        this.mDatas.addAll(items);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, OrderEntity.DataEntity.ListEntity item) {

    }

    public void notifySetChange(List<OrderEntity.DataEntity.ListEntity> mDatas) {
        this.mDatas.clear();
        this.mDatas.addAll(mDatas);
        notifyDataSetChanged();
    }
}
