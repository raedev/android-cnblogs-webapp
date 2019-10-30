package com.cnblogs.article.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 文章作者信息
 */
public class ArticleAuthorInfo implements Parcelable {
    // 作者Id
    private String id;
    // 用户Id
    private String userId;
    // 昵称
    private String name;
    // 头像地址
    private String avatar;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.userId);
        dest.writeString(this.name);
        dest.writeString(this.avatar);
    }

    public ArticleAuthorInfo() {
    }

    protected ArticleAuthorInfo(Parcel in) {
        this.id = in.readString();
        this.userId = in.readString();
        this.name = in.readString();
        this.avatar = in.readString();
    }

    public static final Parcelable.Creator<ArticleAuthorInfo> CREATOR = new Parcelable.Creator<ArticleAuthorInfo>() {
        @Override
        public ArticleAuthorInfo createFromParcel(Parcel source) {
            return new ArticleAuthorInfo(source);
        }

        @Override
        public ArticleAuthorInfo[] newArray(int size) {
            return new ArticleAuthorInfo[size];
        }
    };
}
