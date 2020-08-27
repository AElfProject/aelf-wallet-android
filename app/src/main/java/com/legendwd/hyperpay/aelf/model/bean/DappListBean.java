package com.legendwd.hyperpay.aelf.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DappListBean {
    private List<BannerBean> banner;
    @SerializedName("dapp_link")
    private String dappLink;
    private List<DappBean> dapp;
    private List<DappGroupBean> list;

    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public String getDappLink() {
        return dappLink;
    }

    public void setDappLink(String dappLink) {
        this.dappLink = dappLink;
    }

    public List<DappGroupBean> getList() {
        return list;
    }

    public void setList(List<DappGroupBean> list) {
        this.list = list;
    }

    public List<DappBean> getDapp() {
        return dapp;
    }

    public void setDapp(List<DappBean> dapp) {
        this.dapp = dapp;
    }
}
