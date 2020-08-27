package com.legendwd.hyperpay.aelf.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author myth_li
 * @date 2020/8/26
 * description:
 */
public class DappGroupBean {

    @SerializedName("category_title")
    private String categoryTitle;
    private String cat;
    private List<DappBean> data;

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public List<DappBean> getData() {
        return data;
    }

    public void setData(List<DappBean> data) {
        this.data = data;
    }
}
