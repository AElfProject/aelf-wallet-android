package com.legendwd.hyperpay.aelf.business.market.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.JsonObject;
import com.gyf.immersionbar.ImmersionBar;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.market.adapters.HomeMarketAdapter;
import com.legendwd.hyperpay.aelf.model.MessageEvent;
import com.legendwd.hyperpay.aelf.model.bean.CoinDetailBean;
import com.legendwd.hyperpay.aelf.model.bean.MarketDetailBean;
import com.legendwd.hyperpay.aelf.model.bean.MarketLineBean;
import com.legendwd.hyperpay.aelf.model.bean.MarketListBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.param.MarketLineParam;
import com.legendwd.hyperpay.aelf.presenters.IMarketLinePresenter;
import com.legendwd.hyperpay.aelf.presenters.impl.MarketLinePresenter;
import com.legendwd.hyperpay.aelf.util.FavouritesUtils;
import com.legendwd.hyperpay.aelf.util.JsonUtils;
import com.legendwd.hyperpay.aelf.views.IMarketLineView;
import com.legendwd.hyperpay.lib.CacheUtil;
import com.legendwd.hyperpay.lib.Constant;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 市场详情
 */
public class HomeMarketFragment extends BaseFragment implements IMarketLineView {
    @BindView(R.id.img_star)
    ImageView ivStar;
    @BindView(R.id.line_chart)
    LineChart line_chart;
    @BindView(R.id.tv_day)
    TextView tv_day;
    @BindView(R.id.tv_week)
    TextView tv_week;
    @BindView(R.id.tv_month)
    TextView tv_month;

