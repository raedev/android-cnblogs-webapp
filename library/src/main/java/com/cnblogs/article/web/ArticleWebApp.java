package com.cnblogs.article.web;

import android.util.Log;
import android.webkit.WebView;

import java.lang.ref.WeakReference;

/**
 * 文章Web注入的WebApp对象
 * 调用 {{@link #initWebView(WebView)}}方法初始化注入对象
 */
public final class ArticleWebApp {

    /**
     * 弱引用WebView
     */
    private WeakReference<WebView> mWebView;

    /**
     * 调用方法
     *
     * @param name 方法签名
     */
    private void invokeMethod(String name) {
        if (mWebView == null || mWebView.get() == null) {
            Log.w("rae", "网页已经释放，无法调用方法：" + name);
            return;
        }
        mWebView.get().evaluateJavascript("javascript:webapp." + name, null);
    }

    /**
     * 初始化
     */
    public void initWebView(WebView webView) {
        mWebView = new WeakReference<>(webView);
    }

    public void dispose() {
        mWebView.clear();
    }

    public void onLoadData() {
        invokeMethod("onLoadData()");
    }

    public void onLoadCommentError() {
        invokeMethod("onLoadCommentError()");
    }

    public void onLoadComments() {
        invokeMethod("onLoadComments()");
    }

    public void onLikeFinish() {
        invokeMethod("onLikeFinish()");
    }

    public void scrollToComment() {
        invokeMethod("scrollToComment()");
    }
}
