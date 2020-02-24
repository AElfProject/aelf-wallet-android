package com.legendwd.hyperpay.aelf.model;

import java.util.List;

public class DappModel {


    private List<BannerBean> banner;
    private List<DappBean> dapp;
    private List<?> group;

    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public List<DappBean> getDapp() {
        return dapp;
    }

    public void setDapp(List<DappBean> dapp) {
        this.dapp = dapp;
    }

    public List<?> getGroup() {
        return group;
    }

    public void setGroup(List<?> group) {
        this.group = group;
    }

    public static class BannerBean {
        /**
         * title : 333-44444
         * url : http://ww.baidu.com
         * img : http://hyperpay.oss-ap-southeast-1.aliyuncs.com/2019-05/20190516175741110597.png
         * flag : 2
         * gid : 3
         * logo : http://hyperpay.oss-ap-southeast-1.aliyuncs.com/onchain.default.png
         * desc :
         * name :
         */

        private String title;
        private String url;
        private String img;
        private String flag;
        private String gid;
        private String logo;
        private String desc;
        private String name;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class DappBean {
        /**
         * id : 1
         * ico : 2019-05/20190516193214162211.png
         * coin : btc
         * name : 22222
         * desc : 2222-222-2222
         * cat : 1
         * url : http://ww.baidu.com
         * isindex : 1
         * website : http://ww.baidu.com
         * logo : http://hyperpay.oss-ap-southeast-1.aliyuncs.com/2019-05/20190516193214162211.png
         * type : Game
         */

        private String id;
        private String ico;
        private String coin;
        private String name;
        private String desc;
        private String cat;
        private String url;
        private String isindex;
        private String website;
        private String logo;
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIco() {
            return ico;
        }

        public void setIco(String ico) {
            this.ico = ico;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getCat() {
            return cat;
        }

        public void setCat(String cat) {
            this.cat = cat;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getIsindex() {
            return isindex;
        }

        public void setIsindex(String isindex) {
            this.isindex = isindex;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
