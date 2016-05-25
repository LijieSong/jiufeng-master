package com.uniquedu.cemetery.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/18.
 */
public class ShuJu implements Serializable{

    private List<Dead> rows;

    public List<Dead> getRows() {
        return rows;
    }

    public void setRows(List<Dead> rows) {
        this.rows = rows;
    }

    public ShuJu(List<Dead> rows) {

        this.rows = rows;
    }

    @Override
    public String toString() {
        return "ShuJu{" +
                "rows=" + rows +
                '}';
    }

    public ShuJu() {
        super();
    }
}
