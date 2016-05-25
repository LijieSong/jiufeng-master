package com.uniquedu.cemetery.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baoyi on 2016/3/7.
 * 进入在线祭扫界面的获取的信息
 */
public class DeadCallBack {
    private String exCode="";
    private String exMsg="";
    private String id="";
    private String MemorialNumber="";
    private String TombType="";
    private String ManPhoto="";
    private String FlowerState="";
    /**
     * Cup是香的状态 Incensory是酒的状态
     */
    private String IncensoryState="";

    private String CupState="";
    private String Mp3State="";
    private String total="";
    private List<DeadInformation> rows;

    public String getExCode() {
        return exCode;
    }

    public void setExCode(String exCode) {
        this.exCode = exCode;
    }

    public String getExMsg() {
        return exMsg;
    }

    public void setExMsg(String exMsg) {
        this.exMsg = exMsg;
    }

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

    public String getTombType() {
        return TombType;
    }

    public void setTombType(String tombType) {
        TombType = tombType;
    }

    public String getManPhoto() {
        return ManPhoto;
    }

    public void setManPhoto(String manPhoto) {
        ManPhoto = manPhoto;
    }

    public String getFlowerState() {
        return FlowerState;
    }

    public void setFlowerState(String flowerState) {
        FlowerState = flowerState;
    }

    public String getIncensoryState() {
        return IncensoryState;
    }

    public void setIncensoryState(String incensoryState) {
        IncensoryState = incensoryState;
    }

    public String getCupState() {
        return CupState;
    }

    public void setCupState(String cupState) {
        CupState = cupState;
    }

    public String getMp3State() {
        return Mp3State;
    }

    public void setMp3State(String mp3State) {
        Mp3State = mp3State;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<DeadInformation> getRows() {
        return rows;
    }

    public void setRows(List<DeadInformation> rows) {
        this.rows = rows;
    }
}
