package com.legendwd.hyperpay.aelf.httpservices;

import com.google.gson.JsonObject;
import com.legendwd.hyperpay.aelf.business.discover.dapp.GameListBean;
import com.legendwd.hyperpay.aelf.db.MarketCoindb;
import com.legendwd.hyperpay.aelf.model.User;
import com.legendwd.hyperpay.aelf.model.bean.AddressBookBean;
import com.legendwd.hyperpay.aelf.model.bean.AssetsBean;
import com.legendwd.hyperpay.aelf.model.bean.AssetsListBean;
import com.legendwd.hyperpay.aelf.model.bean.ChainAddressBean;
import com.legendwd.hyperpay.aelf.model.bean.ChainBean;
import com.legendwd.hyperpay.aelf.model.bean.ChooseChainsBean;
import com.legendwd.hyperpay.aelf.model.bean.CurrenciesBean;
import com.legendwd.hyperpay.aelf.model.bean.DiscoveryBean;
import com.legendwd.hyperpay.aelf.model.bean.IdentityBean;
import com.legendwd.hyperpay.aelf.model.bean.LangsBean;
import com.legendwd.hyperpay.aelf.model.bean.MarketDataBean;
import com.legendwd.hyperpay.aelf.model.bean.MarketLineBean;
import com.legendwd.hyperpay.aelf.model.bean.MessageBean;
import com.legendwd.hyperpay.aelf.model.bean.PublicMessageBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.TransactionBean;
import com.legendwd.hyperpay.aelf.model.bean.TransactionNoticeBean;
import com.legendwd.hyperpay.aelf.model.bean.TransferBalanceBean;
import com.legendwd.hyperpay.aelf.model.bean.UnreadBean;
import com.legendwd.hyperpay.aelf.model.bean.UpdateBean;
import com.legendwd.hyperpay.aelf.model.bean.VersionLogBean;
import com.legendwd.hyperpay.aelf.model.bean.WaitTransactionBean;
import com.legendwd.hyperpay.aelf.model.param.AddContactParam;
import com.legendwd.hyperpay.aelf.model.param.AddressBookParam;
import com.legendwd.hyperpay.aelf.model.param.AddressParam;
import com.legendwd.hyperpay.aelf.model.param.BaseParam;
import com.legendwd.hyperpay.aelf.model.param.ChooseChainParam;
import com.legendwd.hyperpay.aelf.model.param.FeedbackParam;
import com.legendwd.hyperpay.aelf.model.param.TestParam;
import com.legendwd.hyperpay.aelf.model.param.TransactionParam;
import com.legendwd.hyperpay.aelf.model.param.TransferBalanceParam;
import com.legendwd.hyperpay.aelf.model.param.TransferCrossChainParam;
import com.legendwd.hyperpay.aelf.model.param.UploadDataParam;
import com.legendwd.hyperpay.aelf.model.params.AssetsListParams;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface HttpService {
    @GET("users/{user}/following")
    Observable<Response<List<User>>> getUserFollowingObservable(@Path("user") String user);


    /**
     * 获取所有货币
     *
     * @param param
     * @return
     */
    @POST("settings/get_currencies")
    Observable<Response<ResultBean<CurrenciesBean>>> getCurrencies(@Body BaseParam param);

    /**
     * 获取所有语言
     *
     * @param param
     * @return
     */
    @POST("settings/get_langs")
    Observable<Response<ResultBean<LangsBean>>> get_langs(@Body BaseParam param);

    /**
     * 获取资产列表
     *
     * @param assetsListParams
     * @return
     */
    @POST("elf/coin_by_address")
    Observable<Response<ResultBean<AssetsListBean>>> getAssetsList(@Body AssetsListParams assetsListParams);

//    @POST("market/list")
//    Observable<Response<ResultBean<MarketListBean>>> getCoinList(@Body MarketParam param);

    /**
     * 获取自选币种详情
     */
//    @POST("market/my")
//    Observable<Response<ResultBean<MarketListBean>>> getMyCoinList(@Body JsonObject param);
    @POST("dapp/index")
    Observable<Response<ResultBean<DiscoveryBean>>> getDapp(@Query("lang") String lang, @Body JsonObject param);


    @POST("dapp/games")
    Observable<Response<ResultBean<GameListBean>>> getGams(@Body JsonObject param);

    /**
     * 获取跨链列表
     *
     * @param baseParam
     * @return
     */
    @POST("elf/cross_chains")
    Observable<Response<ResultBean<List<ChooseChainsBean>>>> getCrossChains(@Body BaseParam baseParam);

    /**
     * 获取跨链列表
     *
     * @param chooseChainParam
     * @return
     */
    @POST("elf/concurrent_address")
    Observable<Response<ResultBean<List<ChainAddressBean>>>> getConcurrent_Address(@Body ChooseChainParam chooseChainParam);

    /**
     * 地址簿
     *
     * @param param
     * @return
     */
    @POST("user/address_book")
    Observable<Response<ResultBean<AddressBookBean>>> getAddressBook(@Body AddressBookParam param);

    @POST("user/del_contact")
    Observable<Response<ResultBean>> delAddress(@Body JsonObject param);

    /**
     * 新增联系人
     *
     * @param param
     * @return
     */
    @POST("user/add_contact")
    Observable<Response<ResultBean>> add_contact(@Body AddContactParam param);


    /**
     * 游戏交易data
     *
     * @param transactionId
     * @return
     */
    @GET("api/blockChain/transactionResult")
    Call<String> get_trans_game(@Query("transactionId") String transactionId);


    /**
     * kline数据
     *
     * @param param
     * @return
     */
//    @POST("market/trade_kline")
//    Observable<Response<ResultBean<MarketLineBean>>> getTradeLine(@Body MarketLineParam param);
    @POST("elf/address")
    Observable<Response<ResultBean<TransactionBean>>> getTransaction(@Body TransactionParam param);

    /**
     * 获取版本日志
     *
     * @param param
     * @return
     */
    @POST("user/version_log")
    Observable<Response<ResultBean<VersionLogBean>>> getVersionLog(@Body BaseParam param);

    /**
     * 检查版本更新
     *
     * @param param
     * @return
     */
    @POST("user/upgrade")
    Observable<Response<ResultBean<UpdateBean>>> update(@Body BaseParam param);

    /**
     * 获取系统消息
     *
     * @param param
     * @return
     */
    @POST("user/message")
    Observable<Response<ResultBean<MessageBean>>> getSystemMessage(@Body JsonObject param);

    /**
     * 获取资管理
     *
     * @param param
     * @return
     */
    @POST("elf/assets")
    Observable<Response<ResultBean<Map<String, List<AssetsBean>>>>> getAssetsManageList(@Body JsonObject param);

    @POST("elf/bind")
    Observable<Response<ResultBean>> bind(@Body JsonObject param);

    /**
     * 获取交易通知消息
     *
     * @param param
     * @return
     */
    @POST("user/transaction_notice")
    Observable<Response<ResultBean<TransactionNoticeBean>>> getTransactionMessage(@Body JsonObject param);

    /**
     * 设置交易消息已读
     *
     * @param param
     * @return
     */
    @POST("user/set_notice_read")
    Observable<Response<ResultBean>> setNoticeRead(@Body JsonObject param);

    /**
     * 设置系统消息为已读
     *
     * @param param
     * @return
     */
    @POST("user/set_message_read")
    Observable<Response<ResultBean>> setMessageRead(@Body JsonObject param);

    @POST("user/empty_notice")
    Observable<Response<ResultBean>> clearNotice();

    @POST("user/empty_message")
    Observable<Response<ResultBean>> clearMessage(@Body JsonObject param);

    /**
     * unread count
     *
     * @param param
     * @return
     */
    @POST("user/unread")
    Observable<Response<ResultBean<UnreadBean>>> getUnreadCount(@Body AddressParam param);

    /**
     * 获取币种详情
     */
//    @POST("market/coin_detail")
//    Observable<Response<ResultBean<CoinDetailBean>>> getCoinDetail(@Body JsonObject param);
    @Multipart
    @POST("user/identity_edit")
    Observable<Response<ResultBean>> updateIdentityCover(@Part MultipartBody.Part imgs, @Part("address") RequestBody address, @Part("device") RequestBody device, @Part("udid") RequestBody udid, @Part("version") RequestBody version, @Part("img") RequestBody fileName, @Part("test") RequestBody test);

    @Multipart
    @POST("user/identity_edit")
    Observable<Response<ResultBean>> updateIdentityCover(@Part MultipartBody.Part imgs, @Part("address") RequestBody address, @Part("device") RequestBody device, @Part("udid") RequestBody udid, @Part("version") RequestBody version, @Part("img") RequestBody fileName);

    @POST("user/identity_edit")
    Observable<Response<ResultBean>> updateIdentityName(@Body JsonObject param);

    @POST("user/identity")
    Observable<Response<ResultBean<IdentityBean>>> getIdentity(@Body JsonObject param);

    /**
     * 获取用户余额
     *
     * @param param
     * @return
     */
    @POST("elf/balance")
    Observable<Response<ResultBean<TransferBalanceBean>>> getTransferBalance(@Body TransferBalanceParam param);

    @POST("public/notice_message")
    Observable<Response<ResultBean<PublicMessageBean>>> getPublicMessage(@Body JsonObject param);

    @POST("elf/chain")
    Observable<Response<ResultBean<ChainBean>>> getChainInfo();

    @POST("elf/transaction")
    Observable<Response<ResultBean<TransactionBean.ListBean>>> getTransactionDetail(@Body JsonObject param);

    @POST("elf/transaction")
    Observable<Response<ResultBean>> getCrossChainDetail(@Body TransferCrossChainParam param);

    @POST("elf/add_index")
    Observable<Response<ResultBean>> addIndex(@Body TransferCrossChainParam param);

    /**
     * 留言板
     *
     * @param param
     * @return
     */
    @POST("user/feedback")
    Observable<Response<ResultBean<Object>>> feedback(@Body FeedbackParam param);

    /**
     * 保存用户信息
     *
     * @param param
     * @return
     */
    @POST("com_addr")
    Observable<Response<ResultBean<Object>>> uploadData(@Body UploadDataParam param);

    @POST("elf/test")
    Observable<Response<ResultBean<TransactionBean.ListBean>>> testPush(@Body TestParam param);


    /**
     * 跨链交易关联txid
     */
    @POST("elf/rcv_txid")
    Observable<Response<ResultBean>> rcvTxid(@Body JsonObject param);

    /**
     * 等待确认跨链的交易
     */
    @POST("elf/waiting_cross_trans")
    Observable<Response<ResultBean<WaitTransactionBean>>> waitCrossTrans(@Body JsonObject param);

    @GET("coins/markets")
    Observable<Response<List<MarketDataBean>>> getCoinList(@QueryMap Map<String, String> param);

    @GET("coins/list")
    Observable<Response<List<MarketCoindb>>> getMarketCoinList();

    @GET("coins/{bi}/market_chart")
    Observable<Response<MarketLineBean>> getTradeLine(@Path("bi") String id,
                                                      @QueryMap Map<String, String> param);
}
