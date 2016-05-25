package com.uniquedu.cemetery.bean;

import java.io.Serializable;

/**
 * Created by ZhongHang on 2016/3/4.
 * 逝者信息
 */
public class Dead implements Serializable {
    private String RowNumber="";
    private String IsAudit="";
    private String id="";
    private String ManPhoto="";
    private String Number="";
    private String MemorialName="";
    private String CreateDate="";
    private String CreateUser="";
    private String ChangeDate="";
    private String ChangeUser="";
    private String AuditDate="";
    private String AuditUser="";
    private String UpdateDate="";
    private String TombType="";
    private String man="";
    private String woman="";
    private String ClickCount="";


    public String getRowNumber() {
        return RowNumber;
    }

    public void setRowNumber(String rowNumber) {
        RowNumber = rowNumber;
    }

    public String getIsAudit() {
        return IsAudit;
    }

    public void setIsAudit(String isAudit) {
        IsAudit = isAudit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getManPhoto() {
        return ManPhoto;
    }

    public void setManPhoto(String manPhoto) {
        ManPhoto = manPhoto;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getMemorialName() {
        return MemorialName;
    }

    public void setMemorialName(String memorialName) {
        MemorialName = memorialName;
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

    public String getAuditDate() {
        return AuditDate;
    }

    public void setAuditDate(String auditDate) {
        AuditDate = auditDate;
    }

    public String getAuditUser() {
        return AuditUser;
    }

    public void setAuditUser(String auditUser) {
        AuditUser = auditUser;
    }

    public String getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(String updateDate) {
        UpdateDate = updateDate;
    }

    public String getTombType() {
        return TombType;
    }

    public void setTombType(String tombType) {
        TombType = tombType;
    }

    public String getMan() {
        return man;
    }

    public void setMan(String man) {
        this.man = man;
    }

    public String getWoman() {
        return woman;
    }

    public void setWoman(String woman) {
        this.woman = woman;
    }

    public String getClickCount() {
        return ClickCount;
    }

    public void setClickCount(String clickCount) {
        ClickCount = clickCount;
    }
}
