package com.cnblogs.article.web;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.cnblogs.article.IArticleActivity;
import com.cnblogs.article.model.ArticleAuthorInfo;
import com.cnblogs.article.model.ArticleCommentInfo;
import com.cnblogs.article.model.ArticleInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * 文章与JS脚本交互
 * Created by rae on 2019-09-29.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public final class ArticleJsBridge {

    //region 字段定义
    @NonNull
    private final Gson mGson = new Gson();
    @NonNull
    private final IArticleActivity mArticleActivity;
    private String mArticleJson;
    private String mNextArticleJson;
    private String mArticleCommentJson;

    // 管理subscriptions订阅的释放
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public ArticleJsBridge(@NonNull WebView webView, @NonNull IArticleActivity activity) {
        mArticleActivity = activity;
        webView.addJavascriptInterface(this, "cnblogsApp");
    }


    //endregion

    //region 交互接口

    /**
     * 获取博客实体
     */
    @JavascriptInterface
    public String getArticle() {
        return mArticleJson;
    }

    /**
     * 获取下一篇文章
     */
    @JavascriptInterface
    public String getNextArticle() {
        return mNextArticleJson;
    }

    /**
     * 获取文章的评论
     */
    @JavascriptInterface
    public String getArticleComment() {
        return mArticleCommentJson;
    }

    /**
     * 点击主页
     */
    @JavascriptInterface
    public void onAuthorHomeClick(final String json) {

        final ArticleAuthorInfo authorInfo = mGson.fromJson(json, ArticleAuthorInfo.class);
        if (authorInfo == null) return;

        runOnMainThread(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                mArticleActivity.onRouteToAuthorHome(authorInfo);
            }
        });
    }

    /**
     * 点击文章
     */
    @JavascriptInterface
    public void onArticleClick(final String json) {
        final ArticleInfo articleInfo = mGson.fromJson(json, ArticleInfo.class);
        if (articleInfo == null) return;
        runOnMainThread(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                mArticleActivity.onRouteToArticleView(articleInfo);
            }
        });
    }

    /**
     * 点击图片
     *
     * @param src    当前图片地址
     * @param images 图片数组
     */
    @JavascriptInterface
    public void onImageClick(final String src, final String images) {
        runOnMainThread(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                if (TextUtils.isEmpty(src) || TextUtils.isEmpty(images)) return;
                ArrayList<String> imageList = mGson.fromJson(images, new TypeToken<ArrayList<String>>() {
                }.getType());
                int index = Math.max(0, imageList.indexOf(src));
                mArticleActivity.onRouteToImagePreview(imageList, index);
            }
        });
    }


    /**
     * 加载评论
     *
     * @param page 页码
     */
    @JavascriptInterface
    public void onLoadCommentClick(final String page) {
        runOnMainThread(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                // 加载评论
                mArticleActivity.getPresenter().loadArticleComments(parseInt(page));
            }
        });
    }

    /**
     * 点赞
     */
    @JavascriptInterface
    public void onLikeClick(final String isLike) {
        runOnMainThread(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                mArticleActivity.getPresenter().likeArticle(parseBoolean(isLike));
            }
        });
    }


    /**
     * 点击评论
     */
    @JavascriptInterface
    public void onCommentClick(@Nullable String item) {

        final ArticleCommentInfo commentInfo = mGson.fromJson(item, ArticleCommentInfo.class);
        if (commentInfo == null) return;

        runOnMainThread(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                mArticleActivity.onCommentClick(commentInfo);
            }
        });
    }

    /**
     * 关闭当前页面
     */
    @JavascriptInterface
    public void close() {
        runOnMainThread(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                mArticleActivity.close();
            }
        });
    }

    // endregion

    //region 设置数据接口

    public void setArticleComment(List<ArticleCommentInfo> data) {
        this.mArticleCommentJson = toJson(data);
    }

    public void setArticle(ArticleInfo article) {
        this.mArticleJson = toJson(article);
    }

    public void setNextArticles(List<ArticleInfo> data) {
        this.mNextArticleJson = toJson(data);
    }

    // endregion

    private void runOnMainThread(Consumer<? super Integer> observer) {
        Observable<Integer> observable = Observable.just(0).subscribeOn(AndroidSchedulers.mainThread());
        mDisposable.add(observable.subscribe(observer));
    }

    public void dispose() {
        mDisposable.dispose();
        mDisposable.clear();
    }

    public int parseInt(String text) {
        if (TextUtils.isEmpty(text)) return 0;
        try {
            return Integer.parseInt(text);
        } catch (Exception ignored) {
        }
        return 0;
    }


    private boolean parseBoolean(String value) {
        return "true".equalsIgnoreCase(value);
    }

    private String toJson(Object obj) {
        return mGson.toJson(obj);
    }
}
