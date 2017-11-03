package com.jidu.scan.retorift;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import rx.Subscriber;

/**
 * BaseSubscriber
 * Created by Tamic on 2016-08-03.
 */
public abstract class BaseSubscriber<T> extends Subscriber<T> {

    @Override
    public void onError(Throwable e) {
        Log.e("Tamic", e.getMessage());
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCompleted() {

    }


}
