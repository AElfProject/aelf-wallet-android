package com.legendwd.hyperpay.aelf.business.discover.dapp;

import java.util.List;

/**
 * @author Colin
 * @date 2019/10/16.
 * description： Dapp js data moudleo
 */
public class GetAccountBean {
    public int code;
    public GameData data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public GameData getData() {
        return data;
    }

    public void setData(GameData data) {
        this.data = data;
    }

    public List getError() {
        return error;
    }

    public void setError(List error) {
        this.error = error;
    }

    public List error;


}
