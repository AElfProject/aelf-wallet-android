package com.legendwd.hyperpay.aelf.model.bean;

import java.util.List;

/**
 * created by joseph at 2019/6/19
 */

public class PublicMessageBean {


    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 11
         * message : <p>123123213</p>
         * type : 2
         * desc : 12321
         * create_time : 0
         * sort : 2
         */

        private String id;
        private String message;
        private String type;
        private String desc;
        private String create_time;
        private String sort;
        private String title;

        public String getTitle() {
            return title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }
    }
}
