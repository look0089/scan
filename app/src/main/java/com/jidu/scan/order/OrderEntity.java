package com.jidu.scan.order;

import com.jidu.scan.retorift.BaseEntity;

import java.util.List;

/**
 * Created by Jzs on 2017/11/2 0002.
 */

public class OrderEntity extends BaseEntity {


    /**
     * data : {"page":1,"page_size":10,"list":[{"id":"1","order_no":"JS20170427013","status":"0","order_time":"2017-04-27","create_time":"2017-11-02 11:56:44","scan_count":"1","count":"8","status_cn":"未完成"}]}
     */

    public DataEntity data;

    public static class DataEntity {
        /**
         * page : 1
         * page_size : 10
         * list : [{"id":"1","order_no":"JS20170427013","status":"0","order_time":"2017-04-27","create_time":"2017-11-02 11:56:44","scan_count":"1","count":"8","status_cn":"未完成"}]
         */

        public int page;
        public int page_size;
        public List<ListEntity> list;

        public static class ListEntity {
            /**
             * id : 1
             * order_no : JS20170427013
             * status : 0
             * order_time : 2017-04-27
             * create_time : 2017-11-02 11:56:44
             * scan_count : 1
             * count : 8
             * status_cn : 未完成
             */

            public String id;
            public String order_no;
            public String status;
            public String order_time;
            public String create_time;
            public String scan_count;
            public String count;
            public String status_cn;
        }
    }
}
