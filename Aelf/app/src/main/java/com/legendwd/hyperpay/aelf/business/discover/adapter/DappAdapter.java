package com.legendwd.hyperpay.aelf.business.discover.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.github.ont.connector.utils.CommonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.legendwd.hyperpay.aelf.R;
import com.legendwd.hyperpay.aelf.base.BaseFragment;
import com.legendwd.hyperpay.aelf.business.discover.DappGameListFragment;
import com.legendwd.hyperpay.aelf.model.bean.DiscoveryBean;
import com.legendwd.hyperpay.aelf.util.ScreenUtils;
import com.legendwd.hyperpay.aelf.widget.GridSpacingItemDecoration;
import com.legendwd.hyperpay.aelf.widget.webview.WebviewFragment;
import com.legendwd.hyperpay.lib.Constant;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DappAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_BANNER = 1; // 轮播图
    private static final int TYPE_RECOMMENDED = 2;
    private static final int TYPE_EXCHANGE = 3;
    private static final int TYPE_APPLY = 4;
    private BaseFragment mFragment;
    private LayoutInflater layoutInflater;
    private DiscoveryBean mDiscoveryBean;

    private DappRecommendItemAdapter mDappRecommedItemAdapter;
    private DappExchangeAdapter mDappExchangeAdapter;
    private List<DiscoveryBean.DappBean> mExList;
    private List<DiscoveryBean.DappBean> mReList;

    public DappAdapter(BaseFragment fragment, DiscoveryBean discoveryBean) {
        this.mFragment = fragment;
        layoutInflater = LayoutInflater.from(fragment.getContext());
        mDiscoveryBean = discoveryBean;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case TYPE_BANNER:
                viewHolder = new BannerViewHolder(layoutInflater.inflate(R.layout.item_dicover_banner, parent, false));
                break;
            case TYPE_RECOMMENDED:
                viewHolder = new RecommendedViewHolder(layoutInflater.inflate(R.layout.item_dicover_recommend, parent, false));
                break;
            case TYPE_EXCHANGE:
                viewHolder = new ExchangeViewHolder(layoutInflater.inflate(R.layout.item_dicover_exchange, parent, false));
                break;
            case TYPE_APPLY:
                viewHolder = new ApplyViewHolder(layoutInflater.inflate(R.layout.item_dicover_apply, parent, false));
                break;
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BANNER;
        } else if (position == 1) {
            return TYPE_RECOMMENDED;
        } else if(position == 2){
            return TYPE_EXCHANGE;
        } else if(position == 3){
            return  TYPE_APPLY;
        }else {
            return  -1;
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BannerViewHolder) {
            BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
            //设置banner样式
            bannerViewHolder.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);


            int bannerWidth = ScreenUtils.getScreenWidth(mFragment.getContext())- CommonUtil.dp2Px(mFragment.getContext(),40);

            int bannerHeight = bannerWidth *30 /100;

            bannerViewHolder.banner.setLayoutParams(
                    new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,bannerHeight

                    ));


            //设置图片加载器
            bannerViewHolder.banner.setImageLoader(new GlideImageLoader());

            List<String> imgList = new ArrayList<>(mDiscoveryBean.getBanner().size());
            for (DiscoveryBean.BannerBean bannerBean : mDiscoveryBean.getBanner()) {
                imgList.add(bannerBean.getImg());
            }


            //设置图片集合
            bannerViewHolder.banner.setImages(imgList);

            //设置banner动画效果
            bannerViewHolder.banner.setBannerAnimation(Transformer.Default);

            //设置自动轮播，默认为true
            bannerViewHolder.banner.isAutoPlay(true);
            //设置轮播时间
            bannerViewHolder.banner.setDelayTime(3000);
            //设置指示器位置（当banner模式中有指示器时）
            bannerViewHolder.banner.setIndicatorGravity(BannerConfig.CENTER);

            //banner设置方法全部调用完毕时最后调用
            bannerViewHolder.banner.start();
            bannerViewHolder.banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.BundleKey.KEY_WEBVIEW_TITLE, mDiscoveryBean.getBanner().get(position).getTitle());
                    bundle.putString(Constant.BundleKey.KEY_WEBVIEW_URl, mDiscoveryBean.getBanner().get(position).getUrl());
                    mFragment.startBrotherFragment(WebviewFragment.newInstance(bundle));
                }
            });

        } else if (holder instanceof RecommendedViewHolder) {
            mReList = mDiscoveryBean.getDapp();
            mDappRecommedItemAdapter = new DappRecommendItemAdapter(mFragment, mReList);
            ((RecommendedViewHolder) holder).recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, ScreenUtils.dip2px(mFragment.getContext(), 25), false));
            ((RecommendedViewHolder) holder).recyclerView.setLayoutManager(new GridLayoutManager(mFragment.getContext(), 4));
            ((RecommendedViewHolder) holder).recyclerView.setAdapter(mDappRecommedItemAdapter);
            ((RecommendedViewHolder) holder).tv_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragment.startBrotherFragment(DappGameListFragment.newInstance());
                }
            });
        } else if (holder instanceof ExchangeViewHolder) {
            mExList = mDiscoveryBean.getTool();
            mDappExchangeAdapter = new DappExchangeAdapter(mFragment, mExList);
            ExchangeViewHolder exchangeViewHolder = (ExchangeViewHolder) holder;
            exchangeViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mFragment.getContext()));
            exchangeViewHolder.recyclerView.setAdapter(mDappExchangeAdapter);
            exchangeViewHolder.tx_game_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFragment.startBrotherFragment(DappGameListFragment.newInstance());
                }
            });
        } else if (holder instanceof ApplyViewHolder) {
            ApplyViewHolder applyViewHolder = (ApplyViewHolder) holder;
            applyViewHolder.tx_apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.BundleKey.KEY_WEBVIEW_TITLE, mFragment.getString(R.string.dapp_apply));
                    bundle.putString(Constant.BundleKey.KEY_WEBVIEW_URl, "http://aelfaelf1616.mikecrm.com/Z8EMGWN");
                    mFragment.startBrotherFragment(WebviewFragment.newInstance(bundle));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //Glide 加载图片简单用法
            RoundedCorners roundedCorners= new RoundedCorners(20);
            RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
            Glide.with(context).load(path).apply(options.placeholder(R.mipmap.img_hyper_dragon)).into(imageView);
        }

        @Override
        public ImageView createImageView(Context context) {
            return (ImageView) LayoutInflater.from(context).inflate(R.layout.item_discover_banner_imagview, null, false);
        }
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.banner)
        Banner banner;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class RecommendedViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;
        @BindView(R.id.tv_more)
        TextView tv_more;



        public RecommendedViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ExchangeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;
        @BindView(R.id.tx_game_more)
        TextView tx_game_more;

        public ExchangeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ApplyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tx_apply)
        TextView tx_apply;

        public ApplyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
