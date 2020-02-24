package com.legendwd.hyperpay.aelf.model.bean;

import java.util.List;

public class MessageBean {


    /**
     * count : 0
     * unread_count : 0
     * list : []
     */

    private String count;
    private String unread_count;
    private List<SystemMessageBean> list;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getUnreadCount() {
        return unread_count;
    }

    public void setUnreadCount(String unread_count) {
        this.unread_count = unread_count;
    }

    public List<SystemMessageBean> getList() {
        return list;
    }

    public void setList(List<SystemMessageBean> list) {
        this.list = list;
    }

    public static class SystemMessageBean {
        String id;
        String message;
        String type;
        String desc;
        String sort;
        String create_time;
        String is_read;
        String title;

        public String getTitle() {
            return title;
        }

        public String getIs_read() {
            return is_read;
        }

        public void setIs_read(String is_read) {
            this.is_read = is_read;
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

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getCreateTime() {
            return create_time;
        }

        public void setCreateTime(String create_time) {
            this.create_time = create_time;
        }
    }

}
