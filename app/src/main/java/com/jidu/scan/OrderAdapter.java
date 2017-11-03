package com.jidu.scan;

import android.content.Context;
import android.support.annotation.LayoutRes;

import com.jidu.scan.OrderEntity.DataEntity.ListEntity;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.SuperViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jzs on 2017/7/18.
 */

public class OrderAdapter extends SuperAdapter<ListEntity> {

    protected List<ListEntity> mDatas = new ArrayList<>();

    public OrderAdapter(Context context, List<ListEntity> items, @LayoutRes int layoutResId) {
        super(context, items, layoutResId);
        this.mDatas.clear();
        this.mDatas.addAll(items);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int layoutPosition, ListEntity item) {

    }

    public void notifySetChange(List<ListEntity> mDatas) {
        this.mDatas.clear();
        this.mDatas.addAll(mDatas);
        notifyDataSetChanged();
    }
}
