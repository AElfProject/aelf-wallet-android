package com.legendwd.hyperpay.aelf.base;

public class BaseAdapterModel {

    public int mItemType = ItemType.DEFAULT;

    public int getItemType() {
        return mItemType;
    }

    public void setItemType(int itemType) {
        mItemType = itemType;
    }

    public static class ItemType {
        public static int DEFAULT = 0;
        public static int EMPTY = 1;

    }
}
