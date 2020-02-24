package com.legendwd.hyperpay.aelf.business.my.adapters;
/**
 * Created by admin on 2016/7/22.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.model.bean.AddressBookBean;
import com.legendwd.hyperpay.aelf.util.StringUtil;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * 国家码选择
 *
 * <p>
 * 类详细描述
 * </p>
 *
 * @author duanbokan
 */

public class AddressBookAdapter extends BaseAdapter implements SectionIndexer {

    private List<AddressBookBean.ListBean> mList;

    private Context mContext;
    OnItemClickListener mListener;

    public void setListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    /***
     * 初始化
     *
     * @param mContext
     * @param list
     */
    public AddressBookAdapter(Context mContext, List<AddressBookBean.ListBean> list) {
        this.mContext = mContext;
        if (list == null) {
            this.mList = new ArrayList<AddressBookBean.ListBean>();
        } else {
            this.mList = list;
        }
    }

    @Override
    public int getCount() {
        return this.mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final AddressBookBean.ListBean mContent = mList.get(position);

        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_address_book, null);
            viewHolder.country_sortName = (TextView) view.findViewById(R.id.country_catalog);
            viewHolder.country_name = (TextView) view.findViewById(R.id.country_name);
            viewHolder.country_number = (TextView) view.findViewById(R.id.country_number);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            viewHolder.country_sortName.setVisibility(View.VISIBLE);
            viewHolder.country_sortName.setText(mContent.sortLetters);
        } else {
            viewHolder.country_sortName.setVisibility(View.GONE);
        }

        viewHolder.country_name.setText(mList.get(position).name);
        viewHolder.country_number.setText(StringUtil.checkAddress(mList.get(position).address));

        return view;
    }

    @Override
    public int getPositionForSection(int section) {
        if (section != 42) {
            for (int i = 0; i < getCount(); i++) {
                String sortStr = mList.get(i).sortLetters;
                char firstChar = sortStr.toUpperCase(Locale.CHINESE).charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }
        } else {
            return 0;
        }

        return -1;
    }

    @Override
    public int getSectionForPosition(int position) {
        return mList.get(position).sortLetters.charAt(0);
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<AddressBookBean.ListBean> list) {
        if (list == null) {
            this.mList = new ArrayList<AddressBookBean.ListBean>();
        } else {
            this.mList = list;
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        // 国家码简拼所属的字母范围
        public TextView country_sortName;

        // 国家名
        public TextView country_name;

        // 代码
        public TextView country_number;

    }

    public interface OnItemClickListener {
        void onItemClick(AddressBookBean.ListBean bean);
    }

}
