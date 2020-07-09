package com.legendwd.hyperpay.aelf.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.legendwd.hyperpay.aelf.db.greendao.DaoMaster;
import com.legendwd.hyperpay.lib.Logger;

import org.greenrobot.greendao.database.Database;

/**
 * @author myth_li
 * @date 2020/5/19
 * description:
 */
public class UpgradeHelper extends DaoMaster.DevOpenHelper {

    public static String TAG = UpgradeHelper.class.getSimpleName();

    public UpgradeHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    /**
     * Here is where the calls to upgrade are executed
     * onUpgrade方法中进行数据库的迁移
     */
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {

        /* i represent the version where the user is now and the class named with this number implies that is upgrading from i to
        i++ schema */
        Logger.d("UpgradeHelper", "oldVersion" + oldVersion + " newVersion: " + newVersion);
//        switch (oldVersion) {
//            case 1:
//                break;
//            default:
//                break;
//        }
    }
}
