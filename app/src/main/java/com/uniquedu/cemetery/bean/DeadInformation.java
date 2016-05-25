package com.uniquedu.cemetery.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/9.
 * 逝者详细信息
 */
public class DeadInformation implements Serializable {
    private String id="";
    private String MemorialNumber="";
    private String DeadName="";//逝者姓名
    private String sex="";//性别
    private String nationality="";//民族
    private String birthday="";//生日
    private String feteday="";//离世时间
    private String nativeplace="";//籍贯
    private String MemorialID="";//纪念馆ID
    private String Summary="";//简介
    private String CreateDate="";//创建时间
    private String CreateUser="";//创建者
    private String ChangeDate="";//更改时间
    private String ChangeUser="";//更改者
    private String ArticleSummary="";//生平
    private String Filename="";//图片
    private String TombDeathPersonId="";
    private String BFYEAR="";//生卒

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemorialNumber() {
        return MemorialNumber;
    }

    public void setMemorialNumber(String memorialNumber) {
        MemorialNumber = memorialNumber;
    }

    public String getDeadName() {
        return DeadName;
    }

    public void setDeadName(String deadName) {
        DeadName = deadName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getFeteday() {
        return feteday;
    }

    public void setFeteday(String feteday) {
        this.feteday = feteday;
    }

    public String getNativeplace() {
        return nativeplace;
    }

    public void setNativeplace(String nativeplace) {
        this.nativeplace = nativeplace;
    }

    public String getMemorialID() {
        return MemorialID;
    }

    public void setMemorialID(String memorialID) {
        MemorialID = memorialID;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
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

    public String getArticleSummary() {
        return ArticleSummary;
    }

    public void setArticleSummary(String articleSummary) {
        ArticleSummary = articleSummary;
    }

    public String getFilename() {
        return Filename;
    }

    public void setFilename(String filename) {
        Filename = filename;
    }

    public String getTombDeathPersonId() {
        return TombDeathPersonId;
    }

    public void setTombDeathPersonId(String tombDeathPersonId) {
        TombDeathPersonId = tombDeathPersonId;
    }

    public String getBFYEAR() {
        return BFYEAR;
    }

    public void setBFYEAR(String BFYEAR) {
        this.BFYEAR = BFYEAR;
    }
}

