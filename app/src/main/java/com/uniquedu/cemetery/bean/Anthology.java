package com.uniquedu.cemetery.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ZhongHang on 2016/3/4.
 * 选集
 */
public class Anthology implements Parcelable {
    private String RowNumber="";
    private String id="";
    private String MemorialID="";
    private String MemorialNumber="";
    private String IsDisplay="";
    private String ArticleClass="";
    private String ArticleTitle="";
    private String CreateDate="";
    private String CreateUser="";
    private String ChangeDate="";
    private String ChangeUser="";
    private String ArticleImg="";

    public String getArticleSource() {
        return ArticleSource;
    }

    public void setArticleSource(String articleSource) {
        ArticleSource = articleSource;
    }

    private String ArticleSource;

    public Anthology() {
    }

    protected Anthology(Parcel in) {
        RowNumber = in.readString();
        id = in.readString();
        MemorialID = in.readString();
        MemorialNumber = in.readString();
        IsDisplay = in.readString();
        ArticleClass = in.readString();
        ArticleTitle = in.readString();
        CreateDate = in.readString();
        CreateUser = in.readString();
        ChangeDate = in.readString();
        ChangeUser = in.readString();
        ArticleImg = in.readString();
        ArticleSource = in.readString();
    }

    public static final Creator<Anthology> CREATOR = new Creator<Anthology>() {
        @Override
        public Anthology createFromParcel(Parcel in) {
            return new Anthology(in);
        }

        @Override
        public Anthology[] newArray(int size) {
            return new Anthology[size];
        }
    };

    public String getRowNumber() {
        return RowNumber;
    }

    public void setRowNumber(String rowNumber) {
        RowNumber = rowNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemorialID() {
        return MemorialID;
    }

    public void setMemorialID(String memorialID) {
        MemorialID = memorialID;
    }

    public String getMemorialNumber() {
        return MemorialNumber;
    }

    public void setMemorialNumber(String memorialNumber) {
        MemorialNumber = memorialNumber;
    }

    public String getIsDisplay() {
        return IsDisplay;
    }

    public void setIsDisplay(String isDisplay) {
        IsDisplay = isDisplay;
    }

    public String getArticleClass() {
        return ArticleClass;
    }

    public void setArticleClass(String articleClass) {
        ArticleClass = articleClass;
    }

    public String getArticleTitle() {
        return ArticleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        ArticleTitle = articleTitle;
    }


    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getCreateUser() {
        return CreateUser;
    }

    public void setCreateUser(String createUser) {
        CreateUser = createUser;
    }

    public String getChangeDate() {
        return ChangeDate;
    }

    public void setChangeDate(String changeDate) {
        ChangeDate = changeDate;
    }

    public String getChangeUser() {
        return ChangeUser;
    }

    public void setChangeUser(String changeUser) {
        ChangeUser = changeUser;
    }

    public String getArticleImg() {
        return ArticleImg;
    }

    public void setArticleImg(String articleImg) {
        ArticleImg = articleImg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(RowNumber);
        dest.writeString(id);
        dest.writeString(MemorialID);
        dest.writeString(MemorialNumber);
        dest.writeString(IsDisplay);
        dest.writeString(ArticleClass);
        dest.writeString(ArticleTitle);
        dest.writeString(CreateDate);
        dest.writeString(CreateUser);
        dest.writeString(ChangeDate);
        dest.writeString(ChangeUser);
        dest.writeString(ArticleImg);
        dest.writeString(ArticleSource);
    }
}
