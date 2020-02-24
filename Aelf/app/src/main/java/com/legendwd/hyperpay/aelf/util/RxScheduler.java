package com.legendwd.hyperpay.aelf.util;


import com.legendwd.hyperpay.aelf.httpservices.AndroidSchedulers;
import com.legendwd.hyperpay.aelf.widget.LoadDialog;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableTransformer;

/**
 * @author lovelyzxing
 * @date 2019/5/21
 * @Description
 */
public class RxScheduler {
    public static <T> void doTask(final Task<T> task, LoadDialog loadDialog) {
        Observable.create((ObservableOnSubscribe<T>) emitter ->
        {
            emitter.onNext(task.doOnIOThread());
            emitter.onComplete();
        }).doFinally(() ->
        {
            if (null != loadDialog) {
                loadDialog.dismiss();
            }
        }).compose(applySchedulers()).subscribe(t -> task.doOnUIThread(t));
    }

    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return upstream -> upstream.subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .unsubscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> void doTask(final Task<T> task) {
        doTask(task, null);
    }

}
