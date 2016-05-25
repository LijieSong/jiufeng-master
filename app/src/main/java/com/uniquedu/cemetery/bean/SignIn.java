package com.uniquedu.cemetery.bean;

/**
 * Created by ZhongHang on 2016/4/26.
 */
public class SignIn {

    /**
     * exCode : 0
     * exMsg :
     * token : 4822f7f2-f48e-4474-a25f-d72bb208627f
     * account : 20160053
     * memorialId : 3913
     * memorialName : 谢直
     * memorialType : 单人馆
     * accountEmail : null
     * accountPhone : 8782****
     * memorialPhoto : ./UserData/PersonalMemorial/public/Tomb/OnlineImg01.jpg
     * memorialBh : 兰园五区-JF14-1-1-8
     */

    private String exCode;
    private String exMsg;
    private String token;
    private String account;
    private String memorialId;
    private String memorialName;
    private String memorialType;
    private String accountEmail;
    private String accountPhone;
    private String memorialPhoto;
    private String memorialBh;

    public SignIn() {
    }

    public void setExCode(String exCode) {
        this.exCode = exCode;
    }

    public void setExMsg(String exMsg) {
        this.exMsg = exMsg;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setMemorialId(String memorialId) {
        this.memorialId = memorialId;
    }

    public void setMemorialName(String memorialName) {
        this.memorialName = memorialName;
    }

    public void setMemorialType(String memorialType) {
        this.memorialType = memorialType;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public void setAccountPhone(String accountPhone) {
        this.accountPhone = accountPhone;
    }

    public void setMemorialPhoto(String memorialPhoto) {
        this.memorialPhoto = memorialPhoto;
    }

    public void setMemorialBh(String memorialBh) {
        this.memorialBh = memorialBh;
    }

    public String getExCode() {
        return exCode;
    }

    public String getExMsg() {
        return exMsg;
    }

    public String getToken() {
        return token;
    }

    public String getAccount() {
        return account;
    }

    public String getMemorialId() {
        return memorialId;
    }

    public String getMemorialName() {
        return memorialName;
    }

    public String getMemorialType() {
        return memorialType;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public String getAccountPhone() {
        return accountPhone;
    }

    public String getMemorialPhoto() {
        return memorialPhoto;
    }

    public String getMemorialBh() {
        return memorialBh;
    }
}
