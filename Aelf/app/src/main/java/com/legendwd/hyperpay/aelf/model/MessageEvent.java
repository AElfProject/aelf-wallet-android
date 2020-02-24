package com.legendwd.hyperpay.aelf.model;

import android.os.Bundle;

/**
 * @author lovelyzxing
 * @date 2018/9/19
 */
public class MessageEvent {
    private int message;
    private Bundle data;

    public MessageEvent(int what) {
        this.message = what;
    }

    public MessageEvent() {
    }

    public MessageEvent(Bundle data) {
        this.data = data;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public Bundle getData() {
        return data;
    }

    public void setData(Bundle data) {
        this.data = data;
    }
}