    @BindView(R.id.tv_rate)
    TextView tv_rate;
    @BindView(R.id.tv_market_value)
    TextView tv_market_value;
    @BindView(R.id.tv_value)
    TextView tv_value;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.img_back)
    ImageView img_back;
    DecimalFormat df2 = new DecimalFormat("######0.000");
    private HomeMarketAdapter mHomeMarketAdapter;
    private IMarketLinePresenter presenter;
    private String time = "1";
    private List<MarketLineBean.ListBean> mLine = new ArrayList<>();
    private List<MarketDetailBean> mBeans = new ArrayList<>();
    private boolean isSrtar = false;
    protected List<MarketListBean.ListBean> mStarData;
    protected int starEnable = 1;
    protected MarketListBean.ListBean mDataBean;

    public static HomeMarketFragment newInstance(Bundle bundle) {
        HomeMarketFragment homeMarketFragment = new HomeMarketFragment();
        homeMarketFragment.setArguments(bundle);
        return homeMarketFragment;
    }

    //时间戳转字符串
    public static String getStrTime(String timeStamp, String isDay) {
        String timeString = null;
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日-HH时-mm分-ss秒");
        SimpleDateFormat sdf = null;
        if (isDay.equals("1")) {
            sdf = new SimpleDateFormat("HH:mm");
        }
        if (isDay.equals("2")) {
            sdf = new SimpleDateFormat("dd");
        }
        if (isDay.equals("3")) {
            sdf = new SimpleDateFormat("MM-dd");
        }
        if (isDay.equals("4")) {
            sdf = new SimpleDateFormat("yyyy-MM");
        }
        long l = Long.valueOf(timeStamp);
        timeString = sdf.format(new Date(l * 1000));//单位秒
        return timeString;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_market;
    }

    @Override
    public void process() {

        line_chart.setNoDataText(getString(R.string.empty_data));
        line_chart.setNoDataTextColor(R.color.colorPrimary);

//        img_back.setImageResource(R.mipmap.back_white);
        String name = getArguments().getString("name");

        mDataBean = (MarketListBean.ListBean) getArguments().getSerializable("bean");

        tv_title.setText(name);

        tv_day.setBackgroundResource(R.drawable.shape_button_purple);
        tv_day.setTextColor(Color.WHITE);

        presenter = new MarketLinePresenter(this);
        String currency = CacheUtil.getInstance().getProperty(Constant.Sp.PRICING_CURRENCY_ID_DEFAULT);

        if (TextUtils.isEmpty(currency)) {
            currency = "USD";
        }
        getTradeLine(time, currency);
        getCoinDetail();
        checkStar(name);
    }

    /**
     * 检测星星
     */
    private void checkStar(String name) {
        MarketListBean.ListBean listBean = null;
        mStarData = FavouritesUtils.getFavourites();

        if (mStarData != null && mStarData.size() > 0) {
            for (int i = 0; i < mStarData.size(); i++) {
                if (mStarData.get(i).getName().equals(name)) {
                    listBean = mStarData.get(i);
                    isSrtar = true;
                    starEnable = 2;
                }
            }
        }

        ivStar.setImageDrawable(getResources().getDrawable(isSrtar ? R.mipmap.favor_solid : R.mipmap.favour_outline));
        MarketListBean.ListBean finalListBean = listBean;
        ivStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starEnable++;
                if (starEnable % 2 == 0) {
                    ivStar.setImageDrawable(getResources().getDrawable(R.mipmap.favor_solid));
                    Log.d("=====>", JsonUtils.objToJson(mDataBean));
                    if (mStarData != null && mDataBean != null) {
                        mStarData.add(mDataBean);
                        mDataBean = null;
                    } else {
                        mStarData = new ArrayList<MarketListBean.ListBean>();
                        mStarData.add(mDataBean);
                        mDataBean = null;
                    }
                    Log.d("=====>", JsonUtils.objToJson(mStarData));
                    FavouritesUtils.setFavourites(mStarData);
                } else {
                    ivStar.setImageDrawable(getResources().getDrawable(R.mipmap.favour_outline));
                    if (mStarData != null) {
                        mStarData.remove(finalListBean);
                    }
                    FavouritesUtils.setFavourites(mStarData);
                }
            }
        });

    }

    private void getCoinDetail() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", getArguments().getString("name"));
        presenter.getCoinDetail(jsonObject);
    }

    private void getTradeLine(String time, String currency) {
        MarketLineParam param = new MarketLineParam();
        param.time = time;
        param.currency = currency;
        param.type = "1";
        param.name = getArguments().getString("name");
        presenter.getTradeLine(param);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().postSticky(new MessageEvent(Constant.Event.REFRSH_STAR));
    }

    @Override
    public void initImmersionBar() {
        ImmersionBar.with(this).keyboardEnable(true)
                .navigationBarColorInt(Color.WHITE)
                .statusBarDarkFont(true, 0.2f)
                .autoDarkModeEnable(true, 0.2f).init();
    }

    @Override
    public void onSuccess(ResultBean<MarketLineBean> bean) {

        if (200 == bean.getStatus()) {
            if (null == bean.getData().list || bean.getData().list.size() <= 0) {
                return;
            }

            mLine = bean.getData().list;
            LineData lineData = getLineData(bean.getData().list);
            showChart(line_chart, lineData, Color.parseColor("#00000000"));
            Description description = new Description();
            description.setEnabled(false);
            line_chart.setDescription(description);
        }

    }

    @OnClick(R.id.img_back)
    public void clickToolBar() {
        pop();
    }

    @OnClick({R.id.tv_day, R.id.tv_week, R.id.tv_month})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.tv_day:
                time = "1";
                break;

            case R.id.tv_week:
                time = "2";
                break;

            case R.id.tv_month:
                time = "3";
                break;
        }
        refreshTimeView();

        getTradeLine(time, "USD");
    }

    private void refreshTimeView() {
        tv_day.setBackgroundColor(Color.TRANSPARENT);
        tv_week.setBackgroundColor(Color.TRANSPARENT);
        tv_month.setBackgroundColor(Color.TRANSPARENT);


        tv_day.setTextColor(Color.BLACK);
        tv_week.setTextColor(Color.BLACK);
        tv_month.setTextColor(Color.BLACK);


        switch (time) {
            case "1":
                tv_day.setBackgroundResource(R.drawable.shape_button_purple);
                tv_day.setTextColor(Color.WHITE);
                break;

            case "2":
                tv_week.setBackgroundResource(R.drawable.shape_button_purple);
                tv_week.setTextColor(Color.WHITE);
                break;

            case "3":
                tv_month.setBackgroundResource(R.drawable.shape_button_purple);
                tv_month.setTextColor(Color.WHITE);
                break;
        }

    }

    @Override
    public void onError(int code, String msg) {

    }

    @Override
    public void onCoinDetailSuccess(ResultBean<CoinDetailBean> bean) {
        if (bean != null && bean.getData() != null) {
//            float rate = Float.parseFloat(bean.getData().getGlobal_rate()) * 100;
//            String result = String.format("%.2f", rate);
//            tv_rate.setText(rate > 0 ? ("+" + result + "%")
//                    : (result + "%"));

            float increase = Float.parseFloat(getArguments().getString("increase")) * 100;
            StringBuilder stringBuilder = new StringBuilder();
            if (increase >= 0) {
                stringBuilder.append("+")
                        .append(String.format("%.2f", increase))
                        .append("%");
            } else {
                stringBuilder.append(String.format("%.2f", increase))
                        .append("%");
            }
            tv_rate.setBackgroundResource(increase >= 0 ? R.drawable.shape_r3_c1abb97 : R.drawable.shape_r3_ce83323);
            tv_rate.setText(stringBuilder.toString());
            String symbol = CacheUtil.getInstance().getProperty(Constant.Sp.PRICING_CURRENCY_SYMBOL_DEFAULT);
            if (TextUtils.isEmpty(symbol)) {
                symbol = "$";
            }
            String valueS = getArguments().getString("price");
            if(TextUtils.isEmpty(valueS)) {
                valueS = "0";
            }
            double value = Double.valueOf(valueS);
            tv_market_value.setText(symbol + String.format("%.2f", value));
            double cny = Double.valueOf(bean.getData().getUsd_cny());
            if("$".equals(symbol)) {
                tv_value.setText("￥" + String.format("%.2f", value * cny));
            }else {
                tv_value.setText("$" + String.format("%.2f", value / cny));
            }
            mBeans.clear();

            createMarketDetailBean(getString(R.string.market_value), bean.getData().getMarket_value());
            createMarketDetailBean(getString(R.string.market_rank), "#" + bean.getData().getMarket_value_order());
            createMarketDetailBean(getString(R.string.market_24), bean.getData().getVol_trans());
            createMarketDetailBean(getString(R.string.market_supply), bean.getData().getSupply());


            if (mHomeMarketAdapter == null) {
                recyclerView.setLayoutManager(new GridLayoutManager(_mActivity, 2));
                mHomeMarketAdapter = new HomeMarketAdapter(mBeans);
                recyclerView.setAdapter(mHomeMarketAdapter);
            } else {
                mHomeMarketAdapter.notifyDataSetChanged();
            }
        }
    }

    private void createMarketDetailBean(String name, String value) {
        MarketDetailBean detailBean = new MarketDetailBean();
        detailBean.name = name;
        detailBean.value = value;
        mBeans.add(detailBean);
    }

    @Override
    public void onCoinDetailError(int code, String msg) {

    }

    // 设置显示的样式
    private void showChart(LineChart lineChart, LineData lineData, int color) {
        lineChart.setDrawBorders(false); // 是否在折线图上添加边框

        // no description text
        // 数据描述
        lineChart.setNoDataText("");

        // enable / disable grid background
        lineChart.setDrawGridBackground(true); // 是否显示表格颜色
        //	lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度
        lineChart.setGridBackgroundColor(Color.TRANSPARENT);

        // enable touch gestures
        lineChart.setTouchEnabled(false); // 设置是否可以触摸


        // enable scaling and dragging
        lineChart.setDragEnabled(false);// 是否可以拖拽
        lineChart.setScaleEnabled(false);// 是否可以缩放
        lineChart.getAxisRight().setEnabled(true);
        lineChart.getAxisRight().setTextColor(getResources().getColor(R.color.blue_641eb0));
        lineChart.getAxisRight().enableGridDashedLine(10f,10f, 0f);

        // 显示 Y轴 文字显示隐藏
//        if(isShow){
        lineChart.getAxisRight().setDrawLabels(true);
//        }else{
//            lineChart.getAxisRight().setDrawLabels(false);
//        }

        lineChart.getAxisRight().setGridColor(Color.parseColor("#536e87"));
        lineChart.getAxisLeft().setEnabled(false); // 隐藏左边的坐标轴
        lineChart.getAxisRight().setAxisLineColor(Color.parseColor("#1C2F58"));
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//设置横坐标在底部

        // 设置X轴 竖直wang网格线
        lineChart.getXAxis().setEnabled(true);
        //lineChart.getXAxis().setGridColor(Color.TRANSPARENT);//去掉网格中竖线的显示
//        lineChart.getXAxis().setGridColor(Color.parseColor("#00000000"));
        lineChart.getAxisRight().setGridColor(Color.parseColor("#536e87"));
        lineChart.getXAxis().setDrawAxisLine(false);

//		lineChart.getXAxis().setAxisLineColor(getResources().getColor(R.color.blue_line));


        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);//

        lineChart.setBackgroundColor(color);// 设置背景

        // add data
        lineChart.setData(lineData); // 设置数据


        // get the legend (only possible after setting data)
        final Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的
        mLegend.setEnabled(false);  // 隐藏 Legend


        // modify the legend ...
