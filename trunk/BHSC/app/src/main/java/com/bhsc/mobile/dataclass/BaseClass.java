package com.bhsc.mobile.dataclass;

/**
 * Created by lynn on 15-9-30.
 */
public class BaseClass {
    /**
     * 数据状态，0：正常数据，1：删除的数据
     */
    private int dataStatus;

    public BaseClass(){

    }

    public int getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(int dataStatus) {
        this.dataStatus = dataStatus;
    }
}
