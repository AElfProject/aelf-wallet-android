package com.legendwd.hyperpay.aelf.httpservices;

import com.legendwd.hyperpay.aelf.model.bean.ResultBean;
import com.legendwd.hyperpay.aelf.model.bean.ResultListBean;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ResponseTransformer {

    public static <T> ObservableTransformer<Response<T>, T> handleResult(LifecycleProvider<ActivityEvent> provider) {
        return upstream -> upstream
                .onErrorResumeNext(new ErrorResumeFunction<>())
                .flatMap(new ResponseFunction<>())
                .subscribeOn(Schedulers.newThread())
                .compose(provider.bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 非服务器产生的异常，比如本地无无网络请求，Json数据解析错误等等。
     *
     * @param <T>
     */
    private static class ErrorResumeFunction<T> implements Function<Throwable, ObservableSource<? extends Response<T>>> {

        @Override
        public ObservableSource<? extends Response<T>> apply(Throwable throwable) {
            return Observable.error(throwable);
        }
    }

    /**
     * 服务其返回的数据解析
     * 正常服务器返回数据和服务器可能返回的exception
     *
     * @param <T>
     */
    private static class ResponseFunction<T> implements Function<Response<T>, ObservableSource<T>> {

        @Override
        public ObservableSource<T> apply(Response<T> tResponse) throws Exception {
            int code = tResponse.code();
            if (code == 200) {
                if (tResponse.body() instanceof ResultBean) {
                    ResultBean resultBean = (ResultBean) tResponse.body();
                    if (resultBean.getStatus() == 200) {
                        return Observable.just(tResponse.body());
                    } else {
                        return Observable.error(new ResponseThrowable(resultBean.getMsg()));
                    }
                } else if (tResponse.body() instanceof ResultListBean) {
                    ResultListBean resultListBean = (ResultListBean) tResponse.body();
                    if (resultListBean.getStatus() == 200) {
                        return Observable.just(tResponse.body());
                    } else {
                        return Observable.error(new ResponseThrowable(resultListBean.getMsg()));
                    }
                } else {
                    return Observable.just(tResponse.body());
                }
            } else {
                return Observable.error(new Throwable());
            }
        }
    }

    public static class ResponseThrowable extends Throwable {
        private String message;

        public ResponseThrowable(String message) {
            super();
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }
}