package com.uniquedu.cemetery.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ZhongHang on 2016/3/15.
 */
public class PhotoBean implements Parcelable {
    private String Id="";
    private String MemorialID="";
    private String IsDisplay="";
    private String Photo="";
    private String Title="";
    private String Summary="";
    private String MemorialNumber="";
    private boolean isChecked;
    private String CreateDate="";
    private String CreateUser="";
    private String ChangeDate="";
    private String ChangeUser="";
    private String CurrentLoginUserId="";
    private String Data1="";
    private String Data2="";
    private String Data3="";

    public PhotoBean() {
    }

    protected PhotoBean(Parcel in) {
        Id = in.readString();
        MemorialID = in.readString();
        IsDisplay = in.readString();
        Photo = in.readString();
        Title = in.readString();
        Summary = in.readString();
        MemorialNumber = in.readString();
        isChecked = in.readByte() != 0;
        CreateDate = in.readString();
        CreateUser = in.readString();
        ChangeDate = in.readString();
        ChangeUser = in.readString();
        CurrentLoginUserId = in.readString();
        Data1 = in.readString();
        Data2 = in.readString();
        Data3 = in.readString();
    }

    public static final Creator<PhotoBean> CREATOR = new Creator<PhotoBean>() {
        @Override
        public PhotoBean createFromParcel(Parcel in) {
            return new PhotoBean(in);
        }

        @Override
        public PhotoBean[] newArray(int size) {
            return new PhotoBean[size];
        }
    };

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getMemorialID() {
        return MemorialID;
    }

    public void setMemorialID(String memorialID) {
        MemorialID = memorialID;
    }

    public String getIsDisplay() {
        return IsDisplay;
    }

    public void setIsDisplay(String isDisplay) {
        IsDisplay = isDisplay;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }

    public String getMemorialNumber() {
        return MemorialNumber;
    }

    public void setMemorialNumber(String memorialNumber) {
        MemorialNumber = memorialNumber;
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

    public String getCurrentLoginUserId() {
        return CurrentLoginUserId;
    }

    public void setCurrentLoginUserId(String currentLoginUserId) {
        CurrentLoginUserId = currentLoginUserId;
    }

    public String getData1() {
        return Data1;
    }

    public void setData1(String data1) {
        Data1 = data1;
    }

    public String getData2() {
        return Data2;
    }

    public void setData2(String data2) {
        Data2 = data2;
    }

    public String getData3() {
        return Data3;
    }

    public void setData3(String data3) {
        Data3 = data3;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(MemorialID);
        dest.writeString(IsDisplay);
        dest.writeString(Photo);
        dest.writeString(Title);
        dest.writeString(Summary);
        dest.writeString(MemorialNumber);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeString(CreateDate);
        dest.writeString(CreateUser);
        dest.writeString(ChangeDate);
        dest.writeString(ChangeUser);
        dest.writeString(CurrentLoginUserId);
        dest.writeString(Data1);
        dest.writeString(Data2);
        dest.writeString(Data3);
    }
}
