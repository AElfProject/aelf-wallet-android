package com.legendwd.hyperpay.router;

import android.content.Context;

/**
 * Created by joseph on 2018/6/1.
 */
public interface IMainService {

    String getVersion();

    Context getContext();

    String getAddress();

    String getCurrency();

    String getLang();
}