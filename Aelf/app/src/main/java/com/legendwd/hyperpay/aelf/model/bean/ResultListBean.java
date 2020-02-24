package com.legendwd.hyperpay.aelf.model.bean;

import java.util.List;

/**
 * @author lovelyzxing
 * @date 2019/6/10
 * @Description
 */
public class ResultListBean<T> {
    private int status;
    private String msg;
    private List<T> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
