package com.cnblogs.article;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.cnblogs.article.web.ArticleJsBridge;
import com.cnblogs.article.web.ArticleWebApp;
import com.cnblogs.article.web.ArticleWebChromeClient;
import com.cnblogs.article.web.ArticleWebViewClient;
import com.qmuiteam.qmui.widget.webview.QMUIWebView;

import java.io.File;

/**
 * 文章查看WebView
 */
public class ArticleWebFragment extends Fragment {

    protected QMUIWebView mWebView;

    private ArticleContract.Presenter mPresenter;

    // 脚本交互
    private ArticleJsBridge mJsBridge;
    private ArticleWebApp mWebApp;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fm_web_article, container, false);
        if (mWebView != null) {
            mWebView.destroy();
        }
        mWebView = new QMUIWebView(requireContext().getApplicationContext());
        mWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        view.addView(mWebView, 0);
        this.initWebViewSetting();
        this.initWebViewClient();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentActivity activity = getActivity();

        if (activity instanceof IArticleActivity) {
            // 逻辑交互
            IArticleActivity articleActivity = (IArticleActivity) activity;
            mPresenter = articleActivity.getPresenter();
            mJsBridge = new ArticleJsBridge(mWebView, articleActivity);
            mWebApp.initWebView(mWebView);
        } else {
            throw new IllegalArgumentException("Activity必须实现IArticleActivity接口");
        }
        // 先加载网页
        mWebView.loadUrl("file:///android_asset/index.html");
        // 启动
        mPresenter.start();
    }

    /**
     * 初始化WebView设置
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewSetting() {
        WebView.setWebContentsDebuggingEnabled(true);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        File cacheDir = requireContext().getExternalCacheDir();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (cacheDir != null && cacheDir.canRead() && cacheDir.canWrite()) {
            settings.setAppCacheEnabled(true);
            settings.setAppCachePath(cacheDir.getPath());
        }
    }

    /**
     * 初始化WebClient
     */
    private void initWebViewClient() {
        mWebView.setWebChromeClient(new ArticleWebChromeClient());
        mWebView.setWebViewClient(new ArticleWebViewClient());
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onResume() {
        mWebView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }
        if (mWebApp != null) {
            mWebApp.dispose();
            mWebApp = null;
        }
        if (mJsBridge != null) {
            mJsBridge.dispose();
            mJsBridge = null;
        }
        if (mPresenter != null) {
            mPresenter.destroy();
            mPresenter = null;
        }
        super.onDestroy();
    }

    public WebView getWebView() {
        return mWebView;
    }

}
