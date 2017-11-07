package com.jidu.scan;

import com.jidu.scan.order.OrderEntity;
import com.jidu.scan.retorift.BaseApi;
import com.jidu.scan.utils.AppHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jzs on 2017/10/30 0030.
 */

public class Api extends BaseApi {

    private static Api instance;

    public static Api getInstance() {
        if (instance == null) {
            return new Api();
        }
        return instance;
    }

    public Api check(String code) {
        Map<String, String> maps = new HashMap<>();
        maps.put("code", code);
        maps.put("meid", AppHelper.getIMEI(MyApplaciton.getInstance()));
        setConfig(AppConfig.CHECK, maps, OrderEntity.class);
        return this;
    }

    public Api order(String order_no, String page) {
        Map<String, String> maps = new HashMap<>();
        maps.put("order_on", order_no);
        maps.put("page", page);
        setConfig(AppConfig.ORDER, maps, OrderEntity.class);
        return this;
    }
}
