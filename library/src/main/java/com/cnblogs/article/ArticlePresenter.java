package com.cnblogs.article;

import android.text.TextUtils;

import com.cnblogs.article.model.ArticleInfo;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 文章逻辑处理基础类
 */
public abstract class ArticlePresenter implements ArticleContract.Presenter {

    protected ArticleContract.View mView;

    // 管理subscriptions订阅的释放
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    public void setView(ArticleContract.View view) {
        mView = view;
    }

    @Override
    public void start() {
        // 是否需要获取文章详情
        ArticleInfo article = mView.getArticle();

        // 请求文章详情
        if (checkRequestArticleDetailEnable(article)) {
            Observable<ArticleInfo> observable = createArticleDetailObservable(article);
            if (observable != null) {
                mDisposable.add(observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnError(new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                mView.onLoadDataFailed(throwable.getMessage());
                            }
                        }).subscribe(new Consumer<ArticleInfo>() {
                            @Override
                            public void accept(ArticleInfo articleInfo) throws Exception {
                                mView.setArticle(articleInfo);
                                onLoadArticleContent();
                            }
                        }));
            }
        } else {
            onLoadArticleContent();
        }
    }

    /**
     * 加载文章内容
     */
    protected abstract void onLoadArticleContent();

    @Override
    public void destroy() {
        mDisposable.dispose();
        mDisposable.clear();
    }

    /**
     * 是否需要请求文章详情
     */
    protected boolean checkRequestArticleDetailEnable(ArticleInfo article) {
        return TextUtils.isEmpty(article.getId());
    }


    /**
     * 当前需要请求文章详情时调用该方法, 结合{@link #checkRequestArticleDetailEnable(ArticleInfo)}方法来调用。
     *
     * @param article 文章详情
     */

    protected Observable<ArticleInfo> createArticleDetailObservable(ArticleInfo article) {
        return null;
    }
}