//         mLegend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);
        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(6f);// 字体
        mLegend.setTextColor(Color.WHITE);// 颜色
//        mLegend.setYOffset(100);

        // mLegend.setTypeface(mTf);// 字体

        XAxis xAxisK = lineChart.getXAxis();
        xAxisK.setDrawLabels(true);
//        xAxisK.setLabelCount(5);
//        xAxisK.setAxisLineWidth(5f);
        xAxisK.setTextColor(getResources().getColor(R.color.grey_999999));
//        xAxisK.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxisK.setPosition(XAxis.XAxisPosition.BOTTOM);

        // 设置 X轴下标
        xAxisK.setValueFormatter((value, axis) -> {
            int index = (int) value;

            if (mLine.size() == 1) {
                // 0 资产特殊处理
                return getStrTime(mLine.get(0).createTime, time);
            } else {
                if (mLine.size() > 0) {
                    if (index <= mLine.size()) {
                        return getStrTime(mLine.get(index).createTime, time);
                    }
                }
            }


            return "";
        });


        // 设置 Y轴 标识
        YAxis yAxis = lineChart.getAxisRight();
//        yAxis.setAxisMaxValue(150);
//        yAxis.setAxisMinValue(100);

        yAxis.setValueFormatter((value, axis) -> {

            if (value < 0) {
                return "";
            } else {
                if(value<1){
                    return df2.format(value);
                }else{
                    return  value+"";
                }

            }
        });
        lineChart.animateX(500); // 立即执行的动画,x轴
    }

    private LineData getLineData(List<MarketLineBean.ListBean> yData) {
        // y轴的数据
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        for (int i = 0; i < yData.size(); i++) {
            // float value = (float) (Math.random() * range) + 3;
            // yValues.add(new Entry(value, i));
            if (yData.get(i) != null) {
                if (yData.get(i).last != null) {

                    yValues.add(new Entry(i, Float.valueOf(yData.get(i).last)));//不截取小数位，直接取数据，截取小数位可能导致折线图为一条直线
//                    yValues.add(new Entry(i,Float.valueOf(getLegalPriceStr2(yData.get(i).last))));
                }
            }

        }

        // create a dataset and give it a type
        // y轴的数据集合
        LineDataSet lineDataSet = new LineDataSet(yValues, "" /* 显示在比例图上 */);
        lineDataSet.setDrawValues(true);  // 不显示数值


        // mLineDataSet.setFillAlpha(110);
        // mLineDataSet.setFillColor(Color.RED);

        // 用y轴的集合来设置参数
        lineDataSet.setLineWidth(1.5f); // 线宽
        lineDataSet.setCircleSize(0f);// 显示的圆形大小
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setDrawValues(false);//隐藏折线图每个数据点的值
        lineDataSet.setColor(Color.parseColor("#4B95EC"));// 显示颜色
//        lineDataSet.setCircleColor(getResources().getColor(R.color.green_theme));// 圆形的颜色
        lineDataSet.setCircleColor(Color.parseColor("#00000000"));// 圆形的颜色

        //	lineDataSet.setHighLightColor(getResources().getColor(R.color.blue_theme)); // 高亮的线的颜色

        ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
        lineDataSets.add(lineDataSet); // add the datasets
        lineDataSet.setDrawCircles(true);

//		lineDataSet.setCubicIntensity(0.6f);
//		lineDataSet.setDrawFilled(true);
//		lineDataSet.setFillColor(Color.rgb(0, 255, 255));


        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        //添加数据集
        dataSets.add(lineDataSet);

        // create a data object with the datasets
        LineData lineData = new LineData(dataSets);

        return lineData;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("======", "onDestroy:+HomeMarketFragment ");
        mStarData = null;
        mDataBean = null;
    }
}
