package com.legendwd.hyperpay.aelf.db.dao;

import com.legendwd.hyperpay.aelf.AelfApplication;
import com.legendwd.hyperpay.aelf.db.MarketDatadb;
import com.legendwd.hyperpay.aelf.db.greendao.MarketCoindbDao;
import com.legendwd.hyperpay.aelf.db.greendao.MarketDatadbDao;
import com.legendwd.hyperpay.aelf.model.bean.MarketDataBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author myth_li
 * @date 2020/6/29
 * description:
 */
public class MarketDataDao {

    public static MarketDataBean queryData(String id) {
        MarketDatadbDao dbDao = AelfApplication.getDaoSession().getMarketDatadbDao();
        List<MarketDatadb> list = dbDao.queryBuilder()
                .where(MarketCoindbDao.Properties.Id.eq(id))
                .list();
        if (list.isEmpty()) {
            return null;
        } else {
            return changeList(list).get(0);
        }
    }

    public static List<MarketDataBean> queryList() {
        MarketDatadbDao dbDao = AelfApplication.getDaoSession().getMarketDatadbDao();
        return changeList(dbDao.loadAll());
    }

    public static void save(MarketDataBean bean) {
        MarketDatadbDao dbDao = AelfApplication.getDaoSession().getMarketDatadbDao();
        dbDao.insertInTx(convetData(bean));
    }


    public static void deleteData(String id) {
        MarketDatadbDao dbDao = AelfApplication.getDaoSession().getMarketDatadbDao();
        dbDao.deleteByKey(id);
    }

    public static void deleteAll() {
        MarketDatadbDao dbDao = AelfApplication.getDaoSession().getMarketDatadbDao();
        dbDao.deleteAll();
    }

    private static List<MarketDataBean> changeList(List<MarketDatadb> list) {
        List<MarketDataBean> dataList = new ArrayList<>(list.size());
        for (MarketDatadb datadb : list) {
            dataList.add(convetData(datadb));
        }
        return dataList;
    }

    private static MarketDataBean convetData(MarketDatadb datadb) {
        MarketDataBean bean = new MarketDataBean();
        bean.setId(datadb.getId());
        bean.setCurrentPrice(datadb.getCurrentPrice());
        bean.setImage(datadb.getImage());
        bean.setName(datadb.getName());
        bean.setMarketCap(datadb.getMarketCap());
        bean.setMarketCapChange24h(datadb.getMarketCapChange24h());
        bean.setMarketCapRank(datadb.getMarketCapRank());
        bean.setMarketCapChangePercentage24h(datadb.getMarketCapChangePercentage24h());
        bean.setSymbol(datadb.getSymbol());
        bean.setTotalSupply(datadb.getTotalSupply());
        return bean;
    }

    private static MarketDatadb convetData(MarketDataBean bean) {
        MarketDatadb datadb = new MarketDatadb();
        datadb.setId(bean.getId());
        datadb.setCurrentPrice(bean.getCurrentPrice());
        datadb.setImage(bean.getImage());
        datadb.setName(bean.getName());
        datadb.setMarketCap(bean.getMarketCap());
        datadb.setMarketCapChange24h(bean.getMarketCapChange24h());
        datadb.setMarketCapRank(bean.getMarketCapRank());
        datadb.setMarketCapChangePercentage24h(bean.getMarketCapChangePercentage24h());
        datadb.setSymbol(bean.getSymbol());
        datadb.setTotalSupply(bean.getTotalSupply());
        return datadb;
    }
}
