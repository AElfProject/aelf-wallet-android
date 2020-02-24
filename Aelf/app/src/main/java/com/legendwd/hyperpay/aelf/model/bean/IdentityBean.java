package com.legendwd.hyperpay.aelf.model.bean;

/**
 * created by joseph at 2019/6/19
 */

public class IdentityBean {


    /**
     * id : 25
     * name : david1
     * img : http://hyperpay.oss-ap-southeast-1.aliyuncs.com/elf_wallet/2019-06/201906051757501812481421.png
     * create_time : 0
     */

    private String id;
    private String address;
    private String name;
    private String img;
    private String create_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
