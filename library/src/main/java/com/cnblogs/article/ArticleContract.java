package com.cnblogs.article;

import android.support.annotation.NonNull;

import com.cnblogs.article.model.ArticleCommentInfo;
import com.cnblogs.article.model.ArticleInfo;

import java.util.List;

/**
 * 文章处理
 */
public interface ArticleContract {

    interface Presenter {

        void setView(View view);

        /**
         * 加载文章数据
         */
        void start();


        void destroy();

        /**
         * 获取文章的评论
         *
         * @param page 页码
         */
        void loadArticleComments(int page);


        /**
         * 文章点赞
         *
         * @param isLike 喜欢 / 不喜欢
         */
        void likeArticle(boolean isLike);
    }


    interface View {

        /**
         * 文章实体类
         */
        @NonNull
        ArticleInfo getArticle();


        /**
         * 设置文章实体类
         * 一般是从源地址获取更新实体对象
         */
        void setArticle(ArticleInfo article);

        /**
         * 文章详情加载失败
         */
        void onLoadDataFailed(String message);

        /**
         * 加载文章内容成功
         *
         * @param article 文章
         */
        void onLoadDataSuccess(ArticleInfo article);

        /**
         * 加载下一篇文章数据成功
         *
         * @param articles 文章
         */
        void onLoadNextArticleData(List<ArticleInfo> articles);

        /**
         * 加载评论失败
         *
         * @param message 错误消息
         */
        void onLoadCommentError(String message);

        /**
         * 加载评论成功
         *
         * @param data 评论数据
         */
        void onLoadDataComments(List<ArticleCommentInfo> data);


        /**
         * 操作需要登录
         */
        void onNeedLogin();

        /**
         * 点赞成功
         */
        void onLikeSuccess();

        /**
         * 点赞失败
         *
         * @param msg 错误消息
         */
        void onLikeError(String msg);
    }
}
