package com.bhsc.news.model;

import com.orm.SugarRecord;

/**
 * Created by lynn on 10/20/15.
 */
public class Data_DB_NewsType extends SugarRecord {
    private String TypeName;
    private int TypeId;
    public Data_DB_NewsType(){
        super();
        this.TypeName = "";
        this.TypeId = 0;
    }

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }

    public int getTypeId() {
        return TypeId;
    }

    public void setTypeId(int typeId) {
        TypeId = typeId;
    }
}
