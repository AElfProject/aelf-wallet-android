package com.legendwd.hyperpay.aelf.model.bean;

import java.util.List;

/**
 * @author lovelyzxing
 * @date 2019/6/12
 * @Description
 */
public class VersionLogBean {


    public List<ListBean> list;

    public static class ListBean {
        /**
         * id : 1
         * key : iOS
         * intro : ["增加USDT,LTC和ONT自管钱包（链上钱包），支持通过私钥导入钱包","钱包安全升级，优化用户体验","(iPhone 5及以下机型暂不支持，请勿更新)"]
         * verNo : 3.8
         * status : 1
         * is_force : 1
         * min_version :
         * create_time : 1561356470
         * update_time : 1561357637
         * upgrade_time : 1561356470
         */

        public String id;
        public String key;
        public String verNo;
        public String status;
        public String is_force;
        public String min_version;
        public String create_time;
        public String update_time;
        public String upgrade_time;
        public List<String> intro;
    }
}
