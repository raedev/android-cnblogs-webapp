package com.cnblogs.article;

import android.support.annotation.NonNull;

import com.cnblogs.article.model.ArticleAuthorInfo;
import com.cnblogs.article.model.ArticleCommentInfo;
import com.cnblogs.article.model.ArticleInfo;

import java.util.ArrayList;

public interface IArticleActivity {

    ArticleContract.Presenter getPresenter();

    /**
     * 跳转到作者主页
     *
     * @param authorInfo 作者信息
     */
    void onRouteToAuthorHome(@NonNull ArticleAuthorInfo authorInfo);

    /**
     * 跳转到文章查看
     *
     * @param articleInfo 文章信息
     */
    void onRouteToArticleView(@NonNull ArticleInfo articleInfo);

    /**
     * 跳转到图片查看
     *
     * @param images       图片集合
     * @param currentIndex 当前图片索引
     */
    void onRouteToImagePreview(ArrayList<String> images, int currentIndex);

    /**
     * 点击评论回调
     *
     * @param commentInfo 评论信息
     */
    void onCommentClick(@NonNull ArticleCommentInfo commentInfo);

    /**
     * 关闭当前页面
     */
    void close();
}
