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

    public Api apiCheck() {
        setConfig(AppConfig.APICHECK, new HashMap<>(), OrderEntity.class);
        return this;
    }

    /**
     * @param code
     * @param type 1全库扫码，2固定查询结果扫码
     * @return
     */
    public Api check(String code, String type) {
        Map<String, String> maps = new HashMap<>();
        maps.put("code", code);
        maps.put("meid", AppHelper.getMEID(MyApplaciton.getInstance()));
        maps.put("type", type);
        setConfig(AppConfig.CHECK, maps, OrderEntity.class);
        return this;
    }

    public Api order(String order_no, String page, String type) {
        Map<String, String> maps = new HashMap<>();
        maps.put("order_no", order_no);
        maps.put("meid", AppHelper.getMEID(MyApplaciton.getInstance()));
        maps.put("page", page);
        maps.put("type", type);
        setConfig(AppConfig.ORDER, maps, OrderEntity.class);
        return this;
    }
}
