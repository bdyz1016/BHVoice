package com.bhsc.mobile.net;

import com.bhsc.mobile.dataclass.Data_DB_Discuss;

import java.util.List;

/**
 * Created by lynn on 11/5/15.
 */
public class DiscussResponse extends Response{
    private int code;
    private List<Data_DB_Discuss> list;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Data_DB_Discuss> getList() {
        return list;
    }

    public void setList(List<Data_DB_Discuss> list) {
        this.list = list;
    }
}
