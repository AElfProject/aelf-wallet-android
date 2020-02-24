package com.legendwd.hyperpay.aelf.business.my;

/**
 * Created by admin on 2016/7/22.
 */

import com.legendwd.hyperpay.aelf.model.bean.AddressBookBean;

import java.util.Comparator;

/**
 * 类简要描述
 *
 * <p>
 * 类详细描述
 * </p>
 *
 * @author duanbokan
 */

public class CountryComparator implements Comparator<AddressBookBean.ListBean> {

    @Override
    public int compare(AddressBookBean.ListBean o1, AddressBookBean.ListBean o2) {

        if (o1.sortLetters.equals("@") || o2.sortLetters.equals("#")) {
            return -1;
        } else if (o1.sortLetters.equals("#") || o2.sortLetters.equals("@")) {
            return 1;
        } else {
            return o1.sortLetters.compareTo(o2.sortLetters);
        }
    }

}
