package com.bhsc.mobile.datalcass;

/**
 * Created by lynn on 15-9-30.
 */
public class BaseClass {
    private String DataId = "";
    /**
     * 数据状态，0：正常数据，1：删除的数据
     */
    private int dataStatus;

    public String getDataId() {
        return DataId;
    }

    public void setDataId(String dataId) {
        DataId = dataId;
    }

    public int getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(int dataStatus) {
        this.dataStatus = dataStatus;
    }
}
