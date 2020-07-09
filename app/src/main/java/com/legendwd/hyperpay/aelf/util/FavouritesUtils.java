package com.legendwd.hyperpay.aelf.util;

import android.util.Log;

import com.legendwd.hyperpay.aelf.model.bean.MarketDataBean;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Colin
 * @date 2019/8/9.
 * description： 收藏 ** 工具类 封装
 */
public class FavouritesUtils {
    /**
     * 检索本地仓库 star 数据集
     *
     * @return
     */
    public static List<MarketDataBean> getFavourites() {
        List<MarketDataBean> localDatas = null;
        try {
            String property = CacheUtil.getInstance().getProperty(Constant.Sp.MARKET_STAR);
            Log.d("====getFavourites", property);
            localDatas = JsonUtils.jsonToList(property, MarketDataBean.class);
        } catch (Exception e) {
            Log.d("====getFavourites", "error");
        }
        if (localDatas == null) {
            localDatas = new ArrayList<>();
            return localDatas;
        }
        return localDatas;
    }

    /**
     * set数据集
     *
     * @return
     */
    public static boolean setFavourites(List<MarketDataBean> listBeans) {
        try {
            if (listBeans != null) {
                String data = JsonUtils.objToJson(listBeans);
                Log.d("====setFavourites", data);
                CacheUtil.getInstance().setProperty(Constant.Sp.MARKET_STAR, data);
                return true;
            }
        } catch (Exception e) {
            Log.d("====setFavourites", "error");
        }

        return false;
    }
}
