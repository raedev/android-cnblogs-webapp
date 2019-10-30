package com.cnblogs.article.model;

import android.support.annotation.Nullable;

/**
 * 文章评论
 */
public class ArticleCommentInfo {
    // 评论ID
    private String id;
    // 评论内容
    private String content;
    // 评论作者
    private ArticleAuthorInfo author;
    // 评论日期
    private String date;
    // 应用的评论
    @Nullable
    private ArticleCommentInfo quote;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArticleAuthorInfo getAuthor() {
        return author;
    }

    public void setAuthor(ArticleAuthorInfo author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Nullable
    public ArticleCommentInfo getQuote() {
        return quote;
    }

    public void setQuote(@Nullable ArticleCommentInfo quote) {
        this.quote = quote;
    }
}
