package com.legendwd.hyperpay.aelf.db.dao;

import com.legendwd.hyperpay.aelf.AelfApplication;
import com.legendwd.hyperpay.aelf.db.MarketCoindb;
import com.legendwd.hyperpay.aelf.db.greendao.MarketCoindbDao;

import java.util.List;

/**
 * @author myth_li
 * @date 2020/6/29
 * description:
 */
public class MarketCoinDao {

    public static MarketCoindb queryData(String id) {
        MarketCoindbDao dbDao = AelfApplication.getDaoSession().getMarketCoindbDao();
        List<MarketCoindb> list = dbDao.queryBuilder()
                .where(MarketCoindbDao.Properties.Id.eq(id))
                .list();
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    public static List<MarketCoindb> queryList(String name) {
        MarketCoindbDao dbDao = AelfApplication.getDaoSession().getMarketCoindbDao();
        return dbDao.queryBuilder()
                .where(MarketCoindbDao.Properties.Name.like("%" + name + "%"))
                .build()
                .list();
    }

    public static void save(List<MarketCoindb> list) {
        MarketCoindbDao dbDao = AelfApplication.getDaoSession().getMarketCoindbDao();
        dbDao.insertInTx(list);
    }

    public static void deleteAll() {
        MarketCoindbDao dbDao = AelfApplication.getDaoSession().getMarketCoindbDao();
        dbDao.deleteAll();
    }
}
