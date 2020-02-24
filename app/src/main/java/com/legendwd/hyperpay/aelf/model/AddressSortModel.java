package com.legendwd.hyperpay.aelf.model;

/**
 * Created by admin on 2016/7/22.
 */

/**
 * 类简要描述
 * <p/>
 * <p>
 * 类详细描述
 * </p>
 *
 * @author duanbokan
 */

public class AddressSortModel extends CountryModel {
    // 显示数据拼音的首字母
    public String sortLetters;

    public CountrySortToken sortToken = new CountrySortToken();

    public AddressSortModel(String name, String number, String countrySortKey) {
        super(name, number, countrySortKey);
    }

}
