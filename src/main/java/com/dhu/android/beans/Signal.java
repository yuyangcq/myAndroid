package com.dhu.android.beans;

/**
 * 新建与数据库表对应的实体类
 * Created by yuyang on 2017/8/2.
 */
public class Signal {
    //实体类的属性和表的字段名称一一对应
    private String RP;
    private int RSSI;

    public Signal() {
    }

    public Signal(String RP, int RSSI) {
        this.RP = RP;
        this.RSSI = RSSI;
    }

    public int getRSSI() {
        return RSSI;
    }

    public void setRSSI(int RSSI) {
        this.RSSI = RSSI;
    }

    public String getRP() {
        return RP;
    }

    public void setRP(String RP) {
        this.RP = RP;
    }


}
