package com.cnblogs.article.model;

/**
 * 文章信息
 */
public class ArticleInfo {

    // 文章ID
    private String id;
    // 文章标题
    private String title;
    // 作者
    private ArticleAuthorInfo author;
    // 发表日期
    private String date;
    // 文章内容
    private String content;
    // 文章摘要
    private String summary;
    // 原文链接
    private String sourceUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
}
